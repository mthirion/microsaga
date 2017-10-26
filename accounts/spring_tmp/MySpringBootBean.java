package org.jboss.integration.fis2demo.account;

import org.springframework.stereotype.Component;

@Component("myBean")
public class MySpringBootBean {

    private String msg="Hello my Bean";

    public String writeMessage() {
        return msg;
    }

}