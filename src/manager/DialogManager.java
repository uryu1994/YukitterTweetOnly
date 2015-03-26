package manager;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import application.AlertDialogController;
import twitter4j.Status;
import twitter4j.User;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class DialogManager {
	private static DialogManager instance;
	private static List<AlertDialogController> alerts;
	private static Media sound;
	
	/**
	 * DialogManagerのコンストラクタ
	 * 
	 */
	private DialogManager() {
		alerts  = new ArrayList<AlertDialogController>();
		sound = new Media(getClass().getResource("sample.mp3").toExternalForm());
	}

	/**
	 * アラートダイアログを生成
	 * 
	 * @param status ツイートのStatus
	 * @param color アラートダイアログの背景色を設定する文字列
	 *   - skyBlue: リプライ
	 *   - khaki: お気に入り
	 *   - palegreen: リツイート
	 */
	public void createDialog(Status status, User user, String func) {
		for(AlertDialogController alert : alerts) {
			if(alert.getStatus().getId() == status.getId() && func.equals(alert.getFunc())) {
				alerts.get(alerts.indexOf(alert)).setInfo(status, user);
				sound();
				showAllAlerts();
				return ;
			}
		}
		
		AlertDialogController controller = new AlertDialogController(status, user, func);
		controller.setNum(alerts.size());

		alerts.add(controller);
		sound();
		showAllAlerts();
	}
	
	/**
	 * ArrayListに格納されているすべてのアラートダイアログを表示する
	 * 
	 */
	public void showAllAlerts() {
		for(int i=0; i<alerts.size(); i++) {
			Stage dialog = alerts.get(i).updateView();

			dialog.setX(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 284);
			dialog.setY(50 + 90*i);
			dialog.show();
		}
		System.out.println(alerts);
	}
	
	/**
	 * アラートダイアログの終了処理
	 * 
	 *  - ダイアログを非表示にする
	 *  - ArrayList内から削除する
	 *  - ArrayList内部のナンバリングを整理する
	 *  - アラートダイアログを再描写
	 *  
	 * @param num AlertDialogControllerが保持しているArrayList内のインデクス
	 */
	public void closeDialog(int num) {
		alerts.get(num).getStage().close();
		alerts.remove(num);
		reNumbering(num);
		showAllAlerts();
	}
	
	/**
	 * ArrayListのインデクスと保持しているインデクスを一致させる
	 * 
	 * あるインデクスのダイアログがArrayList内から削除されると
	 * それ以降のインデクスを一つづつ前にずらしていく
	 * 
	 * @param num 削除されたインデクスの数字
	 */
	private void reNumbering(int num) {
		for(int i=num; i<alerts.size();i++) {
			alerts.get(i).setNum(i);
		}
	}
	
	/**
	 * ダイアログ表示時に音を鳴らす
	 */
	private void sound() {
	    MediaPlayer mediaPlayer = new MediaPlayer(sound);
	    mediaPlayer.play();
	}
	
	/**
	 * シングルトン実装でインスタンスを取得するgetterメソッド
	 * 
	 * @return ialogManagerのインスタンス
	 */
	public static DialogManager getSingleton() {
		if(instance == null) {
			instance = new DialogManager();
		}
		
		return instance;
	}
	
	/**
	 * すべてのダイアログを閉じる
	 * 
	 * シャットダウン時に呼ばれる
	 */
	public void shutdownAllDialogs() {
		for(int i=0; i<alerts.size(); i++) {
			closeDialog(i);
		}
	}
}