package main.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.java.handlers.IApiHandler;
import main.java.handlers.RedditApiHandler;
import main.java.handlers.TwitterApiHandler;
import main.java.handlers.YoutubeApiHandler;
import main.java.util.Credentials;

public class App {
	
	private static final int MAX_REQUEST_RETRIES = 3;
	
	private static List<IApiHandler> apiHandlers; ;
	
	public static void main(String args[]) {
		Credentials cred = new Credentials();
		apiHandlers = initializeApiHandlers(cred);
		String query = getUserQuery();
		System.out.println("Executing search...");
		JSONObject results = executeSearch(query);
		System.out.println("Writing results to file...");
		writeJsonFile(results);
		System.out.println("Done!");
	}
	
	private static List<IApiHandler> initializeApiHandlers(Credentials cred) {
		List<IApiHandler> handlers = new ArrayList<IApiHandler>();
		handlers.add(new RedditApiHandler(cred.getRedditAppId(), cred.getRedditAppSecret(), cred.getRedditAppUserAgent()));
		handlers.add(new TwitterApiHandler(cred.getTwitterAppId(), cred.getTwitterAppSecret(), cred.getTwitterAppUserAgent()));
		handlers.add(new YoutubeApiHandler(cred.getYoutubeApiKey(), cred.getYoutubeAppUserAgent()));
		return handlers;
	}
	
	private static String getUserQuery() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String userInput = null;
		System.out.println("Enter a query: ");
		try {
			userInput = reader.readLine();
			while (userInput.isBlank()) {
				System.out.println("Please enter valid input.");
				userInput = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userInput.strip();
	}

	private static JSONObject executeSearch(String queryText) {
		JSONObject aggregateResults = new JSONObject();
		try {
			JSONArray aggregatePosts = new JSONArray();
			// get posts
			for (IApiHandler handler : apiHandlers) {
				int counter = 0;
				JSONArray posts = null;
				while (posts == null && counter < MAX_REQUEST_RETRIES) {
					handler.requestToken();
					if (handler.hasValidToken()) posts = handler.makeQuery(queryText).getJSONArray("posts");
					counter++;
				}
				if (posts == null) continue;  // move on to the next api if unable to retrieve posts even after multiple requests
				for (int i = 0; i < posts.length(); i++) aggregatePosts.put(posts.get(i));
			}
			aggregateResults.put("posts", aggregatePosts);
			// add metadata
			JSONObject metaInfo = new JSONObject();
			metaInfo.put("query", queryText);
			aggregateResults.put("meta", metaInfo);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return aggregateResults;
	}
	
	private static void writeJsonFile(JSONObject json) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("aggregateResults.json"));
			writer.write(json.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
