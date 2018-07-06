package com.capitalone.socialApiFb.model;

public class ApiPageRequest {
	String pageid;
	String accesstoken;
	String metrics;
	
	public String getMetrics() {
		return metrics;
	}
	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}
	public String getPageid() {
		return pageid;
	}
	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accesstoken == null) ? 0 : accesstoken.hashCode());
		result = prime * result + ((pageid == null) ? 0 : pageid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApiPageRequest other = (ApiPageRequest) obj;
		if (accesstoken == null) {
			if (other.accesstoken != null)
				return false;
		} else if (!accesstoken.equals(other.accesstoken))
			return false;
		if (pageid == null) {
			if (other.pageid != null)
				return false;
		} else if (!pageid.equals(other.pageid))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ApiPageRequest [pageid=" + pageid + ", accesstoken="
				+ accesstoken + "]";
	}
	public ApiPageRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ApiPageRequest(String pageid, String accesstoken, String metrics) {
		super();
		this.pageid = pageid;
		this.accesstoken = accesstoken;
		this.metrics = metrics;
	}
	

}
