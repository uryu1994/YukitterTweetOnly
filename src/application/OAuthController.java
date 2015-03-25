package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import serialize.OAuthConfiguration;
import serialize.Deserialize;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitterUtil.TwitterUtil;
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
	
	//-- リクエストトークンのインスタンス --//
	private RequestToken requestToken;
	//-- アクセストークンのインスタンス --//
	private static AccessToken accessToken;
	//-- リクエストトークンとアクセストークンの生成に使用するTwitterインスタンス --//
	private Twitter twitter;
	//-- OAuthControllerのステージのインスタンス --//
	private Stage stage;
	
	@FXML
	private File file;
	@FXML
	private WebView webView;
	@FXML
	private TextField pinText;
	
	/**
	 * OAuthControllerのコンストラクタ
	 * 
	 * 設定ファイルがあればロードしてメイン画面を表示
	 * 設定ファイルがなければ認証画面を表示
	 * 認証設定クラスの OAuthConfiguration をデシリアライズ
	 * 
	 * @param owner 親ウインドウのStageインスタンス
	 */
	public OAuthController (Stage owner) {
		this.file = new File(".yukitter_setting");

		Deserialize.deserializationOAuthConfiguration();
		
		if(file.exists()) {
			loadAccessToken();
		} else {
			showOAuthWindow(owner);
		}
	}
	
	/**
	 * 設定ファイルをロードしてメイン画面を表示する
	 * 
	 */
	public void loadAccessToken() {
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			OAuthConfiguration.setAccessTokenKey(br.readLine());
			OAuthConfiguration.setAccessTokenSecret(br.readLine());
			br.close();
			
			TwitterUtil.createSingleton();
			@SuppressWarnings("unused")
			MainController main = new MainController();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * OAuth認証を行うためのウインドウを生成
	 * 
	 * @param owner 親ステージのStageインスタンス
	 */
	private void showOAuthWindow(Stage owner) {
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
		
		//-- リクエストトークンの生成 --//
		try {
			twitter = TwitterFactory.getSingleton();
			twitter.setOAuthConsumer(OAuthConfiguration.getConsumerKey(), OAuthConfiguration.getConsumerSecret());
			requestToken = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		//-- リクエストトークンのURLをwebViewで開く --//
		webView.getEngine().load(requestToken.getAuthenticationURL());
	}
	
	/**
	 * 認証ボタンを押したときのイベント
	 * 
	 * 認証に成功した場合の処理
	 *  - 認証画面を閉じる
	 *  - アクセストークンキーとアクセストークンシークレットを認証設定にセットする
	 *  - 設定ファイルに書き出す
	 *  - メイン画面の起動
	 * @TODO:認証に失敗した場合の処理
	 *  - どうしよう？
	 *  
	 * @param e 認証ボタンを押したときのイベント
	 */
	public void onOAuth(MouseEvent e) {
		if(pinText.getText().length() == 7) {
			try {
				accessToken = twitter.getOAuthAccessToken(requestToken, pinText.getText());
				stage.close();
				OAuthConfiguration.setAccessTokenKey(accessToken.getToken());
				OAuthConfiguration.setAccessTokenSecret(accessToken.getTokenSecret());
				writeAccessToken();
				
				TwitterUtil.createSingleton();
				
				@SuppressWarnings("unused")
				MainController main = new MainController();
			} catch (TwitterException te) {
				if(401 == te.getStatusCode()){
					System.out.println("Unable to get the access token.");
				}else{
					te.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 設定ファイルにアクセストークンキーとアクセストークンシークレットを書き出す
	 * 
	 */
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
