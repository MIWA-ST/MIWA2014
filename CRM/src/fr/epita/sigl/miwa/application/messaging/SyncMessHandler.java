package fr.epita.sigl.miwa.application.messaging;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
	//private XMLManager manager = new 
	
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
		try {
		if (sender == EApplication.CAISSE)
		{
			LOGGER.info("*****Message synchrone reçu de la caisse :" + message);

			PrintWriter out = null;
			try {
				out = new PrintWriter("caisse.xml");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.print(message);
			out.close();
			
			try {
				XMLManager.getInstance().dispatchXML("", "caisse.xml");
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (sender == EApplication.INTERNET)
		{
			// Demande d'information client
			LOGGER.info("*****Message synchrone reçu d'internet :" + message);
			
			PrintWriter out = null;
			try {
				out = new PrintWriter("internet.xml");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.print(message);
			out.close();
			
			LOGGER.info("*****Fichier crée");
			
			try {
				XMLManager.getInstance().dispatchXML("", "internet.xml");
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			LOGGER.info("*****Envoi des information client à Internet");
			//SyncMessHandler.getSyncMessSender().sendMessage(EApplication.INTERNET, res);
			
			//TODO : envoyer les infos client demandées à internet
		}
		else if (sender == EApplication.MONETIQUE)
		{
			// Validation création / MAJ / suppression compte après avoir fait la demande
			LOGGER.info("*****Message synchrone reçu de la monétique :" + message);
			
			PrintWriter out = null;
			try {
				out = new PrintWriter("monetique.xml");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.print(message);
			out.close();
			
			try {
				XMLManager.getInstance().dispatchXML("", "monetique");
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		else
		{
			LOGGER.info("Mesasage synchrone reçu de : " + sender.toString() + " non traité.");
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//XMLManager.getInstance().dispatchXML("Traitement", message);
		return false;
	}

	/*
	 * L'application sender vous demande request
	 * Vous devez lui renvoyer une string
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public String answerToRequestMessage(EApplication sender, String request) {
		// TODO Auto-generated method stub
		try{
		if (sender == EApplication.CAISSE)
		{
			LOGGER.info("*****XML synchrone reçu de la caisse :"); // + xml.getDocumentURI());
			
			PrintWriter out = null;
			try {
				out = new PrintWriter("caisse.xml");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.print(request);
			out.close();
			
			try {
				XMLManager.getInstance().dispatchXML("", "caisse.xml");
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if (sender == EApplication.INTERNET)
		{
			LOGGER.info("*****XML synchrone reçu d'internet :"); // + xml.getDocumentURI());
			// Demande d'information client
			
			PrintWriter out = null;
			try {
				out = new PrintWriter("internet.xml");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.print(request);
			out.close();
			
			//LOGGER.info("*****Fichier crée");
			
			try {
				XMLManager.getInstance().dispatchXML("", "internet.xml");
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//LOGGER.info("*****Envoi des information client à Internet");
		}
		else if (sender == EApplication.MONETIQUE)
		{
			LOGGER.info("*****XML synchrone reçu de la monétique :"); // + xml.getDocumentURI());
			PrintWriter out = null;
			try {
				out = new PrintWriter("monetique.xml");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.print(request);
			out.close();
			
			try {
				XMLManager.getInstance().dispatchXML("", "monetique");
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		else
		{
			LOGGER.info("*****Request  synchrone non géré reçu de : " + sender.toString() + " non utilisé.");
		}
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
			LOGGER.info("*****XML synchrone reçu de la caisse :" + xml.getDocumentURI());
			try {
				XMLManager.getInstance().dispatchXML("", xml.getDocumentURI());
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if (sender == EApplication.INTERNET)
		{
			
			LOGGER.info("*****XML synchrone reçu d'internet :" + xml.getDocumentURI());
			String res = null;
			
			try {
				XMLManager.getInstance().dispatchXML("", xml.getDocumentURI());
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LOGGER.info("*****Envoi des information client à Internet");
			SyncMessHandler.getSyncMessSender().sendMessage(EApplication.INTERNET, res);
		}
		else if (sender == EApplication.MONETIQUE)
		{
			LOGGER.info("*****XML synchrone reçu de la monétique :" + xml.getDocumentURI());
			String res = null;
			
			try {
				res = XMLManager.getInstance().dispatchXML("", xml.getDocumentURI());
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
		}
		else
		{
			LOGGER.info("*****XML synchrone reçu de : " + sender.toString() + " non utilisé.");
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
