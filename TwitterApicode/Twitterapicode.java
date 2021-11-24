package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/*
 * Sample code to demonstrate the use of the Full archive search endpoint
 * */
public class Twitterapicode {
	 private final static String getTokenURL = "https://api.twitter.com/oauth2/token";
     private static String bearerToken;
	 public static final String CONSUMER_KEY = "Hx0UGvHKBJ28vyFUJC9QzlBui";
	 public static final String CONSUMER_SECRET= "VYDMrBZkbpdLdPzNB0A08R7k3AiiB900mrsrVAWYhhOCGoa6Ns";

	 
	 
	 public static void main(String args[]) throws IOException, URISyntaxException {
		 bearerToken = requestBearerToken(getTokenURL);
		  
		    if (null != bearerToken) {
		 
		    	 ArrayList<Tweet> tweets = search("pizza", bearerToken);
		    	 System.out.println(tweets.size());
		    } else {
		      System.out.println("There was a problem getting you bearer token. Please make sure you set the BEARER_TOKEN environment variable");
		    }
		  }
 

  private static String encodeKeys(String consumerKey, String consumerSecret) {
      try {
          String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
          String encodedConsumerSecret = URLEncoder.encode(consumerSecret,
                  "UTF-8");

          String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;
          byte[] encodedBytes = Base64.encodeBase64(fullKey.getBytes());

          return new String(encodedBytes);
      } catch (UnsupportedEncodingException e) {
          return new String();
      }
  }

  // Constructs the request for requesting a bearer token and returns that
  // token as a string
  public static String requestBearerToken(String endPointUrl)
          throws IOException {
      HttpsURLConnection connection = null;
      String encodedCredentials = encodeKeys(CONSUMER_KEY, CONSUMER_SECRET);

      System.out.println("encodedCredentials "+encodedCredentials);
      try {
          URL url = new URL(endPointUrl);
          connection = (HttpsURLConnection) url.openConnection();
          System.out.println(connection);
          connection.setDoOutput(true);
          connection.setDoInput(true);
          connection.setRequestMethod("POST");
          connection.setRequestProperty("Host", "api.twitter.com");
          connection.setRequestProperty("User-Agent", "Android Phuse Application");
          connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
          connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
          connection.setRequestProperty("Content-Length", "29");
          connection.setUseCaches(false);

          writeRequest(connection, "grant_type=client_credentials");

          // Parse the JSON response into a JSON mapped object to fetch fields
          // from.
          JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));

          if (obj != null) {
              String tokenType = (String) obj.get("token_type");
              String token = (String) obj.get("access_token");

              return ((tokenType.equals("bearer")) && (token != null)) ? token
                      : "";
          }
          return new String();
      } catch (MalformedURLException e) {
          throw new IOException("Invalid endpoint URL specified.", e);
      } finally {
          if (connection != null) {
              connection.disconnect();
          }
      }
  }
  
  
  
  /*
   * This method calls the full-archive search endpoint with a the search term passed to it as a query parameter
   * */
  private static JSONArray search(String searchString, String bearerToken) throws IOException, URISyntaxException {
	    String searchResponse = null;
	    JSONArray ja = null;
	   // ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	    HttpClient httpClient = HttpClients.custom()
	        .setDefaultRequestConfig(RequestConfig.custom()
	            .setCookieSpec(CookieSpecs.STANDARD).build())
	        .build();

	    URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent");
	    ArrayList<NameValuePair> queryParameters;
	    queryParameters = new ArrayList<>();
	    queryParameters.add(new BasicNameValuePair("query", searchString));
        queryParameters.add(new BasicNameValuePair("expansions","referenced_tweets.id"));

	    uriBuilder.addParameters(queryParameters);

	    HttpGet httpGet = new HttpGet(uriBuilder.build());
	    httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
	    httpGet.setHeader("Content-Type", "application/json");

	    HttpResponse response = httpClient.execute(httpGet);
	    HttpEntity entity = response.getEntity();
	    if (null != entity) {
	      searchResponse = EntityUtils.toString(entity, "UTF-8");
	      System.out.println("Response = " + searchResponse);
	      
          System.out.println("---------------------------------");

          // Parse the JSON response into a JSON mapped object to fetch fields from.
          JSONObject objSearch = (JSONObject) JSONValue.parse(searchResponse);
           ja = (JSONArray) objSearch.get("data");
          System.out.println("Printing the Json array of statuses");
          System.out.println(ja);
         
	      
	    }
	    return ja;
	  }
  // Writes a request to a connection
  private static boolean writeRequest(HttpURLConnection connection,
          String textBody) {
      try {
          BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
                  connection.getOutputStream()));
          wr.write(textBody);
          wr.flush();
          wr.close();

          return true;
      } catch (IOException e) {
          return false;
      }
  }

  // Reads a response for a given connection and returns it as a string.
  private static String readResponse(HttpURLConnection connection) {
      try {
          StringBuilder str = new StringBuilder();

          BufferedReader br = new BufferedReader(new InputStreamReader(
                  connection.getInputStream()));
          String line = "";
          while ((line = br.readLine()) != null) {
              str.append(line + System.getProperty("line.separator"));
          }
          return str.toString();
      } catch (IOException e) {
          return new String();
      }
  }

	  public static class Tweet {
	      public String username;
	      public String message;
	      public String image_url;

      public Tweet(String username, String message, String url) {
          this.username  = username;
          this.message   = message;
          this.image_url = url;

          
      }
     
   } 
  
}
