
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
    public void writeToXML(ArrayList<String> listOfTransactions,String Output)
    {
    	 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;
         try 
         {
             dBuilder = dbFactory.newDocumentBuilder();
             Document doc = dBuilder.newDocument();
             Element rootElement = doc.createElement("Transactions");
             doc.appendChild(rootElement);
             for(int i =0 ,count =0;i < listOfTransactions.size();i=i+4,count++)
             	rootElement.appendChild(getEmployee(doc, count+1+"", listOfTransactions.get(i), listOfTransactions.get(i+1), listOfTransactions.get(i+2), listOfTransactions.get(i+3)));
             TransformerFactory transformerFactory = TransformerFactory.newInstance();
             Transformer transformer = transformerFactory.newTransformer();
             transformer.setOutputProperty(OutputKeys.INDENT, "yes");
             DOMSource source = new DOMSource(doc);
             StreamResult file = new StreamResult(new File(Output));
             transformer.transform(source, file);
             System.out.println("DONE");
         } 
         catch (Exception e) 
         {
             e.printStackTrace();
         }
    }

    private static Node getEmployee(Document doc, String id, String QuoteRequest, String QuoteResponse, String Order, String Execution) 
    {
        Element transaction = doc.createElement("Transaction");

        transaction.setAttribute("id", id);

        transaction.appendChild(getEmployeeElements(doc, transaction, "QuoteRequest", QuoteRequest));

        transaction.appendChild(getEmployeeElements(doc, transaction, "QuoteResponse", QuoteResponse));

        transaction.appendChild(getEmployeeElements(doc, transaction, "Order", Order));

        transaction.appendChild(getEmployeeElements(doc, transaction, "Execution", Execution));

        return transaction;
    }


    //utility method to create text node
    private static Node getEmployeeElements(Document doc, Element element, String tag, String value) 
    {
        Element node = doc.createElement(tag);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

}
