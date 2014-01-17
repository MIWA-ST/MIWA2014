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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.ConfigurationException;
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
class JMSManager implements IAsyncMessageManager, IAsyncFileNotifier {

	private static final Logger LOGGER = Logger.getLogger(JMSManager.class
			.getName());

	private static final int CONNECTION_TIMEOUT = 15000;

	private JMSListenerRunnable listenerRunnable = null;

	protected class JMSContext {
		private ConnectionFactory connectionFactory = null;
		private Queue queue = null;
		private Connection connection = null;
		private Session session = null;
		private MessageProducer producer = null;
	}

	protected class JMSListenerRunnable implements Runnable {

		private JMSContext context = null;
		private AAsyncMessageListener listener = null;
		private boolean isAlive = true;

		protected JMSListenerRunnable(JMSContext context,
				AAsyncMessageListener listener) {
			this.context = context;
			this.listener = listener;
		}

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
			if (context != null) {
				if (context.session != null) {
					context.session.close();
					context.session = null;
				}

				if (context.connection != null) {
					context.connection.close();
					context.connection = null;
				}
			}
		}

		@Override
		public void run() {
			do {
				try {
					try {
						context = initConnection(context);
					} catch (AsyncMessageException e1) {
						LOGGER.severe("Cannot init connection.");
						continue;
					}
					MessageConsumer consumer = null;

					LOGGER.fine("Creating message consumer...");
					try {
						consumer = context.session
								.createConsumer(context.queue);
						LOGGER.fine("JMSMessage consumer created.");
					} catch (JMSException e) {
						LOGGER.severe("Failed to create message consumer.");
					}

					LOGGER.fine("Setting message listener...");
					try {
						consumer.setMessageListener(listener);
						LOGGER.fine("JMSMessage listener set");
					} catch (JMSException e) {
						LOGGER.severe("Failed to set message listener.");
					}

					LOGGER.fine("Starting connection...");
					try {
						context.connection.start();
						LOGGER.fine("Connection started.");
					} catch (JMSException e) {
						LOGGER.severe("Failed to start connection.");
					}

					try {
						Thread.sleep(CONNECTION_TIMEOUT);
					} catch (InterruptedException e) {
					}

				} finally {
					try {
						if (context.session != null) {
							context.session.close();
						}
						if (context.connection != null) {
							context.connection.close();
						}
					} catch (JMSException e) {
					}
					context.connection = null;
					context.session = null;
				}
			} while (isAlive);
			try {
				if (context.session != null) {
					context.session.close();
				}
				if (context.connection != null) {
					context.connection.close();
				}
			} catch (JMSException e) {
			}
			context.connection = null;
			context.session = null;
		}

		public boolean isAlive() {
			return isAlive;
		}

