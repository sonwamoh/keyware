package controller;

import java.io.IOException;
import java.net.URL;

import application.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.classes.DBReader;

public class LoginController 
{
	/**
	 * Fxml tags for add Login Controller controllers and all the fx-id in addLogin fxml
	 */
	@FXML TextField usernameField;
	@FXML TextField passwordField;
	@FXML VBox loginVBox;
	@FXML Pane loginPane;
	@FXML Button loginButton;
	@FXML Text createAccountButton;
	@FXML Text forgotPasswordButton;
	
	/*
	 * New object instantiated 
	 */
	DBReader db = new DBReader();
	Alert alert = new Alert (Alert.AlertType.ERROR);
	
	//static variables
	static User userLoggedIn = null;

	/**
	 * Validate the user info to move into password Manager
	 * If the user login is successful, the password and username are correct
	 * the user moves from login to password manager
	 * otherwise, get's a pop up that login informaton is incorrect.
	 */
	public void checkUserInfo()
	{
		User user = new User(usernameField.getText(), passwordField.getText());
		boolean validate = db.searchUserInfo(user);
		if (validate == true)
		{
				userLoggedIn = user;
				
			  URL url = getClass().getClassLoader().getResource("view/passwordManager.fxml"); 
			  try 
			  {
				  userLoggedIn.showExpiredPasswordsAtLogin = true;
				  AnchorPane managerPane = (AnchorPane) FXMLLoader.load(url);
				  loginPane.getChildren().add(managerPane); 
			  } 
			  
			  catch (IOException e) 
			  {
				  e.printStackTrace(); 
			  }
		}
		else
		{
			alert.setHeaderText(null);
			alert.setContentText("Login information is incorrect");
			alert.show();
		}
	}
	
	/**
	 * User moves to create account from login
	 */
	public void createAccount()
	{
		URL url = getClass().getClassLoader().getResource("view/Signup.fxml");
		try 
		{
			Pane signupPane = (Pane) FXMLLoader.load(url);
			loginPane.getChildren().add(signupPane);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * user moves to reset from login.
	 */
	public void resetPassword()
	{
		URL url = getClass().getClassLoader().getResource("view/Reset.fxml");
		try 
		{
			Pane resetPane = (Pane) FXMLLoader.load(url);
			loginPane.getChildren().add(resetPane);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
