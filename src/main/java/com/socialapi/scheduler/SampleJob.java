package com.socialapi.scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;


import com.capitalone.socialApiFb.model.ApiPageRequest;
import com.capitalone.socialApiFb.model.ApiRequestMessage;
import com.capitalone.socialApiFb.model.ApiResponseMessage;
import com.capitalone.socialApiFb.model.CSVReaderUtils;
import com.capitalone.socialApiFb.model.CSVUtils;
import com.capitalone.socialApiFb.model.Comments;
import com.capitalone.socialApiFb.model.PagePostImpression;
//import com.capitalone.socialApiFb.model.LikePosts;
import com.capitalone.socialApiFb.model.PagePostImpressions;
import com.capitalone.socialApiFb.model.Post;
import com.capitalone.socialApiFb.model.PostAllData;
import com.capitalone.socialApiFb.model.Posts;
import com.capitalone.socialApiFb.model.SharedPosts;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.socialapi.scheduler.TwitterConfig;

import twitter4j.Paging;
import twitter4j.TwitterException;

public class SampleJob extends QuartzJobBean {
	
	
	private static final Logger log = LoggerFactory.getLogger(SampleJob.class);
	@Autowired
	private  Environment env;
	private String name;
	
	// For twitter
	@Autowired
	TwitterConfig twitterservice ;
	
	
	
//	Defining Dates for the data collection time frame.
//	will give you the current machine /Server time
	public  LocalDate untildate=LocalDate.now();
	
	// The from date variable will give us the the date from which  we want fetch data .
	// The minus month will give the number of month we go back to fetch data 
	 public LocalDate  fromdate = untildate.minusMonths(9);
	 
	 
	
	
//	Specifying the date range from to get the post with in the page
	//LocalDate untildate = LocalDate.now(); // Or whatever you want
	
	
	//public LocalDate fromdate = null;


//System.out.println("the lastmonth   date is "+fromdate);
	
	
	
	
	
	
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	 SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd hh/mm/ss");
	ObjectMapper mapper = new ObjectMapper();
	 RestTemplate restTemplate = new RestTemplate();
	 
	 
	// Invoked if a Job data map entry with that name
	public void setName(String name) {
		this.name = name;
	}
	
	
	

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		
		
		// The code to pass the proxy in fire-wall environment
		System.setProperty("https.proxyHost", "irvcache.capgroup.com");
				System.setProperty("https.proxyPort", "8080");
		
				
				
		// below method code will fetch data for previous day on dailybasis
		preparedataperpostDailybasis();
		
		
		
