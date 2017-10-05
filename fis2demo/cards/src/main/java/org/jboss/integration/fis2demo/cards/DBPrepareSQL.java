package org.jboss.integration.fis2demo.cards;

import java.util.*;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import net.sf.json.JSONObject;

public class DBPrepareSQL implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		Message m = exchange.getIn();
		Object body = m.getBody();
		if ( !(body == null)) {

			/* GET HEADER INFO
			 * 	TransactionID, clientID, accountID
			 */
			JSONObject object = JSONObject.fromString(m.getBody(String.class));
			String transactionId = object.getString("transactionID");
			String clientId = object.getString("clientID");
			String accountId = object.getString("accountID");
//			String accountNumber="BE90-0634-2187-2132";
			System.out.println("retrieved clientID = " + clientId);
			System.out.println("retrieved accountNumber = " + accountId);
			
			/* GET CARD INFO */			
			JSONObject card = object.getJSONObject("Card");
			String type = card.getString("Type");
			System.out.println("retrieved card type = " + type);

			/* UPDATE CARD INFO */	
			String cardId = Util.getBankCardNumberBE();
//			String cardId="000-111";
			card.put("cardNumber", cardId);
			
			/* UPDATE OBJECT */
			object.put("cardID", cardId);
			object.remove("Card");object.put("Card", card);
			
			/* SAVE THE BODY */
			m.setHeader("saved_bBody", object.toString());
			
			
			/*
			 * FIRST SOLUTION
			 * 	DOES NOT WORK WITH TRANSACTIONS
			 * 
			 *  set the body to SQL query to insert into DB using Camel JDBC
			 */
//			String statement = "insert into Cards (clientid, accountid, type) values ";
//			statement += "(\""  +clientid + "\",\"" + accountNumber + "\",\"" + type +"\")";
//			statement += " ON DUPLICATE KEY UPDATE accountid=\"" + accountNumber + "\", type=\"" + type + "\";";
//			System.out.println("statement is " + statement);
//			m.setBody(statement); 

			
			/*
			 * SECOND SOLUTION
			 * 	WORK WITH TRANSACTIONS
			 * 
			 *  set the properties in the header to be used by Camel SQL
			 */
			m.setHeader("transactionID", transactionId);
			m.setHeader("clientID", clientId);
			m.setHeader("accountID", accountId);
			m.setHeader("cardType", type);
			m.setHeader("cardID", cardId);
			
			
			/* UPDATE THE EVENT */
			object.remove("eventName");object.put("eventName", "NEW_CARD_CREATED");
			m.setHeader("saved_body", object.toString());
			
			/* UPDATE BODY */
			m.setBody(object.toString());
		}
		
		exchange.setIn(m);
	}


}
