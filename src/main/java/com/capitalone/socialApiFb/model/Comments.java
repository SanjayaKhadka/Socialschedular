package com.capitalone.socialApiFb.model;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comments 
{
	ArrayList<Comment>data;
	private JsonNode summary;
	
	public ArrayList<Comment> getData() {
		return data;
	}
	public void setData(ArrayList<Comment> data) {
		this.data = data;
	}
	public JsonNode getSummary() {
		return summary;
	}
	public void setSummary(JsonNode summary) {
		this.summary = summary;
	}
	public Comments(ArrayList<Comment> data, JsonNode summary) {
		super();
		this.data = data;
		this.summary = summary;
	}
	public Comments() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
