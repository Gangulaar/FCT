package com.portware.FIXCertification;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConstructInbound {
	static List<String> FIXTags;
	List<String> externalTags;
	ArrayList<String> TransactionSet;
	private String ExecTransaction;
	private String QuoteResponse;
	private String OrderTransaction;
	private String QuoteRequest;
	private String InboundOrder;
	FIXHandler order, trans;
	private String OutputFileName = "Output.xml";

	public ConstructInbound() {
		try {
			order = new FIXHandler();
			trans = new FIXHandler();
			FIXTags = new ArrayList<String>();
			externalTags = new ArrayList<String>();
			TransactionSet = new ArrayList<String>();
			Collections.addAll(FIXTags, "35", "55", "21", "15", "54", "40", "38");
			Collections.addAll(externalTags, "11=$UNIQUE", "60=$TIMESTAMP");
			File f = new File(XMLParser.Output);
			OutputFileName = f.getParent() + "\\" + OutputFileName;
			System.out.println(OutputFileName);
			FileHandler fh = new FileHandler();
			fh.createFile(OutputFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buildInbound() {

		XMLWriterDOM smlwd = new XMLWriterDOM();
		smlwd.createRootElement();
		for (Transaction t : XMLParser.t) {
			ExecTransaction = t.getExecution();
			//QuoteResponse = t.getQuote();
			OrderTransaction = t.getOrder();
			QuoteRequest = t.getRequest();
			ExecTransaction = modifyTransactions(ExecTransaction);
			QuoteResponse = modifyTransactions(QuoteResponse);
			OrderTransaction = modifyTransactions(OrderTransaction);
			QuoteRequest = modifyTransactions(QuoteRequest);
			if (typeOfOrder(QuoteRequest, OrderTransaction)) {
				// Inbound order
				InboundOrder = addInBoundOrder(OrderTransaction, FIXTags) + setExternalTags();
				t.setInboundOrder(InboundOrder);
				// System.out.println(QuoteRequest +"\n" +QuoteResponse+"\n"
				// +OrderTransaction+"\n" +ExecTransaction+"\n" +InboundOrder);
				Collections.addAll(TransactionSet, QuoteRequest, QuoteResponse, OrderTransaction, ExecTransaction,
						InboundOrder);
				smlwd.addAllTrans(TransactionSet);
				TransactionSet.clear();
			} else {
				System.out.println("Discretionary");
				// skip this
				// will be impl later
			}
		}
		smlwd.writeToFile(OutputFileName);
	}

	private String modifyTransactions(String Transaction) {
		Transaction = trans.cleanMessage(Transaction);
		return Transaction;
	}

	private String setExternalTags() {
		String ExternalKeyValue = new String();
		for (String KeyValue : externalTags) {
			ExternalKeyValue += KeyValue + FIX.PARSED_DELIMITER;
		}
		return ExternalKeyValue;
	}

	private String addInBoundOrder(String Order, List<String> mandatoryParameters) {
		// This function will build an Inbound Order from the trade order
		String InboundOrder = new String();
		String value = new String();
		for (String para : mandatoryParameters) {
			value = order.getValueForKey(para);
			InboundOrder += para + "=" + value + "|";
		}
		return InboundOrder;
	}

	public boolean typeOfOrder(String Request, String Order) {
		/*
		 * returns 0 if transaction type is Inbound returns 1 if transaction type is
		 * Discretionary
		 */
		FIXHandler req = new FIXHandler();
		Request = req.cleanMessage(Request);
		Order = order.cleanMessage(Order);
		req.splitMesage(Request);
		order.splitMesage(Order);
		// req.displayAllValues();
		// System.out.println("----------------------------------------------------------");
		// order.displayAllValues();
		// System.out.println("===========================================================");
		return req.getValueForKey("38").contains(order.getValueForKey("38"));
	}
}
