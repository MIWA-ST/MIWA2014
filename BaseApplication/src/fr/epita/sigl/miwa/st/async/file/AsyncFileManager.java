package fr.epita.sigl.miwa.st.async.file;

import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

class AsyncFileManager implements IAsyncFileManager {

	private static final Logger LOGGER = Logger
			.getLogger(AsyncFileManager.class.getName());

	@Override
	public void send(String filename, EApplication destination)
			throws AsyncFileException {
		IAsyncFileHelper helper = AsyncFileFactory.getInstance()
				.getFileHelper();
		helper.send(filename, destination);

		IAsyncFileNotifier notifier = AsyncMessageFactory.getInstance()
				.getAsyncFileNotifier();
		AsyncFileMessage message = new AsyncFileMessage(Conf
				.getInstance().getCurrentApplication(), destination, filename);
		try {
			notifier.notify(message);
		} catch (AsyncMessageException e) {
			LOGGER.severe("Failed to notify the application that we sent a file for it.");
			throw new AsyncFileException(
					"Failed to notify the application that we sent a file for it.",
					e);
		}
	}

}
