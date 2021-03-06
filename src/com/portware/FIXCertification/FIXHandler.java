package com.portware.FIXCertification;

import java.util.Hashtable;

public class FIXHandler 
{
	Hashtable<String, String> FIXmsg;
	private Boolean findFixTagPresence(String fixTag)
	{
		for(String temp : FIXmsg.keySet())
		{
			if(temp.equals(fixTag))
				return true;
		}
		return false;
	}
	public void splitMesage(String msg)
	{
		FIXmsg = new Hashtable<String, String>();
		String transaction[] = msg.split(("\\"+FIX.PARSED_DELIMITER));
		for (String temp : transaction)
		{
			String TagValue[] = temp.split("=");
			FIXmsg.put(TagValue[0], TagValue[1]);
		}
	}
	public void displayAllValues()
	{
		for(String temp : FIXmsg.keySet())
		{
			String value = FIXmsg.get(temp);
				System.out.println(temp+" = "+value);
		}
	}
	public String getValueForKey(String key)
	{
		if(!findFixTagPresence(key))
		{
			System.out.println(key+" isn't found in the message");
			return null;
		}
		return FIXmsg.get(key);
	}
	public String cleanMessage(String msg)
	{
		String IntialTag="[8=FIX";
		int index = msg.indexOf(IntialTag);
		msg= msg.substring(index+1,msg.length()-2);
		return msg;
	}
	public String quickSearch(String transaction,String key)
	{
		key = FIX.FIXDELIMITER+key;
		transaction =transaction.substring(transaction.indexOf(key)+key.length()+1);
		return transaction.substring(0, transaction.indexOf(FIX.FIXDELIMITER));
	}
}
