package fr.epita.sigl.miwa.application.messaging;

import org.jdom2.input.DOMBuilder;
import org.w3c.dom.Document;

import fr.epita.sigl.miwa.application.ParseXML;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.sync.ISyncMessSender;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class SyncMessHandler {
	
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
		// Envoi de message classqiue (XML en String ou messge)
		if (sender == EApplication.CRM)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(message, ParseXML.TYPE_LANGUAGE.STRING))
				return parser.parseCRM();
			else
				return false;
		}
		else if (sender == EApplication.GESTION_COMMERCIALE)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(message, ParseXML.TYPE_LANGUAGE.STRING))
				return parser.parseGC();
			else
				return false;
		}
		else if (sender == EApplication.MDM)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(message, ParseXML.TYPE_LANGUAGE.STRING))
				return parser.parseMDM();
			else
				return false;
		}
		
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
		// Réponse à la request demandée ... Renvoi laString (XML ou info...)
		/*
		else if (sender == EApplication.GESTION_COMMERCIALE)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(message, ParseXML.TYPE_LANGUAGE.STRING))
				return parser.parseGC();
			else
				return false;
		}
		else if (sender == EApplication.MDM)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(message, ParseXML.TYPE_LANGUAGE.STRING))
				return parser.parseMDM();
			else
				return false;
		}
		*/
		
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
		if (sender == EApplication.CRM)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(xml, ParseXML.TYPE_LANGUAGE.DOCUMENT))
				return parser.parseCRM();
			else
				return false;
		}
		else if (sender == EApplication.GESTION_COMMERCIALE)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(xml, ParseXML.TYPE_LANGUAGE.DOCUMENT))
				return parser.parseGC();
			else
				return false;
		}
		else if (sender == EApplication.MDM)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(xml, ParseXML.TYPE_LANGUAGE.DOCUMENT))
				return parser.parseMDM();
			else
				return false;
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
		return null;
	}
	
	private SyncMessHandler() {
		
	}
}
