package main.java.handlers;

import org.json.JSONObject;

public interface IApiHandler {
	
	public void requestToken();
	public boolean hasValidToken();
	public JSONObject makeQuery(String q);
	
}
