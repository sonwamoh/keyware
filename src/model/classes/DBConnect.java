package model.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DBConnect
{
	@SuppressWarnings("resource")
	static Scanner connectDB()
	{
		//creates a new file for database
		File db = new File("resources/database/database.txt");
		
		Scanner sc = null;
		try
		{
			sc = new Scanner(db).useDelimiter(",|\\n");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return sc;
	}
}
