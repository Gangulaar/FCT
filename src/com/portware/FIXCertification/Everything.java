package com.portware.FIXCertification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Everything 
{
	String QuoteFile;// = "C:\\Users\\agangula\\eclipse-workspace\\FCT\\BOAFXQUOTES_20170919.log" ;
	String TradeFile;// = "C:\\Users\\agangula\\eclipse-workspace\\FCT\\BOAFXTRADES_20170919.log";
	String Outfile ;//= "C:\\Users\\agangula\\eclipse-workspace\\FCT\\Dummy.xml";
	static ArrayList<String> transactions;
	Everything()
	{}
	public Everything(String TradeFile, String QuoteFile,String Outfile) 
	{
		this.QuoteFile = QuoteFile;
		this.TradeFile = TradeFile;
		this.Outfile = Outfile;
		transactions = new ArrayList<String>();
	}
	public void collectEverything()
	{
		try
		{
			filterQuotesRequest();
			SavingXML sxml = new SavingXML(Outfile);
			sxml.writeTransactions(transactions);
			System.out.println("DONE");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void filterQuotesRequest()
	{
		try 
		{
			Files.lines(Paths.get(QuoteFile))
				.filter(trans -> trans.contains("35=R"))
				.forEach(trans -> getAssociatedResponse(trans));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getAssociatedResponse(String Request)
	{
		transactions.add(Request);
		try 
		{
			Files.lines(Paths.get(QuoteFile))
				.filter(msg -> !msg.contains("35=R"))
				.filter(trans -> trans.contains("131="+getFIXValue(Request,"131")))
				.forEach(trans -> getAssociatedOrder(trans));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getAssociatedOrder(String Response)
	{
		transactions.add(Response);
		try 
		{
			Files.lines(Paths.get(TradeFile))
				.filter(trans -> trans.contains("117="+getFIXValue(Response,"117")))
				.forEach(trans -> getAssociatedExecution(trans));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getAssociatedExecution(String Order)
	{
		transactions.add(Order);
		try 
		{
			Files.lines(Paths.get(TradeFile))
				.filter(trans -> trans.contains("37="+getFIXValue(Order,"117")))
				.forEach(trans -> transactions.add(trans));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getFIXValue(String msg, String key)
	{
		key = FIX.PARSED_DELIMITER + key ;
		String temp = msg.substring(msg.indexOf(key)+key.length());
		return temp.substring(1,temp.indexOf(FIX.PARSED_DELIMITER));
	}
}
