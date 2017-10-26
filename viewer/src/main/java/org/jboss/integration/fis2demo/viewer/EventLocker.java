package org.jboss.integration.fis2demo.viewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class EventLocker implements Processor {

	private Connection conn;

	public EventLocker() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fis2demo?user=root&password=mysql");
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		Message m = exchange.getIn();
		
		//Statement query = conn.createStatement();
		//query.executeQuery("SELECT * FROM AccountEvent ORDER BY EventID DESC LIMIT 1 FOR UPDATE");

		Map<String, Object> row = (Map<String, Object>) m.getBody();
		Iterator<Entry<String, Object>> it = row.entrySet().iterator();

		// Get the first Map entry = first column = EventID
		Entry<String, Object> entry = it.next();
		Integer eventId = (Integer) entry.getValue();
		m.setHeader("eventID", eventId);

		// Get the second entry = second column = EventName
		entry = it.next();
		String eventName = (String) entry.getValue();
		m.setHeader("eventName", eventName);

		// Get the third entry = third column = Data (CLOB --TEXT)
		entry = it.next();
		String data = (String) entry.getValue();

		// Get the fourth entry = fourth column = ownership --LOCK
		entry = it.next();
		String lock = (String) entry.getValue();

		if (lock == null) {
			PreparedStatement update = conn
					.prepareStatement("update EventsTable set ownership = '100' where EventID =?");
			update.setInt(1, eventId);
			try {
				update.executeUpdate();
				//PreparedStatement delete = conn.prepareStatement("delete from EventsTable where EventID = ?");
				//delete.setString(1, eventId);

				conn.commit();
			} catch (Exception e) {
				exchange.setProperty("LOCK", "true");
				return;
			}
			m.setBody(data);

		}
		else
			exchange.setProperty("LOCK", "true");
		
		exchange.setIn(m);
	}

}
