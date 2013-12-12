package fr.epita.sigl.miwa.st.async.message;

import fr.epita.sigl.miwa.st.EApplication;

public interface IAsyncMessageReceiver {

	public void addListener(AAsyncMessageListener listener, EApplication application)
			throws AsyncMessageException;
}
