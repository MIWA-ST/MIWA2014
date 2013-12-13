package fr.epita.sigl.miwa.st.async.message;

import fr.epita.sigl.miwa.st.async.file.IAsyncFileNotifier;

public class AsyncMessageFactory {

	public IAsyncMessageManager getAsyncMessageManager() {
		return new JMSManager();
	}
	
	public IAsyncFileNotifier getAsyncFileNotifier() {
		return new JMSManager();
	}
}
