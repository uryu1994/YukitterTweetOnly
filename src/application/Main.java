package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	@Override

	public void start(Stage primaryStage) {
		try {
			Parent view = FXMLLoader.load(getClass().getResource("tweetOnly.fxml"));
			Scene scene = new Scene(view);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Yukitter");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args) {
		launch(args);
	}
	
	@Override
	public void stop() {
		mainController.getTwitterStreamInstance().shutdown();
	}
}