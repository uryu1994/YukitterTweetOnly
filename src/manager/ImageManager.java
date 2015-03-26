package manager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import application.ThumbnailBuilder;
import javafx.scene.image.Image;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.User;

public class ImageManager {
	private static Map<User, Image> userIconImage;
	private static Map<String, Image> functionImage;
	private static Map<Long, ArrayList<Image>> tweetInImage;

	private static ImageManager instance;
	
	private ImageManager() {
		userIconImage = new HashMap<User, Image>();
		functionImage = new HashMap<String, Image>();
		tweetInImage = new HashMap<Long, ArrayList<Image>>();
		
		Image replyImage = new Image("image/reply.png");
		Image favoriteImage = new Image("image/favorite.png");
		Image favoritedImage = new Image("image/favorited.png");
		Image retweetImage = new Image("image/retweet.png");
		Image retweetedImage = new Image("image/retweeted.png");
		
		functionImage.put("reply", replyImage);
		functionImage.put("favorite", favoriteImage);
		functionImage.put("favorited", favoritedImage);
		functionImage.put("retweet", retweetImage);
		functionImage.put("retweeted", retweetedImage);
	}
	
	public Image getImage(User user) {
		if(!userIconImage.containsKey(user)) {
			userIconImage.put(user, new Image(user.getProfileImageURL()));
		}
		return userIconImage.get(user);
	}
	
	public Image getImage(String function) {
		return functionImage.get(function);
	}

	/**
	 * ツイートに添付された画像の配列を返す
	 * 
	 * リツイート元があった場合はリツイート元を参照する
	 * 
	 * @param myStatus ツイートのステータス情報
	 * @param retweetedStatus リツイート元のステータス情報
	 * @return ツイートに添付された画像のArrayList
	 */
	public ArrayList<Image> getImage(Status myStatus, Status retweetedStatus) {
		Status status;
		
		if(retweetedStatus == null) {
			status = myStatus;
		} else {
			status = retweetedStatus;
		}
		
		MediaEntity[] medias = status.getExtendedMediaEntities();
		ArrayList<Image> array = new ArrayList<Image>();
		if(medias.length == 0) {
			return array;
		} else {
			if(!tweetInImage.containsKey(status.getId())) {
				for(MediaEntity media : medias) {
					try {
						array.add(ThumbnailBuilder.getThumbnailImage(new URL(media.getMediaURL())));
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
				tweetInImage.put(status.getId(), array);
			}
			return tweetInImage.get(status.getId());			
		}
	}
	
	public static ImageManager getSingleton() {
		if(instance == null) {
			instance = new ImageManager();
		}
		return instance;
	}
}
