package controller;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import application.User;
import edu.sjsu.yazdankhah.crypto.util.PassUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.classes.DBReader;
import model.classes.DBWriter;

public class SearchPasswordController {

	/**
	 * Fxml tags for add password Manager Controller controllers and all the fx-id in password Manager fxml
	 */
	@FXML TextField password1;
	@FXML AnchorPane searchPane;
	@FXML VBox mainBox;
	@FXML Button addPassword;
	@FXML Button exit;
	@FXML GridPane gridPane;
	@FXML HBox hbox;
	@FXML Button searchButton;
	@FXML Button cancelSearchButton;
	@FXML TextField searchField;
	@FXML Button cancelButton;
	
	
	/*
	 * Instance variables
	 */
	int rowID;
	int deleteID;
	int copyId;
	
	/**
	 * Object instantiated 
	 */
	ArrayList <String> userAccounts = new ArrayList <String>();
	Alert alert = new Alert (Alert.AlertType.ERROR);
	@FXML Button generateButton;

	/**
	 * Redirect to add password from password manager
	 */
	public void addPassword() {
		URL url = getClass().getClassLoader().getResource("view/AddPassword.fxml");
		try {
			AnchorPane pane = (AnchorPane)FXMLLoader.load(url);
			searchPane.getChildren().add(pane);
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/**
	 * Retrieve the stored account data from database.
	 * Display the account info in password manager filtered by the search parameter.
	 * Increases the gridPane to display data dynamically
	 */
	public void searchAccounts()
	{
		PassUtil pu = new PassUtil();
		if (searchField.getText() == null)
		{
			alert.setHeaderText(null);
			alert.setContentText("Please enter a username or application name to search for in the neighboring field." );
			alert.show();
		}
		else
		{
			clearGrid();
			rowID = 1;
			int displayedSearchResults = 0;
			userAccounts = retrieveAccounts();
			//for all stored accounts for the logged in user, display in the saved password table
			for (int i = 0; i < userAccounts.size(); i++)
			{
				if (userAccounts.get(i).isEmpty() != true)
				{
					String [] enteredAccount = userAccounts.get(i).split(",");
					TextField applicationName = new TextField(enteredAccount [0]);
					TextField userName = new TextField(enteredAccount [1]);
					TextField password = new TextField(pu.decrypt(enteredAccount [2]));
					TextField emailID = new TextField(enteredAccount [3]);
					TextField creationDate = new TextField(enteredAccount [4]);
					TextField expireDate = new TextField(enteredAccount [5]);
					//check if entered search parameter matches any stored usernames or application names
					if (enteredAccount [0].contains(searchField.getText()) || enteredAccount [1].contains(searchField.getText()))
					{
						Button editPass = new Button("Edit");
						editPass.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() { 
					         @Override 
					         public void handle(javafx.scene.input.MouseEvent e) { 
					        	 TextInputDialog td = new TextInputDialog();
					        	 td.setTitle("");
					        	 td.setHeaderText("Edit (" + enteredAccount[0] + ") " + enteredAccount[1] + "'s password?");
					        	 td.setContentText("Old Password:  " + pu.decrypt(enteredAccount[2]));
					        	 DialogPane dp = td.getDialogPane();
					        	 dp.setHeaderText("Edit (" + enteredAccount[0] + ") " + enteredAccount[1] + "'s account info?");
					        	 TextField website = new TextField(enteredAccount[0]);
					        	 TextField userID = new TextField(enteredAccount[1] );
					        	 TextField pass = new TextField(pu.decrypt(enteredAccount[2]));
					        	 TextField email = new TextField(enteredAccount[3] );
					        	 TextField expire = new TextField(enteredAccount[5] );
					        	 VBox textInputs = new VBox(8, website, userID, pass, email, expire);
					        	 VBox textFields = new VBox(18, new Text("Old Website: " + enteredAccount [0]), new Text("Old UserID: " + enteredAccount [1])
					        	 , new Text("Old Password: " + pu.decrypt(enteredAccount [2])), new Text("Old Email: " + enteredAccount [3]), new Text("Old Expiration Date: " + enteredAccount [5]));
					        	 HBox hb = new HBox(8, textFields, textInputs);
					        	 dp.setContent(hb);
					        	 Optional<String> tda = td.showAndWait();
					        	 if (tda.isPresent())
					        	 {
					        		 //Check if textinputfield is empty or just spaces
					        		 if ((website.getText().trim().isEmpty() || userID.getText().trim().isEmpty() || pass.getText().trim().isEmpty() || email.getText().trim().isEmpty()) || expire.getText().trim().isEmpty())
					        		 {
					        			 Alert alert = new Alert(AlertType.WARNING, "Cannot be empty!");
					        			 alert.showAndWait();
					        		 }
					        		 else
					        		 {
					        				 for (int j = 0; j <userAccounts.size(); j++)
									        	{
									        		//Check if user account name to be edited is correct
									        		if(enteredAccount[1].equals(userAccounts.get(j).split(",")[1]) && enteredAccount[0].equals(userAccounts.get(j).split(",")[0]))
									        		{
									        			//The TRUE row number to be used to edited
									        			int trueRowEditID = deleteID + j + 1;
									        			
									        			DBWriter.editAccount(trueRowEditID, website.getText(), userID.getText(), pu.encrypt(pass.getText()), email.getText(), expire.getText());
									        			//Refresh the password manager page to show item is edited
									        			URL url = getClass().getClassLoader().getResource("view/passwordManager.fxml");
									        			try {
									        				AnchorPane pane = (AnchorPane)FXMLLoader.load(url);
									        				searchPane.getChildren().add(pane);
									        				
									        			} catch (IOException x) {
									        				x.printStackTrace();
									        			}	
									        		}
									        	}
					        		 }
					        		 
					        	 }
					        	 else
					        	 {
					        	 }
					         } 
					      });
						Button delete = new Button("Delete");
						delete.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() { 
					         @Override 
					         public void handle(javafx.scene.input.MouseEvent e) { 
					        	 Alert alert = new Alert(AlertType.CONFIRMATION, "Delete (" + enteredAccount[0] + ") " + enteredAccount[1] + " ?", ButtonType.YES, ButtonType.CANCEL);
					        	 alert.showAndWait();
					        	 if (alert.getResult() == ButtonType.YES) {
					        		 for (int j = 0; j <userAccounts.size(); j++)
							        	{
							        		//Check if user account name to be deleted is 
							        		if(enteredAccount[1].equals(userAccounts.get(j).split(",")[1]) && enteredAccount[0].equals(userAccounts.get(j).split(",")[0]))
							        		{
							        			//The TRUE row number to be used to delete
							        			int trueRowDeleteID = deleteID + j + 1;
							        			
							        			DBWriter.deleteAccount(trueRowDeleteID);
							        			//Refresh the password manager page to show item is deleted
							        			URL url = getClass().getClassLoader().getResource("view/passwordManager.fxml");
							        			try {
							        				AnchorPane pane = (AnchorPane)FXMLLoader.load(url);
							        				searchPane.getChildren().add(pane);
							        				
							        			} catch (IOException x) {
							        				x.printStackTrace();
							        			}	
							        		}
							        	}   
					        	 }       
					            
					         } 
					      });   
						
						Button copy = new Button("Copy");
						copy.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
							
							@Override
							public void handle(javafx.scene.input.MouseEvent m) {
								for(int i = 0; i < userAccounts.size(); i++) {
									if(enteredAccount[1].equals(userAccounts.get(i).split(",")[1]) && enteredAccount[0].equals(userAccounts.get(i).split(",")[0])) {
										
										//int copyRowId = copyId +1 +i;
										DBReader.copyToClipboard(pu.decrypt(enteredAccount[2]));
									}
								}
								
								Alert alert = new Alert(AlertType.INFORMATION, "Password copied to clipboard!");
								alert.show();
								
								URL url = getClass().getClassLoader().getResource("view/passwordManager.fxml");
								AnchorPane pane;
								try {
									pane = (AnchorPane) FXMLLoader.load(url);
									searchPane.getChildren().add(pane);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						});
						//populate password table in the main page with the filtered search parameter
						gridPane.addRow(rowID, applicationName, userName, password, emailID, creationDate, expireDate, editPass, delete, copy);
						rowID++;
						displayedSearchResults++;
					}
				}
			}
			if (displayedSearchResults == 0)
			{
				alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("No accounts found with the entered username/application name.");
				alert.show();
			}
		}
	}
	
