package org.jboss.integration.fis2demo.gateway;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import net.sf.json.JSONObject;

public class JMSSender {

	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;

	private String default_brokerURL = ActiveMQConnection.DEFAULT_BROKER_URL;

	public JMSSender(String brokerUrl, String user, String password) {

		try {
			factory = new ActiveMQConnectionFactory(brokerUrl);
			connection = factory.createConnection(user, password);

			if (connection != null) {
				connection.start();
				session = connection.createSession(true, 0);
			} else
				System.out.println("Cannot make connection to ActiveMQ");

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public JMSSender() {

		try {
			factory = new ActiveMQConnectionFactory(default_brokerURL);
			connection = factory.createConnection("admin", "admin");

			if (connection != null) {
				connection.start();
				session = connection.createSession(true, 0);
			} else
				System.out.println("Cannot make connection to ActiveMQ");

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void send(String destinationName, JSONObject obj, String replyTo) {

		try {
			//destination = session.createQueue(destinationName);
			destination = session.createTopic(destinationName);
			producer = session.createProducer(destination);

			TextMessage msg = session.createTextMessage();
			msg.setText(obj.toString());

			System.out.println("===== GATEWAY ===== forward message to : " + destination.toString());
			System.out.println(msg.getText());
			System.out.println();
			producer.send(msg);

			session.commit();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}