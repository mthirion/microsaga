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

	private String replyTo = "topic://VirtualTopic.EVENTS_SOURCE";
	private String forwardTo = "topic:VirtualTopic.EVENTS_SOURCE";
	// String replyToCompensation="topic://COMPENSATION";
	private String replyToCompensation = "topic://VirtualTopic.EVENTS_SOURCE";

	private String inboundq = "Consumer." + JMSCLientID + ".VirtualTopic.EVENTS_SOURCE";
	private String inbound_endpoint = "activemqswarm:queue:Consumer." + JMSCLientID
			+ ".VirtualTopic.EVENTS_SOURCE?replyTo=" + replyTo;
	private String compensationq = "Consumer." + JMSCLientID_compensation + ".VirtualTopic.EVENTS_SOURCE";
	private String compensation_endpoint = "activemqswarm:queue:Consumer." + JMSCLientID_compensation
			+ ".VirtualTopic.EVENTS_SOURCE?exchangePattern=InOnly&replyTo=" + replyToCompensation;

	private String mongoEndpointParams = "database=fis2demo&amp;collection=Accounts";
	
	@Override
    public void configure() throws Exception {

     
    	from(inbound_endpoint+"&selector=eventName='NEW_USER_CREATED'")
  
        .onException(java.lang.IllegalArgumentException.class)	//.log("error").end()
        														.to(compensation_endpoint).end()
       
        .convertBodyTo(String.class)
        .log("===== ACCOUNTS ===== reading event [NEW_USER_CREATED]")
        .log("${body}")
        

        //.to("mongodb:mongoclient?"+mongoEndpointParams+"&operation=save")
        // .convertBodyTo(com.mongodb.DBObject.class)
        .bean("preparemsg","insert")
        .bean("mongoholder", "insert")
        .setBody(simple("${header.saved_body}"))
        
        //.process("in_mysql")
//      .throwException(new java.lang.IllegalArgumentException("Trying an exception !"))
        .log("===== ACCOUNTS ===== forwarding event [${header.transactionID} - NEW_ACCOUNT_CREATED]")
        	
//      Using the (automatic) replyTo feature
        .to("activemqswarm:"+forwardTo+"?exchangePattern=InOnly");	
        
        
        from(compensation_endpoint + "&selector=eventName='CARD_COMPENSATED'")
        	.log("===== ACCOUNTS ===== reading compensation event [CARD_COMPENSATED]")
        	//.to("mongodb:mongoclient?"+mongoEndpointParams+"&operation=remove")
        	.bean("preparemsg","delete")
        	.bean("mongoholder", "delete")
        	.log("===== ACCOUNTS ===== forwarding event to " + replyToCompensation)
        	// use automatic "replyTo" feature
        	.to("activemqswarm:"+replyToCompensation+"?exchangePattern=InOnly")
        	;

    }
}