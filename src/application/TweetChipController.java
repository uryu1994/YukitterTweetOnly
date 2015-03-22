package application;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Status;
import twitter4j.TwitterException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class TweetChipController extends ListCell<Status>{
	
	private Status status;
	
	@FXML
	private HBox container;
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
	@FXML
	private BorderPane statusPane;
	@FXML
	private HBox functionPane;
	
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
		icon.setImage(ImageManager.getImage(status.getUser()));
		text.setText(status.getText());
		
		Matcher matcher = pattern.matcher(status.getSource());

		if(matcher.find()) {
			via.setText("via " + matcher.group(2));
		}
		
		this.status = status;
		
		setGraphic(container);
	}
	
	public void onChangeMenuPane(MouseEvent e) {
		System.out.println("onChangeMenuPane are Pushed !!");
		
		if(statusPane.isDisable()) {
			statusPane.setDisable(false);
			functionPane.setVisible(false);
			System.out.println("show StatusPane");
		} else {
			statusPane.setDisable(true);
			functionPane.setVisible(true);
			System.out.println("show FunctionPane");
		}
	}
	
	public void onReply(MouseEvent e) {
		MainController.getInstance().setText("@"+status.getUser().getScreenName()+" ", status.getId());
		System.out.println("onReply button pushed !!");
	}
	
	public void onFavorite(MouseEvent e) throws TwitterException {
		MainController.getInstance().getTwitter().createFavorite(status.getId());
		System.out.println("onFavorite button pushed !!");
	}
	
	public void onRetweet(MouseEvent e) throws TwitterException {
			MainController.getInstance().getTwitter().retweetStatus(status.getId());
		System.out.println("onRetweet button pushed !!");
	}
}
