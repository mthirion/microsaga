package org.jboss.integration.fis2demo.account;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import net.sf.json.JSONObject;

public class DBHolder implements Processor {

	private AccountJDBC jdbc;
	
	
	public DBHolder() {
		jdbc=new AccountJDBC();
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		
		Message m = exchange.getIn();
		String body = m.getBody(String.class);
		
		System.out.println("DEBUG: body to be handled by MySQL JDBC driver:");
		System.out.println(body);
		
		JSONObject obj = JSONObject.fromString(body);
		String transactionId = obj.getString("transactionID");
		String clientId = obj.getString("clientID");
		
		JSONObject account = obj.getJSONObject("Account");
		String accountType = account.getString("Type");
		
		System.out.println("DEBUG: account type = " + accountType + ".  --Generating new account number");
		
		/* ADD ACCOUNT NUMBER TO JSON OBJECT */
		String accountNumber = Util.getBankAccountNumberBE();
//		String accountNumnber = "BE90-0634-2187-2132";
		obj.put("accountID", accountNumber);
		account.put("accountNumber", accountNumber);
		
		/* UPDATE THE ACCOUNT OBJECT */
		obj.remove("Account");obj.put("Account", account);
		
		/* UPDATE THE EVENT */
		obj.remove("eventName");obj.put("eventName", "NEW_ACCOUNT_CREATED");
		
		m.setBody(obj.toString());

		
		try {
			jdbc.accumulateInsertAccount(transactionId, clientId, accountNumber, accountType);
			Object flag = m.getHeader("useEventTable");
			if (flag != null && flag.toString()=="true") {	// Switch between activemq and event-table
				/* GENERATE AN EVENT ID */
				String eventID = Util.getEventID();
				jdbc.accumulateInsertClob(eventID, obj.toString());
			}
		} catch (Exception e) {
			throw e;
		}
		jdbc.CommitAll();
		
		// Update message body
		exchange.setIn(m);
	}


}
