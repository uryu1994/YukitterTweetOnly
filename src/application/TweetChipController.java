package application;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Status;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class TweetChipController extends ListCell<Status>{
	
	@FXML
	private AnchorPane container;
	@FXML
	private ImageView icon;
	@FXML
	private Label userName;
	@FXML
	private Label screenName;
	@FXML
	private Label text;
	@FXML
	private Label via;
	
	private final Pattern pattern = Pattern.compile("<a href=\"(.*)\" rel=\".*\">(.*)</a>");
	
	public TweetChipController () {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tweetChip.fxml"));
		fxmlLoader.setController(this);
		
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}
	
	@Override
	protected void updateItem(Status status, boolean empty) {
		super.updateItem(status, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
			return;
		}

		
		userName.setText(status.getUser().getName());
		screenName.setText("@"+ status.getUser().getScreenName());
		Image image = new Image(status.getUser().getBiggerProfileImageURL());
		icon.setImage(image);
		text.setText(status.getText());
		
		Matcher matcher = pattern.matcher(status.getSource());

		if(matcher.find()) {
			via.setText(matcher.group(2));
		}
		
		setGraphic(container);
	}
	
}
