package application;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import twitter4j.User;

public class ImageManager {
	private static Map<User, Image> imageManager;
	private static Map<String, Image> functionImage;

	private static ImageManager instance;
	
	private ImageManager() {
		imageManager = new HashMap<User, Image>();
		functionImage = new HashMap<String, Image>();
		
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
		if(!imageManager.containsKey(user)) {
			imageManager.put(user, new Image(user.getProfileImageURL()));
		}
		return imageManager.get(user);
	}
	
	public Image getImage(String function) {
		return functionImage.get(function);
	}

	public static ImageManager getSingleton() {
		if(instance == null) {
			instance = new ImageManager();
		}
		return instance;
	}
}
