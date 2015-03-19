package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class MainController implements Initializable{
	
	private static MainController instance;
	private static TwitterStream twitterStream;
	
	public ArrayList<Stage> list = new ArrayList<Stage>();
	
	// twitter icon
	@FXML
	private ImageView icon;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//--- ユーザー情報を取得して表示 ---//
		try {
			Image image = new Image(TwitterFactory.getSingleton().verifyCredentials().getBiggerProfileImageURL());
			icon.setImage(image);
		} catch (IllegalStateException | TwitterException e) {
			e.printStackTrace();
		}
		
		//--- ツイッターストリームのインスタンス取得 ---//
        twitterStream = TwitterStreamFactory.getSingleton();
		twitterStream.addListener(new MyUserStreamAdapter());
		twitterStream.user();
		
		instance = this;
		
		DialogManager.getInstance();
	}

	public void onClickedIcon(MouseEvent e) {		
		TweetDialogController tweetDialogController = new TweetDialogController();
		System.out.println("Clicked.");
	}
	
	//--- TwitterStreamインスタンスのgetter ---//
	public static TwitterStream getTwitterStreamInstance() {
		return twitterStream;
	}
	
	public static MainController getInstance() {
		return instance;
	}
}