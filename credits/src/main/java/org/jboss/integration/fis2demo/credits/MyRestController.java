package org.jboss.integration.fis2demo.credits;

import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

//import org.json.JSONObject;
import net.sf.json.JSONObject;



@RestController
@RequestMapping("/api")
@CrossOrigin
public class MyRestController {

	@Autowired private JmsTemplate jmsTemplate;
    
	private String databaseName = "fis2demo";
	private String collectionName = "users";
	
	private MongoClient mongoClient = null;
	private MongoDatabase database = null;
	private MongoCollection<Document> collection = null;
	
	public MyRestController() {
//    	mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    	mongoClient = new MongoClient();	// local and default URL
    	
    	database = mongoClient.getDatabase(databaseName);
    	collection = database.getCollection(collectionName);		
	}
	
    @RequestMapping(

            method = RequestMethod.POST,

            //produces = MediaType.APPLICATION_JSON_VALUE,

            //value={"/{entity}", "/{entity}/{id}/relationships/{entity2}", "/{entity}/{id}/{child}", "/{entity}/{id}"})
            value={"/open_account"}
    )
    public String doPost(@RequestParam final Map<String, String> allRequestParams, @RequestBody String requestBody) {
    	
    	JSONObject data = JSONObject.fromString(requestBody);
    	
    	jmsTemplate.convertAndSend("fis2demo", data.toString());
    	System.out.println(data.toString());

    	return requestBody;
    }    
    
    @RequestMapping(

            method = RequestMethod.GET,
            value={"/user"}
    )
    public String doGet(@RequestParam final Map<String, String> allRequestParams) {
    	
    	String fake_data="{Nom: Someone, Prenom: Also}";
    	JSONObject data = JSONObject.fromString(fake_data);
    	
    	System.out.println(data.toString());
    	   	
       	Document d = Document.parse(data.toString());    	
    	collection.insertOne(d);

    	return "Done";
    }    
    
    @RequestMapping(

            method = RequestMethod.GET,
            value={"/user/{id}"}
    )    
    public String doGet(@PathVariable("id") long id) {

    	return "User : " + Long.toString(id);
    }    
    
    
}
