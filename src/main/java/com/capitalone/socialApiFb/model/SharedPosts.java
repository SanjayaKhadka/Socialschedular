package com.capitalone.socialApiFb.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SharedPosts {
	
	ArrayList<SharedPost>data;

	public ArrayList<SharedPost> getData() {
		return data;
	}

	public void setData(ArrayList<SharedPost> data) {
		this.data = data;
	}

	public SharedPosts() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SharedPosts(ArrayList<SharedPost> data) {
		super();
		this.data = data;
	}

	

}
