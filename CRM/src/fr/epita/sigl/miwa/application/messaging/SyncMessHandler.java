package fr.epita.sigl.miwa.application.messaging;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.XMLManager;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.sync.ISyncMessSender;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class SyncMessHandler {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	
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
	static public boolean receiveMessage(EApplication sender, String message) throws SAXException, IOException, AsyncMessageException, ParseException {
		
		if (sender == EApplication.CAISSE)
		{
			LOGGER.info("Message synchrone reçu de la caisse :" + message);
		}
		else if (sender == EApplication.INTERNET)
		{
			// Demande d'information client
			LOGGER.info("Message synchrone reçu d'internet :" + message);
			
			//TODO : envoyer les infos client demandées à internet
		}
		else if (sender == EApplication.MONETIQUE)
		{
			// Validation création / MAJ / suppression compte après avoir fait la demande
			LOGGER.info("Message synchrone reçu de la monétique :" + message);
		}
		else
		{
			LOGGER.info("Mesasage synchrone reçu de : " + sender.toString() + " non traité.");
		}
		XMLManager.getInstance().dispatchXML("Traitement", message);
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
		
		if (sender == EApplication.CAISSE)
		{
			LOGGER.info("Request synchrone reçue de la caisse :" + request);
		} 
		else if (sender == EApplication.INTERNET)
		{
			LOGGER.info("Request synchrone reçue d'internet :" + request);
		}
		else if (sender == EApplication.MONETIQUE)
		{
			LOGGER.info("Request synchrone reçue de la monétique :" + request);
		}
		else
		{
			LOGGER.info("Request synchrone reçu de : " + sender.toString() + " non traitée.");
		}
		
		return null;
	}
	
	/*
	* L'application sender vous envoie le XML xml
	* Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	* Elle est automatiquement appel�e lorsqu'une application vous contacte
	*/
	@Deprecated
	static public boolean receiveXML(EApplication sender, Document xml){
		// TODO Auto-generated method stub
		
		if (sender == EApplication.CAISSE)
		{
			LOGGER.info("XML synchrone reçu de la caisse :" + xml.getDocumentURI());
		}
		else if (sender == EApplication.INTERNET)
		{
			LOGGER.info("XML synchrone reçu d'internet :" + xml.getDocumentURI());
		}
		else if (sender == EApplication.MONETIQUE)
		{
			LOGGER.info("XML synchrone reçu de la monétique :" + xml.getDocumentURI());
		}
		else
		{
			LOGGER.info("XML synchrone reçu de : " + sender.toString() + " non utilisé.");
		}
		
		return false;
	}

	/*
	 * L'application sender vous demande un XML avec la requete request
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public Document answerToRequestXML(EApplication sender, String request){
		// TODO Auto-generated method stub

		if (sender == EApplication.CAISSE)
		{
			LOGGER.info("Demande XML et request synchrone reçus de la caisse :" + request);
		}
		else if (sender == EApplication.INTERNET)
		{
			LOGGER.info("Demande XML et request synchrone reçus d'internet :" + request);
		}
		else if (sender == EApplication.MONETIQUE)
		{
			LOGGER.info("Demande XML et request synchrone reçus de la monétique :" + request);
		}
		else
		{
			LOGGER.info("Demande XML non traitée. Reçue de : " + sender.toString());
		}
		return null;
	}
	
	private SyncMessHandler() {
		
	}
}
