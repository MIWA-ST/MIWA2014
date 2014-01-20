package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.controller.BIController;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	private BIController controller = BIController.getInstance();

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
				String filename = controller.generateSegmentation(message);
				AsyncFileFactory.getInstance().getFileManager().send(filename, EApplication.CRM);
				LOGGER.info("Fichier " + filename + "envoyé au CRM");
			} catch (AsyncFileException e) {
				LOGGER.severe("Erreur pendant l'envoi du fichier au CRM");
				LOGGER.severe("L'erreur est : " + e);
			} catch (Exception e){
				LOGGER.severe("Erreur pendant la segmentation client");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		// Message de la GC (stock)
		else if (source == EApplication.GESTION_COMMERCIALE){
			LOGGER.info("Message reçu de la GC");
			try{
				controller.parseGCMessage(message);
			} catch (Exception e){
				LOGGER.severe("Erreur pendant le parsing du message de la GC");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		// Message du BO (stock ou vente (toutes les 15min) ou promo locales)
		else if (source == EApplication.BACK_OFFICE){
			LOGGER.info("Message reçu du BO");
			try{
				controller.parseBOMessage(message);
			} catch (Exception e){
				LOGGER.severe("Erreur pendant le parsing du message du BO");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		// Message de l'internet (vente (toutes les 15min))
		else if (source == EApplication.INTERNET){
			LOGGER.info("Message reçu d'Internet");
			try {
				controller.parseInternetMessage(message);
			} catch (Exception e){
				LOGGER.severe("Erreur pendant le parsing du message de l'Internet");
				LOGGER.severe("L'erreur est : " + e);
			}
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
			try {
				controller.parseCRMFile(file);
			} catch (Exception e){
				LOGGER.severe("Erreur de parsing du fichier du CRM");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		// Fichier du référentiel (liste produits)
		else if (source == EApplication.MDM){
			LOGGER.info("Fichier reçu du référentiel");
			try {
				controller.parseMDMFile(file);
			} catch (Exception e){
				LOGGER.severe("Erreur de parsing du fichier du MDM");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		// Fichier du BO (ventes détaillées)
		else if (source == EApplication.BACK_OFFICE){
			LOGGER.info("Fichier reçu du BO");
			try {
				controller.parseBOFile(file);
			} catch (Exception e){
				LOGGER.severe("Erreur de parsing du fichier du BO");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		// Fichier d'Internet (ventes détaillées)
		else if (source == EApplication.INTERNET){
			LOGGER.info("Fichier reçu d'Internet");
			try {
				controller.parseInternetFile(file);
			} catch (Exception e){
				LOGGER.severe("Erreur de parsing du fichier de l'Internet");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		// Fichier non attendu
		else {
			LOGGER.severe("Fichier non attendu de " + source);
			LOGGER.severe("Le path du fichier est : " + file.getAbsolutePath());
		}
	}
}
