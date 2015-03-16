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
import twitter4j.TwitterStreamFactory;

public class mainController implements Initializable{
	
	private static mainController instance;
	private static TwitterStream twitterStream;
	
	public ArrayList<Stage> list = new ArrayList<Stage>();
	
	// TextArea wrote tweet text
	@FXML
	private TextArea tweetText;

	// User Name
	@FXML
	private Label name;
	
	// TwitterID
	@FXML
	private Label screenId;
	
	// twitter icon
	@FXML
	private ImageView icon;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//--- ユーザー情報を取得して表示 ---//
		try {
			name.setText(TwitterFactory.getSingleton().verifyCredentials().getName());
			screenId.setText("@"+TwitterFactory.getSingleton().getScreenName());
			Image image = new Image(TwitterFactory.getSingleton().verifyCredentials().getBiggerProfileImageURL());
			icon.setImage(image);
		} catch (IllegalStateException | TwitterException e) {
			e.printStackTrace();
		}
		
		//--- ツイッターストリームのインスタンス取得 ---//
        twitterStream = TwitterStreamFactory.getSingleton();
		twitterStream.addListener(new MyUserStreamAdapter());
		twitterStream.user();
		
		this.instance = this;
		
		DialogManager.getInstance();
	}
	
	// --- 本文をツイートするボタンの処理 ---//
	public void onTweet(ActionEvent e) throws TwitterException {
		String txt = tweetText.getText();
		if(txt.length() == 0) return;
		TwitterFactory.getSingleton().updateStatus(txt);

		tweetText.setText("");
		System.out.println("-->> Tweet:" + txt);
	}
	
	//--- IDをクリックするとステータス画面が開いちゃうぞ---//
	public void onMyStatus(MouseEvent e) {
		System.out.println("onMyStatus");
	}
	
	public void setText(String s) {
		tweetText.setText(s);
	}
	
	//--- TwitterStreamインスタンスのgetter ---//
	public static TwitterStream getTwitterStreamInstance() {
		return twitterStream;
	}
	
	public static mainController getInstance() {
		return instance;
	}
	
}