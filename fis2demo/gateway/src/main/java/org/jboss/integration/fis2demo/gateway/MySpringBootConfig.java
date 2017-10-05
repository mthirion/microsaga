package org.jboss.integration.fis2demo.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gateway")
public class MySpringBootConfig {
	
	protected String activemq_user;
	protected String activemq_password;
	protected String activemq_url;
	protected String open_account;
	protected String new_account;
	protected String update_account;
	protected String replyTo;

	public MySpringBootConfig() {
	}
	
	
	public String getOpen_account() {
		return open_account;
	}


	public void setOpen_account(String open_account) {
		this.open_account = open_account;
	}


	public String getUpdate_account() {
		return update_account;
	}


	public void setUpdate_account(String update_account) {
		this.update_account = update_account;
	}


	public String getActivemquser() {
		return activemq_user;
	}

	public void setActivemquser(String activemq_user) {
		this.activemq_user = activemq_user;
	}

	public String getActivemqpassword() {
		return activemq_password;
	}

	public void setActivemqpassword(String activemq_password) {
		this.activemq_password = activemq_password;
	}

	public String getActivemqurl() {
		return activemq_url;
	}

	public void setActivemqurl(String activemq_url) {
		this.activemq_url = activemq_url;
	}
	

	public String getNewaccount() {
		return new_account;
	}

	public void setNewaccount(String new_account) {
		this.new_account = new_account;
	}



	
	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}


}