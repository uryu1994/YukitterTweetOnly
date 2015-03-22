package application;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import twitter4j.User;

public class ImageManager {
	private static Map<User, Image> imageManager = new HashMap<User, Image>();

	private ImageManager() { }
	
	public static Image getImage(User user) {
		if(!imageManager.containsKey(user)) {
			imageManager.put(user, new Image(user.getProfileImageURL()));
		}
		return imageManager.get(user);
	}
}
