package org.jboss.integration.fis2demo.account;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "env")
public class MySpringBootEnv {

	protected String stage;

	public MySpringBootEnv() {
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

}