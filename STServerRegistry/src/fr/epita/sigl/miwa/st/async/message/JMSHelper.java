package fr.epita.sigl.miwa.st.async.message;

import java.util.Properties;
import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Connection;
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
public class JMSHelper implements IAsyncMessageSender, IAsyncMessageReceiver {

	private static final Logger LOGGER = Logger.getLogger(JMSHelper.class
			.getName());

	private static final String INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
	private static final String PROVIDER_URL = "jnp://localhost:1099";

	private ConnectionFactory connectionFactory = null;
	private Queue queue = null;
	private Connection connection = null;
	private Session session = null;
	private MessageProducer producer = null;

	/**
	 * Initialise le bazar JMS
	 * 
	 * @throws AsyncMessageException
	 */
	private void init(EApplication destination) throws AsyncMessageException {
		Context jndiContext = null;
		String queueName = null;

		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL,
				System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));

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
			connectionFactory = (ConnectionFactory) jndiContext
					.lookup("/ConnectionFactory");
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
			LOGGER.info("Retrieving queue " + queueName
					+ " from jndi context...");
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
		LOGGER.info("Queue retrieved.");

		try {
			jndiContext.close();
		} catch (NamingException ne) {
		}
		jndiContext = null;

		try {
			LOGGER.info("Establishing connection...");
			connection = connectionFactory.createConnection();
		} catch (JMSException e) {
			LOGGER.severe("Failed to establish connection to the queue.");
			connectionFactory = null;
			throw new AsyncMessageException(
					"Failed to establish connection to the queue.", e);
		}
		LOGGER.info("Connection established.");

		try {
			LOGGER.info("Creating session...");
			session = connection.createSession(false,
					Session.CLIENT_ACKNOWLEDGE);
		} catch (JMSException e) {
			LOGGER.severe("Failed to create queue session.");
			connectionFactory = null;
			try {
				connection.close();
			} catch (JMSException e1) {
			}
			throw new AsyncMessageException("Failed to create session.", e);
		}
		LOGGER.info("Session sucessfully created.");
	}

	@Override
	public void send(String message, EApplication destination)
			throws AsyncMessageException {
		init(destination);

		TextMessage textMessage = null;

		LOGGER.info("Creating producer...");
		try {
			producer = session.createProducer(queue);
		} catch (JMSException e) {
			LOGGER.severe("Failed to create producer.");
			connectionFactory = null;
			try {
				session.close();
				connection.close();
			} catch (JMSException e1) {
			}
			throw new AsyncMessageException("Failed to create producer.", e);
		}
		LOGGER.info("Producer intialized.");

		try {
			LOGGER.info("Creating text message...");
			textMessage = session.createTextMessage(message);
		} catch (JMSException e) {
			LOGGER.severe("Failed to create text message.");
			connectionFactory = null;
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
			connectionFactory = null;
			try {
				session.close();
				connection.close();
			} catch (JMSException e1) {
			}
			throw new AsyncMessageException("Failed to send massage to "
					+ destination.toString(), e);
		}
		connectionFactory = null;
		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
		}
	}

	@Override
	public void addListener(AAsyncMessageListener listener,
			EApplication application) throws AsyncMessageException {
		init(application);
		
		MessageConsumer consumer = null;

		LOGGER.info("Creating message consumer...");
		try {
			consumer = session.createConsumer(queue);
			LOGGER.info("Message consumer created.");
		} catch (JMSException e) {
			LOGGER.severe("Failed to create message consumer.");
			try {
				session.close();
				connection.close();
			} catch (JMSException e1) {
			}
			throw new AsyncMessageException(
					"Failed to create message consumer.", e);
		}

		LOGGER.info("Setting message listener...");
		try {
			consumer.setMessageListener(listener);
		} catch (JMSException e) {
			LOGGER.severe("Failed to set message listener.");
			try {
				session.close();
				connection.close();
			} catch (JMSException e1) {
			}
			throw new AsyncMessageException("Failed to set message listener.",
					e);
		}

		LOGGER.info("Starting connection...");
		try {
			connection.start();
			LOGGER.info("Connection started.");
		} catch (JMSException e) {
			LOGGER.severe("Failed to start connection.");
			try {
				session.close();
				connection.close();
			} catch (JMSException e1) {
			}
			throw new AsyncMessageException("Failed to start connection.", e);
		}
		
		LOGGER.info("Waiting for messages ...");
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException ie) {
        }
        LOGGER.info("Closing connection.");
		
		connectionFactory = null;
		try {
			session.close();
			connection.close();
		} catch (JMSException e1) {
		}
	}

}
