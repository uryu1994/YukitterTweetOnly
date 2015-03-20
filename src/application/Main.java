package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	@Override

	public void start(Stage primaryStage) throws IOException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("tweetOnly.fxml"));
			Parent view = loader.load();

			Scene scene = new Scene(view);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Yukitter");
			primaryStage.show();
			MainController mainController = (MainController) loader.getController();
			mainController.setStage(primaryStage);
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