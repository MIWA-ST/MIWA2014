package fr.epita.sigl.miwa.st.async.file;

import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.ConfigurationContainer;
import fr.epita.sigl.miwa.st.ConfigurationException;

/**
 * La factory permettant d'instancier le gestionnaire de fichier asynchrone
 * 
 * @author francois
 * 
 */
public class AsyncFileFactory {

	private static final Logger LOGGER = Logger.getLogger(AsyncFileFactory.class
			.getName());

	private boolean isLocal = true;
	private static AsyncFileFactory instance = null;
	
	public static AsyncFileFactory getInstance() {
		if (instance == null) {
			instance = new AsyncFileFactory();
		}
		return instance;
	}
	
	private AsyncFileFactory() {
		try {
			isLocal = ConfigurationContainer.getInstance().isLocal();
		} catch (ConfigurationException e) {
			LOGGER.severe("Could not determine with we are working with localhost.");
		}
		LOGGER.info("Using configuration : " + (isLocal ? "local" : "remote"));
	}

	/**
	 * Retourne le gestionnaire d'envoi et de réception de fichiers asynchrone
	 * 
	 * @return
	 */
	public IAsyncFileHelper getFileHelper() {
		IAsyncFileHelper manager = null;
		if (isLocal) {
			manager = new LocalFileHelper();
		} else {
			manager = new FTPHelper();
		}
		LOGGER.info("Return file helper : " + manager.getClass().getName());
		return manager;
	}
	
	public IAsyncFileManager getFileManager() {
		return new AsyncFileManager();
	}
}
