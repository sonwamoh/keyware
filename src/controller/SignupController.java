package controller;

import java.io.IOException;
import java.net.URL;

import application.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.classes.DBReader;
import model.classes.DBWriter;

public class SignupController 
{
	/**
	 * Fxml tags for reset  Controller controllers and all the fx-id in reset password fxml
	 */
	@FXML Text goBackButton;
	@FXML Button signupButton;
	@FXML TextField passwordField;
	@FXML TextField usernameField;
	@FXML Pane signupPane;
	@FXML TextField confirmPasswordField;
	@FXML ComboBox<String> securityDropdown;
	@FXML TextField securityAnswerField;
	@FXML TextField emailField;
	String [] securityQuestions = {"What is your father's middle name?", 
			"What is your city of birth?", "What was the make of your first car?", 
			"What was your first pet's name?", "What is your favorite sports team?"};
	
	//Objects Instantiated
	DBReader dbRead = new DBReader();
	Alert alertError = new Alert (Alert.AlertType.ERROR);
	Alert alertConfirm = new Alert (Alert.AlertType.CONFIRMATION);
	
	/**
	 * display the security questions to securtiy drop down
	 */
	public void initialize()
	{
		for (int i = 0; i < securityQuestions.length; i++)
		{
			securityDropdown.getItems().add(securityQuestions [i]);
		}
	}
	
	/**
	 * Go back from sign up to login
	 */
	public void goBack()
	{
		URL url = getClass().getClassLoader().getResource("view/Login.fxml");
		try
		{
			Pane loginPane = (Pane) FXMLLoader.load(url);
			signupPane.getChildren().add(loginPane);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * To validate user sign up information before storing in into database
	 * The textfield, user's username & password are validated
	 * If the user's sign up information is not validates then there's 
	 * a pop up for the error
	 */
	public void validateInfo()
	{
		alertError.setHeaderText(null);
		alertConfirm.setHeaderText(null);
		//check that all fields have been filled out
		if (emailField.getText() != null && usernameField.getText() != null 
				&& passwordField.getText() != null && confirmPasswordField.getText() 
				!= null && securityDropdown.getSelectionModel().isEmpty() == false 
				&& securityAnswerField.getText() != null)
		{
			//check that passwords match
			if (passwordField.getText().equals(confirmPasswordField.getText()))
			{
				User user = new User(usernameField.getText(), passwordField.getText(), emailField.getText(), 
						securityDropdown.getValue(), securityAnswerField.getText());
				//check that username and email are available
				Boolean validate = dbRead.searchSignUpInfo(user); 
				if (validate == true)
				{
					alertConfirm.setContentText("Account created successfully");
					alertConfirm.show();
					DBWriter.writeSignupInfoToDB(user);
					goBack();
				}
				else
				{
					alertError.setContentText("Username and/or email is already registered to another account");
					alertError.show();
				}
			}
			//passwords do not match
			else
			{
				alertError.setContentText("Entered passwords do not match");
				alertError.show();
			}
		}
		else
		{
			alertError.setContentText("Enter missing information");
			alertError.show();
		}
	}
}
