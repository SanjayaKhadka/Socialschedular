package com.capitalone.socialApiFb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SharedPost {
	private String created_time;
	private String story;
	private String id;
	public String getCreated_time() {
		return created_time;
	}
	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}
	public String getStory() {
		return story;
	}
	public void setStory(String story) {
		this.story = story;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SharedPost(String created_time, String story, String id) {
		super();
		this.created_time = created_time;
		this.story = story;
		this.id = id;
	}
	public SharedPost() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "SharedPost [created_time=" + created_time + ", story=" + story
				+ ", id=" + id + "]";
	}
	
	
}
