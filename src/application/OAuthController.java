package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OAuthController {
	
	private RequestToken requestToken;
	private static AccessToken accessToken;
	private Twitter twitter;
	private Stage stage;
	private Stage owner;
	
	@FXML
	private File file;
	
	@FXML
	private WebView webView;
	
	@FXML
	private TextField pinText;
	
	@FXML
	private Button authButton;
	
	public OAuthController (Stage owner) {
		this.file = new File(".yukitter_setting");
		this.owner = owner;
		
		try {
			if(file.createNewFile()) {
				showOAuthWindow();
			} else {
				loadAccessToken();
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public void loadAccessToken() {
		String accessTokenKey;
		String accessTokenSecretKey;

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			try {
				accessTokenKey = br.readLine();
				accessTokenSecretKey = br.readLine();
				br.close();
				System.out.println(accessTokenKey);
				System.out.println(accessTokenSecretKey);
				accessToken = new AccessToken(accessTokenKey, accessTokenSecretKey);

				MainController main = new MainController(accessToken);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			//--- ファイルがない = まだ認証をしていない = 認証を開始する ---//
			e.printStackTrace();
		}
	}
	
	private void showOAuthWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("oAuthWindow.fxml"));
		loader.setController(this);
		
		try {
			loader.load();
			Scene scene = new Scene(loader.getRoot());
			stage = new Stage(StageStyle.UNIFIED);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.setTitle("OAuth認証");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(owner);
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		twitter = TwitterFactory.getSingleton();
//		twitter.setOAuthConsumer("[consumer key]", "[consumer secret]");
		try {
			requestToken = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		webView.getEngine().load(requestToken.getAuthenticationURL());
	}
	
	public void onOAuth(MouseEvent e) {
		if(pinText.getText().length() > 0) {
			try {
				accessToken = twitter.getOAuthAccessToken(requestToken, pinText.getText());
				
				//--- デバッグ　保存処理を実装
				System.out.println("AccessToken = [[" + accessToken.getToken() + "]]");
				System.out.println("AccessTokenSecret = [[" + accessToken.getTokenSecret()+"]]");
			} catch (TwitterException te) {
				if(401 == te.getStatusCode()){
					//--- 認証に失敗した場合の処理 ---//
					System.out.println("Unable to get the access token.");
				}else{
					te.printStackTrace();
				}
			}
		}
		writeAccessToken();
		stage.close();
		MainController main = new MainController(accessToken);
	}
	
	private void writeAccessToken() {
		try{
			FileWriter fw = new FileWriter(file);
			fw.write(accessToken.getToken()+"\n");
			fw.write(accessToken.getTokenSecret()+"\n");
			fw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
