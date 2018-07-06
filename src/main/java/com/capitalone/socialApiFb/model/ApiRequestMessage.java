package com.capitalone.socialApiFb.model;

public class ApiRequestMessage 
{
	String pageid;
	String nativeurl;
	String platform;
	String acesstoken;
	String requestType;
	String fields;
	String period;
	String metric;
	boolean insights;
	public String getNativeurl() {
		return nativeurl;
	}
	public void setNativeurl(String nativeurl) {
		this.nativeurl = nativeurl;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getAcesstoken() {
		return acesstoken;
	}
	public void setAcesstoken(String acesstoken) {
		this.acesstoken = acesstoken;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	public boolean isInsights() {
		return insights;
	}
	public void setInsights(boolean insights) {
		this.insights = insights;
	}
	public ApiRequestMessage() {
		super();
	}
	public String getPageid() {
		return pageid;
	}
	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
	public ApiRequestMessage(String pageid, String nativeurl, String platform,
			String acesstoken, String requestType, String fields,
			String period, String metric, boolean insights) {
		super();
		this.pageid = pageid;
		this.nativeurl = nativeurl;
		this.platform = platform;
		this.acesstoken = acesstoken;
		this.requestType = requestType;
		this.fields = fields;
		this.period = period;
		this.metric = metric;
		this.insights = insights;
	}
	
	

}
