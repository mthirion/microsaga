package org.jboss.integration.fis2demo.account;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import net.sf.json.JSONObject;

public class MsgFormatter {


	public void insert(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		
		Message m = exchange.getIn();
		String body = m.getBody(String.class);
		
		//System.out.println("DEBUG: body to be handled by MySQL JDBC driver:");
		//System.out.println(body);
		
		JSONObject obj = JSONObject.fromString(body);
		String transactionId = obj.getString("transactionID");
		m.setHeader("transactionID", transactionId);
		
		String clientId = obj.getString("clientID");
		m.setHeader("clientID", clientId);
		
		JSONObject account = obj.getJSONObject("Account");
		String accountType = account.getString("Type");
		m.setHeader("accountType", accountType);
		
		
		/* ADD ACCOUNT NUMBER TO JSON OBJECT */
		String accountNumber = Util.getBankAccountNumberBE();
//		String accountNumnber = "BE90-0634-2187-2132";
		obj.put("accountID", accountNumber);
		account.put("accountNumber", accountNumber);
		m.setHeader("accountNumber", accountNumber);
		
		
		/* UPDATE THE ACCOUNT OBJECT */
		account.put("_id", transactionId);
		obj.remove("Account");obj.put("Account", account);
		
		/* UPDATE THE EVENT */
		obj.remove("eventName");obj.put("eventName", "NEW_ACCOUNT_CREATED");
		m.setHeader("eventName", "NEW_ACCOUNT_CREATED");
		
		/* SAVE BODY AND UPDATE THE MESSAGE */
		m.setHeader("saved_body", obj.toString());
		m.setBody(account.toString());
		exchange.setIn(m);
	}
	
	public void delete(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		
		Message m = exchange.getIn();
		String body = m.getBody(String.class);
		
		
		JSONObject obj = JSONObject.fromString(body);
		String transactionId = obj.getString("transactionID");
		m.setHeader("transactionID", transactionId);
	
		/* UPDATE THE EVENT */
		obj.remove("eventName");obj.put("eventName", "ACCOUNT_COMPENSATED");
		m.setHeader("eventName", "ACCOUNT_COMPENSATED");
		
		/* SAVE BODY AND UPDATE THE MESSAGE */
		m.setBody(obj.toString());
		exchange.setIn(m);
	}	

}
