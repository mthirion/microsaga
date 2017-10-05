package org.jboss.integration.fis2demo.users;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
//@EnableCircuitBreaker
@ImportResource("classpath:camel/camel-context.xml")
//@EnableTransactionManagement
public class MySpringBootMain
{
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootMain.class, args);
    }

}

