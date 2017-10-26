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

           
            from("activemq:"+events_edpt).routeId("eventslogroute")  
        		.process("eventparser")
        		.log("${body}")
            	//.to("jdbc:mysqlDataSource?resetAutoCommit=false")
//        		.to("sql:INSERT INTO EventsTable (eventID, eventName, data, ownership) values (:#${header.cardID}, :#${header.clientID} , :#${header.accountid}, :#${header.cardType}, :#${header.transactionID}) ON DUPLICATE KEY UPDATE data=:#${header.data} , eventName=:#${header.eventName}...
        		.to("sql:INSERT INTO EventsTable (eventName, data, ownership) values (:#${header.eventName} , :#${header.eventData}, NULL)?dataSource=#mysqlDataSource")
        		;

    }

}
