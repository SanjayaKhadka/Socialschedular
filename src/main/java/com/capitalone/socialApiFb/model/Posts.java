package com.capitalone.socialApiFb.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

/*{
	"data": [
		{
			"created_time": "2018-06-03T21:37:28+0000",
			"message": "mood - music - magic rainy days - fun days test-comment :)",
			"id": "150235238375384_1819652064767018"
		},
		{
			"created_time": "2011-04-28T07:08:41+0000",
			"story": "Music is magic added 3 new photos.",
			"id": "150235238375384_150250518373856"
		},
		{
			"created_time": "2011-04-28T07:02:31+0000",
			"message": "I love listening to music !!!!! n wat abt u ??",
			"id": "150235238375384_150249741707267"
		}
	],
	"paging": {
		"cursors": {
			"before": "Q2c4U1pXNTBYM0YxWlhKNVgzTjBiM0o1WDJsa0R5TXhOVEF5TXpVeU16ZA3pOelV6T0RRNk5URXpOVFl3TkRRME56ZA3hOREkyTWpFNE9BOE1ZAWEJwWDNOMGIzSjVYMmxrRHlBeE5UQXlNelV5TXpnek56VXpPRFJmTVRneE9UWTFNakEyTkRjMk56QXhPQThFZAEdsdFpRWmJGRitZAQVE9PQZDZD",
			"after": "Q2c4U1pXNTBYM0YxWlhKNVgzTjBiM0o1WDJsa0R5UXhOVEF5TXpVeU16ZA3pOelV6T0RRNkxUYzJNalV6TURjek1UUXhNamd6TWpFNU1qVVBER0ZA3YVY5emRHOXllVjlwWkE4ZAk1UVXdNak0xTWpNNE16YzFNemcwWHpFMU1ESTBPVGMwTVRjd056STJOdzhFZAEdsdFpRWk51UkVIQVE9PQZDZD"
		}
	}
}*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Posts 
{
	ArrayList<Post>data;
	private JsonNode paging;
	public ArrayList<Post> getData() {
		return data;
	}
	public void setData(ArrayList<Post> data) {
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
		return "Posts [data=" + data + ", paging=" + paging + "]";
	}
	public Posts() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Posts(ArrayList<Post> data, JsonNode paging) {
		super();
		this.data = data;
		this.paging = paging;
	}
	
	
}
