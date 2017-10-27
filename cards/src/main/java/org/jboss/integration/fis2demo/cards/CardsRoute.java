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
	private String replyTo="topic://VirtualTopic.EVENTS_SOURCE";
	private String forwardTo="topic:VirtualTopic.EVENTS_SOURCE";
	private String replyToEndpoint="activemq:topic:VirtualTopic.EVENTS_SOURCE";
    //String replyToCompensation="topic://COMPENSATION";
	private String replyToCompensation="topic://VirtualTopic.EVENTS_SOURCE";
	//private String forwardToCompensation="topic://VirtualTopic.EVENTS_SOURCE";
	
	private String inboundq = "queue:Consumer."+JMSClientID+".VirtualTopic.EVENTS_SOURCE";
	private String compensationq = "queue:Consumer."+JMSClientID_compensation+".VirtualTopic.EVENTS_SOURCE";
	private String card_mq_endpoint="activemq:queue:Consumer."+JMSClientID+".VirtualTopic.EVENTS_SOURCE?replyTo="+replyTo;
	private String compensation_endpoint="activemq:queue.Consumer."+JMSClientID_compensation+".VirtualTopic.EVENTS_SOURCE?exchangePattern=InOnly&replyTo="+replyToCompensation;
	
	@Override
    public void configure() {
    	
    		org.apache.camel.processor.RedeliveryPolicy redelivery = new org.apache.camel.processor.RedeliveryPolicy();
    		redelivery.setMaximumRedeliveries(0);
       		            
           
            from(card_mq_endpoint + "&selector=eventName='NEW_ACCOUNT_CREATED'").routeId("mqcardroute")
        		.onException(java.lang.IllegalArgumentException.class)	//.log("exception").end()
        																.handled(true).to(compensation_endpoint).end()
        		.log("===== CARDS ===== read event [NEW_ACCOUNT_CREATED]")   
        		.log("${body}")
        		.process("preparesql")
            	//.to("jdbc:mysqlDataSource?resetAutoCommit=false")
            	.to("sql:INSERT INTO Cards (CardNumber, ClientID, AccountID, CardType, TransactionID) values (:#${header.cardID}, :#${header.clientID} , :#${header.accountid}, :#${header.cardType}, :#${header.transactionID}) ON DUPLICATE KEY UPDATE ClientID=:#${header.clientID} , AccountID=:#${header.accountid}, CardType=:#${header.cardType}, TransactionID=:#${header.transactionID}?dataSource=#mysqlDataSource")
            	.log("===== CARDS ===== storing new Card in database")
            	.log("${body}")
            	
            	.setBody(simple("${header.saved_body}"))
            	
            	.delay(5000)
            	.delay(15000)
            	.throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
            	.log("===== CARDS ===== forwaring event [${header.transactionID} - NEW_CARD_CREATED]")
            	.to("activemq:"+forwardTo+"?exchangePattern=InOnly");
            	// use automated replyTo feature
            	//.delay(2000);
      	
      
            from(compensation_endpoint + "?selector=eventName='CARD_IN_ERROR'")
            	.log("===== CARDS ===== reading compensation message [CARD_IN_ERROR]")
            	.process("compensation")
            	.log("===== CARDS ===== deleting Card from database [tx = ${header.transactionID}]")
            	.to("sql:DELETE FROM Cards WHERE TransactionID = :#${header.transactionID} ?dataSource=#mysqlDataSource")
            	.log("==== CARDS ===== forwarding event [${header.transactionID} - CARD_COMPENSATED -event=${header.eventName}]")
            	.to("activemq:"+replyToCompensation+"?exchangePattern=InOnly");

    }

}
