package main.java.util;

public class Credentials {
	
	// reddit credentials
	
	private static final String REDDIT_APP_ID = "AV79bHKhGrsMuZgmoL2chw";
	private static final String REDDIT_APP_SECRET = "3eYG48zgNaVKoTWJcf3KTfVBin1GNQ";  // PRIVATE: must not be shared
	private static final String REDDIT_USER_AGENT = "SocialScope/0.1 by u/SocialScopeBot";

	public static String getRedditAppId() {
		return REDDIT_APP_ID;
	}
	
	public static String getRedditAppSecret() {
		return REDDIT_APP_SECRET;
	}
	
	public static String getRedditAppUserAgent() {
		return REDDIT_USER_AGENT;
	}
	
	// twitter credentials
	// TODO: put any credential constants here for privacy
	
	// youtube credentials
	// TODO: put any credential constants here for privacy
}
