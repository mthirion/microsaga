package org.jboss.integration.fis2demo.credits;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
//@EnableCircuitBreaker
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class MySpringBootMain
{

    public static void main(String[] args) {
        SpringApplication.run(MySpringBootMain.class, args);
    }

}

