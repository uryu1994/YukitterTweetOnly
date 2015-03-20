package application;

import java.net.URL;
import java.util.ResourceBundle;

import twitter4j.Status;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ReplyDialogController implements Initializable {
	
	private int num;
	
	private Status status;
	
	private Stage stage;
	
	@FXML
	private Label name;

	@FXML
	private Label text;
	
	@FXML
	private ImageView icon; 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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
		MainController.getInstance().setText("@"+status.getUser().getScreenName()+" ", status.getId());
		DialogManager.getInstance().hideDialog(num);
//	    MainController.getInstance().getStage().isFocused();
		
	}
	
	public Long getInRwplyToStatusId() {
		return status.getInReplyToStatusId();
	}
	
	public void onShowStatus(MouseEvent e) {
		
	}
	
	public void onCloseDialog(MouseEvent e) {
		DialogManager.getInstance().hideDialog(num);
		
	}
}
