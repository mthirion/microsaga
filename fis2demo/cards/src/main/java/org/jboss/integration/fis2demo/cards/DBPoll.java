package org.jboss.integration.fis2demo.cards;

import java.util.*;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class DBPoll implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		Message m = exchange.getIn();
		Object body = m.getBody();
		if ( !(body == null)) {

			// List<Map<String, Object>> result = (List<Map<String, Object>>) content;
			// Only one object so we just have one Map, no List
			// The Map consists of couples such as: [columnName] - [ColumnVlaue]
			// Here we will have 2 entries: eventId and data
			Map<String, Object> row = (Map<String, Object>) body;

			Iterator<Entry<String, Object>> it = row.entrySet().iterator();

			// Get the first Map entry = first column = eventID
			Entry<String, Object> entry = it.next();
			String eventID = (String) entry.getValue();
			System.out.println("DEBUG : eventID = " + eventID);
			m.setHeader("eventid", eventID);
			m.setHeader("transactionid", eventID);
			// evnetID or transactionID

			// Get the second entry = second column = CLOB
			entry = it.next();
			String data = (String) entry.getValue();
			System.out.println("DEBUG : CLOB = " + data);
			
			// set the JSON object in the body for the next processor
			m.setBody(data);

			exchange.setIn(m);

		}
	}

}
