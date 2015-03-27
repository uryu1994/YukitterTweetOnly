package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import manager.DialogManager;
import manager.ImageManager;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitterUtil.TwitterUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class AlertDialogController {

	//-- アラートダイアログの配列のインデクス --//
	private int num;
	//-- ツイートのステータス --//
	private Status status;
	//-- funcの操作を行ったユーザー --//
	private User user;
	//-- リプライ、お気に入り、リツイートを示す文字列--//
	private String func;
	//-- ダイアログのStage --//
	private Stage stage;
	//-- 軌道から7秒後にダイアログを閉じるスレッドのインスタンス --//
	Timeline closeAlertThread;
	@FXML
	private Label userName;
	@FXML
	private Label text;
	@FXML
	private ImageView icon;
	@FXML
	private ImageView userIcon;
	@FXML
	private Label information;
	@FXML
	private AnchorPane notificationPane;
	
	/**
	 * AlertDialogControllerのコンストラクタ
	 */
	public AlertDialogController(Status status, User user, String func) {
		this.status = status;
		this.user = user;
		this.func = func;
		
		FXMLLoader loader;
		if("reply".equals(func)) {
			loader = new FXMLLoader(getClass().getResource("ReplyDialog.fxml"));	
		} else {
			loader = new FXMLLoader(getClass().getResource("FavRetDialog.fxml"));
		}
		loader.setController(this);

		try {
			Scene scene = new Scene(loader.load());
			stage = new Stage(StageStyle.TRANSPARENT);
			stage.setScene(scene);
			stage.setResizable(false);

			deleteDialogTimer();			
			updateView();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * インデクス情報を更新するsetter
	 * 
	 * @param n ArrayList内のインデクス
	 */
	public void setNum(int n) {
		this.num = n;
	}
	
	/**
	 * ステータス情報と処理を行ったユーザー情報のsetter
	 *
	 * @param status 新しいステータス情報
	 * @param user 新しいユーザー情報
	 */
	
	public void setInfo(Status status, User user) {
		closeAlertThread.stop();
		this.status = status;
		this.user = user;
		updateView();
	}
	
	/**
	 * ステータス情報のgetter
	 * @return このコントローラーが保持するステータス情報
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * funcのgetter
	 * 
	 * @param リプライ(reply)か、お気に入り(favorite)か、リツイート(retweet)のいずれかの文字列
	 */
	public String getFunc() {
		return func;
	}
	/**
	 * 外部からステージをShow/CloseするためのStageのgetterメソッド
	 * 
	 * @return AlertDialogのStage
	 */
	public Stage getStage() {
		return this.stage;
	}
	
	/**
	 * ツイート本文をクリックした時にスクリーンネームを
	 * メイン画面のツイート入力エリアに追記してフォーカスする
	 * 
	 * @param e ツイート本文をクリックした時のイベント
	 */
	public void onReply(MouseEvent e) {
		System.out.println("@"+status.getUser().getScreenName()+" ");
		
		MainController.getInstance().setText(createMentionString(), status.getId());
		DialogManager.getSingleton().closeDialog(num);
	}

	/**
	 * リプライを送るユーザーの文字列を作成(複数宛に対応)
	 * 
	 * 以下の順でリプライ用の文字列を生成する
	 *  1. リプライを送ったユーザーのスクリーンネームを追加する
	 *  2. UserMentionEntity配列の中から、自分以外のユーザー宛にリプライを作成
	 * 
	 * @return リプライを送るユーザー全員宛のスクリーンネーム群の文字列
	 */
	private String createMentionString() {
		String userStr = "@" + status.getUser().getScreenName() + " ";
		List<UserMentionEntity> mentionList
				= new ArrayList<UserMentionEntity>(Arrays.asList(status.getUserMentionEntities()));
		for(UserMentionEntity user : mentionList) {
			if(!TwitterUtil.getScreenName().equals(user.getScreenName())) {
				userStr += "@" + user.getScreenName() + " ";
			}
		}
		return userStr;
	}

	/**
	 * スクリーンネームをクリックしたら
	 * そのユーザーのステータス情報を見ることができる
	 * 
	 * @param e スクリーンネームをクリックした時のイベント
	 */
	public void onShowStatus(MouseEvent e) {
		
	}
	
	public Stage updateView() {
		userName.setText(status.getUser().getName());
		text.setText(status.getText());
		icon.setImage(ImageManager.getSingleton().getImage(status.getUser()));
		
		if("reply".equals(func)) {
			notificationPane.setStyle("-fx-background-color: skyBlue;");
		} else if("favorite".equals(func)) {
			notificationPane.setStyle("-fx-background-color: khaki;");
			userIcon.setImage(ImageManager.getSingleton().getImage(user));

			String favStr = "Favorited by " + user.getName();
			if(status.getFavoriteCount() > 1 ) {
				favStr += " and " + (status.getFavoriteCount()-1) + " Others";
			}
			
			information.setText(favStr);
		} else if("retweet".equals(func)) {
			notificationPane.setStyle("-fx-background-color: palegreen;");
			userIcon.setImage(ImageManager.getSingleton().getImage(user));

			String retStr = "Retweeted by " + user.getName();
			if(status.getRetweetCount() > 1 ) {
				retStr += " and " + (status.getRetweetCount()-1) + " Others";
			}
			
			information.setText(retStr);
		}
		
		if(!"reply".equals(func)) {
			closeAlertThread.play();	
		}
		
		return stage;
	}
	
	/**
	 * アイコンをクリックした時に呼び出される
	 * 
	 * アイコンをクリックしたらダイアログを閉じる
	 * 
	 * @param e アイコンをクリックした時のイベント
	 */
	public void onCloseDialog(MouseEvent e) {
		DialogManager.getSingleton().closeDialog(num);
	}
	
	public void deleteDialogTimer() {
		closeAlertThread = new Timeline(
				new KeyFrame(Duration.seconds(7), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
				DialogManager.getSingleton().closeDialog(num);
		        System.out.println("this is called every 7 seconds on UI thread");
		    }
		}));
	}
	
}
