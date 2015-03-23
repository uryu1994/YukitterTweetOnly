package application;

import java.io.IOException;

import twitter4j.Status;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ReplyDialogController {
	
	private int num;
	private Status status;
	private Stage stage;
	
	@FXML
	private Label name;
	@FXML
	private Label text;
	@FXML
	private ImageView icon; 
	@FXML
	private AnchorPane notificationPane;
	
	public ReplyDialogController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("replyDialog.fxml"));
		loader.setController(this);

		try {
			Scene scene = new Scene(loader.load());
			stage = new Stage(StageStyle.TRANSPARENT);
			stage.setScene(scene);
			stage.setResizable(false);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setNum(int n) {
		this.num = n;
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
	public void setUserName(String name) {
		this.name.setText(name);
	}

	public void setIcon(Image image) {
		this.icon.setImage(image);
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	public void onReply(MouseEvent e) {
		System.out.println("@"+status.getUser().getScreenName()+" ");
		MainController.getInstance().setText("@"+status.getUser().getScreenName()+" ", status.getId());
		DialogManager.getSingleton().hideDialog(num);
	}
	
	public Long getInRwplyToStatusId() {
		return status.getInReplyToStatusId();
	}
	
	public void onShowStatus(MouseEvent e) {
		
	}
	
	public void onCloseDialog(MouseEvent e) {
		DialogManager.getSingleton().hideDialog(num);
	}
	
	public void setColor(String color) {
		notificationPane.setStyle("-fx-background-color: " + color + ";");
	}
	
	public void startTimeLagDeleteThread() {
		TimeLagDelete timeLagDelete = new TimeLagDelete();
		timeLagDelete.run();
	}
	
	class TimeLagDelete implements Runnable {
		private int time = 0;
		@Override
		public void run() {
			Platform.runLater( () -> {
				System.out.println("CountStart");
				while(time<200000) {
					time++;
					if(time%5000 == 0) {
						System.out.println("Now [[ "+ time + " ]] Counted.");		
					}
				}
				/*
				if(stage.isShowing()) {
					System.out.println("stage close !!");
					DialogManager.getSingleton().hideDialog(num);
				}
				*/
			});
		}
	}
}
