package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ImageDialogController {
	
	private Stage stage;
	
	@FXML
	private ImageView imageView;
	
	public ImageDialogController(String string) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageDialog.fxml"));
		loader.setController(this);
		
		try {
			Scene scene = new Scene(loader.load());
			
			stage = new Stage(StageStyle.TRANSPARENT);
			stage.setScene(scene);
			stage.setResizable(false);
			
			imageView.setImage(new Image(string));
			
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onDialogClose(MouseEvent e) {
		stage.close();
	}
}
