package org.jboss.integration.fis2demo.monitor;





import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@EnableCircuitBreaker
@Configuration
@EnableAutoConfiguration
@ComponentScan
@ImportResource("classpath:camel/beans.xml")
public class MySpringBootMain
{

    public static void main(String[] args) {
        SpringApplication.run(MySpringBootMain.class, args);
    }

}

