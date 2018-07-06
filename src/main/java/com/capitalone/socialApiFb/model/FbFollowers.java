package com.capitalone.socialApiFb.model;

public class FbFollowers {

	String followers_count;
	String date;
	public String getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(String followers_count) {
		this.followers_count = followers_count;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public FbFollowers() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FbFollowers(String followers_count, String date) {
		super();
		this.followers_count = followers_count;
		this.date = date;
	}
	
	
}
