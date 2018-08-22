package com.portware.FIXCertification;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XMLParser 
{
	static Transaction t[];
	static List<String> FIXTags = new ArrayList<String>();
	static String TradesFile ;
	static String QuotesFile ;
	static String Output = "C:\\Users\\agangula\\Documents\\GitHub\\FCT-Automation\\FIXCerticficationAnalyzer\\cache.xml";
	
	public static void main(String a[])
	{
		XMLParser xmlp =new XMLParser(TradesFile, QuotesFile, Output);
		xmlp.initalized();
	}
	
	
	public XMLParser(String TradesFile,String QuotesFile ,String Output)
	{
		XMLParser.TradesFile = TradesFile;
		XMLParser.QuotesFile =QuotesFile;
		XMLParser.Output = Output;
	}
	
	  public void initalized()
	  {
		  try 
		    {
		    	Collections.addAll(FIXTags, "35","49","56","55","21","15","54","40","38");
				File fXmlFile = new File(XMLParser.Output);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();	
				NodeList nList = doc.getElementsByTagName("Transaction");
				int numberOfObjects = nList.getLength();
				t  =new Transaction[numberOfObjects];

				int count =0;
				for (int temp = 0; temp < nList.getLength(); temp++) 
				{
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) 
					{
						Element eElement = (Element) nNode;
						
						t[count++] = setToObjects(eElement,FIXTags,count);
					}
				}
		    } 
		    catch (Exception e) 
		    {
		    	e.printStackTrace();
		    }
	  }
	public static Transaction setToObjects(Element e,List<String> mandatoryParameters,int count)
	  {
		Transaction object = new Transaction();
		object.setID(count);
		object.setExecution(e.getElementsByTagName("Execution").item(0).getTextContent());
		object.setQuote(e.getElementsByTagName("QuoteResponse").item(0).getTextContent());
		object.setRequest(e.getElementsByTagName("QuoteRequest").item(0).getTextContent());
		object.setTrade(e.getElementsByTagName("Order").item(0).getTextContent());
		object.setInboundOrder(addInBoundOrder(e.getElementsByTagName("Order").item(0).getTextContent(),mandatoryParameters));
		object.displayAll(); 
		return object;
	  }
	
	private static String addInBoundOrder(String Order,List<String> mandatoryParameters) 
	{
		//This function will build an Inbound Order from the trade order
		FIXHandler fix = new FIXHandler();
		Order = fix.cleanMessage(Order);
		fix.splitMesage(Order);
		String InboundOrder = new String();
		String value = new String();
		for(String para : mandatoryParameters)
		{
			value = fix.getValueForKey(para);
			InboundOrder += para+"="+value+"|";
		}
		return InboundOrder;
	}
}

class Transaction
{
	private String QuoteRequest;
    private String QuoteResponse;
    private String Execution;
    private String Order;
    private String InboundOrder;
    private int ID;
    public int getID() 
    {
        return ID;
    }
    public void setID(int ID) 
    {
        this.ID = ID;
    }
    public String getRequest() 
    {
        return QuoteRequest;
    }
    public void setRequest(String QuoteRequest) 
    {
        this.QuoteRequest = QuoteRequest;
    }
    public String getQuote() 
    {
        return QuoteResponse;
    }
    public void setQuote(String QuoteResponse) 
    {
        this.QuoteResponse = QuoteResponse;
    }
    public String getExecution() 
    {
        return Execution;
    }
    public void setExecution(String Execution) 
    {
        this.Execution = Execution;
    }
    public String getTrade() 
    {
        return Order;
    }
    public void setTrade(String Order) 
    {
        this.Order = Order;
    }
    public String getInboundOrder() 
    {
        return InboundOrder;
    }
    public void setInboundOrder(String InboundOrder) 
    {
        this.InboundOrder = InboundOrder;
    }
    public void displayAll()
    {
    	System.out.println("Number:" +this.ID);
    	System.out.println("InboundOrder : " + this.InboundOrder);
    	System.out.println();
    	System.out.println("QuoteRequest : " + this.QuoteRequest);
		System.out.println("QuoteResponse : " + this.QuoteResponse);
		System.out.println("Order : " + this.Order);
		System.out.println("Execution : " + this.Execution);
		System.out.println("=============================================================================");
    }
}