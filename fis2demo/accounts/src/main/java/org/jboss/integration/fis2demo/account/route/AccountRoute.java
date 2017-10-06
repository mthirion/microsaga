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

	private String BROKER_URL = "tcp://localhost:61617?broker.persistent=false&broker.useJmx=false&broker.useShutdownHook=false";
	private String JMSCLientID = "40";
	private String JMSCLientID_compensation = "41";

	private String replyTo = "topic://VirtualTopic.NEW_ACCOUNT_CREATED";
	private String forwardTo = "topic:VirtualTopic.NEW_ACCOUNT_CREATED";
	// String replyToCompensation="topic://COMPENSATION";
	private String replyToCompensation = "topic://VirtualTopic.ACCOUNT_COMPENSATED";

	private String inboundq = "Consumer." + JMSCLientID + ".VirtualTopic.NEW_USER_CREATED";
	private String inbound_endpoint = "activemqswarm:queue:Consumer." + JMSCLientID
			+ ".VirtualTopic.NEW_USER_CREATED?replyTo=" + replyTo;
	private String compensationq = "Consumer." + JMSCLientID_compensation + ".VirtualTopic.NEW_USER_CREATED";
	private String compensation_endpoint = "activemqswarm:queue:Consumer." + JMSCLientID_compensation
			+ ".VirtualTopic.CARD_COMPENSATED?exchangePattern=InOnly&replyTo=" + replyToCompensation;

	@Override
    public void configure() throws Exception {

     
    	from(inbound_endpoint)
		//from("activemqswarm:queue:Consumer.40.VirtualTopic.NEW_USER_CREATED?replyTo=topic://VirtualTopic.NEW_ACCOUNT_CREATED?exchangePattern=InOnly")
       
        .onException(java.lang.IllegalArgumentException.class)	//.log("error").end()
        														.to(compensation_endpoint).end()
       
        // HOOKS
        //.setHeader("useEventTable", constant("true"))	// Switch between activemq and event-table
        
        .convertBodyTo(String.class)
        .log("===== ACCOUNTS ===== reading message from "+inboundq)
        .log("${body}")
        
        .choice()
        .when(header("useEventTable").isEqualTo("true"))
        	.process("dbholder")
            .log("===== ACCOUNTS ===== handling SAGA with Event Table")
//        	.throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
        .otherwise()
        	.process("dbholder")
//        	.throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
        	.log("===== ACCOUNTS ===== forwarding message to " + replyTo)
        	
//      Using the (automatic) replyTo feature
        .to("activemqswarm:"+forwardTo+"?exchangePattern=InOnly")	
        .end();
        
        
        from(compensation_endpoint)
        	.log("===== ACCOUNTS ===== reading compensation message from "+compensationq)
        	.process("compensate")
        	.log("===== ACCOUNTS ===== forwarding message to " + replyToCompensation)
        	// use automatic "replyTo" feature
        	.to("activemqswarm:"+replyToCompensation+"?exchangePattern=InOnly")
        	;

    }
}