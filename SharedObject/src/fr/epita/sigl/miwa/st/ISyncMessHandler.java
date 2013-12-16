package fr.epita.sigl.miwa.st;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.w3c.dom.Document;

public interface ISyncMessHandler extends Remote {
	public boolean receiveMessage(EApplication sender, String message) throws RemoteException;
	public String answerToRequestMessage(EApplication sender, String request) throws RemoteException;
	
	public boolean receiveXML(EApplication sender, Document xml) throws RemoteException;
	public Document answerToRequestXML (EApplication sender, String request) throws RemoteException;
}
