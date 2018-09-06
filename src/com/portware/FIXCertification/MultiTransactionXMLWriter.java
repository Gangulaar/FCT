package com.portware.FIXCertification;

import java.io.File;
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

public class MultiTransactionXMLWriter {

	Document doc;
	Element rootElement;

	public Element createRootElement() {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.newDocument();
			rootElement = doc.createElement("Transactions");
			doc.appendChild(rootElement);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootElement;
	}

	public void writeToFile(String Output) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult file = new StreamResult(new File(Output));
			transformer.transform(source, file);
			System.out.println("DONE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Node getTransactionElements(String tag, String value) {
		Element node = doc.createElement(tag);
		node.appendChild(doc.createTextNode(value));
		return node;
	}

	public Element startTransaction(int ID) {
		Element transaction = doc.createElement("Transaction");
		transaction.setAttribute("id", Integer.toString(ID));
		return transaction;
	}

	public void appendChildTrans(Element transaction, String TrasType, String Value) {
		transaction.appendChild(getTransactionElements(TrasType, Value));
	}
}
