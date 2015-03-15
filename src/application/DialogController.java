package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

public class DialogController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void setText(String text) {
		
	}
	
	public void setUserName(String name) {
		
	}

	public void setIcon() {
		
	}
	
	public void onReply(MouseEvent e) {
		mainController.getInstance().list.get(0).hide();
	}
	
	public void onShowStatus(MouseEvent t) {
		
	}
}
