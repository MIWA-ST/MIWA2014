package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.controller.BIController;
import fr.epita.sigl.miwa.application.criteres.Critere;
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
		Date date = ClockClient.getClock().getHour();
		if (source == EApplication.CRM){
			LOGGER.info("***** Message reçu du CRM à " + date);
			LOGGER.info("***** Message is : " + message);
			// Envoi du fichier de segmentation au CRM suite à leur demande
			try {
				List<Critere> criteres = controller.parseCRMMessage(message);
				LOGGER.info("***** Message bien parsé. Données insérées en base");
				String filename = controller.generateSegmentation(criteres);
				AsyncFileFactory.getInstance().getFileManager().send(filename, EApplication.CRM);
				LOGGER.info("***** Fichier " + filename + "envoyé au CRM");
			} catch (AsyncFileException e) {
				LOGGER.severe("***** Erreur pendant l'envoi du fichier au CRM");
				LOGGER.severe("***** L'erreur est : " + e);
			} catch (Exception e){
				LOGGER.severe("***** Erreur pendant la segmentation client");
				LOGGER.severe("***** L'erreur est : " + e);
			}
		}
		// Message de la GC (stock)
		else if (source == EApplication.GESTION_COMMERCIALE){
			LOGGER.info("***** Message reçu de la GC à " + date);
			LOGGER.info("***** Message is : " + message);
			try{
				controller.parseGCMessage(message);
				LOGGER.info("***** Message bien parsé. Données insérées en base");
			} catch (Exception e){
				LOGGER.severe("***** Erreur pendant le parsing du message de la GC");
				LOGGER.severe("***** L'erreur est : " + e);
			}
		}
		// Message du BO (stock ou vente (toutes les 15min) ou promo locales)
		else if (source == EApplication.BACK_OFFICE){
			LOGGER.info("***** Message reçu du BO à " + date);
			LOGGER.info("***** Message is : " + message);
			try{
				controller.parseBOMessage(message);
				LOGGER.info("***** Message bien parsé. Données insérées en base");
			} catch (Exception e){
				LOGGER.severe("***** Erreur pendant le parsing du message du BO");
				LOGGER.severe("***** L'erreur est : " + e);
			}
		}
		// Message de l'internet (vente (toutes les 15min))
		else if (source == EApplication.INTERNET){
			LOGGER.info("***** Message reçu d'Internet");
			LOGGER.info("Message is : " + message);
			try {
				controller.parseInternetMessage(message);
				LOGGER.info("Message bien parsé. Données insérées en base");
			} catch (Exception e){
				LOGGER.severe("***** Erreur pendant le parsing du message de l'Internet");
				LOGGER.severe("***** L'erreur est : " + e);
			}
		}
		// Message pas attendu
		else {
			LOGGER.info("***** Message qui ne nous intéresse pas de " + source);
			LOGGER.info("***** Le message est : " + message);
		}
	}

	@Override
	public void onFile(File file, EApplication source) {
		Date date = ClockClient.getClock().getHour();
		// Fichier du CRM pour informations clients
		if (source == EApplication.CRM){
			LOGGER.info("***** Fichier " + file.getAbsolutePath() + " reçu du CRM à " + date);
			try {
				controller.parseCRMFile(file);
				LOGGER.info("***** Fichier bien parsé");
			} catch (Exception e){
				LOGGER.severe("***** Erreur de parsing du fichier du CRM");
				LOGGER.severe("***** L'erreur est : " + e);
			}
		}
		// Fichier du référentiel (liste produits)
		else if (source == EApplication.MDM){
			LOGGER.info("***** Fichier " + file.getAbsolutePath() + " reçu du référentiel");
			try {
				controller.parseMDMFile(file);
				LOGGER.info("***** Fichier bien parsé");
			} catch (Exception e){
				LOGGER.severe("***** Erreur de parsing du fichier du MDM");
				LOGGER.severe("***** L'erreur est : " + e);
			}
		}
		// Fichier du BO (ventes détaillées)
		else if (source == EApplication.BACK_OFFICE){
			LOGGER.info("***** Fichier " + file.getAbsolutePath() + " reçu du BO à " + date);
			try {
				controller.parseBOFile(file);
				LOGGER.info("***** Fichier bien parsé");
			} catch (Exception e){
				LOGGER.severe("***** Erreur de parsing du fichier du BO");
				LOGGER.severe("***** L'erreur est : " + e);
			}
		}
		// Fichier d'Internet (ventes détaillées)
		else if (source == EApplication.INTERNET){
			LOGGER.info("***** Fichier " + file.getAbsolutePath() + " reçu d'Internet à " + date);
			try {
				controller.parseInternetFile(file);
				LOGGER.info("***** Fichier bien parsé");
			} catch (Exception e){
				LOGGER.severe("***** Erreur de parsing du fichier de l'Internet");
				LOGGER.severe("***** L'erreur est : " + e);
			}
		}
		// Fichier non attendu
		else {
			LOGGER.severe("***** Fichier non attendu de " + source);
			LOGGER.severe("***** Le path du fichier est : " + file.getAbsolutePath());
		}
	}
}
