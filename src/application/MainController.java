package application;

import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
import javafx.util.Callback;
import twitter4j.ResponseList;
import twitter4j.Status;
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
	private ObservableList<Status> timelineList;
	private Stage stage;
	private Long inReplyToStatusId;
	
	@FXML
	private Label textCounter;
	@FXML
	private TextArea tweetText;
	@FXML
	private MenuItem menuTweet;
	@FXML
	private Label userName;
	@FXML
	private Label screenName;
	@FXML
	private ImageView icon;

	@FXML
	private Button tweetButton;
	@FXML
	public ListView<Status> timeline;
	

	public MainController (OAuthAuthorization oauth){

		//--- 自身のインスタンスを保持 ---//
		instance = this;

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("tweetOnly.fxml"));
			loader.setController(this);
			loader.load();
			
			Scene scene = new Scene(loader.getRoot());
			stage = new Stage(StageStyle.UNIFIED);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.setTitle("Yukitter");
			stage.setHeight(95);
			
			//--- Comand+Enterでツイートを送信するショートカットキーを設定 ---//
			menuTweet.setAccelerator(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//--- タイムラインを表示するための設定 ---//
		timelineList = FXCollections.observableArrayList();

		timeline.setCellFactory(new Callback<ListView<Status>, ListCell<Status>>() {
			@Override
			public ListCell<Status> call(ListView<Status> listView) {
				return new TweetChipController();
			}
		});
		timeline.setItems(timelineList);
		
		// --- ユーザー情報を取得して表示 ---//
		try {
			twitter = new TwitterFactory().getInstance(oauth);
			timelineList.addAll(twitter.getHomeTimeline());
			userName.setText(twitter.verifyCredentials().getName());
			screenName.setText("@"+ twitter.getScreenName());
			Image image = new Image(twitter.verifyCredentials().getBiggerProfileImageURL());
			icon.setImage(image);
			icon.requestFocus();
		} catch (IllegalStateException | TwitterException e) {
			e.printStackTrace();
		}
		
		// --- ツイッターストリームのインスタンス取得 ---//
		twitterStream = new TwitterStreamFactory().getInstance(oauth);
		twitterStream.addListener(new MyUserStreamAdapter());
		twitterStream.user();
			
		stage.show();
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
			e.printStackTrace();
		}
	}

	public void evolveTimelinePane(MouseEvent e) {
		if(stage.getHeight() == 95 ) {
			stage.setHeight(360);
			timeline.requestFocus();
		} else {
			stage.setHeight(95);
			icon.requestFocus();
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
		
		if(tweetText.getText().length() == 0) {
			inReplyToStatusId = null;
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
		tweetText.requestFocus();
	}

	public static void shutdownTwitterStream() {
		if(twitterStream != null) {
			twitterStream.shutdown();
		}
	}
	
	public void addStatus(Status status) {
		Platform.runLater( () -> {
			timelineList.add(0, status);
		});

	}
	
	public static MainController getInstance() {
		return instance;
	}
	
	public void addStatuses(ResponseList<Status> list){
		Platform.runLater(() -> {
			try {
				timelineList.addAll(list);
			} catch (Exception ex){
				ex.printStackTrace();
			}
		});
	}
	
	public Twitter getTwitter() {
		return twitter;
	}
}