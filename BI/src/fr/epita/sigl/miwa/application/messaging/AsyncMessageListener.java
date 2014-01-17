package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.controller.BIController;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	private BIController controller = new BIController();
	
	@Override
	public void onException(Exception e) {
		LOGGER.severe("Erreur : " + e);		
	}

	@Override
	public void onMessage(String message, EApplication source) {
		// Message du CRM (demande de segmentation avec critères)
		if (source == EApplication.CRM){
			LOGGER.info("Message reçu du CRM");
			// Envoi du fichier de segmentation au CRM suite à leur demande
			try {
				// A décommenter quand ça marchera
				//String filename = controller.generateSegmentation(message);
				String filename = "/segmentation-client.xml";
				AsyncFileFactory.getInstance().getFileManager().send(filename, EApplication.CRM);
				LOGGER.info("Fichier " + filename + "envoyé au CRM");
			} catch (AsyncFileException e) {
				LOGGER.severe("Erreur pendant l'envoi du fichier au CRM");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		// Message de la GC (stock)
		else if (source == EApplication.GESTION_COMMERCIALE){
			LOGGER.info("Message reçu de la GC");
			controller.parseGCMessage(message);
		}
		// Message du BO (stock ou vente (toutes les 15min) ou promo locales)
		else if (source == EApplication.BACK_OFFICE){
			LOGGER.info("Message reçu du BO");
			controller.parseBOMessage(message);
		}
		// Message de l'internet (vente (toutes les 15min))
		else if (source == EApplication.INTERNET){
			LOGGER.info("Message reçu d'Internet");
			controller.parseInternetMessage(message);
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
			controller.parseCRMFile(file);
		}
		// Fichier du référentiel (liste produits)
		else if (source == EApplication.MDM){
			LOGGER.info("Fichier reçu du référentiel");
			controller.parseMDMFile(file);
		}
		// Fichier du BO (ventes détaillées)
		else if (source == EApplication.BACK_OFFICE){
			LOGGER.info("Fichier reçu du BO");
			controller.parseBOFile(file);
		}
		// Fichier d'Internet (ventes détaillées)
		else if (source == EApplication.INTERNET){
			LOGGER.info("Fichier reçu d'Internet");
			controller.parseInternetFile(file);
		}
		// Fichier non attendu
		else {
			LOGGER.severe("Fichier non attendu de " + source);
			LOGGER.severe("Le path du fichier est : " + file.getAbsolutePath());
		}
	}
}
