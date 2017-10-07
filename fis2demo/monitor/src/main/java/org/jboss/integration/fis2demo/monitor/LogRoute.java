package org.jboss.integration.fis2demo.monitor;

import javax.sql.DataSource;

import org.apache.activemq.RedeliveryPolicy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sql.SqlComponent;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LogRoute extends RouteBuilder {

	private String JMSClientID="100";
	private String appID=JMSClientID;
	private String events_edpt = "queue:Consumer."+JMSClientID+".VirtualTopic.EVENTS_SOURCE";

	@Override
    public void configure() {
    	
    		org.apache.camel.processor.RedeliveryPolicy redelivery = new org.apache.camel.processor.RedeliveryPolicy();
    		redelivery.setMaximumRedeliveries(0);


//            from("sql:SELECT * FROM AccountEvents ORDER BY eventid DESC LIMIT 1 FOR UPDATE?dataSource=#mysqlDataSource").routeId("dbpollroute")
//            	.onException(java.lang.IllegalArgumentException.class).to(compensation_endpoint).end()
//            															//.log("Exception").stop().end()
//            	.transacted("required")
//            	.log("===== CARDS ===== polled an event from ACCOUNT Event Table")           	
//            	.process("dbpoll")
//            	.log("${body}") 
//            	
//            	.process("preparesql")
//
//            	//.to("jdbc:mysqlDataSource?resetAutoCommit=false")
//            	.to("sql:INSERT INTO Cards (CardNumber, ClientID, AccountID, CardType, TransactionID) values (:#${header.cardID}, :#${header.clientID} , :#${header.accountid}, :#${header.cardType}, :#${header.transactionID}) ON DUPLICATE KEY UPDATE ClientID=:#${header.clientID} , AccountID=:#${header.accountid}, CardType=:#${header.cardType}, TransactionID=:#${header.transactionID}?dataSource=#mysqlDataSource")
//            	.log("===== CARDS ===== storing new Card in database")
//            	.log("${body}")
//            	
//            	.setBody(simple("${header.saved_body}"))
//
//            	.log("===== CARDS ===== sending message to : " + replyTo)
//            	.to(replyToEndpoint)
//            	
//            	.delay(10000)
//           	.throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
//            	.to("sql:DELETE FROM AccountEvents WHERE EventID = :#${header.eventID}?dataSource=#mysqlDataSource")
//            	.log("===== CARDS ===== suppress Event from Event Table")
//            	.delay(2000);
//           		
            
           
            from("activemq:"+events_edpt).routeId("eventslogroute")  
        		.process("eventparser")
        		.log("${body}")
            	//.to("jdbc:mysqlDataSource?resetAutoCommit=false")
//        		.to("sql:INSERT INTO EventsTable (eventID, eventName, data, ownership) values (:#${header.cardID}, :#${header.clientID} , :#${header.accountid}, :#${header.cardType}, :#${header.transactionID}) ON DUPLICATE KEY UPDATE data=:#${header.data} , eventName=:#${header.eventName}...
        		.to("sql:INSERT INTO EventsTable (eventID, eventName, data, ownership) values (:#${header.eventID}, :#${header.eventName} , :#${header.eventData}, "+appID +")?dataSource=#mysqlDataSource")
        		;

    }

}
