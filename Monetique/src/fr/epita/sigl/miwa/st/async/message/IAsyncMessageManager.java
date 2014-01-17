package fr.epita.sigl.miwa.st.async.message;

import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public interface IAsyncMessageManager {

	/**
	 * Envoi le message à l'application
	 * 
	 * @param message
	 *            le message à envoyer
	 * @param destination
	 *            l'application à qui envoyer le message
	 * @throws AsyncFileException
	 */
	public void send(String message, EApplication destination)
			throws AsyncMessageException;

	public void initListener(AAsyncMessageListener listener)
			throws AsyncMessageException;
	
	public void stopListener();
}
