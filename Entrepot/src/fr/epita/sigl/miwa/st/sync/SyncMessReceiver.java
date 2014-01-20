package fr.epita.sigl.miwa.st.sync;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.ConfigurationException;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.ISyncMessReceiver;

class SyncMessReceiver extends UnicastRemoteObject implements ISyncMessReceiver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1925073667781359257L;
	static private SyncMessReceiver _instance;
	static private Object _instanceLock = new Object();
	static private final Logger LOG = Logger.getGlobal();
	
	public static SyncMessReceiver getInstance() {
		if (_instance == null) {
			synchronized (_instanceLock) {
				if (_instance == null) {
					try {
						_instance = new SyncMessReceiver();
						_instance.initConnection();
					} catch (RemoteException e) {
						LOG.severe("SyncMessReceiver : RemoteException during construct\n" + e.getMessage());
					}
					
				}
			}
		}
		return _instance;
	}

	/*
	 * l'application sender vous envoie la string message.
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Override
	public boolean receiveMessage(EApplication sender, String message)
			throws RemoteException {
		return SyncMessHandler.receiveMessage(sender, message);
	}

	/*
	 * L'application sender vous demande request
	 * Vous devez lui renvoyer une string
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Override
	public String answerToRequestMessage(EApplication sender, String request) throws RemoteException {
		return SyncMessHandler.answerToRequestMessage(sender, request);
	}
	
	/*
	* L'application sender vous envoie le XML xml
	* Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	* Elle est automatiquement appel�e lorsqu'une application vous contacte
	*/
	@Override
	public boolean receiveXML(EApplication sender, Document xml)
			throws RemoteException {
		return SyncMessHandler.receiveXML(sender, xml);
	}

	/*
	 * L'application sender vous demande un XML avec la requete request
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Override
	public Document answerToRequestXML(EApplication sender, String request)
			throws RemoteException {
		return SyncMessHandler.answerToRequestXML(sender, request);
	}
	
	private SyncMessReceiver() throws RemoteException {

	}
	
	private void initConnection() {
		EApplication app = Conf.getInstance().getCurrentApplication();
		try {
			if (!Conf.getInstance().clockIsLocal()) {
			try {
				LocateRegistry.createRegistry(1099);
				LOG.severe("registry runned");
			} catch (RemoteException e1) {
				LOG.severe("Failed to create Registry" + e1.getMessage());
			}
			}
		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = "rmi://"
				+ Conf.getInstance()
						.getApplicationHostAddress() + "/SyncMessReceiver"
				+ app.getShortName();
		try {
			Naming.rebind(url, _instance);
			LOG.info("SyncMessReceiver" + app.getShortName() + " registred.");
		} catch (RemoteException | MalformedURLException e) {
			LOG.log(Level.SEVERE,
					"SyncMessReceiver : Failed to (re)bind the connection.\n"
							+ e.getMessage());
		}
	}
	
	
}
