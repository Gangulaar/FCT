package com.portware.FIXCertification;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader 
{
	public static void main(String args[])
	{
		Properties p = new Properties();
		InputStream input =null;
		try 
		{
			
			input = new FileInputStream("C:\\FIXCA\\config\\config.properties");

			p.load(input);
			
			System.out.println(p.getProperty("Excecution"));
		//	System.out.println(p.getProperty("dbuser"));
			//System.out.println(p.getProperty("dbpassword"));

		} 
		catch (IOException ex) 
		{
			System.out.println("Config properties file is misisng");
			ex.printStackTrace();
		} 
		finally 
		{
			if (input != null) 
			{
				try 
				{
					input.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
