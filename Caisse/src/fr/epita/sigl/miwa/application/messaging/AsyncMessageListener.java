package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	/*
	liste des messages/fichiers :
	FA - BO vers Caisse (articles, prix et promotions)
	MA - BO vers Caisse (mise à jour des articles, prix et promotions en cours de journée)
	MS - Caisse vers BO (ticket de vente au fil de l’eau)
	FA - Caisse vers BO (tous les tickets de vente en fin de journée)
	*/
	
	@Override
	public void onException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String message, EApplication source) {
		if (source == EApplication.BACK_OFFICE){
			LOGGER.info("Message reçu du Back Office");
			LOGGER.info("Le message est : " + message);
		}
	}

	@Override
	public void onFile(File file, EApplication source) {
		if (source == EApplication.BACK_OFFICE){
			LOGGER.info("Fichier reçu du Back Office");
			LOGGER.info("Le path du fichier est : " + file.getAbsolutePath());
		}
	}

}
