package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TweetDialogController implements Initializable {

	private Stage stage;
	
	@FXML
	private TextArea tweetText;
	
	@FXML
	private Button tweetButton;
	
	public void initialize(URL location, ResourceBundle resources) {
		try {
			System.out.println("Start Loader");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("tweetDialog.fxml"));
			Scene scene = new Scene(loader.load());
			System.out.println("END Loader");
			Stage stage = new Stage(StageStyle.UNIFIED);
			stage.setScene(scene);

			TweetDialogController tweetDialogController = loader.<TweetDialogController>getController();
			stage.show();
			stage.toFront();
			
			System.out.println("END SHOW");
			
			this.stage = stage;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onTweet(ActionEvent event) {
		String txt = tweetText.getText();
		if(txt.length() == 0) return;
		try {
			TwitterFactory.getSingleton().updateStatus(txt);
			tweetText.setText("");
			System.out.println("-->> Tweet:" + txt);
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void hide() {
		stage.hide();
	}
}
