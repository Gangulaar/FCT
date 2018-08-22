
package com.portware.FIXCertification;


import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLWriterDOM 
{
	Document doc;
	Element rootElement;
	int id=0;
	public void writeToXML()
	{
		createRootElement();
	}
    public void createRootElement()
    {
    	 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;
         try 
         {
             dBuilder = dbFactory.newDocumentBuilder();
             doc = dBuilder.newDocument();
             rootElement = doc.createElement("Transactions");
             doc.appendChild(rootElement);
             
         } 
         catch (Exception e) 
         {
             e.printStackTrace();
         }
    }
    public void addChildNodes(ArrayList<String> listOfTransactions)
    {
    	for(int i =0 ,count =0;i < listOfTransactions.size();i=i+4,count++)
         	rootElement.appendChild(getTransaction(doc, count+1+"", listOfTransactions.get(i), listOfTransactions.get(i+1), listOfTransactions.get(i+2), listOfTransactions.get(i+3)));
    	
    }
    public void addAllTrans(ArrayList<String> listOfTransactions)
    {
    	/*for(String trans: listOfTransactions)
    	{
    		System.out.println(trans);
    	}*/
    	rootElement.appendChild(getTransaction(doc, (++id)+"", listOfTransactions.get(0), listOfTransactions.get(1), listOfTransactions.get(2), listOfTransactions.get(3), listOfTransactions.get(4)));
    	
    }
    public void writeToFile(String Output)
    {
    	try
    	{
    		TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult file = new StreamResult(new File(Output));
            transformer.transform(source, file);
            System.out.println("DONE");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    private static Node getTransaction(Document doc, String... allId)
    {
        Element transaction = doc.createElement("Transaction");

        transaction.setAttribute("id", allId[0]);

        transaction.appendChild(getTransactionElements(doc, transaction, "QuoteRequest", allId[1]));

        transaction.appendChild(getTransactionElements(doc, transaction, "QuoteResponse", allId[2]));

        transaction.appendChild(getTransactionElements(doc, transaction, "Order", allId[3]));

        transaction.appendChild(getTransactionElements(doc, transaction, "Execution", allId[4]));
        
        if(allId.length > 5)
        {
        	transaction.appendChild(getTransactionElements(doc, transaction, "Inboundorder", allId[5]));
        }
        	

        return transaction;
    }


    //utility method to create text node
    private static Node getTransactionElements(Document doc, Element element, String tag, String value) 
    {
        Element node = doc.createElement(tag);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

}
