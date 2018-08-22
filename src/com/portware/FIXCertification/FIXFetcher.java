package com.portware.FIXCertification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;




public class FIXFetcher 
{

	static String TradesFile ;
	static String QuotesFile ;
	static String Output ;
	
	ArrayList<String> transaction = new ArrayList<String>();
	public FIXFetcher(String TradesFile,String QuotesFile ,String Output)
	{
		FIXFetcher.TradesFile = TradesFile;
		FIXFetcher.QuotesFile =QuotesFile;
		FIXFetcher.Output = Output;
	}
	public ArrayList<String> getExecutionReport()
	{
		try 
		{
			 Files.lines(Paths.get(FIXFetcher.TradesFile)).parallel()
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "35=8"))
					//.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "40=D"))
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+"37="))				
					.forEach(exec -> getAssociateTrades(exec));
				return transaction;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public boolean getAssociateTrades(String exec)
	{
		try 
		{

			Optional<String> optionalTrade = Files.lines(Paths.get(FIXFetcher.TradesFile)).parallel()
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "35=D"))
					.filter(msg -> msg.contains(FIX.FIXDELIMITER + "11="+getFIXValue(exec,"11")))
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "117="+getFIXValue(exec,"37")))
					.findFirst();
			if(optionalTrade.isPresent()) 
			{
				String excecution = exec;
				String trade = optionalTrade.get();
				if(getResponse(trade))
				{
					trade = FIX.getParsedLog(trade);
					excecution = FIX.getParsedLog(excecution);
					transaction.add(trade);
					transaction.add(excecution);
					return true;
				}
				
			}
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public String getFIXValue(String msg, String key)
	{
		key = FIX.FIXDELIMITER + key ;
		String temp = msg.substring(msg.indexOf(key)+key.length());
		return temp.substring(1,temp.indexOf(FIX.FIXDELIMITER));
	}
	public boolean getResponse(String trades)
	{
		try 
		{
			Optional<String> optionalQuote = Files.lines(Paths.get(FIXFetcher.QuotesFile)).parallel()
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "35=S"))
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+"117="+getFIXValue(trades,"117")))
					.findFirst();
			if(optionalQuote.isPresent() && optionalQuote.get()!=null) 
			{
				String quote = optionalQuote.get();
				if(getAssociateRequest(quote))
				{
					quote = FIX.getParsedLog(quote);
					transaction.add(quote);
					return true;
				}
				//System.out.println("Adding quote response:" + quote);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean getAssociateRequest(String response)
	{
		try 
		{
			//System.out.println(result);
			 Optional<String> Opt_request = Files.lines(Paths.get(FIXFetcher.QuotesFile)).parallel()
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "35=R"))
					.filter(msg -> msg.contains(FIX.FIXDELIMITER + "131="+getFIXValue(response,"131")))
					.findFirst();
			if (Opt_request.isPresent()) 
			{
				//System.out.println("Addign Quote Response: "+ request.get());
				String Request = FIX.getParsedLog(Opt_request.get());
				transaction.add(Request);
				return true;
			}
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
		return false;
	}
	public String printData(String msg)
	{
		System.out.println(msg);
		return msg;
	}
}
