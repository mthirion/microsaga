package org.jboss.integration.fis2demo.account;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import net.sf.json.JSONObject;

public class Compensate implements Processor {

	private AccountJDBC jdbc;
	
	
	public Compensate() {
		jdbc=new AccountJDBC();
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {

		
		Message m = exchange.getIn();
		String body = m.getBody(String.class);

		JSONObject obj = JSONObject.fromString(body);
		String transactionId = obj.getString("transactionID");
		
		try {
			System.out.println("===== ACCOUNTS ===== deleting Account from database  [tx = "+transactionId+"]");
			jdbc.deleteAccount(transactionId);
			jdbc.CommitAll();
		} catch (Exception e) {
			throw e;
		}
	
		// SET NEW EVENT 
		obj.remove("eventName");obj.put("eventName", "ACCOUNT_COMPENSATED");
		
		// SET BODY
		m.setBody(obj.toString());
		exchange.setIn(m);
	}

}
