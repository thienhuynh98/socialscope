package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class TwitterApi {
	
	
        private final static String getTokenURL = "https://api.twitter.com/oauth2/token";
        private static String bearerToken;
	    public static final String CONSUMER_KEY = "Hx0UGvHKBJ28vyFUJC9QzlBui";
	    public static final String CONSUMER_SECRET= "VYDMrBZkbpdLdPzNB0A08R7k3AiiB900mrsrVAWYhhOCGoa6Ns";

        /**
	     * @param args
	     */
	    public static void main(String[] args) {

	        new Thread(new Runnable() {

	            @Override
	            public void run() {
	                try {

	                    bearerToken = requestBearerToken(getTokenURL);
	                    String query = "tesla";
	            		ArrayList<Tweet> tweets = fetchSearchTweet("https://api.twitter.com/1.1/search/tweets.json?",
	                                                               bearerToken, query);
		                System.out.println();
		                System.out.println("Query: " + query);
		                System.out.println("Printing array of tweets usernames, messages and urls");
	            		System.out.println(tweets);
		                System.out.println();
		                System.out.println("Printing the size of tweets");
	                    System.out.println(tweets.size());
	            		

	                    
	                    

	                } catch (IOException e) {
	                    System.out.println("IOException e");
	                    e.printStackTrace();
	                }
	            }
	        }).start();

	    }

	    // Encodes the consumer key and secret to create the basic authorization key
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


	    // Fetches the first tweet from a given user's timeline
	        public static ArrayList<Tweet> fetchSearchTweet(String endPointUrl, String aBearerToken, String query)
	                throws IOException {
	            HttpsURLConnection connection = null;
	            
	        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

	            try {
	            	Map<String, String> parameters = new HashMap<>();
	            	parameters.put("q", query);
	                URL url = new URL(endPointUrl +  buildParameterString(parameters));
	                connection = (HttpsURLConnection) url.openConnection();
	                connection.setDoOutput(true);
	                connection.setDoInput(true);
	                connection.setRequestMethod("GET");
	                connection.setRequestProperty("Host", "api.twitter.com");
	                connection.setRequestProperty("User-Agent", "anyApplication");
	                connection.setRequestProperty("Authorization", "Bearer " +  aBearerToken);
	                connection.setUseCaches(false);
	            	
	                String response = readResponse(connection);

	                System.out.println("Response = " + response);
	                System.out.println(connection.getResponseMessage());
	                System.out.println(connection.getResponseCode());
	                System.out.println("---------------------------------");

	                // Parse the JSON response into a JSON mapped object to fetch fields from.
	                JSONObject objSearch = (JSONObject) JSONValue.parse(response);
	                JSONArray ja = (JSONArray) objSearch.get("statuses");
	                System.out.println("Printing the Json array of statuses");
	                System.out.println(ja);
	                if (ja != null) {
	                    for (int i = 0; i < ja.size(); i++)
	                    {
	                        Tweet tweet = new Tweet((((JSONObject)((JSONObject) ja.get(i)).get("user")).get("screen_name").toString()), 
	                                                ((JSONObject) ja.get(i)).get("text").toString(), 
	                                                (((JSONObject)((JSONObject) ja.get(i)).get("user")).get("profile_image_url").toString()));
	                        tweets.add(tweet);
	                    }
	                    
	                }
	                return tweets;
	            } catch (MalformedURLException e) {
	                throw new IOException("Invalid endpoint URL specified.", e);
	            } finally {
	                if (connection != null) {
	                    connection.disconnect();
	                }
	            }
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
	        public String gettext()
	        {
	        	return message;
	        }
	       public String toString()
	        {
	        	return "NAME: "+username+" MESSAGE: "+message+" URL: "+image_url;
	       }
	     }
	    
	    private static String buildParameterString(Map<String, String> parameters) throws UnsupportedEncodingException {
			StringBuilder outString = new StringBuilder();
			for (Map.Entry<String, String> param : parameters.entrySet()) {
				outString.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				outString.append("=");
				outString.append(URLEncoder.encode(param.getValue(), "UTF-8"));
				outString.append("&");
			}
			
			if (outString.length() > 0) outString.setLength(outString.length() - 1);
			return outString.toString();
		}
	    
	   
}
