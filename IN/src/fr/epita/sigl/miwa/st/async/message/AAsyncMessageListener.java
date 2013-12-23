package fr.epita.sigl.miwa.st.async.message;

import java.io.File;
import java.util.logging.Logger;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.AsyncFileMessage;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public abstract class AAsyncMessageListener implements MessageListener,
		ExceptionListener {

	private static final Logger LOGGER = Logger
			.getLogger(AAsyncMessageListener.class.getName());

	@Override
	public void onMessage(Message message) {
		JMSMessage jmsMessage = null;

		LOGGER.fine("Receiving a message...");
		if (message instanceof ObjectMessage) {
			LOGGER.fine("ObjectMessage detected");
			ObjectMessage oMessage = (ObjectMessage) message;
			try {
				if (oMessage.getObject() instanceof JMSMessage) {
					jmsMessage = (JMSMessage) oMessage.getObject();
				} else {
					LOGGER.severe("Content of the message not recognized.");
					onException(new AsyncMessageException(
							"Content of the message not recognized"));
					return;
				}

			} catch (JMSException e) {
				LOGGER.severe("Failed to retrieve the content of the message : "
						+ e.getLocalizedMessage());
				onException(new AsyncMessageException(
						"Failed to retrieve the content of the message : "
								+ e.getLocalizedMessage(), e));
				return;
			}
		} else {
			LOGGER.severe("Does not reconize the message type : "
					+ message.getClass().getName());
			onException(new AsyncMessageException(
					"Does not reconize the message type : "
							+ message.getClass().getName()));
			return;
		}
		try {
			message.acknowledge();
			LOGGER.fine("JMSMessage correctly open, aknowledge reception.");
		} catch (JMSException e) {
			LOGGER.severe("Failed to aknowledge reception of the message.");
			onException(new AsyncMessageException(
					"Failed to aknowledge reception of the message."));
			return;
		}

		if (jmsMessage instanceof AsyncFileMessage) {
			LOGGER.fine("A file is available for us.");
			AsyncFileMessage fileMessage = (AsyncFileMessage) jmsMessage;

			File file = null;
			try {
				file = AsyncFileFactory.getInstance().getFileHelper()
						.retrieve(fileMessage.getFilename());
			} catch (AsyncFileException e) {
				LOGGER.severe("Failed to retrieve file.");
				onException(new AsyncMessageException(
						"Failed to retrieve file.", e));
				return;
			}
			LOGGER.info("The file : " + file.getAbsolutePath()
					+ " received from " + jmsMessage.getSource());
			onFile(file, jmsMessage.getSource());
		} else {
			LOGGER.info("Message received, giving message to listener.");
			onMessage(jmsMessage.getText(), jmsMessage.getSource());
		}
	}

	@Override
	public void onException(JMSException exception) {
		LOGGER.severe("Received an exception from the queue : "
				+ exception.getLocalizedMessage());
		onException(exception);
	}

	public abstract void onException(Exception e);

	public abstract void onMessage(String message, EApplication source);

	public abstract void onFile(File file, EApplication source);
}
