package com.capitalone.socialApiFb.model;

import java.util.ArrayList;

public class PostAllData {
	ArrayList<PagePostImpression> allimpressionsData=new ArrayList<>();
	ArrayList<Comments>allcommentsData=new ArrayList<>();
	ArrayList<SharedPosts>allsharedpostsData=new ArrayList<>();
	

	public ArrayList<SharedPosts> getAllsharedpostsData() {
		return allsharedpostsData;
	}
	public void setAllsharedpostsData(ArrayList<SharedPosts> allsharedpostsData) {
		this.allsharedpostsData = allsharedpostsData;
	}
	public ArrayList<PagePostImpression> getAllimpressionsData() {
		return allimpressionsData;
	}
	public void setAllimpressionsData(
			ArrayList<PagePostImpression> allimpressionsData) {
		this.allimpressionsData = allimpressionsData;
	}

	public ArrayList<Comments> getAllcommentsData() {
		return allcommentsData;
	}
	public void setAllcommentsData(ArrayList<Comments> allcommentsData) {
		this.allcommentsData = allcommentsData;
	}
	@Override
	public String toString() {
		return "PostAllData [allimpressionsData=" + allimpressionsData
				+ ", allcommentsData=" + allcommentsData + "]";
	}
	public PostAllData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PostAllData(ArrayList<PagePostImpression> allimpressionsData,
			ArrayList<Comments> allcommentsData) {
		super();
		this.allimpressionsData = allimpressionsData;
		this.allcommentsData = allcommentsData;
	}

	
	
}
