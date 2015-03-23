package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class DialogManager {
	private static DialogManager instance;
	private static List<ReplyDialogController> dialogs;
	
	private DialogManager() {
		dialogs  = new ArrayList<ReplyDialogController>(); 
	}
	
	public void createDialog(Status status, String color) {
		ReplyDialogController controller = new ReplyDialogController();
		controller.setStatus(status);
		controller.setUserName(status.getUser().getName());
		controller.setText(status.getText());
		Image image = new Image(status.getUser().getBiggerProfileImageURL());
		controller.setIcon(image);
		controller.setColor(color);
		controller.setNum(dialogs.size());

		dialogs.add(controller);
		sound();
		showDialogs();
		
		if(!"skyBlue".equals(color)) {
			controller.startTimeLagDeleteThread();
		}
	}
	
	public void showDialogs() {
		for(int i=0; i<dialogs.size(); i++) {
			Stage dialog = dialogs.get(i).getStage();
			
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

			dialog.setX(d.getWidth() - 284);
			dialog.setY(50 + 90*i);
			dialog.show();
		}
		System.out.println("SHOW NOTIFICATION DIALOGS !!");
		System.out.println(dialogs);
	}
	
	public List<ReplyDialogController> getDialogControllerList() {
		return dialogs;
	}
	
	public void hideDialog(int num) {
		dialogs.get(num).getStage().close();;
		dialogs.remove(num);
		reNumbering(num);
		showDialogs();
	}
	
	private void reNumbering(int num) {
		for(int i=num; i<dialogs.size();i++) {
			dialogs.get(i).setNum(i);
		}
	}
	
	private void sound() {
	    Media sound = new Media(getClass().getResource("sample.mp3").toExternalForm());
	    MediaPlayer mediaPlayer = new MediaPlayer(sound);
	    mediaPlayer.play();
	}
	
	public static DialogManager getSingleton() {
		if(instance == null) {
			instance = new DialogManager();
		}
		
		return instance;
	}
	
	public void shutdownAllDialogs() {
		for(int i=0; i<dialogs.size(); i++) {
			hideDialog(i);
		}
	}
}