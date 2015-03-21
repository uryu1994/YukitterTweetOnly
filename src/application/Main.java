package application;
	
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	private Stage stage;
	
	@Override
	public void start(Stage primaryStage) throws IOException {

		OAuthController oauth = new OAuthController(primaryStage);
		
		/*
		Parent view = FXMLLoader.load(getClass().getResource("tweetOnly.fxml"));
		Scene scene = new Scene(view);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Yukitter");
		primaryStage.show();
		System.out.println("show");
*/
		
//		OAuthController oauth = new OAuthController();

//		MainController main = new MainController(oauth.getAccessToken());
	}
	
	public void createYukitterStage() {
		Parent view;
		try {
			view = FXMLLoader.load(getClass().getResource("tweetOnly.fxml"));
			Scene scene = new Scene(view);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.setTitle("Yukitter");
			stage.show();
			System.out.println("show");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args) {
		launch(args);
	}
	
	@Override
	public void stop() throws Exception {
		MainController.shutdownTwitterStream();
		super.stop();
	}
}