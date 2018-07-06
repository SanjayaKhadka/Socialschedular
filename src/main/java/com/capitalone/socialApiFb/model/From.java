package com.capitalone.socialApiFb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class From {

	String name;
	String id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "From [name=" + name + ", id=" + id + "]";
	}
	public From(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}
	public From() {
		super();
	}
	
}
