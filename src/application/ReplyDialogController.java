package application;

import java.io.IOException;

import twitter4j.Status;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
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
			Stage dialog = new Stage(StageStyle.TRANSPARENT);
			dialog.setScene(scene);
			dialog.setResizable(false);
			
			this.stage = dialog;
			
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
		DialogManager.getInstance().hideDialog(num);
	}
	
	public Long getInRwplyToStatusId() {
		return status.getInReplyToStatusId();
	}
	
	public void onShowStatus(MouseEvent e) {
		
	}
	
	public void onCloseDialog(MouseEvent e) {
		DialogManager.getInstance().hideDialog(num);
	}
	
	public void setColor(String color) {
		notificationPane.setStyle("-fx-background-color: " + color + ";");
	}
}
