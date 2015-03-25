package manager;

import java.awt.Dimension;
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
	private static List<AlertDialogController> dialogs;
	private static Media sound;
	
	/**
	 * DialogManagerのコンストラクタ
	 * 
	 */
	private DialogManager() {
		dialogs  = new ArrayList<AlertDialogController>();
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
		AlertDialogController controller = new AlertDialogController(status, user, func);
		controller.updateNum(dialogs.size());

		dialogs.add(controller);
		sound();
		showDialogs();
	}
	
	/**
	 * ArrayListに格納されているすべてのアラートダイアログを表示する
	 * 
	 */
	public void showDialogs() {
		for(int i=0; i<dialogs.size(); i++) {
			Stage dialog = dialogs.get(i).getStage();
			
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

			dialog.setX(d.getWidth() - 284);
			dialog.setY(50 + 100*i);
			dialog.show();
		}
		System.out.println(dialogs);
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
		dialogs.get(num).getStage().close();
		dialogs.remove(num);
		reNumbering(num);
		showDialogs();
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
		for(int i=num; i<dialogs.size();i++) {
			dialogs.get(i).updateNum(i);
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
		for(int i=0; i<dialogs.size(); i++) {
			closeDialog(i);
		}
	}
}