package main.java.handlers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.java.util.Credentials;
import main.java.util.HttpUtils;
import main.java.util.RateLimiter;
import main.java.util.Token;

public class RedditApiHandler implements IApiHandler {

	private Token token;
	private List<RateLimiter> limiters = new LinkedList<>();

	public RedditApiHandler() {
		this.token = null;
		this.limiters.add(new RateLimiter(60, 60000));  // 60 requests per minute; currently unimplemented
	}

	@Override
	public void requestToken() {
		
		String requestUri = "https://ssl.reddit.com/api/v1/access_token";

		// build request properties
		Map<String, String> requestProperties = new HashMap<>();
		requestProperties.put("User-Agent", Credentials.getRedditAppUserAgent());
		String auth = Credentials.getRedditAppId() + ":" + Credentials.getRedditAppSecret();
		requestProperties.put("Authorization",
				"Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8)));

		// build request parameters
		Map<String, String> requestParameters = new HashMap<>();
		requestParameters.put("grant_type", "client_credentials");

		// execute request
		JSONObject responseJSON = HttpUtils.executeHttpRequest(requestUri, "POST", 
				requestProperties, requestParameters);
		
		// process response
		String accessToken;
		long expiresIn;
		try {
			accessToken = responseJSON.getString("access_token");
			expiresIn = responseJSON.getLong("expires_in");
		} catch (JSONException e) {
			accessToken = null;
			expiresIn = 0;
		}
		
		// save token
		this.token = new Token(accessToken, System.currentTimeMillis() + expiresIn);
	}

	@Override
	public boolean hasValidToken() {
		return !(this.token.isExpired());
	}

	@Override
	public JSONObject makeQuery(String q) {
		
		String requestUri = "https://oauth.reddit.com/search";
		
		// build request properties
		Map<String, String> requestProperties = new HashMap<>();
		requestProperties.put("User-Agent", Credentials.getRedditAppUserAgent());
		requestProperties.put("Authorization", "bearer " + this.token.getToken());
		
		// build request parameters
		Map<String, String> requestParameters = new HashMap<>();
	 	requestParameters.put("q", q);	
	 	requestParameters.put("limit", "10");

	 	// process response
	 	JSONObject responseJSON = HttpUtils.executeHttpRequest(requestUri, "GET", 
	 			requestProperties, requestParameters);
	 	
	 	return formatQueryJSON(responseJSON);
	}
	
	private JSONObject formatQueryJSON(JSONObject responseData) {
		
		JSONObject outJSON = new JSONObject();
		try {
			
			// ensure correct response format
			assert(responseData.getString("kind").equals("Listing"));
			JSONArray outPosts = new JSONArray();
			JSONArray inPosts = responseData.getJSONObject("data").getJSONArray("children");
			
			// populate post data fields
			for (int i = 0; i < inPosts.length(); i++) {
				JSONObject currentPost = inPosts.getJSONObject(i).getJSONObject("data");
				if (currentPost.getBoolean("over_18")) continue;  // skip posts flagged for mature content
				JSONObject postData = new JSONObject();
				postData.put("platform", "reddit");
				postData.put("created_at", currentPost.getInt("created_utc"));
				postData.put("post_id", hashPostID(currentPost.getString("name")));
				postData.put("lang", "");
				postData.put("title", currentPost.getString("title"));
				postData.put("text", currentPost.getString("selftext"));
				postData.put("poster_id", hashPoster(currentPost.getString("author_fullname")));
				postData.put("positive_votes", currentPost.getInt("score"));
				postData.put("sentiment_score", "Neutral");
				postData.put("sentiment_confidence", 0.0);
				postData.put("has_embedded_media", currentPost.get("secure_media") != JSONObject.NULL);
				postData.put("comment_count", currentPost.getInt("num_comments"));
				postData.put("top_comments", new JSONArray());
				outPosts.put(postData);
			}
			
			// add post to object
			outJSON.put("posts", outPosts);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outJSON;
	}

	private String hashPostID(String id) {
		// TODO post id hashing unimplemented for now
		return id;
	}
	
	private String hashPoster(String poster) {
		// TODO poster name hashing unimplemented for now
		return poster;
	}

}
