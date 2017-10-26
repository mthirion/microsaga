package org.jboss.integration.fis2demo.monitor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import net.sf.json.JSONObject;

public class EventParser implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		
		Message m = exchange.getIn();
		String body = m.getBody(String.class);
		
		/* PARSE MESSAGE */
		JSONObject json = JSONObject.fromString(body);
		String transactionId = json.getString("transactionID");
		String eventName = json.getString("eventName");
		
		/* GENERATE EVENT ID */
		//String eventID = Util.getEventID();
		
		/* GENERATE PARAM FOR EVENT TABLE */
		m.setHeader("transactionID", transactionId);
		m.setHeader("eventName", eventName);
		m.setHeader("eventData", json.toString());
		//m.setHeader("eventID", eventID);
				
		/* GENERATE LOG LINE */
		String log = transactionId + "    |    " + eventName;
		m.setBody(log);
		exchange.setIn(m);
	}

}
