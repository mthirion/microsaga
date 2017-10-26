package org.jboss.integration.fis2demo.users;

import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;

@Component("dbclient")
public class CustomMongoDBClient extends MongoClient {

	public CustomMongoDBClient() throws UnknownHostException {
		super("localhost", 27017);
		// TODO Auto-generated constructor stub
	}

}
