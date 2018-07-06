package com.capitalone.socialApiFb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Post 
{
	private String created_time;
	private String message;
	private String id;
	public String getCreated_time() {
		return created_time;
	}
	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Post [created_time=" + created_time + ", message=" + message
				+ ", id=" + id + "]";
	}
	public Post(String created_time, String message, String id) {
		super();
		this.created_time = created_time;
		this.message = message;
		this.id = id;
	}
	public Post() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
