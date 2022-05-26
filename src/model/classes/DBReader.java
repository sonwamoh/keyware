package model.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import application.User;
import edu.sjsu.yazdankhah.crypto.util.PassUtil;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class DBReader 
{
	/**
	 * The user information is searched in the database
	 * ,by reading each and every line & string in database. 
	 * Used to verify login credentials.
	 * User info line starts with a "#" in database.
	 * @param user the login credentials of user
	 * @return true if user info is found in the database
	 */
	public boolean searchUserInfo(User user)
	{
		PassUtil pu = new PassUtil();
		Scanner sc = DBConnect.connectDB();
		String enteredUsername = user.getUsername();
		String enteredPassword = user.getMasterPassword();
		while (sc.hasNextLine())
		{
			String expectedUsername = sc.next();
			if (expectedUsername.startsWith("#"))
			{
				String expectedPassword = pu.decrypt(sc.next());
				if (expectedUsername.substring(1).equals(enteredUsername) && expectedPassword.equals(enteredPassword))
				{
					sc.close();
					return true;
				}
			}
			sc.nextLine();
		}
		sc.close();
		return false;
	}
	/**
	 * The user sign up information is searched in the database
	 * to check if the account already exists with those credentials.
	 * User sign up info line starts with a "#"
	 * @param user the sign up credentials of user
	 * @return true if user's sign up info is found in the database
	 */
	public boolean searchSignUpInfo(User user)
	{
		Scanner sc = DBConnect.connectDB();
		while (sc.hasNextLine())
		{
			//check if email or username are already taken
			String userName = sc.next();
			if (userName.startsWith("#"))
			{
				sc.next();
				String email = sc.next();
				if (user.getUsername().equals(userName.substring(1)) || user.getEmail().equals(email))
				{
					sc.close();
					return false;
				}
			}
			if (sc.hasNextLine())
			{
				sc.nextLine();
			}
		}
		sc.close();
		//if they are available
		return true;
	}
	
	/**
	 * This method returns an ArrayList of the type ArrayList of String that
	 * is able to be read and altered.
	 * 
	 * On call, this method reads and takes the database as an arraylist of 
	 * type string. Then converts the inner user credentials into of strings
	 * into arraylists of type string. Afterwards, the arraylist is added
	 * to the arraylist with the type arraylist of type string and returns
	 * that item.
	 */
	public static ArrayList<ArrayList<String>> fileArrayList()
	{
		ArrayList <String> info = DBReader.readIntoArray();
		ArrayList <ArrayList<String>> file = new ArrayList<>();
		//
		for(int i = 0; i < info.size(); i++)
		{
			String users = info.get(i);
			ArrayList<String> individual = new ArrayList<>(Arrays.asList(users.split(",")));
			file.add(individual);
			
		}
		return file;
	}
	/**
	 * Read into the line of the database line by line and store it using arrayList
	 * @return data of user's credential stored in database
	 */
	public static ArrayList <String> readIntoArray ()
	{
		ArrayList <String> info = new ArrayList <String>();
		Scanner sc = DBConnect.connectDB();
		while (sc.hasNextLine())
		{
			info.add(sc.nextLine());
		}
		if (sc.hasNextLine())
		{
			sc.nextLine();
		}
		sc.close();
		return info;
	}
	
	
	public static void copyToClipboard(String s) {
		
		final Clipboard cb = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		
		//ArrayList<ArrayList<String>> list = DBReader.fileArrayList();
	
		content.putString(s);
		cb.setContent(content);
	}
}
