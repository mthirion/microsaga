package org.jboss.integration.fis2demo.users;




import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;


@Component
public class MySpringBootRoute extends RouteBuilder {

	@Autowired
	private MySpringBootConfig config;
		
    @Override
    public void configure() {
    	
    	from("direct:start")
        //.transform(method("myBean", "saySomething"))
        .to("stream:out");    	
    }

}
