/*package com.socialapi.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Test {
	public static void main(String[] args) {
		String timeStamp = "2018-01-01T18:23:17+0000";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		
	
	
		LocalDate untildate = LocalDate.now(); // Or whatever you want
		
	
			System.out.println("the todays   date is "+untildate);
			LocalDate fromdate = null;
		 fromdate = untildate.minusMonths(1);
		
		System.out.println("the lastmonth   date is "+fromdate);
		
		String dt = "2008-02-01";  // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		c.add(Calendar.MONTH, -1);  // number of days to add
		dt = sdf.format(c.getTime());  // dt is now the new date
		System.out.println("the current date is "+dt);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date result = cal.getTime();
		
		
		
		
		try {
			System.out.println("Unix timestamp: " + dateFormat.parse(timeStamp).getTime());
		System.out.println(dateFormat2.format(new Date(dateFormat.parse(timeStamp).getTime())));
		System.out.println(dateFormat.format(new Date(dateFormat.parse(timeStamp).getTime())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
*/



package com.socialapi.scheduler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.capitalone.socialApiFb.model.CSVReaderUtils;
import com.capitalone.socialApiFb.model.CSVUtils;

public class Test {
	static String timeStamp = "2014-12-14T18:23:17+0000";
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
	static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	static RestTemplate restTemplate = new RestTemplate();
	public static void main(String[] args) throws IOException {
		/*try {
			System.out.println("Unix timestamp: " + dateFormat.parse(timeStamp).getTime());
		System.out.println(dateFormat2.format(new Date(dateFormat.parse(timeStamp).getTime())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String csvFile ="src/main/resources/some.csv";
        FileWriter writer = new FileWriter(csvFile);

        List<Developer> developers = Arrays.asList(
                new Developer("mkyong", new BigDecimal(120500), 32),
                new Developer("zilap", new BigDecimal(150099), 5),
                new Developer("ultraman", new BigDecimal(99999), 99)
        );

        //for header
        CSVUtils.writeLine(writer, Arrays.asList("Name", "Salary", "Age"));
		
        for (Developer d : developers) {

            List<String> list = new ArrayList<>();
            list.add(d.getName());
            list.add(d.getSalary().toString());
            list.add(String.valueOf(d.getAge()));

            CSVUtils.writeLine(writer, list);

			//try custom separator and quote. 
			//CSVUtils.writeLine(writer, list, '|', '\"');
        }

        writer.flush();
        writer.close();*/
		
		try {
			//checkCSVreader();
			convertBitlyTweet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//main ends here
	public static void convertBitlyTweet() throws Exception
	{
		String tweeturlfilepath="src/main/resources/tweetUrlData";
		String tweeturl=tweeturlfilepath+"/tweetdata.csv";
		String convertedTweet=tweeturlfilepath+"/tweetdataConverted.csv";
		 String line = "";
	        List<String> urls=new ArrayList<String>();
	        FileWriter convertedTweet_writer = new FileWriter(convertedTweet);
			// set columns in csv
			
	    	System.setProperty("https.proxyHost", "irvcache.capgroup.com");
			System.setProperty("https.proxyPort", "8080");
	
	        try (BufferedReader br = new BufferedReader(new FileReader(tweeturl))) {
	        	int count=0;
	            while ((line = br.readLine()) != null) {
	            	String[] lines = line.split(",");
	            	if(count>0)
	            	{
	                // use comma as separator
	                
	                //System.out.println(lines[1]);
	                //urls.add(lines[1]);
	                String htmlpage = restTemplate.getForObject(lines[1], String.class);
					//System.out.println(htmlpage);
					 String filename="src/main/resources/tweetUrlData/input.html";
					 writehtmlinfile(filename, htmlpage);
					 File input = new File(filename);
					 Document doc = Jsoup.parse(input, "UTF-8");
					
					Elements links = doc.select("a[href]").addClass("twitter-timeline-link").select("a[data-expanded-url]").eq(1);
					// a with href
					 //System.out.println(links);
					 //System.out.println(links.get(0).absUrl("data-expanded-url"));
					String bitlyurl="Unable To fetch the expanded bitly url";
					if(links.size()>0)
					{
						bitlyurl=links.get(0).absUrl("data-expanded-url");
					}
					else
					{
						System.out.println("unable to find the bitly expanded version for this url:");
						System.out.println(lines[1]);
					}
					
					 HttpHeaders headers = new HttpHeaders();
					 headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

					 MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
					 map.add("s", bitlyurl);

					 HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

					 ResponseEntity<String> response = restTemplate.postForEntity( "http://urlex.org/", request , String.class );
					//System.out.println(response);
					writehtmlinfile(filename, response.toString());
					input = new File(filename);
					  doc = Jsoup.parse(input, "UTF-8");
					
					 links = doc.select("a[href]").select("a[rel=\"external nofollow\"]").eq(0);
					System.out.println(links.get(0).absUrl("href"));
					List<String>values=new ArrayList<>();
					values.addAll(Arrays.asList(lines));
            		values.add(links.get(0).absUrl("href"));
            		CSVUtils.writeLine(convertedTweet_writer, values);
	            	}
	            	else
	            	{
	            		List<String>header=new ArrayList<>();
	            		header.addAll(Arrays.asList(lines));
	            		header.add("Expanded BitLy Url");
	            		CSVUtils.writeLine(convertedTweet_writer, header);
	            	}
	            	count++;
	            }
	            // while loop ends here
	            convertedTweet_writer.flush();
	            convertedTweet_writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	public static void checkCSVreader() throws Exception
	{
		String tweeturlfilepath="src/main/resources/tweetUrlData";
		String tweeturl=tweeturlfilepath+"/tweetdata.csv";
		List<String>urls=CSVReaderUtils.tweetUrlConverter(tweeturl, ",");
		int count=0;
		
		for(String url:urls)
		{
			/*if(count==0)
			{*/
				String htmlpage = restTemplate.getForObject(url, String.class);
				//System.out.println(htmlpage);
				 String filename="src/main/resources/tweetUrlData/input.html";
				 writehtmlinfile(filename, htmlpage);
				 File input = new File(filename);
				 Document doc = Jsoup.parse(input, "UTF-8");
				
				Elements links = doc.select("a[href]").addClass("twitter-timeline-link").select("a[data-expanded-url]").eq(1);
				// a with href
				 //System.out.println(links);
				 //System.out.println(links.get(0).absUrl("data-expanded-url"));
				String bitlyurl="Unable To fetch the expanded bitly url";
				if(links.size()>0)
				{
					bitlyurl=links.get(0).absUrl("data-expanded-url");
				}
				
				 HttpHeaders headers = new HttpHeaders();
				 headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

				 MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
				 map.add("s", bitlyurl);

				 HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

				 ResponseEntity<String> response = restTemplate.postForEntity( "http://urlex.org/", request , String.class );
				//System.out.println(response);
				writehtmlinfile(filename, response.toString());
				input = new File(filename);
				  doc = Jsoup.parse(input, "UTF-8");
				
				 links = doc.select("a[href]").select("a[rel=\"external nofollow\"]").eq(0);
				System.out.println(links.get(0).absUrl("href"));
				 /*for(Element e:links)
				{
					System.out.println(e.baseUri());
				}*/

			//}
			
			
			//count++;
		}
		
	}
	
	public static Date yesterday() {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	    return cal.getTime();
	}
	public static void writehtmlinfile(String filename, String htmlpage)
	{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {

			

			bw.write(htmlpage);
			
			// no need to close it.
			//bw.close();

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

}

