package fr.epita.sigl.miwa.st.async.message;

import java.util.Properties;
import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileMessage;
import fr.epita.sigl.miwa.st.async.file.IAsyncFileNotifier;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

/**
 * Se charge d'envoyer des messages asynchrone via une queue JMS qui dans notre
 * cas sera une HornetQ
 * 
 * @author francois
 * 
 */
public class JMSManager implements IAsyncMessageManager, IAsyncFileNotifier {

	private static final Logger LOGGER = Logger.getLogger(JMSManager.class
			.getName());

	private static final String INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
	private static final String PROVIDER_URL = "jnp://localhost:1099";

	private class JMSContext {
		private ConnectionFactory connectionFactory = null;
		private Queue queue = null;
		private Connection connection = null;
		private Session session = null;
		private MessageProducer producer = null;
	}

	/**
	 * Initialise le bazar JMS
	 * 
	 * @throws AsyncMessageException
	 */
	private JMSContext init(EApplication destination)
			throws AsyncMessageException {
		JMSContext context = new JMSContext();
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
			context.connectionFactory = (ConnectionFactory) jndiContext
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
			context.queue = (Queue) jndiContext.lookup(queueName);
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
			context.connection = context.connectionFactory.createConnection();
		} catch (JMSException e) {
			LOGGER.severe("Failed to establish connection to the queue.");
			context.connectionFactory = null;
			throw new AsyncMessageException(
					"Failed to establish connection to the queue.", e);
		}
		LOGGER.info("Connection established.");

		try {
			LOGGER.info("Creating session...");
			context.session = context.connection.createSession(false,
					Session.CLIENT_ACKNOWLEDGE);
		} catch (JMSException e) {
			LOGGER.severe("Failed to create queue session.");
			context.connectionFactory = null;
			close(context);
			throw new AsyncMessageException("Failed to create session.", e);
		}
		LOGGER.info("Session sucessfully created.");
		return context;
	}

	private void close(JMSContext context) {
		try {
			context.connectionFactory = null;
			if (context.session != null)
				context.session.close();
			if (context.connection != null)
				context.connection.close();
		} catch (JMSException e1) {
		}
	}

	@Override
	public void send(String message, EApplication destination)
			throws AsyncMessageException {
		JMSContext context = init(destination);

		TextMessage textMessage = null;

		try {
			LOGGER.info("Creating producer...");
			try {
				context.producer = context.session
						.createProducer(context.queue);
			} catch (JMSException e) {
				LOGGER.severe("Failed to create producer.");
				throw new AsyncMessageException("Failed to create producer.", e);
			}
			LOGGER.info("Producer intialized.");

			try {
				LOGGER.info("Creating text message...");
				textMessage = context.session.createTextMessage(message);
			} catch (JMSException e) {
				LOGGER.severe("Failed to create text message.");
				throw new AsyncMessageException(
						"Failed to create text message.", e);
			}
			LOGGER.info("TextMessage created.");

			try {
				LOGGER.info("Sending message to " + destination.toString());
				context.producer.send(textMessage);
				LOGGER.info("Message sent.");
			} catch (JMSException e) {
				LOGGER.severe("Failed to send message to "
						+ destination.toString());
				throw new AsyncMessageException("Failed to send message to "
						+ destination.toString(), e);
			}
		} finally {
			close(context);
		}
	}

	@Override
	public void addListener(AAsyncMessageListener listener,
			EApplication application) throws AsyncMessageException {
		JMSContext context = init(application);

		MessageConsumer consumer = null;

		LOGGER.info("Creating message consumer...");
		try {
			consumer = context.session.createConsumer(context.queue);
			LOGGER.info("Message consumer created.");
		} catch (JMSException e) {
			LOGGER.severe("Failed to create message consumer.");
			close(context);
			throw new AsyncMessageException(
					"Failed to create message consumer.", e);
		}

		LOGGER.info("Setting message listener...");
		try {
			consumer.setMessageListener(listener);
			LOGGER.info("Message listener set");
		} catch (JMSException e) {
			LOGGER.severe("Failed to set message listener.");
			close(context);
			throw new AsyncMessageException("Failed to set message listener.",
					e);
		}

		LOGGER.info("Starting connection...");
		try {
			context.connection.start();
			LOGGER.info("Connection started.");
		} catch (JMSException e) {
			LOGGER.severe("Failed to start connection.");
			close(context);
			throw new AsyncMessageException("Failed to start connection.", e);
		}

		close(context);
	}

	@Override
	public void notify(AsyncFileMessage message) throws AsyncMessageException {
		JMSContext context = init(message.getDestination());
		ObjectMessage objectMessage = null;

		try {
			LOGGER.info("Creating producer...");
			try {
				context.producer = context.session
						.createProducer(context.queue);
			} catch (JMSException e) {
				LOGGER.severe("Failed to create producer.");
				throw new AsyncMessageException("Failed to create producer.", e);
			}
			LOGGER.info("Producer intialized.");

			try {
				LOGGER.info("Creating object message...");
				objectMessage = context.session.createObjectMessage();
			} catch (JMSException e) {
				LOGGER.severe("Failed to create object message.");
				throw new AsyncMessageException(
						"Failed to create object message.", e);
			}
			LOGGER.info("ObjectMessage created.");

			try {
				LOGGER.info("Setting AsyncFileMessage to ObjectMessage...");
				objectMessage.setObject(message);
				LOGGER.info("AsyncFileMessage in ObjectMessage.");
			} catch (JMSException e) {
				LOGGER.severe("Failed to set AsyncFileMessage to ObjectMessage.");
				throw new AsyncMessageException(
						"Failed to set AsyncFileMessage to ObjectMessage.", e);
			}

			try {
				LOGGER.info("Sending message to "
						+ message.getDestination().toString());
				context.producer.send(objectMessage);
				LOGGER.info("Message sent.");
			} catch (JMSException e) {
				LOGGER.severe("Failed to send message to "
						+ message.getDestination().toString());
				throw new AsyncMessageException("Failed to send message to "
						+ message.getDestination().toString(), e);
			}
		} finally {
			close(context);
		}
	}

}
