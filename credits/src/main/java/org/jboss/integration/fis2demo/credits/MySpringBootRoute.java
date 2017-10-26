package org.jboss.integration.fis2demo.credits;



import org.apache.camel.builder.RouteBuilder;
import org.jboss.integration.fis2demo.users.MySpringBootConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

//@HystrixCommand(fallbackMethod = "helloFallback", commandProperties = {
//        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
//		})

@Component
public class MySpringBootRoute extends RouteBuilder {

	@Autowired
	private MySpringBootConfig config;
	
	@Autowired
	private MySpringBootEnv env;
	
	
	
    @Override
    public void configure() {
    	
    	/* CONFIGURE REST ENDPOINTS */
    	restConfiguration().component("restlet")
    	.host("0.0.0.0")
        .contextPath("/api")
        
        
        .port(8082);	// MIND THIS !!!
    	
    	
    	
    	/* CAMEL ROUTES */
    	from("rest:get:myresource")
    	//.setBody().method(config, "getMessage")
        .setBody(simple(config.getMessage() + "  -- from " + env.getStage()))
        //.log("${body}")
        .log("-- BEGIN service1 --")
        .log("-- CALL service2 --")
    	
    	.to("http4:{{target.namespace}}.{{target.service}}.svc.cluster.local/{{target.path}}")
        .setBody(body().append("\n<===  is the response from service2"))
        .log("-- END service1 --");


    }

}
