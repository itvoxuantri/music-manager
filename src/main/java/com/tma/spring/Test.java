package com.tma.spring;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.wildfly.naming.client.WildFlyInitialContextFactory;

import com.tma.spring.entity.Music;
import com.tma.spring.mbean.AuthorServiceImplMBean;
import com.tma.spring.mbean.CategoryServiceImplMBean;
import com.tma.spring.mbean.MusicServiceImplMBean;
import com.tma.spring.mbean.StudentServiceImplMBean;

public class Test {

	public static void main(String[] args) throws URISyntaxException, Exception {
		JMXConnector jmxc = null;
		InitialContext initialContext = null;
		Connection connection = null;
		MessageConsumer consumer = null;
		try {
			// Remote jmx to wildFly
			JMXServiceURL url = new JMXServiceURL("service:jmx:remote+http://localhost:9990");
			jmxc = JMXConnectorFactory.connect(url, null);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
			ObjectName mbeanStudent = new ObjectName("bean:name=studentService");

			ObjectName mbeanCategory = new ObjectName("bean:name=categoryService");

			ObjectName mbeanAuthor = new ObjectName("bean:name=authorService");

			ObjectName mbeanMusic = new ObjectName("bean:name=musicService");
			StudentServiceImplMBean studentService = JMX.newMBeanProxy(mbsc, mbeanStudent,
					StudentServiceImplMBean.class, true);

			CategoryServiceImplMBean categoryService = JMX.newMBeanProxy(mbsc, mbeanCategory,
					CategoryServiceImplMBean.class, true);

			AuthorServiceImplMBean authorService = JMX.newMBeanProxy(mbsc, mbeanAuthor, AuthorServiceImplMBean.class,
					true);

			MusicServiceImplMBean musicService = JMX.newMBeanProxy(mbsc, mbeanMusic, MusicServiceImplMBean.class, true);

			// Remote jms to wildFly
			Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
			env.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
			env.put(Context.SECURITY_PRINCIPAL, "admin");
			env.put(Context.SECURITY_CREDENTIALS, "admin");
			initialContext = new InitialContext(env);
			ConnectionFactory connectionFactory = (ConnectionFactory) initialContext
					.lookup("jms/RemoteConnectionFactory");
			connection = connectionFactory.createConnection("admin1", "admin1");
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = (Destination) initialContext.lookup("jms/topic/testTopic");

			// MessageConsumer is used for receiving (consuming) messages
			consumer = session.createConsumer(destination);

			// Here we receive the message.
			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					TextMessage msg = (TextMessage) message;
					try {
						System.out.println(msg.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}

			});
			Thread.sleep(10 * 1000l);

			Music author = new Music("Duyên Phận", authorService.findRecordById(1), categoryService.findRecordById(1),
					(Date) new SimpleDateFormat("dd/MM/yyyy").parse("28/01/1998"));
			musicService.createRecord(author);
			
			Thread.sleep(10 * 1000l);
		} finally {
			if (jmxc != null || initialContext != null || consumer != null || connection != null) {
				try {
					jmxc.close();
					initialContext.close();
					consumer.close();
					connection.close();
				} catch (Exception e) {
				}

			}
		}
	}

}
