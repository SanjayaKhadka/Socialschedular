package com.socialapi.scheduler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Service("twitterservice")
public class TwitterConfig 
{
	@Autowired
	private Environment env;
	private Twitter conf;
	
	public Twitter getTwitterinstance()
	{
		return  this.conf;
	}
	 @PostConstruct
	  public void init(){
		 ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey(env.getProperty("twitter.oauth.consumerKey"))
			  .setOAuthConsumerSecret(env.getProperty("twitter.oauth.consumerSecret"))
			  .setOAuthAccessToken(env.getProperty("twitter.oauth.accessToken"))
			  .setOAuthAccessTokenSecret(env.getProperty("twitter.oauth.accessTokenSecret"));
			TwitterFactory tf = new TwitterFactory(cb.build());
			 this.conf=tf.getInstance();
	  }

}
