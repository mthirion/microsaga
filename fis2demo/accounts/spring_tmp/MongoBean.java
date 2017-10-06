package org.jboss.integration.fis2demo.account;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;

public class MongoBean extends MongoClient {

	public MongoBean() throws UnknownHostException {
		super("localhost", 27017);
		// TODO Auto-generated constructor stub
	}

}
