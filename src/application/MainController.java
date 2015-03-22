package application;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.OAuthAuthorization;

public class MainController {
	private static MainController instance;
	private static TwitterStream twitterStream;
	private static Twitter twitter;
	
	public static ArrayList<Stage> list = new ArrayList<Stage>();
	private Stage stage;
	private Long inReplyToStatusId;
	
	@FXML
	private Label textCounter;
	
	// TextArea wrote tweet text
	@FXML
	private TextArea tweetText;
	@FXML
	private MenuItem menuTweet;
	// User Name
	@FXML
	private Label name;
	// TwitterID
	@FXML
	private Label screenId;
	// twitter icon
	@FXML
	private ImageView icon;
	// ツイートボタン
	@FXML
	private Button tweetButton;

	public MainController (OAuthAuthorization oauth){
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("tweetOnly.fxml"));
			loader.setController(this);
			loader.load();
			
			Scene scene = new Scene(loader.getRoot());
			stage = new Stage(StageStyle.UNIFIED);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.setTitle("Yukitter");
			
			//--- Comand+Enterでツイートを送信するショートカットキーを設定 ---//
			menuTweet.setAccelerator(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN));
			
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// --- ユーザー情報を取得して表示 ---//
		try {
			
			twitter = new TwitterFactory().getInstance(oauth);
			name.setText(twitter.verifyCredentials().getName());
			screenId.setText("@"+ twitter.getScreenName());
			Image image = new Image(twitter.verifyCredentials().getBiggerProfileImageURL());
			icon.setImage(image);
		} catch (IllegalStateException | TwitterException e) {
			e.printStackTrace();
		}
		
		// --- ツイッターストリームのインスタンス取得 ---//
		twitterStream = new TwitterStreamFactory().getInstance(oauth);
		twitterStream.addListener(new MyUserStreamAdapter());
		twitterStream.user();
	
		//--- 自身のインスタンスを保持 ---//
		instance = this;
		
		//--- リプライ通知のダイアログを管理するマネージャーを起動 ---//
		DialogManager.getInstance();
	}

	// --- 本文をツイートするボタンの処理 ---//
	public void onTweet(ActionEvent e) {
		executeTweet();
	}

	//--- ツイートを実行 ---//
	private void executeTweet() {
		String txt = tweetText.getText();
		if (txt.length() == 0)
			return;
		try {
			StatusUpdate status = new StatusUpdate(txt);
			if(inReplyToStatusId != null) {
				status.setInReplyToStatusId(inReplyToStatusId);
			}
			twitter.updateStatus(status);
			tweetText.clear();
			inReplyToStatusId = null;
			System.out.println("-->> Tweet:" + txt);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			System.out.println("TWITTER EXCEPTION");
			e.printStackTrace();
		}
	}

	public void checkTextCount (KeyEvent e) {
		int count = 140 - tweetText.getText().length();
		textCounter.setText(String.valueOf(count));
		if(count <= 10) {
			textCounter.setStyle("-fx-text-fill: red;");
		} else {
			textCounter.setStyle("-fx-text-fill: black;");
		}
	}
	
	// --- IDをクリックするとステータス画面が開いちゃうぞ---//
	public void onMyStatus(MouseEvent e) {
		System.out.println("onMyStatus");
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return this.stage;
	}

	public void setText(String s, Long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
		tweetText.setText(s);
	}

	public static void shutdownTwitterStream() {
		if(twitterStream != null) {
			twitterStream.shutdown();
		}
	}
	
	public static MainController getInstance() {
		return instance;
	}
	
	public static String getScreenName() {
		try {
			return twitter.getScreenName();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}