package application;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import manager.ImageManager;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitterUtil.TwitterUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TweetChipController extends ListCell<Status>{
	
	//-- ツイートのStatus --//
	private Status status;
	//-- リツイートした元々ツイートのStatus --//
	private Status retweetedStatus;  
	//-- viaを抜き出すためのPattern定義 --//
	private final Pattern pattern = Pattern.compile("<a href=\"(.*)\" rel=\".*\">(.*)</a>");

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
	@FXML
	private ImageView replyImage;
	@FXML
	private ImageView favoriteImage;	
	@FXML
	private ImageView retweetImage;
	@FXML
	private ImageView retweetUserIcon;
	@FXML
	private Label retweetUserName;
	
	/**
	 * TweetChipControllerのコンストラクタ
	 * 
	 */
	public TweetChipController () {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tweetChip.fxml"));
		fxmlLoader.setController(this);
		
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		status = null;
		retweetedStatus = null;
	}
	
	/**
	 * 新着ツイートがあった時の追加処理
	 * @param status 
	 * @param empty 中身が空かどうかを確かめる
	 */
	@Override
	protected void updateItem(Status status, boolean empty) {
		super.updateItem(status, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
			return;
		}
		
		System.out.println("[debug] _______ START UPDATE ITEM _______");
//		retweetedStatus = status.getRetweetedStatus();
//		if(retweetedStatus != null) {
		if(status.getRetweetedStatus() == null) {
			userName.setText(status.getUser().getName());
			screenName.setText("@"+ status.getUser().getScreenName());
			icon.setImage(ImageManager.getSingleton().getImage(status.getUser()));
			text.setText(status.getText());
			
			retweetUserIcon.setVisible(false);
			retweetUserName.setVisible(false);
		} else {
			userName.setText(status.getRetweetedStatus().getUser().getName());
			screenName.setText("@"+ status.getRetweetedStatus().getUser().getScreenName());
			icon.setImage(ImageManager.getSingleton().getImage(status.getRetweetedStatus().getUser()));
			text.setText(status.getRetweetedStatus().getText());
			
			retweetUserIcon.setImage(ImageManager.getSingleton().getImage(status.getUser()));
			retweetUserName.setText("Retweeted by " + status.getUser().getName());
			
			retweetUserIcon.setVisible(true);
			retweetUserName.setVisible(true);
			System.out.println("こいつぁRetweetedStatusをもってるぜ！！");
		}
		
		System.out.println("[debug] added status is >> " + status);
		System.out.println("[debug] retweetedStatus is >> " + status.getRetweetedStatus());
		
		replyImage.setImage(ImageManager.getSingleton().getImage("reply"));
		
		if(status.isFavorited()) {
			favoriteImage.setImage(ImageManager.getSingleton().getImage("favorited"));
		} else {
			favoriteImage.setImage(ImageManager.getSingleton().getImage("favorite"));
		}
		
		if(status.isRetweeted()) {
			retweetImage.setImage(ImageManager.getSingleton().getImage("retweeted"));
		} else {
			retweetImage.setImage(ImageManager.getSingleton().getImage("retweet"));			
		}
		
		Matcher matcher = pattern.matcher(status.getSource());

		if(matcher.find()) {
			via.setText("via " + matcher.group(2));
		}
		
		this.status = status;
		
		setGraphic(container);
		System.out.println("[debug] ^^^^^^^ END UPDATE ITEM ^^^^^^^\n");
	}
	
	public void updateStatus(Status status) {
		this.status = status;
	}
	
	public void onChangeMenuPane(MouseEvent e) {
		System.out.println("[debug] onChangeMenuPane is Called !!");
		
		if(statusPane.isDisable()) {
			statusPane.setDisable(false);
			functionPane.setVisible(false);
			System.out.println("[info] show StatusPane");
		} else {
			statusPane.setDisable(true);
			functionPane.setVisible(true);
			System.out.println("[info] show FunctionPane");
		}
	}
	
	public void onReply(MouseEvent e) {
		System.out.println("[debug] Reply button is Pushed !!");		
		MainController.getInstance().setText("@"+status.getUser().getScreenName()+" ", status.getId());
	}
	
	public void onFavorite(MouseEvent e) throws TwitterException {
		System.out.println("[debug] Favorite button is Pushed !!");
		if(status.isFavorited()) {
			MainController.getInstance().updateStatus(TwitterUtil.getTwitter().destroyFavorite(status.getId()));
		} else {
			System.out.println("お気に入り前のステータス = [[ " + status.getId() + " ]]");
			MainController.getInstance().updateStatus(TwitterUtil.getTwitter().createFavorite(status.getId()));
		}
	}
	
	public void onRetweet(MouseEvent e) throws TwitterException {
		System.out.println("[debug] Retweet button is Pushed !!");
		if(status.isRetweeted()) {
			MainController.getInstance().deleteRetweetStatus(status);
		} else {
			TwitterUtil.getTwitter().retweetStatus(status.getId());
//			MainController.getInstance().updateStatus(TwitterUtil.getTwitter().retweetStatus(status.getId()));
			retweetImage.setImage(ImageManager.getSingleton().getImage("retweeted"));
		}
	}
}
