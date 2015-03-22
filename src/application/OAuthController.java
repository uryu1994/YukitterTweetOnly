package application;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import serialize.OAuthConfiguration;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import twitter4j.auth.RequestToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OAuthController {
	
	private OAuthConfiguration consumer = null;
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
	
	public OAuthController (Stage owner) {
		this.file = new File(".yukitter_setting");
		this.owner = owner;
		DeserializationOAuthConfiguration();
		
		if(file.exists()) {
			loadAccessToken();	
		} else {
			showOAuthWindow();
		}
	}
	
	public void loadAccessToken() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			try {
				OAuthConfiguration.setAccessTokenKey(br.readLine());
				OAuthConfiguration.setAccessTokenSecret(br.readLine());
				br.close();
				System.out.println(consumer);
				MainController main = new MainController(OAuthConfiguration.createConfiguration());

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
		
		try {
			twitter = TwitterFactory.getSingleton();
			twitter.setOAuthConsumer(OAuthConfiguration.getConsumerKey(), OAuthConfiguration.getConsumerSecret());
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
			} catch (TwitterException te) {
				if(401 == te.getStatusCode()){
					//--- 認証に失敗した場合の処理 ---//
					System.out.println("Unable to get the access token.");
				}else{
					te.printStackTrace();
				}
			}
		}
		stage.close();
		OAuthConfiguration.setAccessTokenKey(accessToken.getToken());
		OAuthConfiguration.setAccessTokenSecret(accessToken.getTokenSecret());
		System.out.println(consumer);
		writeAccessToken();
		MainController main = new MainController(OAuthConfiguration.createConfiguration());
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
	
	public void DeserializationOAuthConfiguration() {
		System.out.println("DeserializationConsumer");
	    ObjectInputStream in = null;
	    try {
	    	in = new ObjectInputStream(
	    			new BufferedInputStream(ClassLoader.getSystemResourceAsStream("serialize/OAuth")));
	    	consumer = (OAuthConfiguration)in.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