	/**
	 * Clears the grid pane before each new display of the search results
	 */
	public void clearGrid()
	{
		gridPane.getChildren().clear();
	}
	
	/**
	 * Retrieve the user account info from database by using DBReader
	 * ,and store the user's account info in arrayList to perform operations
	 * on password manager.
	 * @return arrayList of user Account Info.
	 */
	public static ArrayList <String> retrieveAccounts() 
	{
		ArrayList<String> dbInfo = DBReader.readIntoArray();
		ArrayList <String> userAccounts = new ArrayList <String>();
		User user = LoginController.userLoggedIn;
		int startIndex = 0;
		int endIndex = 0;
		for(int i = 0; i <dbInfo.size(); i ++) 
		{
			if(dbInfo.get(i).charAt(0) == '#') {
				int commaIndex = dbInfo.get(i).indexOf(",");
				String userId = dbInfo.get(i).substring(1, commaIndex);
				if(user.getUsername().equals(userId)) {
					startIndex = i;
					break;
				}
			}
		}
		for(int i = startIndex+1; i < dbInfo.size(); i ++) {
			if(dbInfo.get(i).charAt(0) == '#') 
			{
				endIndex = i;
				break;
			}
			if (i+1 == dbInfo.size())
			{
				endIndex = dbInfo.size();
				break;
			}
		}
		
		for(int i = startIndex + 1; i < endIndex; i ++) {
			userAccounts.add(dbInfo.get(i));
		}
		
		return userAccounts;
	}
	
