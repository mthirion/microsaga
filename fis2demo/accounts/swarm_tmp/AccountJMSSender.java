package org.jboss.integration.fis2demo.account;

import javax.inject.Inject;
import javax.jms.*;

public class AccountJMSSender {

	@Inject
    @JMSConnectionFactory("java:/jms/activemq5QCF")
    private JMSContext context;
	
	public void sendJMS() {
		JMSProducer p = context.createProducer();
		TextMessage t = context.createTextMessage();
		Queue q = context.createQueue("new_card");
		p.send(q, t);
	}
}
