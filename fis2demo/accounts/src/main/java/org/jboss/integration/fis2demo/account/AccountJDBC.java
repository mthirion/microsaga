package org.jboss.integration.fis2demo.account;

import java.beans.Statement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.*;
import javax.sql.*;

public class AccountJDBC {

	private Connection conn;

	public AccountJDBC() {

		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("jboss/datasources/AccountDS");
			if (ds == null)
				System.out.println("DEBUGME - Datasource is null");
			conn = ds.getConnection();
			if (conn == null)
				System.out.println("DEBUGME - Conn is null");
			conn.setAutoCommit(false);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		return conn;
	}

	public void accumulateInsertAccount(String transactionId, String clientId, String accountNumber, String accountType) throws SQLException {

		System.out.println("DEBUG - updating accounts in MySQL");

		PreparedStatement s = conn
				.prepareStatement("insert into Accounts (ClientID, AccountType, AccountNumber, TransactionID) values (?, ?, ?, ?) ON DUPLICATE KEY UPDATE ClientID=?,AccountType=?,TransactionID=?");
		if (s == null) {
			System.out.println("DEBUGME - statement is null");
			System.out.flush();
		}
		s.setString(1, clientId);		
		s.setString(2, accountType);
		s.setString(3, accountNumber);
		s.setString(4, transactionId);
		
		s.setString(5, clientId);
		s.setString(6, accountType);
		s.setString(7, transactionId);

		s.executeUpdate();
	}

	public void accumulateInsertClob(String eventId, String object) throws SQLException {

		System.out.println("DEBUG - inserting Event with CLOB data");

		PreparedStatement s = conn.prepareStatement("insert into AccountEvents (EventID, Data) values (?, ?) ;");

		Clob clob = conn.createClob();
		clob.setString(1, object);

		s.setString(1, eventId);
		s.setClob(2, clob);

		s.executeUpdate();
	}

	public void CommitAll() {

		System.out.println("DEBUG - Committing All in Accounts");
		try {
			conn.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteAccount(String transactionId) throws SQLException {

		System.out.println("DEBUG - compensating for transaction ["+transactionId+"]");

		PreparedStatement s = conn.prepareStatement("delete from Accounts where TransactionID = ?;");
		s.setString(1, transactionId);
		s.execute();
	}	

}
