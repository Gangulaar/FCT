package com.portware.FIXCertification;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;

public class SavingXML {
	FIXHandler F;
	HashMap<String, String> MsgTypes;
	MultiTransactionXMLWriter multiWriter;
	String Output;
	int trans_count;
	Element root;

	SavingXML(String Output) {
		F = new FIXHandler();
		trans_count = 0;
		multiWriter = new MultiTransactionXMLWriter();
		MsgTypes = new HashMap<>();
		MsgTypes.put("R", "Request");
		MsgTypes.put("S", "Response");
		MsgTypes.put("D", "Order");
		MsgTypes.put("8", "Execution");
		MsgTypes.put("Z", "UnRegistration");

		this.Output = Output;
	}

	public void writeTransactions(ArrayList<String> transactions) {
		try {
			Element transaction = null;
			root = multiWriter.createRootElement();
			for (String temp : transactions) {
				String msgType = F.quickSearch(temp, "35");
				msgType = MsgTypes.get(msgType);
				if (msgType == null) {
					msgType = "UnCategorized";
				}
				if (msgType.equals("Request")) {
					if (transaction != null) {
						root.appendChild(transaction);
						transaction = null;
					}
					transaction = multiWriter.startTransaction(++trans_count);
				}
				multiWriter.appendChildTrans(transaction, msgType, temp);
			}
			multiWriter.writeToFile(Output);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
