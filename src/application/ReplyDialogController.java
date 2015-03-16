package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ReplyDialogController implements Initializable {
	
	private int num;
	
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

	public void setIcon() {
		
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return this.stage;
	}
	
	public void onReply(MouseEvent e) {
		
	}
	
	public void onShowStatus(MouseEvent e) {
		
	}
	
	public void onCloseDialog(MouseEvent e) {
		DialogManager.getInstance().hideDialog(num);
	}
}
