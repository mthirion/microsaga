package org.jboss.integration.fis2demo.gateway;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

public class Processor {
	
	private static JMSSender sender = null;
	
	public void openAccount(JSONObject data, MySpringBootConfig config) {
		
		JSONObject person = data.getJSONObject("Owner");
		JSONObject account = data.getJSONObject("Account");
		JSONObject card = data.getJSONObject("Card");
		
		/* 
		 * COMPUTE TRANSACTION ID
		 */  
		String transactionId = Util.getTransactionID();
		System.out.println("===== GATEWAY ===== generating transation tx = "+transactionId);

		data.put("transactionID", transactionId);
		data.put("eventName", "OPEN_ACCOUNT");	// matches the ReST URI
		
    	if (sender == null) 
    		sender = new JMSSender(config.getActivemqurl(), config.getActivemquser(), config.getActivemqpassword());
    	sender.send(config.getOpen_account(),data, "OPEN_ACCOUNT");
    }
	
}
