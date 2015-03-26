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
import twitter4j.TwitterException;
import twitterUtil.TwitterUtil;

public class MainController {
	
	//-- MainControllerクラスのインスタンス --//
	private static MainController instance;
	//--イムライン用のツイートを格納するインスタンス --//
	private ObservableList<Status> timelineList;
	//-- MainControllerのステージを保持する --//
	private Stage stage;
	//--- リプライ元のツイートのStatusIDを保持 ---//
	private Long inReplyToStatusId;
	//-- タイムラインを閉じた時のウインドウの高さ --//
	private final int MIN_WINDOW_HEIGHT = 100;
	//-- タイムラインを展開した時のウインドウの高さ --//
	private final int MAX_WINDOW_HEIGHT = 400;
	
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
	

	/**
	 * コンストラクタ
	 * 
	 * FXMLをロードしてviewを生成
	 * ショートカットキーの設定
	 * ストリームタイムラインの起動
	 * ログインとユーザ情報の取得
	 * 
	 * @param oauth OAuth認証に必要な情報を保持している
	 */
	public MainController (){

		//--- 自身のインスタンスを保持 ---//
		instance = this;

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPanel.fxml"));
			loader.setController(this);
			loader.load();
			
			stage = new Stage(StageStyle.UNIFIED);
			stage.setResizable(false);
			stage.setScene(new Scene(loader.getRoot()));
			stage.setTitle("Yukitter");
			stage.setHeight(MIN_WINDOW_HEIGHT);
			
			//--- Command+Enterでツイートを送信するショートカットキーを設定 ---//
			menuTweet.setAccelerator(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN));
			
		} catch (IOException e) {
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
		
		//-- 直近10件のツイートとユーザーの情報を表示 --//
		try {
			timelineList.addAll(TwitterUtil.getTwitter().getHomeTimeline());
			userName.setText(TwitterUtil.getTwitter().verifyCredentials().getName());
			screenName.setText("@"+ TwitterUtil.getTwitter().getScreenName());
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		//-- ユーザーのアイコンを表示 --//
		icon.setImage(TwitterUtil.getIcon());
		icon.requestFocus();
		//-- ウインドウを可視化 --//
		stage.show();
	}
	
	/**
	 * Command+Enterのショートカットキーを押したときに呼び出される
	 * 
	 * テキストエリアの文字数が0の時はつぶやけない
	 * inReplyToStatusIdがnullではない時にリプライチェインをつなぐ
	 * ツイート後はテキストエリアをcleanしてリプライチェインを切る
	 * 
	 * @param e ショートカットキーが押された時のイベント
	 */
	public void onTweet(ActionEvent e) {
		if(tweetText.getText().length() == 0) {
			return ;
		}
		
		StatusUpdate status = new StatusUpdate(tweetText.getText());
		if(inReplyToStatusId != null) {
			status.setInReplyToStatusId(inReplyToStatusId);
		}

		try {
			TwitterUtil.getTwitter().updateStatus(status);
			tweetText.clear();
			inReplyToStatusId = null;
		} catch (TwitterException te) {
			te.printStackTrace();
		}
		
	}
	
	/**
	 * タイムラインの表示/非表示を切り替える
	 * 
	 * @param e ユーザーのアイコンがクリックされた時のイベント
	 */
	public void evolveTimelinePane(MouseEvent e) {
		if(stage.getHeight() == MIN_WINDOW_HEIGHT ) {
//			stage.setHeight(375);
			stage.setHeight(MAX_WINDOW_HEIGHT);
			timeline.requestFocus();
		} else {
			stage.setHeight(MIN_WINDOW_HEIGHT);
			icon.requestFocus();
		}
	}
	
	/**
	 * テキストエリア内の文字数を140字からカウントダウンする
	 * 
	 * 文字数が残り10文字以下になったら赤字に変更
	 * テキストエア内の文字数が0の時にリプライチェインを切る
	 * 
	 * @param e キーボードのキーが(一度押して)放された時のイベント
	 */
	public void checkTextCount (KeyEvent e) {
		int count = 140 - tweetText.getText().length();
		textCounter.setText(String.valueOf(count));
		if(count <= 10) {
			//-- 文字数上限まで残り10文字以下の時は赤字 --//
			textCounter.setStyle("-fx-text-fill: red;");
		} else {
			//-- 0~130文字の時は黒字 --//
			textCounter.setStyle("-fx-text-fill: black;");
		}
		
		//-- テキストエリアの文字列が0の時にリプライチェインを切る --//
		if(tweetText.getText().length() == 0) {
			inReplyToStatusId = null;
		}
	}
	
	/**
	 * @TODO: IDをクリックするとステータス画面が開く
	 * 
	 * @param e ユーザーIクリックした時に呼び出される
	 */
	public void onMyStatus(MouseEvent e) {
		System.out.println("onMyStatus");
	}
	
	/**
	 * テキストエリアにツイートを入力する
	 * 
	 * 主に別のウインドウからリプライ用のユーザークリーンネームを入力するために使う
	 * 
	 * @param s ツイートする本文
	 * @param inReplyToStatusId リプライチェインをつなぐためのStatusID
	 */
	public void setText(String s, Long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
		tweetText.setText(s);
		tweetText.requestFocus();
	}
	
	/**
	 * タイムラインリストに新着ツイートを追加する
	 * 
	 * @param status 新着ツイートのステータス
	 */
	public void addStatus(Status status) {
		Platform.runLater( () -> {
			timelineList.add(0, status);
		});
	}
	
	/**
	 * タイムラインリストに複数の新着ツイートを追加する
	 * 
	 * @param list 新着ツイート群
	 */
	public void addStatuses(ResponseList<Status> list){
		Platform.runLater(() -> {
			try {
				timelineList.addAll(list);
			} catch (Exception ex){
				ex.printStackTrace();
			}
		});
	}
	
	/**
	 * タイムラインリストの中のステータスを更新する
	 * 
	 * synchronized
	 * 主にリツイート/お気に入りした時に呼び出される
	 * 
	 * @param newStatus 情報が変更された新しいステータス
	 */
	public void updateStatus(Status newStatus) {
		System.out.println("[debug] "+ newStatus);
		synchronized(timelineList) {
			for(Status oldStatus : timelineList) {
				int num = timelineList.indexOf(oldStatus);
				if(newStatus.isRetweeted()) {
					if(oldStatus.getId() == newStatus.getRetweetedStatus().getId()) {
						timelineList.remove(num);
						timelineList.add(num, newStatus.getRetweetedStatus());
						System.out.println("[debug] リツイートで入れ替えました。");
						return ;
					}
				} else {
					if(oldStatus.getId() == newStatus.getId()) {
						timelineList.remove(num);
						timelineList.add(num, newStatus);
						System.out.println("[debug] お気に入りで入れ替えました。");
						
						return ;
				}
				

				}
			}
		}
	}
	
	/**
	 * タイムラインリストの中のリツイートしたステータスを削除する
	 * 
	 * 以下の処理をする時に呼び出される
	 *  - リツイートの解除
	 *  @TODO: ツイートの削除
	 */
	public void deleteRetweetStatus(Status myRetweetStatus) {
		synchronized(timelineList) {
			for(Status retweetedStatus : timelineList) {
				if(retweetedStatus.getId() == myRetweetStatus.getId()) {
					int num = timelineList.indexOf(retweetedStatus);

					timelineList.remove(num);
					timelineList.add(num, myRetweetStatus.getRetweetedStatus());
					System.out.println("[info] リツイート消したよ。");
					return ;
				}
			}
		}
	}
	
	/**
	 * MainControllerのインスタンスのgetterメソッド
	 * 
	 * @return MainControllerのインスタンス
	 */
	public static MainController getInstance() {
		return instance;
	}
}