package org.jboss.integration.fis2demo.credits;



import javax.jms.Message;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.client.*;

import net.sf.json.JSONObject;

@Component
public class JMSReceiver {
	
	private static MongoClient mongoClient = null;
	private MongoDatabase database = null;
	private MongoCollection<Document> collection = null;
	
	@Autowired JmsMessagingTemplate jmstemplate;
	@Autowired MySpringBootConfig config;

	@JmsListener(destination = "new_user")
	public void receive(Message message) {
		message.
		JSONObject object = JSONObject.fromString(message);
		System.out.println("received : " + message);
	}
	
	private void insertDB(JSONObject data) {
		if (mongoClient == null) initDB();
	
       	Document d = Document.parse(data.toString());    	
    	collection.insertOne(d);
	}
			
	private void initDB() {
		mongoClient = new MongoClient(config.getDbhost(),config.getDbport());
		database = mongoClient.getDatabase(config.getDbname());
		collection = database.getCollection(config.getDbcollection());
	}
}
