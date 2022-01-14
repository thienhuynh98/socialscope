package main.java.util;

public class Token {

	private String token;
	private long expireTime;
	
	public Token(String token, long expires) {
		this.token = token;
		this.expireTime = expires;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public boolean isExpired() {
		return ((expireTime - System.currentTimeMillis()) <= 0);
	}
	
}
