package com.socialapi.scheduler;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;



import twitter4j.*;

public class TwitterTest{
	
	 @Autowired
	public static 
		TwitterConfig twitterservice ;
public static void main(String[] args) {

    


    int pageno = 1;
    String user = "AmericanFunds";
    List statuses = new ArrayList();

    while (true) {

      try {

        int size = statuses.size(); 
        Paging page = new Paging(pageno++, 100);
        statuses.addAll(twitterservice.getTwitterinstance().getUserTimeline(user, page));
        if (statuses.size() == size)
          break;
      }
      catch(TwitterException e) {

        e.printStackTrace();
      }
    }

    System.out.println("Total: "+statuses.size());
}
}