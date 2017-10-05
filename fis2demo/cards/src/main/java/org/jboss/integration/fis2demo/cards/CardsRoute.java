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

	@Override
    public void configure() {
    	
    		org.apache.camel.processor.RedeliveryPolicy redelivery = new org.apache.camel.processor.RedeliveryPolicy();
    		redelivery.setMaximumRedeliveries(0);

    		String replyTo="topic://NEW_CARD_CREATED";
    		String replyToEndpoint="activemq:topic:NEW_CARD_CREATED";
            //String replyToCompensation="topic://COMPENSATION";
            String replyToCompensation="topic://CARD_COMPENSATED";
            String card_mq_endpoint="activemq:topic:NEW_ACCOUNT_CREATED?replyTo="+replyTo;
            String compensation_endpoint="activemq:topic:CARD_IN_ERROR?exchangePattern=InOnly&replyTo="+replyToCompensation;
                		
    		
    		// Using application.properties and its "spring.datasource.*" properties,
    		// the spring framework creates a datasource named "dataSource"

            from("sql:SELECT * FROM AccountEvents ORDER BY eventid DESC LIMIT 1 FOR UPDATE?dataSource=#mysqlDataSource").routeId("dbpollroute")
            	.onException(java.lang.IllegalArgumentException.class).to(compensation_endpoint).end()
            															//.log("Exception").stop().end()
            	.transacted("required")
            	.process("dbpoll")
            	.log("got the content of the db")
            	.log("${body}")
            	
            	.process("preparesql")
            	.log("message ready for insertion")
            	.log("${body}")
            	//.to("jdbc:mysqlDataSource?resetAutoCommit=false")
            	.to("sql:INSERT INTO Cards (CardNumber, ClientID, AccountID, CardType, TransactionID) values (:#${header.cardID}, :#${header.clientID} , :#${header.accountid}, :#${header.cardType}, :#${header.transactionID}) ON DUPLICATE KEY UPDATE ClientID=:#${header.clientID} , AccountID=:#${header.accountid}, CardType=:#${header.cardType}, TransactionID=:#${header.transactionID}?dataSource=#mysqlDataSource")
            	.log("message inserted")
            	
            	.setBody(simple("${header.savedBody}"))
            	.log("FINAL")
            	.log("${body}")

            	.log("delivering to destination " + replyTo)
            	.to(replyToEndpoint)
            	
//            	.throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
            	.to("sql:DELETE FROM AccountEvents WHERE EventID = :#${header.eventID}?dataSource=#mysqlDataSource")
            	.log("record deleted")
            	.delay(2000);
           		
            
           
            from(card_mq_endpoint).routeId("mqcardroute")
        		.onException(java.lang.IllegalArgumentException.class)	//.log("exception").end()
        																.handled(true).to(compensation_endpoint).end()
        		.process("preparesql")
            	.log("message ready for insertion")
            	.log("${body}")
            	//.to("jdbc:mysqlDataSource?resetAutoCommit=false")
            	.to("sql:INSERT INTO Cards (CardNumber, ClientID, AccountID, CardType, TransactionID) values (:#${header.cardID}, :#${header.clientID} , :#${header.accountid}, :#${header.cardType}, :#${header.transactionID}) ON DUPLICATE KEY UPDATE ClientID=:#${header.clientID} , AccountID=:#${header.accountid}, CardType=:#${header.cardType}, TransactionID=:#${header.transactionID}?dataSource=#mysqlDataSource")
            	.log("message inserted")
            	
            	.setBody(simple("${header.saved_body}"))
            	.log("FINAL")
            	.log("${body}")
            	
            	.throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
            	.log("delivering to destination " + replyTo)
            	//.to("activemq:topic:NEW_CARD_CREATED")
            	// use automated replyTo feature
            	.delay(2000);
      	
      
            from(compensation_endpoint)
            	.log("compensatin cards")
            	.process("compensation")
            	.log("performing compensation for transaction ${header.transactionID}")
            	.to("sql:DELETE FROM Cards WHERE TransactionID = :#${header.transactionID} ?dataSource=#mysqlDataSource")
            	.log("propagating to destination " + replyToCompensation);

    }

}
