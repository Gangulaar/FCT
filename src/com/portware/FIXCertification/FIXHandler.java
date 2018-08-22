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
			{
				//System.out.println("true");
				return true;
			}
			
		}
		return false;
	}

	public void splitMesage(String msg)
	{
		/*message should be in the format of tag=value|tag=value with not extra symbols
		 * Will save the data to FIXmsg - Hashtable 
		 */
		
		FIXmsg = new Hashtable<String, String>();
		//System.out.println(msg);
		//System.out.println(FIX.PARSED_DELIMITER);
		String transaction[] = msg.split(("\\"+FIX.PARSED_DELIMITER));
		for (String temp : transaction)
		{
			//System.out.println(temp);
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
		//System.out.println(FIXmsg.get(key));
		return FIXmsg.get(key);
	}
	/*public static void main(String args[])
	{
		String msg="2017/09/19 14:14:20:147: FIXConnectionData: Sending data on connection {BOAFXTRADES} [8=FIX.4.3|9=0204|35=D|49=Portware2Trades|56=BOA-FIX|34=148442|52=20170919-18:14:20|55=EUR/USD|21=1|60=20170919-18:14:20|15=EUR|117=070JSDOAE2150F1NMDlcm4Fbt62Qfm4Fbt6klim4Fbt60|11=9201471|44=1.19989|54=1|40=D|38=10000000|10=200|]";
		FIXHandler f = new FIXHandler();
		msg = f.cleanMessage(msg);
		f.splitMesage(msg);
		Boolean presence = f.findFixTagPresence("117");
		String Value = f.getValueForKey("117");
	} */
	public String cleanMessage(String msg)
	{
		//System.out.println("Called Clean message");
		String IntialTag="[8=FIX";
		int index = msg.indexOf(IntialTag);
		msg= msg.substring(index+1,msg.length()-2);
		//System.out.println(msg);

		return msg;
	}
	
}
