package com.capitalone.socialApiFb.model;

public class FbStories {

	String story_count;
	String date;
	public FbStories() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FbStories(String story_count, String date) {
		super();
		this.story_count = story_count;
		this.date = date;
	}
	public String getStory_count() {
		return story_count;
	}
	public void setStory_count(String story_count) {
		this.story_count = story_count;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
