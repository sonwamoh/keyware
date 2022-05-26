package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import edu.sjsu.yazdankhah.crypto.util.PassUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.classes.DBReader;
import model.classes.DBWriter;


public class ResetController {
	
	/**
	 * Fxml tags for reset  Controller controllers and all the fx-id in reset password fxml
	 */
	@FXML TextField usernameField;
	@FXML TextField newPasswordField;
	@FXML ComboBox securityQuestions;
	@FXML TextField securityQAnswer;
	@FXML Button resetButton;
	@FXML Button cancelButton;
	@FXML Pane resetPane;
	
	//Instance variables
	String userID = "";
	String newPass = "";
	String securityQ = "";
	String securityQA = "";
	
	//object instantiated
	DBReader db = new DBReader();
	Alert alertConfirm = new Alert (Alert.AlertType.CONFIRMATION);
	Alert alertError = new Alert (Alert.AlertType.ERROR);
	String [] securityQuestion = {"What is your father's middle name?", 
			"What is your city of birth?", "What was the make of your first car?", 
			"What was your first pet's name?", "What is your favorite sports team?"};
	public Stage primaryStage = application.Main.window;

	
	
	/*
	 * This method calls upon the reset method to check and reset the user's password
	 */
	public void resetPassword(ActionEvent event) throws IOException
	{
		
		if (reset() == true)
		{
			alertConfirm.setHeaderText(null);
			alertConfirm.setContentText("Successfully reset user password");
			alertConfirm.show();
			cancelButton();
		}
	}
	
	/*
	 * This method is to be used when the reset password button is pressed.
	 * In order to propagate the correct security questions into the dropdown menu.
	 */
	@FXML
	public void initialize() {
	    securityQuestions.getItems().removeAll(securityQuestions.getItems());
	    securityQuestions.getItems().addAll(securityQuestion);
	    securityQuestions.getSelectionModel().select(securityQuestion[0]);
	}
	/*
	 * This method makes various checks on the credentials input
	 * and if all credentials are verified, the method changes the
	 * password for the specific user in the database with the new
	 * password specified.
	 * 
	 * The method gathers data from both the application fields and
	 * the database then proceeds to iterate through the database line
	 * by line until a match with the username is found. Then various checks
	 * are made to make sure that a mistake in credentials will not happen.
	 * Finally, the method replaces the old password with the new one and 
	 * proceeds to rewrite the entire database file with the replaced
	 * password.
	 */
	public boolean reset() 
	{
		getData();
		PassUtil pu = new PassUtil();
		ArrayList<ArrayList<String>> fileArray = db.fileArrayList();
		ArrayList<String> userLine;
		ArrayList<String> fileDB = new ArrayList<>();
		alertError.setHeaderText(null);
		for (int i = 0; i < fileArray.size(); i++)
		{	
			userLine = fileArray.get(i);
			if (userID.equals(userLine.get(0).substring(1)))
			{
				//Check if new password is same as old
				if (pu.encrypt(newPass).equals(userLine.get(1)))
				{
					alertError.setContentText("The new password is the same as the old");
					alertError.show();
					return false;
				}
				//Check if new password is not entered
				else if (newPass.isEmpty() == true)
				{
					alertError.setContentText("Please enter missing information");
					alertError.show();
					return false;
				}
				//Nuanced check for incorrect credential in db
				else if (userLine.get(userLine.size() - 2).contains("/"))
				{
					alertError.setContentText("User not found");
					alertError.show();
					return false;
				}
				//Check if the answer to the security question is wrong 
				//and if the security question is the right question
				else if ((!securityQA.equals(userLine.get(userLine.size()-1))) || (!securityQ.equals(userLine.get(userLine.size() - 2))))
				{
					alertError.setContentText("The security question and/or answer is incorrect");
					alertError.show();
					return false;
				}
				else
				{
					//Rewrites the old password
					fileArray.get(i).set(1, pu.encrypt(newPass));
					DBWriter.writebackDB(fileArray);
					return true;
				}
			}
		}
		alertError.setContentText("User not found");
		alertError.show();
		return false;
	}
	
	/*
	 * This method gathers the current Strings from the various text fields on the Reset Password page.
	 */
	public void getData()
	{
		userID = usernameField.getText();
		newPass = newPasswordField.getText();
		securityQ = (String) securityQuestions.getValue();
		securityQA = securityQAnswer.getText();
	}
	
	
	/*
	 * When this method is called, the current scene changes to the Login screen.
	 */
	public void cancelButton() throws IOException
	{
		URL url = getClass().getClassLoader().getResource("view/Login.fxml");
		try
		{
			Pane loginPane = (Pane) FXMLLoader.load(url);
			resetPane.getChildren().add(loginPane);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
