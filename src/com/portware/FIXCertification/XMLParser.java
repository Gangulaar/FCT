package com.portware.FIXCertification;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLParser 
{
	static Transaction t[];
	static String TradesFile ;
	static String QuotesFile ;
	static String Output;
	
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
						t[count++] = setToObjects(eElement);
					}
				}
		    } 
		    catch (Exception e) 
		    {
		    	e.printStackTrace();
		    }
	  }
	  public static Transaction setToObjects(Element e)
	  {
		Transaction object = new Transaction();
		object.setExecution(e.getElementsByTagName("Execution").item(0).getTextContent());
		object.setQuote(e.getElementsByTagName("QuoteResponse").item(0).getTextContent());
		object.setRequest(e.getElementsByTagName("QuoteRequest").item(0).getTextContent());
		object.setTrade(e.getElementsByTagName("Order").item(0).getTextContent());
		//object.setInboundOrder(addInBoundOrder(e.getElementsByTagName("Order").item(0).getTextContent(),mandatoryParameters));
		object.displayAll(); 
		return object;
	  }
}
