package fr.epita.sigl.miwa.application.messaging;

import org.w3c.dom.Document;

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
		xml.getDocumentElement().normalize();
		String serviceToPerform = xml.getDocumentElement().getAttribute("service");
		String actionToPerform = xml.getDocumentElement().getAttribute("action");
		
		if (serviceToPerform.equals("paiement_cb"))
		{
			return true;
		}
		else if (serviceToPerform.equals("paiement_cf"))
		{
			return true;
		}
		else if (serviceToPerform.equals("cms_type_carte"))
		{
			if (actionToPerform.equals("c"))
			{
				return true;
			}
			else if (actionToPerform.equals("m"))
			{
				return true;
			}
			else if (actionToPerform.equals("s"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (serviceToPerform.equals("cms_compte_cf"))
		{
			if (actionToPerform.equals("c"))
			{
				return true;
			}
			else if (actionToPerform.equals("m"))
			{
				return true;
			}
			else if (actionToPerform.equals("s"))
			{
				return true;
			}
			else
			{
				return false;
			}			
		}
		else
		{
			return false;
		}
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
