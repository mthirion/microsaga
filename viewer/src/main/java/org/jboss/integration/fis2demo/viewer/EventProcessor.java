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

import net.sf.json.JSONObject;

public class EventProcessor implements Processor {

	private Connection conn;

	public EventProcessor() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fis2demo?user=root&password=mysql");
			conn.setAutoCommit(true);
			// conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
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
		Integer eventId = (Integer) m.getHeader("eventID");
		String eventName = (String) m.getHeader("eventName");
		System.out.println("PROCESSING " + eventName);

		String data = m.getBody(String.class);
		JSONObject json = JSONObject.fromString(data);

		PreparedStatement update;

		try {
			if (eventName.equals("NEW_USER_CREATED")) {

				JSONObject owner = json.getJSONObject("Owner");

				update = conn.prepareStatement("insert into MView values (?, ?, ?, NULL, NULL) ON DUPLICATE KEY UPDATE clientNumber=?;");
				update.setString(2, owner.getString("Nom"));
				update.setString(3, owner.getString("Prenom"));
				update.setString(1, owner.getString("NationalNumber"));
				update.setString(4, owner.getString("NationalNumber"));

				update.executeUpdate();
				// System.out.println("===== VIEWER ===== DOING :::::NEW_USER_CREATED");
			}

			if (eventName.equals("NEW_ACCOUNT_CREATED")) {
				update = conn.prepareStatement("update MView set accountNumber=? where clientNumber=?");
				update.setString(1, json.getString("accountID"));
				update.setString(2, json.getString("clientID"));

				update.executeUpdate();
				// System.out.println("===== VIEWER ===== DOING :::::NEW_ACCOUNT_CREATED");
			}

			if (eventName.equals("NEW_CARD_CREATED")) {
				update = conn.prepareStatement("update MView set cardNumber=? where clientNumber=?");
				update.setString(1, json.getString("cardID"));
				update.setString(2, json.getString("clientID"));

				update.executeUpdate();
				// System.out.println("===== VIEWER ===== DOING :::::NEW_CARD_CREATED");
			}

			if (eventName.equals("CARD_COMPENSATED")) {
				update = conn.prepareStatement("update MView set cardNumber=NULL where clientNumber=?");
				update.setString(1, json.getString("clientID"));

				update.executeUpdate();
				// System.out.println("===== VIEWER ===== DOING :::::CARD_COMPENSATED");
			}

			if (eventName.equals("ACCOUNT_COMPENSATED")) {
				update = conn.prepareStatement("update MView set accountNumber=NULL where clientNumber=?");
				update.setString(1, json.getString("clientID"));

				update.executeUpdate();
				// System.out.println("===== VIEWER ===== DOING :::::ACCOUNT_COMPENSATED");
			}

			if (eventName.equals("USER_COMPENSATED")) {
				update = conn.prepareStatement("delete from MView where clientNumber=?");
				update.setString(1, json.getString("clientID"));

				update.executeUpdate();
				// System.out.println("===== VIEWER ===== DOING :::::USER_COMPENSATED");
			}

			PreparedStatement deleteEvent = conn
					.prepareStatement("delete from EventsTable where ownership='100' and EventID=?");
			deleteEvent.setInt(1, eventId);
			deleteEvent.executeUpdate();

		} catch (Exception e) {

		}
	}

}
