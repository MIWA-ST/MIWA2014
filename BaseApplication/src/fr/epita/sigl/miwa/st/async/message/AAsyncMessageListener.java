package fr.epita.sigl.miwa.st.async.message;

import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public abstract class AAsyncMessageListener implements MessageListener {

	private static final Logger LOGGER = Logger.getLogger(AAsyncMessageListener.class.getName());
	
	@Override
	public void onMessage(Message message) {
		String str = null;
		
		LOGGER.info("Receiving a message...");
		if (message instanceof TextMessage) {
			LOGGER.info("TextMessage detected");
			TextMessage text = (TextMessage) message;
			try {
				str = text.getText();
			} catch (JMSException e) {
				LOGGER.severe("Failed to get text from text message");
				return;
			}
		} else {
			LOGGER.severe("Does not reconize the message type : " + message.getClass().getName());
			return;
		}
		try {
			message.acknowledge();
			LOGGER.info("Message correctly open, aknowledge reception.");
		} catch (JMSException e) {
			LOGGER.severe("Failed to aknowledge reception of the message.");
			return;
		}
		
		LOGGER.info("Giving message to listener.");
		onMessage(str);
	}

	public abstract void onMessage(String message);
}
