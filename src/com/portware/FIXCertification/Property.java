package com.portware.FIXCertification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class Property {

	static String OS;
	final static String WINDOWS = "Windows";
	final static String UNIX = "Linux";
	static String HomePath;
	final static String HOME_DIR = "FIX-Replayer\\";
	final static String PROPERTY_FILE = "Replayer.properties";
	static Properties prop;

	public Property() {
		setOS();
		getAppHomeDirectory();
		checkHomeDir();
		checkPropFile();
		prop = new Properties();
		setGlobalProp();
	}

	public void setOS() {
		if (System.getProperty("os.name").startsWith("Window"))
			Property.OS = WINDOWS;
		else
			Property.OS = UNIX;
	}

	public void getAppHomeDirectory() {
		if (Property.OS.equals(WINDOWS))
			HomePath = "C://" + HOME_DIR;
		else
			HomePath = System.getProperty("user.home") + HOME_DIR;
	}

	public void checkHomeDir() {
		File dir = new File(HomePath);
		if (dir.exists() && dir.isDirectory()) {
			// Dir is present
		} else
			try {
				System.out.println("Setting things for the first time...");
				Files.createDirectory(Paths.get(HomePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void checkPropFile() {
		File file = new File(HomePath + PROPERTY_FILE);
		if (file.exists() && file.isFile()) {
			System.out.println("Structure is in place");
		} else {
			try {
				System.out.println("Setting the default Properties...");
				System.out.println(file.getAbsolutePath());
				file.createNewFile();
				getResourceFile(file.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void getResourceFile(String Path) {
		InputStream stream = Properties.class.getResourceAsStream("/Resources/Replayer.properties");
		BufferedReader fr = new BufferedReader(new InputStreamReader(stream));
		fr.lines().forEach(line -> {
			try {
				Files.write(Paths.get(Path), (line + "\n").getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	public void setGlobalProp() {
		System.out.println("Loading Properties...");
		try {
			InputStream input = new FileInputStream(HomePath + PROPERTY_FILE);
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getGlobalProp(String key)
	{
		return prop.getProperty(key);
	}
}
