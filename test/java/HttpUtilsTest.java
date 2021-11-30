package test.java;

import org.json.JSONException;

import main.java.handlers.YoutubeApiHandler;

public class HttpUtilsTest {

	public static void main(String[] args) {
		YoutubeApiHandler youtube = new YoutubeApiHandler();
		try {
			//System.out.println(youtube.makeQuery("zoo"));
			youtube.formatQueryJSON(youtube.makeQuery("zoo"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
