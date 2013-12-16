package fr.epita.sigl.miwa.application.messaging;

import java.rmi.RemoteException;

import org.w3c.dom.Document;

import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.ISyncMessHandler;

public class SyncMessHandler implements ISyncMessHandler {

	/*
	 * l'application sender vous envoie la string message.
	 * Vous ne devez faire aucun appel à cette fonction, seulement remplir le code
	 * Elle est automatiquement appelée lorsqu'une application vous contacte
	 */
	@Override
	public boolean receiveMessage(EApplication sender, String message)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * L'application sender vous demande request
	 * Vous devez lui renvoyer une string
	 * Vous ne devez faire aucun appel à cette fonction, seulement remplir le code
	 * Elle est automatiquement appelée lorsqu'une application vous contacte
	 */
	@Override
	public String answerToRequestMessage(EApplication sender, String request)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	* L'application sender vous envoie le XML xml
	* Vous ne devez faire aucun appel à cette fonction, seulement remplir le code
	* Elle est automatiquement appelée lorsqu'une application vous contacte
	*/
	@Override
	public boolean receiveXML(EApplication sender, Document xml)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * L'application sender vous demande un XML avec la requete request
	 * Vous ne devez faire aucun appel à cette fonction, seulement remplir le code
	 * Elle est automatiquement appelée lorsqu'une application vous contacte
	 */
	@Override
	public Document answerToRequestXML(EApplication sender, String request)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * Envoyer le message message à l'application to
	 * Vous pouvez appeler cette fonction
	 * Ne pas modifier le code
	 */
	public boolean sendMessage(EApplication to, String message) {
		return true;
	}
	
	/*	
	 * Demander un message à l'application to avec la requete request
	 * Vous pouvez appeler cette fonction
	 * Ne pas modifier le code
	 */
	public String requestMessage(EApplication to, String request) {
		return "";
	}
	
	/*
	 * Envoyer le XML xml à l'application to
	 * vous pouvez appeler cette fonction
	 * Ne pas modifier le code
	 */
	public boolean sendXML (EApplication to, Document xml) {
		return true;
	}
	
	/*
	 * Demander un XML à l'application to avec la requete request
	 * Vous pouvez appeler cette fonction
	 * Ne pas modifier le code
	 */
	public Document requestXML (EApplication to, String request) {
		return null;
	}
}
