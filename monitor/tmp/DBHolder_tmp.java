package org.jboss.integration.fis2demo.monitor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import net.sf.json.JSONObject;

public class DBHolder_tmp implements Processor {

	private AccountJDBC_tmp jdbc;
	
	
	public DBHolder_tmp() {
		jdbc=new AccountJDBC_tmp();
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		
		Message m = exchange.getIn();
		String transactionId = (String)m.getHeader("transactionID");
		String clientId = (String)m.getHeader("clientID");
		String accountNumber = (String)m.getHeader("accountNumber");
		String accountType = (String)m.getHeader("accountType");
		
		
		try {
			System.out.println("===== ACCOUNTS ===== storing new Account in database");
			jdbc.insertAccount(transactionId, clientId, accountNumber, accountType);
			Object flag = m.getHeader("useEventTable");
			if (flag != null && flag.toString()=="true") {	// Switch between activemq and event-table
				System.out.println("===== ACCOUNTS ===== generating Event in Event Table");
			}
		} catch (Exception e) {
			throw e;
		}
		jdbc.CommitAll();
		
		// Update message body
		exchange.setIn(m);
	}


}
