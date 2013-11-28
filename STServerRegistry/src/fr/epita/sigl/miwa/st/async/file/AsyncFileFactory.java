package fr.epita.sigl.miwa.st.async.file;

import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.EApplication;

public class AsyncFileFactory {

	private static final Logger log = Logger.getLogger(AsyncFileFactory.class
			.getName());

	private boolean isLocal = true;

	public AsyncFileFactory() {
		// TODO get appropriate property
		isLocal = true;
		log.info("Using configuration : " + (isLocal ? "local" : "remote"));
	}

	public AsyncFileHelper getFileHelper() {
		AsyncFileHelper manager = null;
		if (isLocal) {
			manager =  new LocalFileHelper();
		} else {
			manager = new FTPHelper();
		}
		log.info("Return file manager : " + manager.getClass().getName());
		return manager;
	}
}
