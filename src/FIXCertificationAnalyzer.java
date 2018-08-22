import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import com.portware.FIXCertification.FIXFetcher;
import com.portware.FIXCertification.FetchAll;
import com.portware.FIXCertification.XMLWriterDOM;
import com.portware.FIXCertification.FileHandler;
import com.portware.FIXCertification.XMLParser;
import com.portware.FIXCertification.FIX;;
public class FIXCertificationAnalyzer 
{
	static String TradesFile ;
	static String QuotesFile ;
	static String Output = "cache.xml";
	static String pwd = System.getProperty("user.dir");
	public static void main(String arg[])
	{
		FileHandler fh = new FileHandler();
		ArrayList <String> files=fh.getPWDFiles(pwd);					 // get the present directory files
		ArrayList<String> nameList_quote = new ArrayList<String>();   	// list for Quotes
		ArrayList<String> nameList_trade = new ArrayList<String>();   	//list for Trades
		if( !files.isEmpty() && files != null)							//if files are present
		{
			for(String file : files)
			{
				if(file.contains("TRADES"))
					nameList_trade.add(file);
				else if(file.contains("QUOTES"))
					nameList_quote.add(file);
			}
			if(nameList_trade.size() > 1 && nameList_quote.size() > 1)  //multiple file are found select trade and quote file
			{
				//select one of the Destination to read
				TradesFile = fh.displayMenu(nameList_trade);
				QuotesFile = fh.displayMenu(nameList_quote);
				Output = pwd + "\\" + Output;
			}
			else if(nameList_trade.size() == 0 || nameList_quote.size() == 0) //If there are file present but there are not valid 
			{
				System.out.println("Can't recognize a valid log, Please enter the folowing details:");
			setCustomEntries();
			}
			else //There is only 1 trade and 1 Quote
			{
				for(String file : files)
				{
					if(file.contains("TRADES"))
						TradesFile = new File(file).getAbsolutePath();
					else
						QuotesFile =  new File(file).getAbsolutePath();
				}
				Output = pwd + "\\" + Output;
			}
		}
		else  // if dir is empty
		{
			System.out.println("Can't recognize a valid log Please enter the folowing details:");
			setCustomEntries();
		}
		System.out.println(TradesFile);
		System.out.println(QuotesFile);
		System.out.println(Output);
		FIX fix = new FIX();
		Optional<String> firstLine = null;
		try 
		{
			firstLine = Files.lines(Paths.get(TradesFile))
				.filter(msg -> msg.contains("8=FIX.4.3"))
				.findFirst();
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		fix.setDelimiter(firstLine.get());
		FIXFetcher f = new FIXFetcher(TradesFile, QuotesFile, Output);
		try 
		{
			File file = new File(Output);
			if(file.exists())
			{
				fh.truncateFile(file);
			}
			else
			{
				Files.createFile(Paths.get(Output));
			}
			System.out.println("Started Analyzing");
			Set<String> tags = new HashSet<>();
			tags.add("35=8");
			tags.add("40=D");
			tags.add("37=");
			//ArrayList<String> trans = f.getExecutionReport();
			FetchAll f1=new FetchAll("C:\\Users\\agangula\\Desktop\\BOA Latest\\v 1.0\\BOAFXTRADES_20170919.log","C:\\Users\\agangula\\Desktop\\BOA Latest\\v 1.0\\BOAFXQUOTES_20170919.log" , "C:\\Users\\agangula\\Desktop\\BOA Latest\\v 1.0\\cache.xml"  );
			f1.getRequest();
			System.out.println("Done Analyzing");
		/*	if(trans.isEmpty() || trans.get(0) == null)
			{
				System.out.println("No transactions to write");
			}
			else
			{
				System.out.println("Writing to cache...");
				XMLWriterDOM smlwd = new XMLWriterDOM();
				smlwd.writeToXML(trans, Output);
			}*/
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		//XMLParser xmlp =new XMLParser(TradesFile, QuotesFile, Output);
		//xmlp.initalized();
	}
	public static void setCustomEntries()
	{
		System.out.println("Enter Trade File location:");
		Scanner scan = new Scanner(System.in);
		TradesFile = scan.nextLine();
		System.out.println("Enter Quote File location:");
		QuotesFile = scan.nextLine();
		Output = new File(TradesFile).getAbsolutePath().substring(0, new File(TradesFile).getAbsolutePath().lastIndexOf("\\"))+"\\"+Output;
		scan.close();
	}
}
