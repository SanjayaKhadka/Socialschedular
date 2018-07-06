package com.capitalone.socialApiFb.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PagePostImpressions {
	ArrayList<PagePostImpression>data;
	private JsonNode paging;
	public ArrayList<PagePostImpression> getData() {
		return data;
	}
	public void setData(ArrayList<PagePostImpression> data) {
		this.data = data;
	}
	public JsonNode getPaging() {
		return paging;
	}
	public void setPaging(JsonNode paging) {
		this.paging = paging;
	}

	@Override
	public String toString() {
		return "PagePostImpressions [data=" + data + ", paging=" + paging + "]";
	}
	public PagePostImpressions(ArrayList<PagePostImpression> data,
			JsonNode paging) {
		super();
		this.data = data;
		this.paging = paging;
	}
	public PagePostImpressions() {
		super();
	}
	
	

}
