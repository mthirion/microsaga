package org.jboss.integration.fis2demo.cards;

import java.util.*;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import net.sf.json.JSONObject;

public class DBPrepareDelete implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		Message m = exchange.getIn();
		Object body = m.getBody();
		if ( !(body == null)) {

			JSONObject object = JSONObject.fromString(m.getBody(String.class));
			String transactionId = object.getString("transactionID");

			m.setHeader("transactionID", transactionId);
			
			/* UPDATE THE EVENT */
			object.remove("eventName");object.put("eventName", "CARD_COMPENSATED");
		}
		//exchange.setIn(m);
	}


}
