package com.capitalone.socialApiFb.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PagePostImpression {
	String name;
	String period;
	List<JsonNode>values;
	String title;
	String description;
	String id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public List<JsonNode> getValues() {
		return values;
	}
	public void setValues(List<JsonNode> values) {
		this.values = values;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public PagePostImpression(String name, String period,
			List<JsonNode> values, String title, String description, String id) {
		super();
		this.name = name;
		this.period = period;
		this.values = values;
		this.title = title;
		this.description = description;
		this.id = id;
	}
	public PagePostImpression() {
		super();
	}
	@Override
	public String toString() {
		return "PagePostImpression [name=" + name + ", period=" + period
				+ ", values=" + values + ", title=" + title + ", description="
				+ description + ", id=" + id + "]";
	}

	
}
