package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	@Override
	public void onException(Exception e) {
		LOGGER.severe("Erreur : " + e);		
	}

	@Override
	public void onMessage(String message, EApplication source) {	
		//Message du Back-office
		if (source == EApplication.BACK_OFFICE){
			LOGGER.info("Message reçu du Back-office");
			LOGGER.info("Le message est : " + message);
		}
		// Message pas attendu
		else {
			LOGGER.info("Message qui ne nous intÃ©resse pas de " + source);
			LOGGER.info("Le message est : " + message);
		}
	}

	@Override
	public void onFile(File file, EApplication source) {
		
		if (source == EApplication.BI){
			// Segmentation client
			LOGGER.info("Fichier reçu du BI");
			LOGGER.info("Le path du fichier est : " + file.getAbsolutePath());
		}	
		// Fichier non attendu
		else {
			LOGGER.severe("Fichier non attendu de " + source);
			LOGGER.severe("Le path du fichier est : " + file.getAbsolutePath());
		}
	}

}
