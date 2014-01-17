package fr.epita.sigl.miwa.application.messaging;

import java.util.Random;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.sync.ISyncMessSender;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class SyncMessHandler {

	//Init logger
	private static final Logger LOGGER = Logger.getLogger(SyncMessHandler.class.getName());
	
	/* 
	 * A utiliser pour pouvoir envoyer des messages synchrones ou faire des request
	 */
	static public ISyncMessSender getSyncMessSender() {
		return SyncMessFactory.getSyncMessSender();
	}

	/*
	 * l'application sender vous envoie la string message.
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public boolean receiveMessage(EApplication sender, String message) {
		return false;
	}

	/*
	 * L'application sender vous demande request
	 * Vous devez lui renvoyer une string
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public String answerToRequestMessage(EApplication sender, String request){
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	* L'application sender vous envoie le XML xml
	* Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	* Elle est automatiquement appel�e lorsqu'une application vous contacte
	*/
	@Deprecated
	static public boolean receiveXML(EApplication sender, Document xml){		
		LOGGER.info("***** Recepting message.");
		xml.getDocumentElement().normalize();
		LOGGER.info("***** Getting message informations.");
		String serviceToPerform = xml.getDocumentElement().getAttribute("service");
		String actionToPerform = xml.getDocumentElement().getAttribute("action");
		LOGGER.info("***** Message received.");
		
		LOGGER.info("***** Decoding message");
		LOGGER.info("***** Retrieving service.");
		if (serviceToPerform.equals("paiement_cb"))
		{
			LOGGER.info("***** Paiement by CB service started.");
			Boolean bankResponse = getRandomPaiement7030motherfucker();
			LOGGER.info("***** Paiement by CB service terminated normally with : " + bankResponse + ".");
			LOGGER.info("***** Sending response.");
			return bankResponse;
		}
		else if (serviceToPerform.equals("paiement_cf"))
		{
			LOGGER.info("***** Paiement by fidelity service started.");
			Boolean bankResponse = getRandomPaiement7030motherfucker();
			LOGGER.info("***** Paiement by fidelity service terminated normally with : " + bankResponse + ".");
			LOGGER.info("***** Sending response.");
			return bankResponse;
		}
		else if (serviceToPerform.equals("cms_type_carte"))
		{
			LOGGER.info("***** Carte type service started.");
			if (actionToPerform.equals("c"))
			{
				LOGGER.info("***** Create a new card.");
				return true;
			}
			else if (actionToPerform.equals("m"))
			{
				LOGGER.info("***** Modify a card.");
				return true;
			}
			else if (actionToPerform.equals("s"))
			{
				LOGGER.info("***** Delete a card.");
				return true;
			}
			else
			{
				LOGGER.severe("***** A fatal error occured when processing Carte type service.");
				return false;
			}
		}
		else if (serviceToPerform.equals("cms_compte_cf"))
		{
			LOGGER.info("***** Fidelity account service started.");
			if (actionToPerform.equals("c"))
			{
				LOGGER.info("***** Create a new account.");
				return true;
			}
			else if (actionToPerform.equals("m"))
			{
				LOGGER.info("***** Modify an account.");
				return true;
			}
			else if (actionToPerform.equals("s"))
			{
				LOGGER.info("***** Delete an account.");
				return true;
			}
			else
			{
				LOGGER.severe("***** A fatal error occured when processing Fidelity Account service.");
				return false;
			}			
		}
		else
		{
			LOGGER.severe("***** A fatal error occured in the Monetique system.");
			return false;
		}
	}

	private static boolean getRandomPaiement7030motherfucker() 
	{
		LOGGER.info("Reading card information.");
		LOGGER.info("Asking bank");
		LOGGER.info("Processing banking treatment.");
		
		Random rnd = new Random();
		Integer jaimelesfrites = rnd.nextInt(100);
		
		LOGGER.info("Treatment finished.");
		LOGGER.info("Retrieving bank response.");
		return jaimelesfrites < 70;
	}

	/*
	 * L'application sender vous demande un XML avec la requete request
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public Document answerToRequestXML(EApplication sender, String request){
		// TODO Auto-generated method stub
		return null;
	}
	
	private SyncMessHandler() {
		
	}
}
