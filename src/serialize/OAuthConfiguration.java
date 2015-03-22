package serialize;

import java.io.Serializable;

import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;

public class OAuthConfiguration implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static String consumerKey = "zo0F2xRn5yDRETNcWGCNRw";
	private static String consumerSecret = "T0ci9KZ2Ra3Olgyz2nBzO4JNJqSOeWBhqTHEWCzDw8";
	private static String accessTokenKey = null;
	private static String accessTokenSecret = null;
	
	public static String getConsumerKey() {
		return consumerKey;
	}
	
	public static String getConsumerSecret() {
		return consumerSecret;
	}
	
	
	public static void setAccessTokenKey(String theAccessTokenKey) {
		accessTokenKey = theAccessTokenKey;
	}
	
	public static void setAccessTokenSecret(String theAccessTokenSecret) {
		accessTokenSecret = theAccessTokenSecret;
	}
	
	public static AccessToken getAccessToken() {
		return new AccessToken(accessTokenKey, accessTokenSecret);
	}
	
	public static OAuthAuthorization createConfiguration() {
		ConfigurationBuilder builder = new ConfigurationBuilder()
		.setOAuthConsumerKey(consumerKey)
		.setOAuthConsumerSecret(consumerSecret)
		.setOAuthAccessToken(accessTokenKey)
		.setOAuthAccessTokenSecret(accessTokenSecret);
		
		OAuthAuthorization oauth = new OAuthAuthorization(builder.build());

		return oauth;
	}
	
	@Override
	public String toString() {
		return "ConsumerKey = [[" + consumerKey + "]]\n"
				+ "ConsumerKey = [[" + consumerSecret + "]]\n"
				+ "ConsumerKey = [[" + accessTokenKey + "]]\n"
				+ "ConsumerKey = [[" + accessTokenSecret + "]]\n";
	}
	
}
