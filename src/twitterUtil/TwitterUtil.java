package twitterUtil;

import javafx.scene.image.Image;
import serialize.OAuthConfiguration;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class TwitterUtil {
	
	//-- TwitterUtilのインスタンス --//
	public static TwitterUtil instance;
	//-- ログインしているユーザーのアイコン --//
	public static Image icon;
	//-- ストリーミングタイムライン用のインスタンス --//
	private static TwitterStream twitterStream;
	//-- ログインしているユーザーのTwitterインスタンス --//
	private static Twitter twitter;
	
	/**
	 * コンストラクタ
	 */
	private TwitterUtil() {
		setupTwitter();
		startStream();
	}
	
	/**
	 * OAuth認証情報をもとにTwitterインスタンスの生成
	 */
	private void setupTwitter() {
		try {
			twitter = new TwitterFactory().getInstance(OAuthConfiguration.createConfiguration());
			icon = new Image(twitter.verifyCredentials().getBiggerProfileImageURL());
		} catch (IllegalStateException | TwitterException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Twitterのインスタンスを返すgetterメソッド
	 * @return twitter OAutu認証したツイッターの情報
	 */
	public static Twitter getTwitter() {
		return twitter;
	}
	
	/**
	 * ストリーミングタイムラインを起動
	 */
	private void startStream() {
		twitterStream = new TwitterStreamFactory().getInstance(OAuthConfiguration.createConfiguration());
		twitterStream.addListener(new MyUserStreamAdapter());
		twitterStream.user();
	}
	
	/**
	 * ログインしているユーザーのアイコンを取得
	 */
	public static Image getIcon() {
		return icon;
	}
	
	public static void createSingleton() {
		if(instance == null) {
			instance = new TwitterUtil();
		}
	}
	
	/**
	 * ストリーミングタイムラインをシャットダウン
	 * 
	 */
	public static void shutdownTwitterStream() {
		if(twitterStream != null) {
			twitterStream.shutdown();
		}
	}
}
