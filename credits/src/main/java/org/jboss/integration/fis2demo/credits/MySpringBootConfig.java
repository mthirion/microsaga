package org.jboss.integration.fis2demo.credits;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "users")
public class MySpringBootConfig {
	
	protected String dbhost;
	protected Integer dbport;
	protected String dbname;
	protected String dbcollection;
	protected String qname;
	
	
	public String getDbhost() {
		return dbhost;
	}
	public void setDbhost(String dbhost) {
		this.dbhost = dbhost;
	}
	public Integer getDbport() {
		return dbport;
	}
	public void setDbport(Integer dbport) {
		this.dbport = dbport;
	}
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	public String getDbcollection() {
		return dbcollection;
	}
	public void setDbcollection(String dbcollection) {
		this.dbcollection = dbcollection;
	}

	public String getQname() {
		return qname;
	}
	public void setQname(String qname) {
		this.qname = qname;
	}

}