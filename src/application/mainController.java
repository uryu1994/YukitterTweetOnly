package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;

public class mainController implements Initializable{
	
	public static mainController instance;
	public static TwitterStream twitterStream;
	
	public ArrayList<Stage> list = new ArrayList<Stage>();
	
	// TextArea wrote tweet text
	@FXML
	public TextArea tweetText;

	// User Name
	@FXML
	public Label name;
	
	// TwitterID
	@FXML
	public Label screenId;
	
	// twitter icon
	@FXML
	public ImageView icon;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {		
		//--- get user status ---//
		try {
			name.setText(TwitterFactory.getSingleton().verifyCredentials().getName());
			screenId.setText("@"+TwitterFactory.getSingleton().getScreenName());
			Image image = new Image(TwitterFactory.getSingleton().verifyCredentials().getBiggerProfileImageURL());
			icon.setImage(image);
		} catch (IllegalStateException | TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//--- save instance of oneself---//
		instance = this;
	}
	
	// --- tweet button which wrote text in TextArea ---//
	public void onTweet(ActionEvent e) throws TwitterException {
/*
		String txt = tweetText.getText();
		if(txt.length() == 0) return;
		TwitterFactory.getSingleton().updateStatus(txt);

		tweetText.setText("");
		System.out.println("Tweet:" + txt);
*/
		FXMLLoader loader = new FXMLLoader(getClass().getResource("replyWindow.fxml"));
        try {
			loader.load();
		    Parent root = loader.getRoot();
		    DialogController controller = loader.getController();
		    controller.setText("今日の外の天気は晴れですか？");
		    Scene scene = new Scene(root);
		    Stage confirmDialog = new Stage(StageStyle.TRANSPARENT);
		    confirmDialog.setScene(scene);
		    confirmDialog.setResizable(false);
		    confirmDialog.setTitle("@ほにゃららら");
		    
		    list.add(confirmDialog);
		    list.get(0).show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//--- show my twitter status when clicked id text---//
	public void onMyStatus(MouseEvent e) {
		System.out.println("onMyStatus");
	}
	
	//--- get instance of this controller ---//
	public static mainController getInstance() {
		return instance;
	}
}