package com.capitalone.socialApiFb.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVReaderUtils {
	
	public static HashMap<String, Integer> readCommentsFile(String csvFile,String cvsSplitBy)
	{
        String line = "";
        HashMap<String, Integer> comments=new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	int count=0;
            while ((line = br.readLine()) != null) {
            	if(count>0)
            	{
            		  // use comma as separator
                    String[] lines = line.split(cvsSplitBy);
                    comments.put(lines[0], Integer.parseInt(lines[1]));
                   // System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");

            	}
            	count++;
              
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return comments;
	}
	public static HashMap<String, Integer> readPostImpressionsFile(String csvFile,String cvsSplitBy)
	{
        String line = "";
        HashMap<String, Integer> impressions=new HashMap<String, Integer>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	int count=0;
            while ((line = br.readLine()) != null) {
            	if(count>0)
            	{
                // use comma as separator
                String[] lines = line.split(cvsSplitBy);
                Integer val=0;
                if(!lines[2].equals(""))
                {
                	val=Integer.parseInt(lines[2]);
                }
                
                impressions.put(lines[0]+"_"+lines[1],val);
               // System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
            	}
            	count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return impressions;
	}
	public static List<String> tweetUrlConverter(String csvFile,String cvsSplitBy)
	{
        String line = "";
        List<String> urls=new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	int count=0;
            while ((line = br.readLine()) != null) {
            	if(count>0)
            	{
                // use comma as separator
                String[] lines = line.split(cvsSplitBy);
                //System.out.println(lines[1]);
                urls.add(lines[1]);
            	}
            	count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
	}

}
