package main.java.util;

public class Credentials {
	
	// reddit credentials
	
	private static final String REDDIT_APP_ID = "AV79bHKhGrsMuZgmoL2chw";
	private static final String REDDIT_APP_SECRET = "3eYG48zgNaVKoTWJcf3KTfVBin1GNQ";  // PRIVATE: must not be shared
	private static final String REDDIT_USER_AGENT = "SocialScope/0.1 by u/SocialScopeBot";
	private static final String Youtube_USER_AGENT = "SocialScope/0.1";
	private static final String API_KEY = "AIzaSyC4oyxeg5BaxWs2LU3hsvDngZQwX6gSj0s";
	
	
	public static String getRedditAppId() {
		return REDDIT_APP_ID;
	}
	
	public static String getRedditAppSecret() {
		return REDDIT_APP_SECRET;
	}
	
	public static String getRedditAppUserAgent() {
		return REDDIT_USER_AGENT;
	}

	public static String getYoutubeAppUserAgent() {
		return Youtube_USER_AGENT;
	}
	public static String getapikey() {
		return API_KEY ;
	}


	
	// twitter credentials
	// TODO: put any credential constants here for privacy
	
	// youtube credentials
	// TODO: put any credential constants here for privacy
}
