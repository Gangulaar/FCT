package com.portware.FIXCertification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class Transaction {
	/*
	 * private String Execution; private String Order;
	 */
	private String QuoteRequest;
	private String InboundOrder;
	private String Unregistration;
	ArrayList<QuoteResponse> arr_res;
	// ArrayList<QuoteRequest> arr_req;
	ArrayList<Order> arr_order;
	ArrayList<Execution> arr_exec;
	ArrayList<UnCategorized> arr_uncat;

	public Transaction() {
		arr_res = new ArrayList<>();
		// arr_req = new ArrayList<>();
		arr_order = new ArrayList<>();
		arr_exec = new ArrayList<>();
		arr_uncat = new ArrayList<>();
	}

	public String getInboundOrder() {
		return InboundOrder;
	}

	public void setInboundOrder(String InboundOrder) {
		this.InboundOrder = InboundOrder;
	}

	public String getRequest() {
		return QuoteRequest;
	}

	public void setRequest(String QuoteReq) {
		this.QuoteRequest = QuoteReq;
	}

	public ArrayList<QuoteResponse> getResponse() {
		return arr_res;
	}

	public void setResponse(String QuoteRes) {
		QuoteResponse res = new QuoteResponse();
		res.set(QuoteRes);
		arr_res.add(res);
	}

	public ArrayList<Order> getOrder() {
		return arr_order;
	}

	public void setOrder(String Order) {
		Order exec = new Order();
		exec.set(Order);
		arr_order.add(exec);
	}

	public ArrayList<Execution> getExecution() {
		return arr_exec;
	}

	public void setExecution(String Exec) {
		Execution exec = new Execution();
		exec.set(Exec);
		arr_exec.add(exec);
	}

	public void setUnRegistration(String Unregistration) {
		this.Unregistration = Unregistration;
	}

	public String getUnRegistration() {
		return Unregistration;
	}

	public void setUnCategorized(String UnCategorized) {
		UnCategorized uncat = new UnCategorized();
		uncat.set(UnCategorized);
		arr_uncat.add(uncat);
	}

	public ArrayList<UnCategorized> getUnCategorized() {
		return arr_uncat;
	}

	public void displayAll(String FileName) {
		printToTempFile(FileName, "InboundOrder", this.InboundOrder);
		printToTempFile(FileName, "QuoteRequest", this.QuoteRequest);
		printToTempFile(FileName, "QuoteResponse");
		for (QuoteResponse qr : arr_res)
			printToTempFile(FileName, qr.get());

		printToTempFile(FileName, "Order");
		for (Order order : arr_order)
			printToTempFile(FileName, order.get());

		printToTempFile(FileName, "Execution");
		for (Execution exec : arr_exec)
			printToTempFile(FileName, exec.get());

		printToTempFile(FileName, "UnRegistration", this.Unregistration);

		printToTempFile(FileName, "UnCategorized");
		for (UnCategorized uncat : arr_uncat)
			printToTempFile(FileName, uncat.get());

		printToTempFile(FileName, "\"=============================================================================\"");
	}

	public void printToTempFile(String FileName, String MesgType, String Value) {
		try {
			Files.write(Paths.get(FileName), (MesgType + ": " + Value + "\n").getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printToTempFile(String FileName, String KeyValue) {
		try {
			Files.write(Paths.get(FileName), (KeyValue + "\n").getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class QuoteRequest {
	String Request;

	public void set(String Request) {
		this.Request = Request;
	}

	public String get() {
		return Request;
	}
}

class QuoteResponse {
	String Response;

	public void set(String Response) {
		this.Response = Response;
	}

	public String get() {
		return Response;
	}
}

class Order {
	String Order;

	public void set(String Order) {
		this.Order = Order;
	}

	public String get() {
		return Order;
	}
}

class Execution {
	String Exec;

	public void set(String Exec) {
		this.Exec = Exec;
	}

	public String get() {
		return Exec;
	}
}

class UnRegistration {
	String UnReg;

	public void set(String UnReg) {
		this.UnReg = UnReg;
	}

	public String get() {
		return UnReg;
	}
}

class UnCategorized {
	String Uncategorized;

	public void set(String Uncategorized) {
		this.Uncategorized = Uncategorized;
	}

	public String get() {
		return Uncategorized;
	}
}