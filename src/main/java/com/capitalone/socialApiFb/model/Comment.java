package com.capitalone.socialApiFb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment 
{	
	private String created_time;
	private String message;
	private String id;
	private From  from;
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
	public From getFrom() {
		return from;
	}
	public void setFrom(From from) {
		this.from = from;
	}
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Comment(String created_time, String message, String id, From from) {
		super();
		this.created_time = created_time;
		this.message = message;
		this.id = id;
		this.from = from;
	}
	@Override
	public String toString() {
		return "Comment [created_time=" + created_time + ", message=" + message
				+ ", id=" + id + ", from=" + from + "]";
	}
	
	
	

}
