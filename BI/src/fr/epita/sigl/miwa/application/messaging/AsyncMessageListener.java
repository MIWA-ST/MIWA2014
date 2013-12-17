package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	@Override
	public void onException(Exception e) {
		LOGGER.severe("Erreur : " + e);		
	}

	@Override
	public void onMessage(String message, EApplication source) {
		// Message du CRM (demande de segmentation avec critères)
		if (source == EApplication.CRM){
			LOGGER.info("Message reçu du CRM");
			LOGGER.info("Le message est : " + message);
			// Envoi du fichier de segmentation au CRM suite à leur demande
			try {
				String fileName = "segmentation-client.xml";
				AsyncFileFactory.getInstance().getFileManager().send(fileName, EApplication.CRM);
				LOGGER.info("Fichier envoyé au CRM");
				LOGGER.info("Nom du fichier : " + fileName); 
			} catch (AsyncFileException e) {
				LOGGER.severe("Erreur pendant l'envoi du fichier au CRM");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		// Message de la GC (stock)
		else if (source == EApplication.GESTION_COMMERCIALE){
			LOGGER.info("Message reçu de la GC");
			LOGGER.info("Le message est : " + message);
		}
		// Message du BO (stock ou vente (toutes les 15min) ou promo locales)
		else if (source == EApplication.BACK_OFFICE){
			LOGGER.info("Message reçu du BO");
			LOGGER.info("Le message est : " + message);
		}
		// Message de l'internet (vente (toutes les 15min))
		else if (source == EApplication.INTERNET){
			LOGGER.info("Message reçu d'Internet");
			LOGGER.info("Le message est : " + message);
		}
		// Message pas attendu
		else {
			LOGGER.info("Message qui ne nous intéresse pas de " + source);
			LOGGER.info("Le message est : " + message);
		}
	}

	@Override
	public void onFile(File file, EApplication source) {
		// Fichier du CRM pour informations clients
		if (source == EApplication.CRM){
			LOGGER.info("Fichier reçu du CRM");
			LOGGER.info("Le path du fichier est : " + file.getAbsolutePath());
		}
		// Fichier du référentiel (liste produits)
		else if (source == EApplication.MDM){
			LOGGER.info("Fichier reçu du référentiel");
			LOGGER.info("Le path du fichier est : " + file.getAbsolutePath());
		}
		// Fichier du BO (ventes détaillées)
		else if (source == EApplication.BACK_OFFICE){
			LOGGER.info("Fichier reçu du BO");
			LOGGER.info("Le path du fichier est : " + file.getAbsolutePath());
		}
		// Fichier d'Internet (ventes détaillées)
		else if (source == EApplication.INTERNET){
			LOGGER.info("Fichier reçu d'Internet");
			LOGGER.info("Le path du fichier est : " + file.getAbsolutePath());
		}
		// Fichier non attendu
		else {
			LOGGER.severe("Fichier non attendu de " + source);
			LOGGER.severe("Le path du fichier est : " + file.getAbsolutePath());
		}
	}
}
