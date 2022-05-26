package model.classes;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


import application.User;
import edu.sjsu.yazdankhah.crypto.util.PassUtil;

public class DBWriter 
{
	/**
	 * Write the sign up info of user into the database
	 * @param user the sign up credentials of the user
	 */
	public static void writeSignupInfoToDB(User user)
	{
		PassUtil pu = new PassUtil();
		ArrayList <String> info = DBReader.readIntoArray();
		String newInfo = "#" + user.getUsername()+ "," + pu.encrypt(user.getMasterPassword()) + 
				"," + user.getEmail() + "," + user.getSecurityQuestion() + "," + 
				user.getSecurityQuestionAnswer();
		try
		{	
			FileWriter fw = new FileWriter("resources/database/database.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			//DB is empty
			if (info.size() == 0)
			{
				info.add(newInfo);
			}
			//data already stored in DB
			else 
			{
				info.add(newInfo);
			}
			for (int i = 0; i < info.size(); i++)
			{
				bw.write(info.get(i) + System.getProperty("line.separator"));
			}
			bw.close();
		}
		catch(Exception e)
		{
			e.getMessage();
			e.printStackTrace();
		}
	}
	/**
	 * Write the account info of user into the database
	 * @param user the account credentials of the user
	 */
	public static void writeUserAccountToDB(User user, String userName)
	{
		PassUtil pu = new PassUtil();
		ArrayList <String> info = DBReader.readIntoArray();
		String newInfo = user.getAccountSite()+ "," + user.getUsername()+ "," + pu.encrypt(user.getMasterPassword()) + 
				"," + user.getEmail() + "," + user.getCreationDate() + "," + 
				user.getExpirationDate();
		for(int i = 0; i < info.size(); i ++)
		{
			if(info.get(i).charAt(0) == '#') 
			{
				int index = info.get(i).indexOf(",");
				String userID = info.get(i).substring(1,index);
				if(userID.equals(userName)) {
					info.add(i+1, newInfo);
					break;
				}
			}
		}
		try
		{	
			FileWriter fw = new FileWriter("resources/database/database.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			//DB is empty
			
			for (int i = 0; i < info.size(); i++)
			{
				bw.write(info.get(i) + System.getProperty("line.separator"));
			}
			bw.close();
		}
		catch(Exception e)
		{
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	/*
	 * This method writes back the fileArray variable into the original
	 * database.
	 * @param fileArray the arraylist to be written back to the database
	 */
	public static void writebackDB(ArrayList<ArrayList<String>> fileArray) 
	{
		ArrayList<String> userLine;
		ArrayList<String> fileDB = new ArrayList<>();
		PassUtil pu = new PassUtil();
		try 
		{
			File file = new File("resources/database/database.txt");
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < fileArray.size(); i++)
			{
				userLine = fileArray.get(i);
				String use = userLine.toString().replaceAll(", ", ",");
				use = use.substring(1, use.length() - 1);					//Delete "[" and "]" from string
				fileDB.add(use);
			}
			//Rewrite entire file without the specified account
			for (int k = 0; k < fileDB.size(); k++)
			{
				bw.write(fileDB.get(k) + System.getProperty("line.separator"));
			}
			bw.close();
			fw.close();
		}
		catch(Exception e)
		{
			System.out.println("Error has occurred " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*
	 * Delete a specific website account in the database by the row
	 * @param j the row/account to be deleted
	 */
	public static void deleteAccount(int j)
	{
		ArrayList<ArrayList<String>> fileArray = DBReader.fileArrayList();
		fileArray.remove(j);
		writebackDB(fileArray);
	}
	
	/*
	 * Replaces the account parameters within a specific website account in the 
	 * database by the row
	 * @param j the row/account to be edited
	 * @param website the website to replace
	 * @param user the new userID to replace
	 * @param newPass the new password to replace
	 * @param email the new email to replace
	 */
	public static void editAccount(int j, String website, String user, String newPass, String email, String expire)
	{
		ArrayList<ArrayList<String>> fileArray = DBReader.fileArrayList();
		fileArray.get(j).set(0, website);
		fileArray.get(j).set(1, user);
		fileArray.get(j).set(2, newPass);
		fileArray.get(j).set(3, email);
		fileArray.get(j).set(5, expire);
		writebackDB(fileArray);
	}
}
