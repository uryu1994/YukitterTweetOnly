package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {

		@SuppressWarnings("unused")
		OAuthController oauth = new OAuthController(primaryStage);
	}

	public static void main(String [] args) {
		launch(args);
	}
	
	@Override
	public void stop() throws Exception {
		DialogManager.shutdownDialogs();
		MainController.shutdownTwitterStream();
		super.stop();
	}
}