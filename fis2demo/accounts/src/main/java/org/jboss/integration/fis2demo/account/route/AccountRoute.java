package org.jboss.integration.fis2demo.account.route;

import javax.enterprise.context.ApplicationScoped;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.wildfly.extension.camel.CamelAware;

//@CamelAware
//@ContextName("jms-context")
@ApplicationScoped
public class AccountRoute extends RouteBuilder {

    private static String BROKER_URL = "tcp://localhost:61617?broker.persistent=false&broker.useJmx=false&broker.useShutdownHook=false";


    @Override
    public void configure() throws Exception {

    	String replyTo="topic://NEW_ACCOUNT_CREATED";
    	//String replyToCompensation="topic://COMPENSATION";
    	String replyToCompensation="topic://ACCOUNT_COMPENSATED";
    	String inbound_endpoint = "activemqswarm:topic:NEW_USER_CREATED?clientId=201&subscriptionDurable=true&replyTo="+replyTo;	
    	String compensation_endpoint = "activemqswarm:topic:CARD_COMPENSATED?exchangePattern=InOnly&clientId=202&subscriptionDurable=true&replyTo="+replyToCompensation;
        
    	from(inbound_endpoint)
        
        .onException(java.lang.IllegalArgumentException.class)	//.log("error").end()
        														.to(compensation_endpoint).end()
       
        // HOOKS
        //.setHeader("useEventTable", constant("true"))	// Switch between activemq and event-table
        
        .convertBodyTo(String.class)
        .log("${body}")
        
        .choice()
        .when(header("useEventTable").isEqualTo("true"))
        	.process("dbholder")
            .log("handled with Event Table.")
//        	.throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
        .otherwise()
        	.process("dbholder")
//        	.throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
        	.log("handled with Messaging.")
        	.log("sending message to " + replyTo)
        	
//      Using the (automatic) replyTo feature
//      .to("activemqswarm:topic:new_card");     	
        .end();
        
        
        from(compensation_endpoint)
        	.process("compensate")
        	.log("sending message to " + replyToCompensation)
        	// use automatic "replyTo" feature
        	;

    }
}