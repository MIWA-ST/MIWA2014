package fr.epita.sigl.miwa.application.messaging;

import java.util.Random;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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
<<<<<<< HEAD
		LOGGER.info("***** Recepting message.");
=======
>>>>>>> 40290d0b2b77203289ded54f69b71132eb3a6936
		xml.getDocumentElement().normalize();
		String serviceToPerform = xml.getDocumentElement().getAttribute("service");
		String actionToPerform = xml.getDocumentElement().getAttribute("action");
		
		if (serviceToPerform.equals("paiement_cb"))
		{
			LOGGER.info("***** Paiement by CB service started.");
			
			String montant = "";
			String[] cb = {"", "", ""};
			
			NodeList nl = xml.getDocumentElement().getChildNodes();
			NodeList cnl = null;
			for (int i = 0; i < nl.getLength(); ++i)
			{
				if (nl.item(i).getNodeName().equals("montant"))
					montant = nl.item(i).getTextContent();
				if (nl.item(i).getNodeName().equals("cb"))
					cnl = nl.item(i).getChildNodes();
			}
			for (int i = 0; i < cnl.getLength(); ++i) 
			{
				if (cnl.item(i).getNodeName().equals("numero"))
					cb[i] = cnl.item(i).getTextContent();
				if (cnl.item(i).getNodeName().equals("date_validite"))
					cb[i] = cnl.item(i).getTextContent();
				if (cnl.item(i).getNodeName().equals("pictogramme"))
					cb[i] = cnl.item(i).getTextContent();
			}
			LOGGER.info("***** REQUEST -> " + montant + "€ for the credit card : " + cb[0]);
			Boolean bankResponse = getBankPaiement();
			LOGGER.info("***** Paiement by CB service terminated normally with : " + bankResponse + ".");
			return bankResponse;
		}
		else if (serviceToPerform.equals("paiement_cf"))
		{
			LOGGER.info("***** Paiement by fidelity service started.");
			Boolean bankResponse = getBankPaiement();
			LOGGER.info("***** Paiement by fidelity service terminated normally with : " + bankResponse + ".");
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

	private static boolean getBankPaiement() 
	{
		LOGGER.info("***** Bank Paiement started.");		
		Random rnd = new Random();
		Integer jaimelesfrites = rnd.nextInt(100);
		
		LOGGER.info("***** Bank Paiement stopped.");
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
