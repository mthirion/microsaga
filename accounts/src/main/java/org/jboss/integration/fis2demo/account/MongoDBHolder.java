package org.jboss.integration.fis2demo.account;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import net.sf.json.JSONObject;

public class MongoDBHolder {

	@Autowired
	private MongoClient mongoclient;

	private MongoDatabase database;
	private MongoCollection<Document> col;

	public MongoDBHolder() {
		mongoclient = new MongoClient(new ServerAddress("localhost", 27017));
		database = mongoclient.getDatabase("fis2demo");
		col = database.getCollection("Accounts");
	}

	public void insert(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		Message m = exchange.getIn();
		String body = m.getBody(String.class);

		Document doc = Document.parse(body);

		System.out.println("===== ACCOUNTS ===== storing new Account in database");
		col.insertOne(doc);

	}

	public void delete(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		Message m = exchange.getIn();
		String transactionId = (String) m.getHeader("transactionID");

		System.out.println("===== ACCOUNTS ===== deleting Account from database [tx="+transactionId+"]");
		Bson condition = new Document("_id", transactionId);
		col.deleteOne(condition);

	}

}