		public void setAlive(boolean isAlive) {
			this.isAlive = isAlive;
		}
	}

	/**
	 * Initialise le bazar JMS
	 * 
	 * @throws AsyncMessageException
	 */
	private JMSContext init(EApplication application)
			throws AsyncMessageException {
		JMSContext context = new JMSContext();
		Context jndiContext = null;
		String connectionFactoryURL = null;
		String initialContextFactory = null;
		String providerURL = null;

		try {
			providerURL = Conf.getInstance()
					.getJMSProviderURL();
			initialContextFactory = Conf.getInstance()
					.getJMSInitialContextFactory();
			connectionFactoryURL = Conf.getInstance()
					.getJMSConnectionFactoryURL();
		} catch (ConfigurationException e1) {
			LOGGER.severe("Failed to load configution.");
			throw new AsyncMessageException("Failed to load configution.", e1);
		}

		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		env.put(Context.PROVIDER_URL, providerURL);

		String queueName = "queue/" + application.getShortName();
		LOGGER.info("Using queue : " + queueName);

		if (jndiContext == null) {
			LOGGER.fine("The context is null, initializing context...");
			try {
				jndiContext = new InitialContext(env);
			} catch (NamingException e) {
				LOGGER.severe("Failed to initialize context");
				throw new AsyncMessageException("Failed to initialize context",
						e);
			}
			LOGGER.fine("Context initialized.");
		}

		try {
			LOGGER.fine("Initializing connection factory...");
			context.connectionFactory = (ConnectionFactory) jndiContext
					.lookup(connectionFactoryURL);
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
		LOGGER.fine("QueueConnectionFactory initialized");

		try {
			LOGGER.fine("Retrieving queue " + queueName
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
		LOGGER.fine("Queue retrieved.");

		try {
			jndiContext.close();
		} catch (NamingException ne) {
		}
		jndiContext = null;

		return context;
	}

	protected JMSContext initConnection(JMSContext context)
			throws AsyncMessageException {
		try {
			LOGGER.fine("Establishing connection...");
			context.connection = context.connectionFactory.createConnection();
		} catch (JMSException e) {
			LOGGER.severe("Failed to establish connection to the queue.");
			throw new AsyncMessageException(
					"Failed to establish connection to the queue.", e);
		}
		LOGGER.fine("Connection established.");

		try {
			LOGGER.fine("Creating session...");
			context.session = context.connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			LOGGER.severe("Failed to create queue session.");
			close(context);
			throw new AsyncMessageException("Failed to create session.", e);
		}
		LOGGER.fine("Session sucessfully created.");
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
		context = initConnection(context);

		JMSMessage mes = new JMSMessage(Conf.getInstance()
				.getCurrentApplication());
		mes.setDestination(destination);
		mes.setText(message);
		ObjectMessage objectMessage = null;

		try {
			LOGGER.fine("Creating producer...");
			try {
				context.producer = context.session
						.createProducer(context.queue);
			} catch (JMSException e) {
				LOGGER.severe("Failed to create producer.");
				throw new AsyncMessageException("Failed to create producer.", e);
			}
			LOGGER.fine("Producer intialized.");

			try {
				LOGGER.fine("Creating object message...");
				objectMessage = context.session.createObjectMessage(mes);
			} catch (JMSException e) {
				LOGGER.severe("Failed to create object message.");
				throw new AsyncMessageException(
						"Failed to create text message.", e);
			}
			LOGGER.fine("ObjectMessage created.");

			try {
				LOGGER.info("Sending message to " + destination.toString());
				context.producer.send(objectMessage);
				LOGGER.info("JMSMessage sent.");
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
	public void initListener(AAsyncMessageListener listener)
			throws AsyncMessageException {
		JMSContext context = init(Conf.getInstance()
				.getCurrentApplication());

		listenerRunnable = new JMSListenerRunnable(context, listener);
		Thread thread = new Thread(listenerRunnable);

		thread.start();
	}

	@Override
	public void stopListener() {
		if (listenerRunnable != null && listenerRunnable.isAlive()) {
			listenerRunnable.setAlive(false);
		}
	}

	@Override
	public void notify(AsyncFileMessage message) throws AsyncMessageException {
		JMSContext context = init(message.getDestination());
		context = initConnection(context);

		ObjectMessage objectMessage = null;

		try {
			LOGGER.fine("Creating producer...");
			try {
				context.producer = context.session
						.createProducer(context.queue);
			} catch (JMSException e) {
				LOGGER.severe("Failed to create producer.");
				throw new AsyncMessageException("Failed to create producer.", e);
			}
			LOGGER.fine("Producer intialized.");

			try {
				LOGGER.fine("Creating object message...");
				objectMessage = context.session.createObjectMessage(message);
			} catch (JMSException e) {
				LOGGER.severe("Failed to create object message.");
				throw new AsyncMessageException(
						"Failed to create object message.", e);
			}
			LOGGER.fine("ObjectMessage created.");

			try {
				LOGGER.info("Sending message to "
						+ message.getDestination().toString());
				context.producer.send(objectMessage);
				LOGGER.info("JMSMessage sent.");
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
