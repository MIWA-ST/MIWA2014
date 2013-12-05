package fr.epita.sigl.miwa.st.async.file;

import java.util.logging.Logger;

/**
 * La factory permettant d'instancier le gestionnaire de fichier asynchrone
 * 
 * @author francois
 * 
 */
public class AsyncFileFactory {

	private static final Logger log = Logger.getLogger(AsyncFileFactory.class
			.getName());

	private boolean isLocal = true;

	public AsyncFileFactory() {
		// TODO get appropriate property
		isLocal = true;
		log.info("Using configuration : " + (isLocal ? "local" : "remote"));
	}

	/**
	 * Retourne le gestionnaire d'envoi et de réception de fichiers asynchrone
	 * 
	 * @return
	 */
	public AsyncFileHelper getFileHelper() {
		AsyncFileHelper manager = null;
		if (isLocal) {
			manager = new LocalFileHelper();
		} else {
			manager = new FTPHelper();
		}
		log.info("Return file manager : " + manager.getClass().getName());
		return manager;
	}
}
