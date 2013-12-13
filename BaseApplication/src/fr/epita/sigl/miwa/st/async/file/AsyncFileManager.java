package fr.epita.sigl.miwa.st.async.file;

import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.file.helper.IAsyncFileHelper;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class AsyncFileManager implements IAsyncFileManager {
	
	private static final Logger LOGGER = Logger.getLogger(AsyncFileManager.class.getName());

	@Override
	public void send(String filename, EApplication destination)
			throws AsyncFileException {
		AsyncFileFactory factory = new AsyncFileFactory();
		IAsyncFileHelper helper = factory.getFileHelper();
		helper.send(filename, destination);
		
		AsyncMessageFactory messageFactory = new AsyncMessageFactory();
		IAsyncFileNotifier notifier = messageFactory.getAsyncFileNotifier();
		AsyncFileMessage message = new AsyncFileMessage(EApplication.BACK_OFFICE, destination, filename);
		try {
			notifier.notify(message);
		} catch (AsyncMessageException e) {
			LOGGER.severe("Failed to notify the application that we sent a file for it.");
			throw new AsyncFileException("Failed to notify the application that we sent a file for it.", e);
		}
	}

}
