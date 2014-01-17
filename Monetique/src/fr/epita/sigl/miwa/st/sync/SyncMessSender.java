package fr.epita.sigl.miwa.st.sync;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.ISyncMessReceiver;

class SyncMessSender implements ISyncMessSender {
	
	static private volatile SyncMessSender _instance;
	static private Logger log = Logger.getGlobal();
	
	public static synchronized SyncMessSender getInstance () {
		if (_instance == null)
			_instance = new SyncMessSender();
		return _instance;
	}
	
	private Map<EApplication, ISyncMessReceiver> _remoteReceivers;
	
	public boolean sendMessage(EApplication to, String message) {
		ISyncMessReceiver receiver = getRemoteReceiver(to, false);
		if (receiver == null)
			return false;
		
		try {
			return receiver.receiveMessage(Conf.getInstance().getCurrentApplication(), message);
		} catch (RemoteException e) {
			log.warning("Failed to send message " + message + " to " + to.getShortName() + " application. Try to reconnect \n" + e.getMessage());
			receiver = getRemoteReceiver(to, true);
			try {
				return receiver.receiveMessage(Conf.getInstance().getCurrentApplication(), message);
			} catch (RemoteException e1) {
				log.warning("Failed to send message " + message + " to " + to.getShortName() + " application for the second time \n" + e.getMessage());
				return false;
			}
		}
	}
	
	public String requestMessage(EApplication to, String request) {
		ISyncMessReceiver receiver = getRemoteReceiver(to, false);
		if (receiver == null)
			return null;
		
		try {
			return receiver.answerToRequestMessage(Conf.getInstance().getCurrentApplication(), request);
		} catch (RemoteException e) {
			log.warning("Failed to request " + request + " to " + to.getShortName() + " application. Try to reconnect \n" + e.getMessage());
			receiver = getRemoteReceiver(to, true);
			try {
				return receiver.answerToRequestMessage(Conf.getInstance().getCurrentApplication(), request);
			} catch (RemoteException e1) {
				log.warning("Failed to send request " + request + " to " + to.getShortName() + " application for the second time \n" + e.getMessage());
				return null;
			}
		}
	}
	
	public boolean sendXML (EApplication to, Document xml) {
		ISyncMessReceiver receiver = getRemoteReceiver(to, false);
		if (receiver == null)
			return false;
		
		try {
			return receiver.receiveXML(Conf.getInstance().getCurrentApplication(), xml);
		} catch (RemoteException e) {
			log.warning("Failed to send xml message " + xml.toString() + " to " + to.getShortName() + " application. Try to reconnect \n" + e.getMessage());
			receiver = getRemoteReceiver(to, true);
			try {
				return receiver.receiveXML(Conf.getInstance().getCurrentApplication(), xml);
			} catch (RemoteException e1) {
				log.warning("Failed to send xml message " + xml.toString() + " to " + to.getShortName() + " application for the second time \n" + e.getMessage());
				return false;
			}
		}
	}
	
	public Document requestXML (EApplication to, String request) {
		ISyncMessReceiver receiver = getRemoteReceiver(to, false);
		if (receiver == null)
			return null;
		
		try {
			return receiver.answerToRequestXML(Conf.getInstance().getCurrentApplication(), request);
		} catch (RemoteException e) {
			log.warning("Failed to request XML " + request + " to " + to.getShortName() + " application. Try to reconnect \n" + e.getMessage());
			receiver = getRemoteReceiver(to, true);
			try {
				return receiver.answerToRequestXML(Conf.getInstance().getCurrentApplication(), request);
			} catch (RemoteException e1) {
				log.warning("Failed to send request XML " + request + " to " + to.getShortName() + " application for the second time \n" + e.getMessage());
				return null;
			}
		}
	}
	
	/* Peut renvoyer null */
	private ISyncMessReceiver getRemoteReceiver (EApplication to, boolean flush) {
		if (flush || _remoteReceivers.get(to) == null) {
			initConnectionWithRemote(to);
		}
		return _remoteReceivers.get(to);
	}
	
	private void initConnectionWithRemote(EApplication to) {
		ISyncMessReceiver syncMessReceiver = null;
		String rmiURL = "rmi://"
				+ Conf.getInstance().getIPOfApp(to)
				+ "/SyncMessReceiver" + to.getShortName();
		try {
			syncMessReceiver = (ISyncMessReceiver) Naming.lookup(rmiURL);
			_remoteReceivers.put(to, syncMessReceiver);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			log.log(Level.SEVERE, "REMOTE CLASS GETTER : Failed to contact "
					+ to.getShortName() + " for the class ISyncMessageReceiver\n" + e.getMessage());
		}
	}
	
	private SyncMessSender() {
		_remoteReceivers = new HashMap<EApplication, ISyncMessReceiver>();
	}

}
