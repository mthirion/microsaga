package org.jboss.integration.fis2demo.users;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

@Component("compensator")
public class CompensationProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		//exchange.setProperty("replyTo", exchange.getIn().getHeader("JMSReplyTo"));
		
		Message in = exchange.getIn();
		String msg = in.getBody().toString();
		
		JSONObject object = JSONObject.fromString(msg);
		
		
		/* GET THE CLIENT OBJECT */
		JSONObject person = object.getJSONObject("Owner");
		String id = person.getString("_id");
		
		if (id==null || id.isEmpty()) {
			
			/* GET TRANSACTION ID AND UPDATE CLIENT*/
			String transactionID = object.getString("transactionID");
			person.put("_id", transactionID);
		}	
		
		/* UPDATE EVENT */
		object.remove("eventName");object.put("eventName", "USER_COMPENSATED");
		
		/* SAVE OBJECT */
		in.setHeader("saved_body", object.toString());
		
		/* UPDATE THE MESSAGE BODY WITH ONLY THE USER DETAILS
		 * TO BE DELETED FROM MONGO DB */
		in.setBody(person.toString());
		exchange.setIn(in);
	}

}
