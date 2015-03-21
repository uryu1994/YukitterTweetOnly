package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class MainController implements Initializable {
	private static MainController instance;
	private static TwitterStream twitterStream;
	public ArrayList<Stage> list = new ArrayList<Stage>();
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

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// --- ユーザー情報を取得して表示 ---//
		try {
			name.setText(TwitterFactory.getSingleton().verifyCredentials().getName());
			screenId.setText("@"+ TwitterFactory.getSingleton().getScreenName());
			Image image = new Image(TwitterFactory.getSingleton().verifyCredentials().getBiggerProfileImageURL());
			icon.setImage(image);
		} catch (IllegalStateException | TwitterException e) {
			e.printStackTrace();
		}
		
		// --- ツイッターストリームのインスタンス取得 ---//
		twitterStream = TwitterStreamFactory.getSingleton();
		twitterStream.addListener(new MyUserStreamAdapter());
		twitterStream.user();

		//--- Comand+Enterでツイートを送信するショートカットキーを設定 ---//
		menuTweet.setAccelerator(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN));
		
		//--- 自身のインスタンスを保持 ---//
		instance = this;
		
		//--- リプライ通知のダイアログを管理するマネージャーを起動 ---//
		DialogManager.getInstance();
	}

	// --- 本文をツイートするボタンの処理 ---//
	public void onTweet(ActionEvent e) {
		executeTweet();
	}

	//---  ---//
	private void executeTweet() {
		String txt = tweetText.getText();
		if (txt.length() == 0)
			return;
		try {
			StatusUpdate status = new StatusUpdate(txt);
			if(inReplyToStatusId != null) {
				status.setInReplyToStatusId(inReplyToStatusId);
			}
			
			TwitterFactory.getSingleton().updateStatus(status);
			tweetText.clear();
			inReplyToStatusId = null;
			System.out.println("-->> Tweet:" + txt);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
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
}