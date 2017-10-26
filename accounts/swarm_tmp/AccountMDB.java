package org.jboss.integration.fis2demo.account;

import javax.ejb.*;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.ejb3.annotation.ResourceAdapter;

@ResourceAdapter(value = "activemq5")
@MessageDriven(name = "AccountMDB", activationConfig = {
           @ActivationConfigProperty(propertyName = "destination", propertyValue = "new_account"),
           @ActivationConfigProperty(propertyName = "useJNDI", propertyValue = "false"),
           @ActivationConfigProperty(propertyName="connectionParameters", propertyValue = "host=127.0.0.1;port=61616"),  
           @ActivationConfigProperty(propertyName="connectorClassName", propertyValue = "org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory"),
//           @ActivationConfigProperty(propertyName="connectorClassName", propertyValue = "org.apache.activemq.artemis.core.remoting.impl.invm.InVMConnectorFactory"),
           @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), })
public class AccountMDB implements MessageListener {

	@Override
	public void onMessage(Message msg) {
		// TODO Auto-generated method stub
		
		if (msg instanceof TextMessage) {
			
			try {
//				String s = msg.getBody(String.class);
				TextMessage m = (TextMessage) msg;
				System.out.println("message is: " + m.getText());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
