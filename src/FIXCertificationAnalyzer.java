import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import com.portware.FIXCertification.FIXFetcher;
import com.portware.FIXCertification.XMLWriterDOM;
import com.portware.FIXCertification.FileHandler;
import com.portware.FIXCertification.Property;
import com.portware.FIXCertification.XMLParser;
import com.portware.FIXCertification.ConstructInbound;
import com.portware.FIXCertification.Everything;
import com.portware.FIXCertification.FIX;;

public class FIXCertificationAnalyzer {
	static String TradesFile;
	static String QuotesFile;
	static String Output = "cache.xml";
	static String pwd = System.getProperty("user.dir");
	FileHandler fh;
	ArrayList<String> files;
	ArrayList<String> nameList_quote;
	ArrayList<String> nameList_trade;

	FIXCertificationAnalyzer() {
		fh = new FileHandler();
		files = fh.getPWDFiles(pwd);
		nameList_quote = new ArrayList<String>(); // list for Quotes
		nameList_trade = new ArrayList<String>(); // list for Trades// get the present directory files

	}

	public static void main(String arg[]) {

		FIXCertificationAnalyzer fixCA = new FIXCertificationAnalyzer();

		fixCA.loadGlobalProps();

		fixCA.setFilePaths();

		fixCA.printFiles();
		fixCA.setDelimterType();

		new FileHandler().createFile(Output);

		if (Property.getGlobalProp("RunType").equals("All")) {
			System.out.println("Collecting Everything");
			fixCA.collectEverything();
		} else {
			System.out.println("Collecting Valid Excecution only");
			fixCA.collectSelectively();
		}

		XMLParser xmlp = new XMLParser(TradesFile, QuotesFile, Output);
		xmlp.initalized();

		ConstructInbound ci = new ConstructInbound();
		//ci.buildInbound();
		System.out.println("Program complete");
	}

	private void loadGlobalProps() {
		new Property();
	}

	public static void setCustomEntries() {
		System.out.println("Enter Trade File location:");
		Scanner scan = new Scanner(System.in);
		TradesFile = scan.nextLine();
		System.out.println("Enter Quote File location:");
		QuotesFile = scan.nextLine();
		Output = new File(TradesFile).getAbsolutePath().substring(0,
				new File(TradesFile).getAbsolutePath().lastIndexOf("\\")) + "\\" + Output;
		scan.close();
	}

	public void printFiles() {
		System.out.println(TradesFile);
		System.out.println(QuotesFile);
		System.out.println(Output);
	}

	public void setDelimterType() {
		FIX fix = new FIX();
		Optional<String> firstLine = null;
		try {
			firstLine = Files.lines(Paths.get(TradesFile)).filter(msg -> msg.contains("8=FIX.4.3")).findFirst();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		fix.setDelimiter(firstLine.get());
	}

	public void collectSelectively() {
		// Bottom-up approach (Gets only executed transactions)
		FIXFetcher f = new FIXFetcher(TradesFile, QuotesFile, Output);
		try {
			System.out.println("Started Analyzing");
			ArrayList<String> trans = f.getExecutionReport();
			System.out.println("Done Analyzing");
			if (trans.isEmpty() || trans.get(0) == null) {
				System.out.println("No transactions to write");
			} else {
				System.out.println("Writing to cache...");
				XMLWriterDOM smlwd = new XMLWriterDOM();
				smlwd.writeToXML();
				smlwd.addChildNodes(trans);
				smlwd.writeToFile(Output);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void collectEverything() {
		// Top-Down approach (Gets all the transactions)
		Everything e = new Everything(TradesFile, QuotesFile, Output);
		try {
			e.collectEverything();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	public void setFilePaths() {
		FileHandler fh = new FileHandler();
		if (!files.isEmpty() && files != null) // if files are present
		{
			for (String file : files) {
				if (file.contains("TRADES"))
					nameList_trade.add(file);
				else if (file.contains("QUOTES"))
					nameList_quote.add(file);
			}
			if (nameList_trade.size() > 1 && nameList_quote.size() > 1) // multiple file are found select
																		// trade and quote file
			{
				// select one of the Destination to read
				TradesFile = fh.displayMenu(nameList_trade);
				QuotesFile = fh.displayMenu(nameList_quote);
				Output = pwd + "\\" + Output;
			} else if (nameList_trade.size() == 0 || nameList_quote.size() == 0) // If there are file
																					// present but there are
																					// not valid
			{
				System.out.println("Can't recognize a valid log, Please enter the folowing details:");
				setCustomEntries();
			} else // There is only 1 trade and 1 Quote
			{
				for (String file : files) {
					if (file.contains("TRADES"))
						TradesFile = new File(file).getAbsolutePath();
					else
						QuotesFile = new File(file).getAbsolutePath();
				}
				Output = pwd + "\\" + Output;
			}
		} else // if dir is empty
		{
			System.out.println("Can't recognize a valid log Please enter the folowing details:");
			setCustomEntries();
		}
	}
}
