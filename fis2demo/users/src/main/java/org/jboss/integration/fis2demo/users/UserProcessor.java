package org.jboss.integration.fis2demo.users;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

@Component("processor")
public class UserProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		//exchange.setProperty("replyTo", exchange.getIn().getHeader("JMSReplyTo"));
		
		Message in = exchange.getIn();
		String msg = in.getBody().toString();
		
		JSONObject object = JSONObject.fromString(msg);


		/* GET TRANSACTION ID */
		String transactionID = object.getString("transactionID");
		in.setHeader("transactionID", transactionID);
		
		/* CLIENT_ID IS THE NATIONAL NUMBER */
		JSONObject person = object.getJSONObject("Owner");
		String clientNumber = person.getString("NationalNumber");
//		string.replaceAll("\\.", "").replaceAll("-", "");

		
		/* UPDATE THE CLIENT OBJECT AND FIX THE MONGO COLLECTION ID */
		person.put("_id", transactionID);
		person.put("clientNumber", clientNumber);
		object.put("clientID", clientNumber);
		
		/* UPDATE THE OBJECT */
		object.remove("Owner");object.put("Owner", person);
		
		
		/* UPDATE THE EVENT */
		object.remove("eventName");object.put("eventName", "NEW_USER_CREATED");
		in.setHeader("eventName", "NEW_USER_CREATED");
		
		/* SAVE THE ENTIRE (NEW) MESSAGE FOR REPLY_TO*/
		in.setHeader("saved_body", object.toString()); // for replying to

		
		/* UPDATE THE MESSAGE BODY WITH ONLY THE USER DETAILS
		 * TO BE INSERTED INTO MONGO DB */
		in.setBody(person.toString());
		exchange.setIn(in);

	}

}
