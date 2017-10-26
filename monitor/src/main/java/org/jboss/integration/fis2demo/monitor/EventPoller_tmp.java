package org.jboss.integration.fis2demo.monitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class EventPoller_tmp implements Processor {

	private Connection conn;
	
	public EventPoller_tmp () {
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
	public void process(Exchange arg0) throws Exception {
		// TODO Auto-generated method stub

		Statement s = conn.createStatement();
		s.executeQuery("select last index from EventTable");
		
		// check key value and get primary key
		
		s.executeUpdate("update EventTable set key='1' where primaryKey");
		// catch update error due to locking
		// re-check column value for keys if lock update has been released
		
		// propagate event to queue
		
		s.executeUpdate("delete from EventTable where key='1' ");
		
		conn.commit();
	
	}

}
