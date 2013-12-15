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
import fr.epita.sigl.miwa.st.async.file.helper.IAsyncFileHelper;

public abstract class AAsyncMessageListener implements MessageListener, ExceptionListener {

	private static final Logger LOGGER = Logger
			.getLogger(AAsyncMessageListener.class.getName());

	@Override
	public void onMessage(Message message) {
		JMSMessage jmsMessage = null;

		LOGGER.info("Receiving a message...");
		if (message instanceof ObjectMessage) {
			LOGGER.info("ObjectMessage detected");
			ObjectMessage oMessage = (ObjectMessage) message;
			try {
				if (oMessage.getObject() instanceof JMSMessage) {
					jmsMessage = (JMSMessage) oMessage.getObject();
				} else {
					LOGGER.severe("Content of the message not recognized.");
					return;
				}

			} catch (JMSException e) {
				LOGGER.severe("Failed to retrieve the content of the message : "
						+ e.getLocalizedMessage());
				return;
			}
		} else {
			LOGGER.severe("Does not reconize the message type : "
					+ message.getClass().getName());
			return;
		}
		try {
			message.acknowledge();
			LOGGER.info("JMSMessage correctly open, aknowledge reception.");
		} catch (JMSException e) {
			LOGGER.severe("Failed to aknowledge reception of the message.");
			return;
		}

		if (jmsMessage instanceof AsyncFileMessage) {
			LOGGER.info("A file is available for us.");
			AsyncFileMessage fileMessage = (AsyncFileMessage) jmsMessage;
			IAsyncFileHelper fileHelper = AsyncFileFactory.getInstance()
					.getFileHelper();
			File file = null;
			try {
				file = fileHelper.retrieve(fileMessage.getFilename());
			} catch (AsyncFileException e) {
				LOGGER.severe("Failed to retrieve file.");
				return;
			}
			LOGGER.info("The file : " + file.getAbsolutePath()
					+ " received from " + jmsMessage.getSource());
			onFile(file, jmsMessage.getSource());
		} else {
			LOGGER.info("Giving message to listener.");
			onMessage(jmsMessage.getText(), jmsMessage.getSource());
		}
	}
	
	@Override
	public void onException(JMSException exception) {
		LOGGER.severe("Received an exception from the queue : " + exception.getLocalizedMessage());
		onException(exception);
	}

	public abstract void onException(Exception e);
	
	public abstract void onMessage(String message, EApplication source);

	public abstract void onFile(File file, EApplication source);
}
