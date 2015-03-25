package application;

import java.io.IOException;

import manager.DialogManager;
import manager.ImageManager;
import twitter4j.Status;
import twitter4j.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertDialogController {

	//-- アラートダイアログの配列のインデクス --//
	private int num;
	//-- ツイートのステータス --//
	private Status status;
	//-- イベントを行ったユーザー --//
	private User user;
	//-- ダイアログのStage --//
	private Stage stage;
	
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AlertDialog.fxml"));
		loader.setController(this);

		try {
			Scene scene = new Scene(loader.load());
			stage = new Stage(StageStyle.TRANSPARENT);
			stage.setScene(scene);
			stage.setResizable(false);
			
			userName.setText(status.getUser().getName());
			text.setText(status.getText());
			icon.setImage(ImageManager.getSingleton().getImage(status.getUser()));
//			notificationPane.setStyle("-fx-background-color: " + color + ";");
//			userIcon.setImage(ImageManager.getSingleton().getImage(user));
//			information.setText(user.getName() + "さん" + infoStr);
			
			if("reply".equals(func)) {
				notificationPane.setStyle("-fx-background-color: skyBlue;");
//				userIcon.setImage(ImageManager.getSingleton().getImage(user));
//				information.setText(user.getName() + "さんからリプライを受信しました");
			} else if("favorite".equals(func)) {
				notificationPane.setStyle("-fx-background-color: khaki;");
				userIcon.setImage(ImageManager.getSingleton().getImage(user));
				information.setText(user.getName() + "さんがお気に入りしました");
			} else if("retweet".equals(func)) {
				notificationPane.setStyle("-fx-background-color: palegreen;");
				userIcon.setImage(ImageManager.getSingleton().getImage(user));
				information.setText(user.getName() + "さんがリツイートしました");
			}
			
			this.user = user;
			this.status = status;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * インデクスを保持する
	 * 
	 * @param n ArrayList内のインデクス
	 */
	public void updateNum(int n) {
		this.num = n;
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
		MainController.getInstance().setText("@"+user.getScreenName()+" ", status.getId());
		DialogManager.getSingleton().closeDialog(num);
	}

	/**
	 * スクリーンネームをクリックしたら
	 * そのユーザーのステータス情報を見ることができる
	 * 
	 * @param e スクリーンネームをクリックした時のイベント
	 */
	public void onShowStatus(MouseEvent e) {
		
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
}
