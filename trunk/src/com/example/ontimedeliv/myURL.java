package com.example.ontimedeliv;

public class myURL {
	public myURL(){
		
	}

	public String getURL(String api,String by,int value,int limit){
		String urlPrefix = "http://enigmatic-springs-5176.herokuapp.com/api/v1/";
		String url="";
		if(by ==null)
		{
			url+=api;
		}		
		else
		{
			url+=url+by+"/"+value+"/"+api;
		}
		
		if(limit>0)
		{
			url+="?limit="+limit;
		}
		return urlPrefix+url;
	}
}
