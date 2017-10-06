package org.jboss.integration.fis2demo.cards;

import javax.sql.DataSource;

import org.apache.activemq.RedeliveryPolicy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sql.SqlComponent;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CardsRoute extends RouteBuilder {

	private String JMSClientID="50";
	private String JMSClientID_compensation="51";
	private String replyTo="topic://VirtualTopic.NEW_CARD_CREATED";
	private String forwardTo="topic:VirtualTopic.NEW_CARD_CREATED";
	private String replyToEndpoint="activemq:topic:VirtualTopic.NEW_CARD_CREATED";
    //String replyToCompensation="topic://COMPENSATION";
	private String replyToCompensation="topic://VirtualTopic.CARD_COMPENSATED";
	private String forwardToCompensation="topic:VirtualTopic.CARD_COMPENSATED";
	
	private String inboundq = "queue:Consumer."+JMSClientID+".VirtualTopic.NEW_ACCOUNT_CREATED";
	private String compensationq = "queue:Consumer."+JMSClientID_compensation+".VirtualTopic.NEW_ACCOUNT_CREATED";
	private String card_mq_endpoint="activemq:queue:Consumer."+JMSClientID+".VirtualTopic.NEW_ACCOUNT_CREATED?replyTo="+replyTo;
	private String compensation_endpoint="activemq:queue.Consumer."+JMSClientID_compensation+".VirtualTopic.CARD_IN_ERROR?exchangePattern=InOnly&replyTo="+replyToCompensation;
	
	@Override
    public void configure() {
    	
    		org.apache.camel.processor.RedeliveryPolicy redelivery = new org.apache.camel.processor.RedeliveryPolicy();
    		redelivery.setMaximumRedeliveries(0);

                		
    		
    		// Using application.properties and its "spring.datasource.*" properties,
    		// the spring framework creates a datasource named "dataSource"

            from("sql:SELECT * FROM AccountEvents ORDER BY eventid DESC LIMIT 1 FOR UPDATE?dataSource=#mysqlDataSource").routeId("dbpollroute")
            	.onException(java.lang.IllegalArgumentException.class).to(compensation_endpoint).end()
            															//.log("Exception").stop().end()
            	.transacted("required")
            	.log("===== CARDS ===== polled an event from ACCOUNT Event Table")           	
            	.process("dbpoll")
            	.log("${body}") 
            	
            	.process("preparesql")

            	//.to("jdbc:mysqlDataSource?resetAutoCommit=false")
            	.to("sql:INSERT INTO Cards (CardNumber, ClientID, AccountID, CardType, TransactionID) values (:#${header.cardID}, :#${header.clientID} , :#${header.accountid}, :#${header.cardType}, :#${header.transactionID}) ON DUPLICATE KEY UPDATE ClientID=:#${header.clientID} , AccountID=:#${header.accountid}, CardType=:#${header.cardType}, TransactionID=:#${header.transactionID}?dataSource=#mysqlDataSource")
            	.log("===== CARDS ===== storing new Card in database")
            	.log("${body}")
            	
            	.setBody(simple("${header.saved_body}"))

            	.log("===== CARDS ===== sending message to : " + replyTo)
            	.to(replyToEndpoint)
            	
//            	.throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
            	.to("sql:DELETE FROM AccountEvents WHERE EventID = :#${header.eventID}?dataSource=#mysqlDataSource")
            	.log("===== CARDS ===== suppress Event from Event Table")
            	.delay(2000);
           		
            
           
            from(card_mq_endpoint).routeId("mqcardroute")
        		.onException(java.lang.IllegalArgumentException.class)	//.log("exception").end()
        																.handled(true).to(compensation_endpoint).end()
        		.log("===== CARDS ===== read message from "+inboundq)   
        		.log("${body}")
        		.process("preparesql")
            	//.to("jdbc:mysqlDataSource?resetAutoCommit=false")
            	.to("sql:INSERT INTO Cards (CardNumber, ClientID, AccountID, CardType, TransactionID) values (:#${header.cardID}, :#${header.clientID} , :#${header.accountid}, :#${header.cardType}, :#${header.transactionID}) ON DUPLICATE KEY UPDATE ClientID=:#${header.clientID} , AccountID=:#${header.accountid}, CardType=:#${header.cardType}, TransactionID=:#${header.transactionID}?dataSource=#mysqlDataSource")
            	.log("===== CARDS ===== storing new Card in database")
            	.log("${body}")
            	
            	.setBody(simple("${header.saved_body}"))
            	
            	.throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
            	.log("===== CARDS ===== forwaring message to : " + replyTo)
            	.to("activemq:"+forwardTo+"?exchangePattern=InOnly");
            	// use automated replyTo feature
            	//.delay(2000);
      	
      
            from(compensation_endpoint)
            	.log("===== CARDS ===== reading compensation message from " +compensationq)
            	.process("compensation")
            	.log("===== CARDS ===== deleting Card from database [tx = ${header.transactionID}]")
            	.to("sql:DELETE FROM Cards WHERE TransactionID = :#${header.transactionID} ?dataSource=#mysqlDataSource")
            	.log("==== CARDS ===== forwarding message to : " + replyToCompensation)
            	.to("activemq:"+forwardToCompensation+"?exchangePattern=InOnly");

    }

}
