package com.example.uploadfiletoserverwithlocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadParams {
	private String URI;
	private String method;
	private Map<String,String> params=new HashMap<String, String>();
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public void setParam(String string,String value){
		params.put(string, value);
		
	}
}