		//System.out.println(String.format("Hello %s!", this.name));
		// this method fetches all data including old values as well 
		PostAllData alldata=savepostimpressions();
		
		
		try {
			savepostimpressionscsv();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// this method only feteched post insights not comments and shares details 
		savepostimpressionsonly();
		//method to get page information
		savepageinformation();
//		method to get page insights per day
		getPerDay();
//		method to get page Insights per week
		getPerWeek();
//		method to get page Insights in 28 days
		getPageLifetime();

// method to  Save the lifetime page information.
		//savepageinformation();
//method to get data for the post level information
				
		
		savepostinformation();
		
		
//method for  twitter API
		TwitterApiController();
	}
	
	
	// Calling the twitter API and getting the detail of the weach post..
//	 gets all the  post of then user Time-line.. 
	
	
//	Get date for yesterday.. to calculate delta..
	public static Date yesterday() {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	    return cal.getTime();
	}
	
	
	// this method gets data on life time basis
		public PostAllData savepostimpressionscsv() throws Exception
		{
			
			  String url="";
			log.info("inside savepostimpressions START");
			log.info(env.getProperty("base.url"));
			String pageId="";
			pageId=env.getProperty("pageid");
			url=env.getProperty("base.url")+pageId+"/posts?"
					+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
					+env.getProperty("suffixparams");
			log.info("url is "+url);
		    JsonNode node = restTemplate.getForObject(url, JsonNode.class);
		  //Get file from resources folder
		    log.info("posts are: "+node);
			
			Posts posts =mapper.convertValue(node, Posts.class);
			log.info("post data is : "+posts.toString());
		/*	ArrayList<PagePostImpression> allimpressions=new ArrayList<>();*/
			PostAllData alldata= new PostAllData();

			/*lines to save data in csv format
			 * 
			 * 
			 * */
			String impressions_directorypath="src/main/resources/"+"folder_impressions_"+dateFormat2.format(Calendar.getInstance().getTime());
			String impressionsfilename=impressions_directorypath+"/impressions_perpost_data_perday_"+dateFormat2.format(Calendar.getInstance().getTime())+"_"+pageId+".csv";
			Path newFilePath = Paths.get(impressionsfilename);
			Path directory = Paths.get(impressions_directorypath);
			File file = new File(impressionsfilename);
			if (file.exists() && file.isFile())
			  {
				log.info("filealready exists by name "+impressionsfilename);
				log.info("deleteing file");
			  file.delete();
			  }
			Files.createDirectories(directory);
			Files.createFile(newFilePath);
			FileWriter impressions_writer = new FileWriter(impressionsfilename);
			// set columns in csv
			CSVUtils.writeLine(impressions_writer, Arrays.asList("PostID ","Reaction Name","Count Value", "Date"));
			
			HashMap<String, Integer> impressions_new=new HashMap<>();
			for(Post p: posts.getData())
			{
				url=env.getProperty("base.url")+p.getId()+"/insights?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("methodname")
						+env.getProperty("fbmetrics")+"["+env.getProperty("metrics")+"]"
						+"&period=lifetime"
						+env.getProperty("suffixparams");
						log.info("url is "+url);
					node = restTemplate.getForObject(url, JsonNode.class);
					log.info("impressionsdata is: "+node.asText().toString());
					PagePostImpressions impressions=mapper.convertValue(node, PagePostImpressions.class);
					
					//allimpressions.addAll(impressions.getData());
					// getting comments for each posts


					for(PagePostImpression po:impressions.getData())
					{
						String val="0";
						if(!po.getValues().get(0).get("value").asText().equals(""))
						{
							val=po.getValues().get(0).get("value").asText();
						}
						CSVUtils.writeLine(impressions_writer, Arrays.asList(p.getId(),po.getName(),val,dateFormat2.format(Calendar.getInstance().getTime())));	
						impressions_new.put(p.getId()+"_"+po.getName(), Integer.parseInt(val));
					}
					
			}
			impressions_writer.flush();
			impressions_writer.close();
			// read old files
			
			// read old file 
			impressions_directorypath="src/main/resources/"+"folder_impressions_"+dateFormat2.format(yesterday());
			impressionsfilename=impressions_directorypath+"/impressions_perpost_data_perday_"+dateFormat2.format(yesterday())+"_"+pageId+".csv";
					
					 HashMap<String, Integer> oldimpressions=CSVReaderUtils.readPostImpressionsFile(impressionsfilename, ",");
					System.out.println("***** old impressions ******");
				
					// calculate delta
					// write the delta
					impressions_directorypath="src/main/resources/"+"folder_impressions_delta_"+dateFormat2.format(Calendar.getInstance().getTime());
					impressionsfilename=impressions_directorypath+"/impressions_perpost_delta_perday_"+dateFormat2.format(Calendar.getInstance().getTime())+"_"+pageId+".csv";
					  directory = Paths.get(impressions_directorypath);
						 newFilePath = Paths.get(impressionsfilename);	
					 file = new File(impressionsfilename);
						if (file.exists() && file.isFile())
						{
						log.info("filealready exists by name "+impressionsfilename);
						log.info("deleteing file");
						file.delete();
						}
						Files.createDirectories(directory);
						Files.createFile(newFilePath);
						//calculating delta and directly writing them into files
						impressions_writer = new FileWriter(impressionsfilename);
						CSVUtils.writeLine(impressions_writer, Arrays.asList("PageID_PostID_ImpressionType", "count","Date"));
					for(Map.Entry<String, Integer> entry:impressions_new.entrySet())
					{
						// data is present in both calculate delta or else write as is
						System.out.println("key: "+entry.getKey());
						if(oldimpressions.containsKey(entry.getKey()))
						{
							String deltacount=Integer.toString(entry.getValue()-oldimpressions.get(entry.getKey()));
							
							System.out.println("delta is: "+deltacount);
							CSVUtils.writeLine(impressions_writer, Arrays.asList(entry.getKey().toString(),deltacount,dateFormat2.format(Calendar.getInstance().getTime())));
						}
						else
						{
							CSVUtils.writeLine(impressions_writer, Arrays.asList(entry.getKey().toString(),entry.getValue().toString(),dateFormat2.format(Calendar.getInstance().getTime())));
						}
						
						
					}
					impressions_writer.flush();
					impressions_writer.close();
					System.out.println("********** old impressions");
					for(Map.Entry<String, Integer> entry:oldimpressions.entrySet())
					{
						System.out.println(entry.getKey());
					}
					System.out.println("saved delta and data file for impressions");
		return alldata;
		
		}
	
	
	
	
	public void TwitterApiController() 
	{
		
		RestTemplate restTemplate = new RestTemplate();
		
		 
		
		 
		log.info("inside getStatus method");
		ObjectNode result = mapper.createObjectNode();
		try {
			
			Paging paging = new Paging(1, 500);

			// code For  passing the url
					System.setProperty("https.proxyHost", "irvcache.capgroup.com");
					System.setProperty("https.proxyPort", "8080");
					
			JsonNode node = mapper.convertValue(twitterservice.getTwitterinstance().getUserTimeline("AmericanFunds",paging).toString(), JsonNode.class);
			
			
			result.put("data", node);
					
			log.info("the result is" +result);
					
			
			//  to save in resource folder
			
			//JsonNode node = restTemplate.getForObject(data, JsonNode.class);
			//Get file from resources folder

			
			try {
				log.info("saving data in a file:twitter");
				
//Making the new folder/directory for each day to save file
				
				
				//trying to place in drive
//				for now we are saving in local c drive 
//				we change this later to save in Shared drive. 
				log.info("Saving twitter data in Twitter.json file under the drive");
				String filesaveindrive =("C:\\Directory1"+"\\folder_"+dateFormat2.format(Calendar.getInstance().getTime()));
				String filename1=filesaveindrive+"/twitter"+".json";
				Path newFilePath1 = Paths.get(filename1);
				Path directory1 = Paths.get(filesaveindrive);
				File file1 = new File(filename1);
				if (file1.exists() && file1.isFile())
				{
					log.info("filealready exists by name "+filename1);
					log.info("deleteing file");
					file1.delete();
				}
				Files.createDirectories(directory1);
				Files.createFile(newFilePath1);
				
				mapper.writerWithDefaultPrettyPrinter().writeValue(file1,node);
				
				
				
				//Saving file in Resource folder
				log.info("Saving twitter file in Twitter.json file under resource folder");
				String directorypath="src/main/resources/"+"folder_"+dateFormat2.format(Calendar.getInstance().getTime());
				String filename=directorypath+"/twitter"+".json";
				Path newFilePath = Paths.get(filename);
				Path directory = Paths.get(directorypath);
				File file = new File(filename);
				if (file.exists() && file.isFile())
				{
					log.info("filealready exists by name "+filename);
					log.info("deleteing file");
					file.delete();
				}
				Files.createDirectories(directory);
				Files.createFile(newFilePath);
				//Object to JSON in file
				//ClassLoader classLoader = getClass().getClassLoader();

				//JsonNode savenode=mapper.convertValue(, JsonNode.class);
				
				log.info(" saving the twitter results in the twitter. json file");
				mapper.writerWithDefaultPrettyPrinter().writeValue(file,result);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//mapper.writeValue(new File("c:\\file.json"), );
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
					
			
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	

	
	
	
	
	
	
	
	// Method to get Data from API  on per Day basis and save in file. 
		//	Page level information only for the Day and save that in resource folder for now.
		//	Only use metrics that can be get in Day  Level
		//	See the read me file for the type of Metrics that can be obtained in day level
		//	File saved as   ------    perday_pageinfo_"+pageid+".json   ----

		public void getPerDay() {

			
			RestTemplate restTemplate = new RestTemplate();
			String url="";
			log.info("inside getPerDay START");
			log.info(env.getProperty("base.url"));
			String pageid="";
			pageid=env.getProperty("pageid");
			log.info("posts are: "+pageid);

			url=env.getProperty("base.url")+pageid+env.getProperty("fbapitype")
			+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")+env.getProperty("fields")+env.getProperty("fbmetrics")+"["+env.getProperty("pagemetrics")+"]"
			+"&period=day"+env.getProperty("suffixparams");
			log.info("url is "+url);
			
			// Code if there is Proxy setting in the firewall
			
			System.setProperty("http.proxyHost", "irvcache.capgroup.com");
			System.setProperty("http.proxyPort", "8080");
			Authenticator authenticator = new Authenticator() {
			     public PasswordAuthentication getPasswordAuthentication() {
			    return (new PasswordAuthentication("INYSKQK","Kathmandu08".toCharArray()));
			    }
			};
			Authenticator.setDefault(authenticator);
			
			
			
			
			
			JsonNode node = restTemplate.getForObject(url, JsonNode.class);
			//Get file from resources folder

			ObjectMapper mapper = new ObjectMapper();
			try {
				log.info("saving data in a file:perday_pageinfo");
				
//Making the new folder/directory for each day to save file
				
				
				//trying to place in drive
				String filesaveindrive =("C:\\Directory1"+"\\folder_"+dateFormat2.format(Calendar.getInstance().getTime()));
				String filename1=filesaveindrive+"/perday_pageinfo_"+pageid+"-----"+dateFormat2.format(Calendar
						.getInstance().getTime())+".json";
				Path newFilePath1 = Paths.get(filename1);
				Path directory1 = Paths.get(filesaveindrive);
				File file1 = new File(filename1);
				if (file1.exists() && file1.isFile())
				{
					log.info("filealready exists by name "+filename1);
					log.info("deleteing file");
					file1.delete();
				}
				Files.createDirectories(directory1);
				Files.createFile(newFilePath1);
				
				mapper.writerWithDefaultPrettyPrinter().writeValue(file1,node);
				
				
				
				//Saving file in Resource folder
				
				
				
				
				String directorypath="src/main/resources/"+"folder_"+dateFormat2.format(Calendar.getInstance().getTime());
				String filename=directorypath+"/perday_pageinfo_"+pageid+"-----"+dateFormat2.format(Calendar
						.getInstance().getTime())+".json";
				Path newFilePath = Paths.get(filename);
				Path directory = Paths.get(directorypath);
				File file = new File(filename);
				if (file.exists() && file.isFile())
				{
					log.info("filealready exists by name "+filename);
					log.info("deleteing file");
					file.delete();
				}
				Files.createDirectories(directory);
				Files.createFile(newFilePath);
				//Object to JSON in file
				//ClassLoader classLoader = getClass().getClassLoader();

				//JsonNode savenode=mapper.convertValue(, JsonNode.class);
				mapper.writerWithDefaultPrettyPrinter().writeValue(file,node);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//mapper.writeValue(new File("c:\\file.json"), );
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			log.info("output is "+node.toString());
			ApiResponseMessage resp= new ApiResponseMessage(4, "success", node);
			log.info("inside getPerDay Page-Insights END");
			
		}
	
	
	
		// Method to get Data from API  on per week basis and save in file.
		//Page level information only for the week and save that in resource folder for now.
		//	Only use metrics that can be get in Week  Level
		//	See the read me file for the type of Metrics that can be obtained in week level
		//	File saved as   -------   perweek_pageinfo_"+pageid+".json    -------
	
		public void getPerWeek() {
			RestTemplate restTemplate = new RestTemplate();
			String url="";
			log.info("inside getPerWeek page Insights START");
			log.info(env.getProperty("base.url"));

			String pageid="";
			pageid=env.getProperty("pageid");
			log.info("page id of the page is "+pageid);
			url=env.getProperty("base.url")+pageid+env.getProperty("fbapitype")+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
			+env.getProperty("fields")+env.getProperty("fbmetrics")+"["+env.getProperty("pagemetrics")+"]"+"&period=week"+env.getProperty("suffixparams");

			log.info("url per week  is "+url);
			JsonNode node = restTemplate.getForObject(url, JsonNode.class);

			//get file from resources folder
			ObjectMapper mapper= new ObjectMapper();

			try {
				log.info("saving data in a file:_perweek_page info");
				
//Making the new folder/directory for each day to save file
				
				
				//trying to place in drive
				String filesaveindrive =("C:\\Directory1"+"\\folder_"+dateFormat2.format(Calendar.getInstance().getTime()));
				String filename1=filesaveindrive+"/perweek_pageinfo_"+pageid+"-----"+dateFormat2.format(Calendar
						.getInstance().getTime())+".json";
				Path newFilePath1 = Paths.get(filename1);
				Path directory1 = Paths.get(filesaveindrive);
				File file1 = new File(filename1);
				if (file1.exists() && file1.isFile())
				{
					log.info("filealready exists by name "+filename1);
					log.info("deleteing file");
					file1.delete();
				}
				Files.createDirectories(directory1);
				Files.createFile(newFilePath1);
				
				mapper.writerWithDefaultPrettyPrinter().writeValue(file1,node);
				
				
				
				//Saving file in Resource folder
				
				
				
				String directorypath="src/main/resources/"+"folder_"+dateFormat2.format(Calendar.getInstance().getTime());
				String filename=directorypath+"/perweek_pageinfo_"+pageid+"-----"+dateFormat2.format(Calendar
						.getInstance().getTime())+".json";
				Path newFilePath = Paths.get(filename);
				Path directory = Paths.get(directorypath);
				File file = new File(filename);
				if (file.exists() && file.isFile())
				{
					log.info("filealready exists by name "+filename);
					log.info("deleteing file");
					file.delete();
				}
				Files.createDirectories(directory);
				Files.createFile(newFilePath);
				//Object to JSON in file
				//ClassLoader classLoader = getClass().getClassLoader();

				//JsonNode savenode=mapper.convertValue(, JsonNode.class);
				mapper.writerWithDefaultPrettyPrinter().writeValue(file,node);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//mapper.writeValue(new File("c:\\file.json"), );
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("output is "+node.toString());
			ApiResponseMessage resp= new ApiResponseMessage(4, "success", node);
			log.info("inside-getPerWeek-page-Insights-END");
			
		}

	
		// Method to get Data from API  on per 28 days or lifetime basis and save in file. 
		//		Page level information only for the 28 days and save that in resource folder for now.
		//		Only use metrics that can be get in Lifetime or 28 days Level
		//		See the read me file for the type of Metrics that can be obtained in lifetime or 28 days level
		//		File saved as  ------   per_28days_pageinfo_"+pageid+".json  -----
		
		public void getPageLifetime() {
			RestTemplate restTemplate = new RestTemplate();
			String url="";
			log.info("inside getPageLifetime/28 days- page Insights START");
			log.info(env.getProperty("base.url"));

			String pageid="";
			pageid=env.getProperty("pageid");
			log.info("page id for the page id "+pageid);

			url=env.getProperty("base.url")+pageid+env.getProperty("fbapitype")+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
			+env.getProperty("fields")+env.getProperty("fbmetrics")+"["+env.getProperty("pagemetrics")+"]"
					+"&period=days_28"+env.getProperty("suffixparams");

			log.info("url is "+url);
			JsonNode node = restTemplate.getForObject(url, JsonNode.class);

			//get file from resources folder
			ObjectMapper mapper= new ObjectMapper();

			try {
				log.info("saving data in a file: pagelifetime/28days_page info");
				
				
				//Making the new folder/directory for each day to save file
				
				
				//trying to place in drive
				String filesaveindrive =("C:\\Directory1"+"\\folder_"+dateFormat2.format(Calendar.getInstance().getTime()));
				String filename1=filesaveindrive+"/per_28days_pageinfo_"+pageid+"-----"+dateFormat2.format(Calendar
						.getInstance().getTime())+".json";
				Path newFilePath1 = Paths.get(filename1);
				Path directory1 = Paths.get(filesaveindrive);
				File file1 = new File(filename1);
				if (file1.exists() && file1.isFile())
				{
					log.info("filealready exists by name "+filename1);
					log.info("deleteing file");
					file1.delete();
				}
				Files.createDirectories(directory1);
				Files.createFile(newFilePath1);
				
				mapper.writerWithDefaultPrettyPrinter().writeValue(file1,node);
				
				
				
				//Saving file in Resource folder
				
				
				String directorypath="src/main/resources/"+"folder_"+dateFormat2.format(Calendar.getInstance().getTime());
				String filename=directorypath+"/per_28days_pageinfo_"+pageid+"-----"+dateFormat2.format(Calendar
						.getInstance().getTime())+".json";
				Path newFilePath = Paths.get(filename);
				Path directory = Paths.get(directorypath);
				File file = new File(filename);
				if (file.exists() && file.isFile())
				{
					log.info("filealready exists by name "+filename);
					log.info("deleteing file");
					file.delete();
				}
				Files.createDirectories(directory);
				Files.createFile(newFilePath);
				//Object to JSON in file
				//ClassLoader classLoader = getClass().getClassLoader();

				//JsonNode savenode=mapper.convertValue(, JsonNode.class);
				mapper.writerWithDefaultPrettyPrinter().writeValue(file,node);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//mapper.writeValue(new File("c:\\file.json"), );
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("output is "+node.toString());
			ApiResponseMessage resp= new ApiResponseMessage(4, "success", node);
			log.info("inside-getPerlifetime-page-Insights-END");
			
		}	

		//API for getting post level insight information
		//Needed to add metrics during execution
		//	Gives you lifetime metrics for all the post 
		//See the read me documentation  for  the available metrics and their meanings also can visit Facebook insights documentation
		//	File saved as ------   postinsights_lifetime_"+pageId+".json   -----
		
		public  PostAllData  savepostimpressionsonly() {
			  String url="";
				log.info("inside savepostimpressionsonly START");
				log.info(env.getProperty("base.url"));
				String pageId="";
				pageId=env.getProperty("pageid");
				log.info("posts are: "+pageId);
				
/*//				Specifying the date range from to get the post with in the page
				LocalDate untildate = LocalDate.now(); // Or whatever you want
				
				
				System.out.println("the todays   date is "+untildate);
				LocalDate fromdate = null;
			 fromdate = untildate.minusMonths(1);
			
			System.out.println("the lastmonth   date is "+fromdate);*/
				
				
				
				url=env.getProperty("base.url")+pageId+"/posts?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")+ "&since=" + fromdate + "&until=" + untildate
						+env.getProperty("suffixparams");
				log.info("url is "+url);
			    JsonNode node = restTemplate.getForObject(url, JsonNode.class);
			  //Get file from resources folder
			    log.info("posts are: "+node);
				
				Posts posts =mapper.convertValue(node, Posts.class);
				log.info("post data is : "+posts.toString());
			/*	ArrayList<PagePostImpression> allimpressions=new ArrayList<>();*/
				PostAllData alldata= new PostAllData();
				
				for(Post p: posts.getData())
				{
					url=env.getProperty("base.url")+p.getId()+"/insights?"
							+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
//							+ "&since=" + fromdate + "&until=" + untildate
							+env.getProperty("methodname")
							+env.getProperty("fbmetrics")+"["+env.getProperty("metrics")+"]"
							+"&period=lifetime"
							+env.getProperty("suffixparams");
							log.info("url is "+url);
						node = restTemplate.getForObject(url, JsonNode.class);
						log.info("impressionsdata is: "+node.asText().toString());
						PagePostImpressions impressions=mapper.convertValue(node, PagePostImpressions.class);
						alldata.getAllimpressionsData().addAll(impressions.getData());
						
						
					/*	//allimpressions.addAll(impressions.getData());
						// getting comments for each posts
						log.info("getting commens data ");
						url=env.getProperty("base.url")+p.getId()+"/comments?"
								+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
								+env.getProperty("suffixparams")+"&summary=1";
						log.info("url for comment api is "+url);
						node = restTemplate.getForObject(url, JsonNode.class);
						Comments comments=mapper.convertValue(node, Comments.class);
						alldata.getAllcommentsData().add(comments);
						log.info("comment data is :"+comments);
						
						
						// getting shared posts data 
						url=env.getProperty("base.url")+p.getId()+"/sharedposts?"
								+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
								+env.getProperty("suffixparams")+"&summary=1";
						log.info("url for comment api is "+url);
						node = restTemplate.getForObject(url, JsonNode.class);
						SharedPosts sharedposts=mapper.convertValue(node, SharedPosts.class);
						if(!sharedposts.getData().isEmpty())
						{
							log.info("sharedposts data is :"+sharedposts);
							alldata.getAllsharedpostsData().add(sharedposts);
						}
*/
						
				}
				
				log.info("impressions are: "+alldata);
				
						try {
							log.info("saving data in a file: postimpressionsonly_lifetime");
							
							//Making the new folder/directory for each day to save file
							
							
							//trying to place in drive
							String filesaveindrive =("C:\\Directory1"+"\\folder_"+dateFormat2.format(Calendar.getInstance().getTime()));
							String filename1=filesaveindrive+"/postimpressions_lifetime_only_"+pageId+"-----"+dateFormat2.format(Calendar
									.getInstance().getTime())+".json";
							Path newFilePath1 = Paths.get(filename1);
							Path directory1 = Paths.get(filesaveindrive);
							File file1 = new File(filename1);
							if (file1.exists() && file1.isFile())
							{
								log.info("filealready exists by name "+filename1);
								log.info("deleteing file");
								file1.delete();
							}
							Files.createDirectories(directory1);
							Files.createFile(newFilePath1);
							
							mapper.writerWithDefaultPrettyPrinter().writeValue(file1,alldata);
							
							
							
							//Saving file in Resource folder
							
							
							String directorypath="src/main/resources/"+"folder_"+dateFormat2.format(Calendar.getInstance().getTime());
							String filename=directorypath+"/postimpressions_lifetime_only_"+pageId+"-----"+dateFormat2.format(Calendar
									.getInstance().getTime())+".json";
				Path newFilePath = Paths.get(filename);
				Path directory = Paths.get(directorypath);
				File file = new File(filename);
				if (file.exists() && file.isFile())
				  {
					log.info("filealready exists by name "+filename);
					log.info("deleteing file");
				  file.delete();
				  }
				Files.createDirectories(directory);
				Files.createFile(newFilePath);
				//Object to JSON in file
				//ClassLoader classLoader = getClass().getClassLoader();
				
				//JsonNode savenode=mapper.convertValue(, JsonNode.class);
					mapper.writerWithDefaultPrettyPrinter().writeValue(file,alldata);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//mapper.writeValue(new File("c:\\file.json"), );
			catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    
			    log.info("output is "+node.toString());
			    ApiResponseMessage resp= new ApiResponseMessage(4, "success", mapper.convertValue(alldata, JsonNode.class));
			log.info("inside getPerDay END");
			return alldata;
		
		}

	//API for getting post level insight information
		//Needed to add metrics during execution
	//Metrics is taken from the application .properties file
		//	Gives you lifetime metrics for all the post 
		//See the read me documentation  for  the available metrics and their meanings also can visit Facebook insights documentation
		//	File saved as ------   postinsights_lifetime_"+pageId+".json   -----
	
	
	// this method gets data on life time basis specified by the from date and to until date
	public PostAllData savepostimpressions()
	{
		
		  String url="";
		log.info("inside savepostimpressions START");
		log.info(env.getProperty("base.url"));
		String pageId="";
		pageId=env.getProperty("pageid");
		log.info("posts are: "+pageId);
		url=env.getProperty("base.url")+pageId+"/posts?"
				+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")+"&since=" + fromdate + "&until=" + untildate
				+env.getProperty("suffixparams");
		log.info("url is "+url);
	    JsonNode node = restTemplate.getForObject(url, JsonNode.class);
	  //Get file from resources folder
	    log.info("posts are: "+node);
		
		Posts posts =mapper.convertValue(node, Posts.class);
		log.info("post data is : "+posts.toString());
	/*	ArrayList<PagePostImpression> allimpressions=new ArrayList<>();*/
		PostAllData alldata= new PostAllData();
		
		for(Post p: posts.getData())
		{
			url=env.getProperty("base.url")+p.getId()+"/insights?"
					+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
					+env.getProperty("methodname")
					+env.getProperty("fbmetrics")+"["+env.getProperty("metrics")+"]"
					+"&period=lifetime"
					+env.getProperty("suffixparams");
					log.info("url is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				log.info("impressionsdata is: "+node.asText().toString());
				PagePostImpressions impressions=mapper.convertValue(node, PagePostImpressions.class);
				alldata.getAllimpressionsData().addAll(impressions.getData());
				
				
				//allimpressions.addAll(impressions.getData());
				// getting comments for each posts
				log.info("getting commens data ");
				url=env.getProperty("base.url")+p.getId()+"/comments?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("suffixparams")+"&summary=1";
				log.info("url for comment api is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				Comments comments=mapper.convertValue(node, Comments.class);
				alldata.getAllcommentsData().add(comments);
				log.info("comment data is :"+comments);
				
				
				// getting shared posts data 
				url=env.getProperty("base.url")+p.getId()+"/sharedposts?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("suffixparams")+"&summary=1";
				log.info("url for comment api is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				SharedPosts sharedposts=mapper.convertValue(node, SharedPosts.class);
				if(!sharedposts.getData().isEmpty())
				{
					log.info("sharedposts data is :"+sharedposts);
					alldata.getAllsharedpostsData().add(sharedposts);
				}

				
		}
		
		log.info("impressions are: "+alldata);
		
				try {
					log.info("saving data in a file: postimpressions_lifetime");
					
//Making the new folder/directory for each day to save file
					
					
					//trying to place in drive
					String filesaveindrive =("C:\\Directory1"+"\\folder_"+dateFormat2.format(Calendar.getInstance().getTime()));
					String filename1=filesaveindrive+"/postimpressions_lifetime_"+pageId+"-----"+dateFormat2.format(Calendar
							.getInstance().getTime())+".json";
					Path newFilePath1 = Paths.get(filename1);
					Path directory1 = Paths.get(filesaveindrive);
					File file1 = new File(filename1);
					if (file1.exists() && file1.isFile())
					{
						log.info("filealready exists by name "+filename1);
						log.info("deleteing file");
						file1.delete();
					}
					Files.createDirectories(directory1);
					Files.createFile(newFilePath1);
					
					mapper.writerWithDefaultPrettyPrinter().writeValue(file1,alldata);
					
					
					
					//Saving file in Resource folder
					
					String directorypath="src/main/resources/"+"folder_"+dateFormat2.format(Calendar.getInstance().getTime());
					String filename=directorypath+"/postimpressions_lifetime_"+pageId+"-----"+dateFormat2.format(Calendar
							.getInstance().getTime())+".json";
		Path newFilePath = Paths.get(filename);
		Path directory = Paths.get(directorypath);
		File file = new File(filename);
		if (file.exists() && file.isFile())
		  {
			log.info("filealready exists by name "+filename);
			log.info("deleteing file");
		  file.delete();
		  }
		Files.createDirectories(directory);
		Files.createFile(newFilePath);
		//Object to JSON in file
		//ClassLoader classLoader = getClass().getClassLoader();
		
		//JsonNode savenode=mapper.convertValue(, JsonNode.class);
			mapper.writerWithDefaultPrettyPrinter().writeValue(file,alldata);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mapper.writeValue(new File("c:\\file.json"), );
	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
	    log.info("output is "+node.toString());
	    ApiResponseMessage resp= new ApiResponseMessage(4, "success", mapper.convertValue(alldata, JsonNode.class));
	log.info("inside getPerDay END");
	return alldata;
	
	}
	
	
	
	
	// API to get Page details with the fields.
		//	Not the insights metrics just the fields. and save the data in file in resource folder.
		//	Page Fan count and page Checkins on the page  are included 
		//	File saved  as -----    basic_pageinfo_lifetime_"+pageId+".json   -----
		
		public void savepageinformation()  {

			RestTemplate restTemplate = new RestTemplate();
			String url="";
			log.info("inside save Page Information START");
			log.info(env.getProperty("base.url"));
			String pageId="";
			pageId=env.getProperty("pageid");
			log.info("page ID is: "+pageId);
			String  fields="id,username,name,fan_count,talking_about_count,checkins,website,link,category,affiliation,about,engagement,founded,new_like_count,rating_count,were_here_count,location";
			
			url=env.getProperty("base.url")+ pageId+"/"+"?fields=" +fields +"&"+env.getProperty("accesstoken")
					+env.getProperty("accesstokenVal");

			log.info("url is "+url);

			JsonNode node = restTemplate.getForObject(url, JsonNode.class);

			ObjectMapper mapper = new ObjectMapper();
			try {
				log.info("saving data in a file: basic_pageinfo_lifetime_");
				//Making the new folder/directory for each day to save file
				
				
				//trying to place in drive
				String filesaveindrive =("C:\\Directory1"+"\\folder_"+dateFormat2.format(Calendar.getInstance().getTime()));
				String filename1=filesaveindrive+"/basic_pageinfo_lifetime_"+pageId+"-----"+dateFormat2.format(Calendar
						.getInstance().getTime())+".json";
				Path newFilePath1 = Paths.get(filename1);
				Path directory1 = Paths.get(filesaveindrive);
				File file1 = new File(filename1);
				if (file1.exists() && file1.isFile())
				{
					log.info("filealready exists by name "+filename1);
					log.info("deleteing file");
					file1.delete();
				}
				Files.createDirectories(directory1);
				Files.createFile(newFilePath1);
				
				mapper.writerWithDefaultPrettyPrinter().writeValue(file1,node);
				
				
				
				//Saving file in Resource folder
				String directorypath="src/main/resources/"+"folder_"+dateFormat2.format(Calendar.getInstance().getTime());
				String filename=directorypath+"/basic_pageinfo_lifetime_"+pageId+"-----"+dateFormat2.format(Calendar
						.getInstance().getTime())+".json";
				Path newFilePath = Paths.get(filename);
				Path directory = Paths.get(directorypath);
				File file = new File(filename);
				if (file.exists() && file.isFile())
				{
					log.info("filealready exists by name "+filename);
					log.info("deleteing file");
					file.delete();
				}
				Files.createDirectories(directory);
				Files.createFile(newFilePath);
				//Object to JSON in file
				//ClassLoader classLoader = getClass().getClassLoader();

				//JsonNode savenode=mapper.convertValue(, JsonNode.class);
				mapper.writerWithDefaultPrettyPrinter().writeValue(file,node);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//mapper.writeValue(new File("c:\\file.json"), );
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("output is "+node.toString());
			ApiResponseMessage resp= new ApiResponseMessage(4, "success", node);
			log.info("inside getPosts END");
		
		}
		
		
		
		// API to get post level information Not the insights metrics just the defined fields.in field value. for each post..
		//  provide you  the post comments count and shared count. and like count.
		//File saved as -----   page_all_post_"+pageId+".json   ------
		
		public void savepostinformation() {
			RestTemplate restTemplate = new RestTemplate();
			String url="";
			log.info("inside Save-post-level-information START");
			log.info(env.getProperty("base.url"));
			String pageId="";
			pageId=env.getProperty("pageid");
			log.info("page id is "+pageId);
			ObjectMapper mapper = new ObjectMapper();
			String  fields="id,message,created_time,updated_time,place,name,parent_id,targeting,type,privacy,link,permalink_url,feed_targeting,promotable_id,shares,likes.limit(0).summary(true),comments.limit(0).summary(true)";
			log.info("the fields that we are getting are "+fields);
			
//			Specifying the date range from to get the post with in the page
			//LocalDate untildate = LocalDate.now(); // Or whatever you want
			
			
			log.info("the todays   date is "+untildate);
			//LocalDate fromdate = null;
		// fromdate = untildate.minusMonths(1);
		
		log.info("the last   date is "+fromdate);
			
			
			
			url=env.getProperty("base.url")+pageId+"/"+"posts?"
					+env.getProperty("accesstoken")
					+env.getProperty("accesstokenVal")+ "&since=" + fromdate + "&until=" + untildate
					+"&fields="+ fields;
			
			log.info("url is "+url);
			log.info("url is "+url);
			JsonNode node = restTemplate.getForObject(url, JsonNode.class);
			log.info("posts are: "+node);
			//Get file from resources folder
			try {
				log.info("saving allposts in a file: allpost_basicfields");
				
//Making the new folder/directory for each day to save file
				
				
				//trying to place in drive
				String filesaveindrive =("C:\\Directory1"+"\\folder_"+dateFormat2.format(Calendar.getInstance().getTime()));
				String filename1=filesaveindrive+"/page_all_post_"+pageId+"-----"+dateFormat2.format(Calendar
						.getInstance().getTime())+".json";
				Path newFilePath1 = Paths.get(filename1);
				Path directory1 = Paths.get(filesaveindrive);
				File file1 = new File(filename1);
				if (file1.exists() && file1.isFile())
				{
					log.info("filealready exists by name "+filename1);
					log.info("deleteing file");
					file1.delete();
				}
				Files.createDirectories(directory1);
				Files.createFile(newFilePath1);
				
				mapper.writerWithDefaultPrettyPrinter().writeValue(file1,node);
				
				
				
				//Saving file in Resource folder
				
				String directorypath="src/main/resources/"+"folder_"+dateFormat2.format(Calendar.getInstance().getTime());
				String filename=directorypath+"/page_all_post_"+pageId+"-----"+dateFormat2.format(Calendar
						.getInstance().getTime())+".json";
				Path newFilePath = Paths.get(filename);
				Path directory = Paths.get(directorypath);
				File file = new File(filename);
				if (file.exists() && file.isFile())
				{
					log.info("filealready exists by name "+filename);
					log.info("deleteing file");
					file.delete();
				}
				Files.createDirectories(directory);
				Files.createFile(newFilePath);
				//Object to JSON in file
				//ClassLoader classLoader = getClass().getClassLoader();

				//JsonNode savenode=mapper.convertValue(, JsonNode.class);

				mapper.writerWithDefaultPrettyPrinter().writeValue(file,node);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//mapper.writeValue(new File("c:\\file.json"), );
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// log.info("posts are: "+node);

			log.info("output is "+node.toString());

			ApiResponseMessage resp= new ApiResponseMessage(4, "success", node);
			log.info("inside Post level information END");
		
		}
		
		
	// comments will be per day post will be liftime 
	// as we need to  get the date comments per day on anypost
	public void preparedataperpostDailybasis()
	{
		  String url="";
		log.info("inside savepostimpressions data in day level START");
		log.info(env.getProperty("base.url"));
		String pageId="";
		pageId=env.getProperty("pageid");
		log.info("page id is : "+pageId);
		
		//Code for passing proxies...
		//System.setProperty("https.proxyHost", "irvcache.capgroup.com");
		//System.setProperty("https.proxyPort", "8080");
		
		
		
		
		
		url=env.getProperty("base.url")+pageId+"/posts?"
				+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
				+env.getProperty("suffixparams");
		log.info("url is "+url);
	    JsonNode node = restTemplate.getForObject(url, JsonNode.class);
	  //Get file from resources folder
	    log.info("posts are: "+node);
		
		Posts posts =mapper.convertValue(node, Posts.class);
		log.info("post data is : "+posts.toString());
	/*	ArrayList<PagePostImpression> allimpressions=new ArrayList<>();*/
		PostAllData alldata= new PostAllData();
		
		for(Post p: posts.getData())
		{
			
				// getting comments for each posts
				log.info("getting commens data ");
				url=env.getProperty("base.url")+p.getId()+"/comments?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("suffixparams")
						+env.getProperty("sincedate")
						+env.getProperty("untildate")
						+"&summary=1";
				
				log.info("url for comment api is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				Comments comments=mapper.convertValue(node, Comments.class);
				alldata.getAllcommentsData().add(comments);
				log.info("comment data is :"+comments);
				
				
				
				// getting shared posts data 
				url=env.getProperty("base.url")+p.getId()+"/sharedposts?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("suffixparams")+env.getProperty("sincedate")
						+env.getProperty("untildate")+"&summary=1";
				log.info("url for comment api is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
				SharedPosts sharedposts=mapper.convertValue(node, SharedPosts.class);
				if(!sharedposts.getData().isEmpty())
				{
					log.info("sharedposts data is :"+sharedposts);
					alldata.getAllsharedpostsData().add(sharedposts);
				}
				
				/*//getting likes per posts data
				// getting shared posts data 
				url=env.getProperty("base.url")+p.getId()+"/likes?"
						+env.getProperty("accesstoken")+env.getProperty("accesstokenVal")
						+env.getProperty("suffixparams")+env.getProperty("sincedate")
						+env.getProperty("untildate")+"&summary=1";
				log.info("url for comment api is "+url);
				node = restTemplate.getForObject(url, JsonNode.class);
			LikePosts likeposts=mapper.convertValue(node, LikePosts.class);
				if(!likeposts.getData().isEmpty())
				{
					log.info("likeposts data is :"+likeposts);
					alldata.getAlllikepostsData().add(likeposts);
				}*/
				
				
		}
		
		log.info("impressions are: "+alldata);
		
				try {
					log.info("saving data in a file: postimpressions_lifetime");
					
					//Making the new folder/directory for each day to save file
					
					
					//trying to place in drive
					String filesaveindrive =("C:\\Directory1"+"\\folder_"+dateFormat2.format(Calendar.getInstance().getTime()));
					String filename1=filesaveindrive+"/allpost_data_perday_"+pageId+"-----"+dateFormat2.format(Calendar
							.getInstance().getTime())+".json";
					Path newFilePath1 = Paths.get(filename1);
					Path directory1 = Paths.get(filesaveindrive);
					File file1 = new File(filename1);
					if (file1.exists() && file1.isFile())
					{
						log.info("filealready exists by name "+filename1);
						log.info("deleteing file");
						file1.delete();
					}
					Files.createDirectories(directory1);
					Files.createFile(newFilePath1);
					
					mapper.writerWithDefaultPrettyPrinter().writeValue(file1,alldata);
					
					
					
					//Saving file in Resource folder
					
					String directorypath="src/main/resources/"+"folder_"+dateFormat2.format(Calendar.getInstance().getTime());
					String filename=directorypath+"/allpost_data_perday_"+pageId+"---"+dateFormat2.format(Calendar
							.getInstance().getTime())+".json";
		Path newFilePath = Paths.get(filename);
		Path directory = Paths.get(directorypath);
		File file = new File(filename);
		if (file.exists() && file.isFile())
		  {
			log.info("filealready exists by name "+filename);
			log.info("deleteing file");
		  file.delete();
		  }
		Files.createDirectories(directory);
		Files.createFile(newFilePath);
		//Object to JSON in file
		//ClassLoader classLoader = getClass().getClassLoader();
		
		//JsonNode savenode=mapper.convertValue(, JsonNode.class);
			mapper.writerWithDefaultPrettyPrinter().writeValue(file,alldata);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mapper.writeValue(new File("c:\\file.json"), );
	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
	    log.info("output is "+node.toString());
	    ApiResponseMessage resp= new ApiResponseMessage(4, "success", mapper.convertValue(alldata, JsonNode.class));
	log.info("inside getPerDay END");
		
		
	}

}