	/*
	 * Finds the value of the instance variable, deleteID
	 * from the database. "deleteID" is the line number
	 * of the user's master account credentials used to login.
	 */
	public void getDeleteID()
	{
		ArrayList<String> dbInfo = DBReader.readIntoArray();
		ArrayList <String> userAccounts = new ArrayList <String>();
		User user = LoginController.userLoggedIn;
		for(int i = 0; i <dbInfo.size(); i ++) 
		{
			if(dbInfo.get(i).charAt(0) == '#') {
				int commaIndex = dbInfo.get(i).indexOf(",");
				String userId = dbInfo.get(i).substring(1, commaIndex);
				if(user.getUsername().equals(userId)) {
					deleteID = i;
					break;
				}
			}
		}
		
	}
	
	private int getCopyId() {
		ArrayList<String> list = DBReader.readIntoArray();
		User user = LoginController.userLoggedIn;
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).charAt(0) == '#') {
				int commaIndex = list.get(i).indexOf(',');
				String id = list.get(i).substring(1, commaIndex);
				if(user.getUsername().equals(id))
					return i;
			}
		}
		return -1;
	}
	
	//Cancel the password search
	public void cancelSearch()
	{
		URL url = getClass().getClassLoader().getResource("view/passwordManager.fxml");
		try
		{
			AnchorPane manager = (AnchorPane) FXMLLoader.load(url);
			searchPane.getChildren().add(manager);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Redirect to generate password from password manager
	 */
	public void generatePassword() {
		URL url = getClass().getClassLoader().getResource("view/generateRandomPassword.fxml");
		try {
			AnchorPane pane = (AnchorPane)FXMLLoader.load(url);
			searchPane.getChildren().add(pane);
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	//Exit from password manager to login
	@FXML public void exit()
	{
		URL url = getClass().getClassLoader().getResource("view/Login.fxml");
		try
		{
			Pane loginPane = (Pane) FXMLLoader.load(url);
			searchPane.getChildren().add(loginPane);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
