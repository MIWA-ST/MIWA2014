package fr.epita.sigl.miwa.st.async.file;

import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public interface IAsyncFileNotifier {
	public void notify(AsyncFileMessage message) throws AsyncMessageException;
}
