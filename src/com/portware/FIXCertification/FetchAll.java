package com.portware.FIXCertification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class FetchAll 
{
	static String TradesFile ;
	static String QuotesFile ;
	static String Output ;
	private Boolean setRequest = false;
	ArrayList<String> Quotes = new ArrayList<String>();
	ArrayList<String> trades = new ArrayList<String>();
	public static void main(String arg[])
	{
		
	}
	public FetchAll(String TradesFile,String QuotesFile ,String Output)
	{
		FIXFetcher.TradesFile = TradesFile;
		FIXFetcher.QuotesFile =QuotesFile;
		FIXFetcher.Output = Output;
	}
	public void getRequest()
	{
		
		try
			{
				Files.lines(Paths.get(FIXFetcher.QuotesFile)).parallel()
						.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "35=R"))
						.forEach(request -> getAllResponses(request));
				
				for(String Quote : Quotes)
				{
					System.out.println(Quote);
				}
			}
			catch (IOException e) 
			{
				e.printStackTrace();	
			}
	}
	public boolean getAllResponses(String request)
	{

		try 
		{
			Quotes.add(request);
			System.out.println(request);
			Optional QuoteResponse=Files.lines(Paths.get(FIXFetcher.QuotesFile)).parallel()
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "35=S"))
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+"131="+getFIXValue(request,"131")))
					.findAny();
			 if(QuoteResponse.isPresent())
			 {
				 System.out.println(QuoteResponse.get());
				 System.out.println();
				 for (String a: QuoteResponse.get())
				 {
					 
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
/*	public boolean getAllOrders(String exec)
	{
		try 
		{

			 Files.lines(Paths.get(FIXFetcher.TradesFile)).parallel()
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "35=D"))
					.filter(msg -> msg.contains(FIX.FIXDELIMITER + "11="+getFIXValue(exec,"11")))
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "117="+getFIXValue(exec,"37")))
					.forEach(msg -> getExecutionReport());
			
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/*public ArrayList<String> getExecutionReport()
	{
		try 
		{
			 Files.lines(Paths.get(FIXFetcher.TradesFile)).parallel()
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "35=8"))
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+ "40=D"))
					.filter(msg -> msg.contains(FIX.FIXDELIMITER+"37="))
					.forEach(exec -> getAssociateTrades(exec));
				return transaction;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}*/

	public String getFIXValue(String msg, String key)
	{
		key = FIX.FIXDELIMITER + key ;
		String temp = msg.substring(msg.indexOf(key)+key.length());
		return temp.substring(1,temp.indexOf(FIX.FIXDELIMITER));
	}

	public String printData(String msg)
	{
		System.out.println(msg);
		return msg;
	}
}
