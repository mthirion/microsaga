package org.jboss.integration.fis2demo.account;



import java.net.UnknownHostException;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MySpringBootRoute extends RouteBuilder {

	@Autowired
	private MySpringBootConfig config;
	
	@Autowired
	private MySpringBootEnv env;
	
	@Autowired
	private MongoBean client;
	
    @Override
    public void configure() {
    	
    	/* CONFIGURE REST ENDPOINTS */
    	restConfiguration().component("restlet")
    	.host("0.0.0.0")
        .contextPath("/api")
        
        
        .port(8082);	// MIND THIS !!!
    	
    	
    	
    	/* CAMEL ROUTES */
    	from("rest:get:yourresource")
    	//.setBody().method(config, "getMessage")
    	.log("-- BEGIN service2 --")
    	.setBody(simple(config.getMessage() + "  -- from " + env.getStage()))
        //.log("${body}")
        .log("-- END service2 --");

    	//from("mongodb:client?database=fis2demo&collection=accounts&operation=insert")
    }

}
