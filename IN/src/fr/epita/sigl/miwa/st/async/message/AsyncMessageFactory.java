package fr.epita.sigl.miwa.st.async.message;

import fr.epita.sigl.miwa.st.async.file.IAsyncFileNotifier;

public class AsyncMessageFactory {

	private static AsyncMessageFactory instance = null;
	
	private IAsyncMessageManager manager = null;
	private IAsyncFileNotifier notifier = null;
	
	public static AsyncMessageFactory getInstance() {
		if (instance == null) {
			instance = new AsyncMessageFactory();
		}
		return instance;
	}
	
	private AsyncMessageFactory() {
		
	}
	
	public IAsyncMessageManager getAsyncMessageManager() {
		if (manager == null) {
			manager = new JMSManager();
		}
		return manager;
	}
	
	public IAsyncFileNotifier getAsyncFileNotifier() {
		if (notifier == null) {
			notifier = new JMSManager();
		}
		return notifier;
	}
}
