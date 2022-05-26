package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * @author team 4
 * Date: 5-7-2022
 * @version KeyWare 0.9
 */

public class Main extends Application {
	
 public static Stage window; 
	/**
	 * Lauch the user interface of KeyWare
	 * @param primaryStage represent the primary window of our javaFX application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root = (Pane)FXMLLoader.load(getClass().getClassLoader().getResource("view/Login.fxml"));
			Scene scene = new Scene(root,1316,900);
			primaryStage.setScene(scene);
			primaryStage.show();
			root.requestFocus();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The main method to run the application
	 * @param args is a string 
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
