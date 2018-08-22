package com.portware.FIXCertification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler 
{
	public String displayMenu(ArrayList<String> files)
	{
		int count =0;
		for (String file : files)
		{
			System.out.println(++count+". "+file);
		}
		Scanner scan = new Scanner(System.in);
		String	FileName = files.get(scan.nextInt()-1);
		scan.close();
		return FileName;
	}

	public  ArrayList<String> getPWDFiles(String pwd)
	{
		System.out.println("Searching the Files in Present Directoy...");
		try
		{
			File present = new File(pwd);
			File[] filesList = present.listFiles();
			ArrayList <String> Logfiles = new ArrayList<String>();
			for (File file : filesList) 
			{	
				if (file.isFile() && getFileExt(file.getCanonicalPath()) && getFileType(file.getCanonicalPath())) 
					Logfiles.add(file.getCanonicalPath());
			}
			return Logfiles.size() > 0 ? Logfiles : new ArrayList<String>();
		}
		catch(Exception e)
		{
			
		}
		return null;
	}
	private  boolean getFileType(String canonicalPath) 
	{
		//System.out.println("Path is: "+canonicalPath);
		return canonicalPath.contains("QUOTES") || canonicalPath.contains("TRADES") ? true : false;
	}
	private  Boolean getFileExt(String string) 
	{
		//System.out.println("Ext: "+string.substring(string.indexOf(".")));
		return string.substring(string.indexOf(".")).contains(".log") ? true : false; 
	}
	public void truncateFile(File file)
	{
		FileWriter fw;
		try 
		{
			fw = new FileWriter(file, false);
			fw.flush();
		    fw.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	

}
