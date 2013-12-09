package fr.epita.sigl.miwa.st.async.message;

import java.util.Properties;
import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Connection;
import javax.jms.QueueConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import fr.epita.sigl.miwa.st.EApplication;

/**
 * Se charge d'envoyer des messages asynchrone via une queue JMS qui dans notre
 * cas sera une HornetQ embarquée dans JBoss AS 7
 * 
 * @author francois
 * 
 */
public class JMSSender implements IAsyncMessageSender {

	private static final Logger LOGGER = Logger.getLogger(JMSSender.class
			.getName());
	
    private static final String DEFAULT_USERNAME = "quickstartUser";
    private static final String DEFAULT_PASSWORD = "quickstartPwd1!";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://localhost:4447";
	
	private ConnectionFactory queueConnectionFactory = null;
	private Queue queue = null;
	private Connection connection = null;
	private Session session = null;
	private MessageProducer producer = null;

	/**
	 * Initialise le bazar JMS
	 * 
	 * @throws AsyncMessageException
	 */
	private void init(EApplication destination)
			throws AsyncMessageException {
		Context jndiContext = null;
		String queueName = null;
		
		final Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
//        env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
//        env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));

		if (queueName == null || queueName == "") {
			// TODO get queue name from ConfigurationManager
			LOGGER.info("Retrieving queue name...");
			queueName = "queue/test";
			LOGGER.info("Queue name is : " + queueName);
		}

		if (jndiContext == null) {
			LOGGER.info("The context is null, initializing context...");
			try {
				jndiContext = new InitialContext(env);
			} catch (NamingException e) {
				LOGGER.severe("Failed to initialize context");
				throw new AsyncMessageException("Failed to initialize context",
						e);
			}
			LOGGER.info("Context initialized.");
		}

		try {
			LOGGER.info("Initializing connection factory...");
			// TODO get JNDI name of connection factory
			queueConnectionFactory = (QueueConnectionFactory) jndiContext
					.lookup("jms/RemoteConnectionFactory");
		} catch (NamingException e) {
			LOGGER.severe("Failed to retrieve ConnectionFactory from jndi context.");
			try {
				jndiContext.close();
			} catch (NamingException ne) {
			}
			jndiContext = null;
			throw new AsyncMessageException(
					"Failed to retrieve ConnectionFactory from jndi context.",
					e);
		}
		LOGGER.info("QueueConnectionFactory initialized");

		try {
			LOGGER.info("Retrieving queue " + queueName + " from jndi context...");
			queue = (Queue) jndiContext.lookup(queueName);
		} catch (NamingException e) {
			LOGGER.severe("Failed to retrieve queue " + queueName
					+ " from jndi context.");
			try {
				jndiContext.close();
			} catch (NamingException ne) {
			}
			jndiContext = null;
			throw new AsyncMessageException("Failed to retrieve queue "
					+ queueName + " from jndi context.", e);
		}
		LOGGER.info("Queue restrieved.");

		try {
			jndiContext.close();
		} catch (NamingException ne) {
		}
		jndiContext = null;
		
		try {
			LOGGER.info("Establishing connection...");
			connection = queueConnectionFactory.createConnection();
		} catch (JMSException e) {
			LOGGER.severe("Failed to establish connection to the queue.");
			queueConnectionFactory = null;
			throw new AsyncMessageException(
					"Failed to establish connection to the queue.", e);
		}
		LOGGER.info("Connection established.");

		try {
			LOGGER.info("Creating session...");
			session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			LOGGER.severe("Failed to create queue session.");
			queueConnectionFactory = null;
			try {
				connection.close();
			} catch (JMSException e1) {
			}
			throw new AsyncMessageException("Failed to create session.",
					e);
		}
		LOGGER.info("Session sucessfully created.");

		LOGGER.info("Creating producer...");
		try {
			producer = session.createProducer(queue);
		} catch (JMSException e) {
			LOGGER.severe("Failed to create producer.");
			queueConnectionFactory = null;
			try {
				session.close();
				connection.close();
			} catch (JMSException e1) {
			}
			throw new AsyncMessageException("Failed to create producer.", e);
		}
		LOGGER.info("Producer intialized.");
	}

	@Override
	public void send(String message, EApplication destination)
			throws AsyncMessageException {
		init(destination);

		TextMessage textMessage = null;
		
		try {
			LOGGER.info("Creating text massage...");
			textMessage = session.createTextMessage(message);
		} catch (JMSException e) {
			LOGGER.severe("Failed to create text message.");
			queueConnectionFactory = null;
			try {
				session.close();
				connection.close();
			} catch (JMSException e1) {
			}
			throw new AsyncMessageException("Failed to create text message.", e);
		}
		LOGGER.info("TextMessage created.");

		try {
			LOGGER.info("Sending message to " + destination.toString());
			producer.send(textMessage);
			LOGGER.info("Message sent.");
		} catch (JMSException e) {
			LOGGER.severe("Failed to send massage to " + destination.toString());
			queueConnectionFactory = null;
			try {
				session.close();
				connection.close();
			} catch (JMSException e1) {
			}
			throw new AsyncMessageException("Failed to send massage to "
					+ destination.toString(), e);
		}
		queueConnectionFactory = null;
		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
		}
	}

}
