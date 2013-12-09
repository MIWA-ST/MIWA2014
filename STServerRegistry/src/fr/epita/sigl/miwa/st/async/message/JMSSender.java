package fr.epita.sigl.miwa.st.async.message;

import java.util.logging.Logger;

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

	private static final Logger log = Logger.getLogger(JMSSender.class
			.getName());

	/**
	 * Le contexte JNDI
	 */
	private Context jndiContext = null;
	/**
	 * Le nom de la queue JMS
	 */
	private String queueName = null;

	/**
	 * Initialise le context JNDI, récupère le nom de la queue et la ConnectionFactory
	 * @throws AsyncMessageException
	 */
	private void init() throws AsyncMessageException {
		if (queueName == null || queueName == "") {
			// TODO get queue name from ConfigurationManager
			log.info("Retrieving queue name...");
			queueName = "test";
			log.info("Queue name is : " + queueName);
		}
		if (jndiContext == null) {
			log.info("The context is null, initializing context...");
			try {
				jndiContext = new InitialContext();
			} catch (NamingException e) {
				log.severe("Failed to initialize context");
				throw new AsyncMessageException("Failed to initialize context",
						e);
			}
			log.info("Context initialized.");
		}
		
	}

	@Override
	public void send(String message, EApplication destination)
			throws AsyncMessageException {

	}

}
