package org.jboss.integration.fis2demo.viewer;

import javax.sql.DataSource;

import org.apache.activemq.RedeliveryPolicy;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sql.SqlComponent;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EventPollerRoute extends RouteBuilder {

	private String appID="100";

	@Override
    public void configure() {
    	

            from("sql:SELECT * FROM EventsTable ORDER BY EventID LIMIT 1 FOR UPDATE?dataSource=#mysqlDataSource").routeId("dbpollroute")
            	//.onException(java.lang.IllegalArgumentException.class).to(compensation_endpoint).end()
            															//.log("Exception").stop().end()

            	.log("===== POLL ===== polled an event from EventsTable Table")           	
            	.process("eventlocker")
            	.log("${header.eventName}") 
            	.choice()
            		.when(header("LOCK").isEqualTo("true"))
            			.log("===== POLL ===== event [${header.eventID}] is locked by another monitor")
            		.otherwise()
            			.log("===== POLL ===== locked event [${header.eventID}]")
            			.log("${body}")
            			.process("eventprocessor")
            			.log("===== POLL ===== event [${header.eventID}] has been processed and deleted")
            	.end()
            	.delay(1000);	// to keep the events in oder --would correspond to "refresh-time"
           	        
        
    }

}
