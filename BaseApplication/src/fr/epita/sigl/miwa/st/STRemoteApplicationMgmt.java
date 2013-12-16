package fr.epita.sigl.miwa.st;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class STRemoteApplicationMgmt {
	private static final Logger log = Logger
			.getLogger(STRemoteApplicationMgmt.class.getName());

	static public void registerRemoteClass(String className,
			Remote classInstance) {
		String url = "rmi://"
				+ ConfigurationContainer.getInstance().getApplicationHostAddress()
				+ "/" + className
				+ ConfigurationContainer.getInstance().getCurrentApplication();
		try {
			Naming.rebind(url, classInstance);
		} catch (RemoteException | MalformedURLException e) {
			log.log(Level.SEVERE,
					"REMOTE CLASS REGISTER : Failed to (re)bind the connection.");
			e.printStackTrace();
		}
	}

	static public Remote getRemoteClass(String className,
			EApplication distantApplication) {
		String rmiURL = "rmi://"
				+ ConfigurationContainer.getInstance().getIPOfApp(distantApplication)
				+ "/" + className + distantApplication.getShortName();
		try {
			return Naming.lookup(rmiURL);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			log.log(Level.SEVERE, "REMOTE CLASS GETTER : Failed to contact "
					+ distantApplication.getShortName() + " for the class "
					+ className);
			log.severe("REMOTE CLASS FINDER : Failed to contact distant app : " + distantApplication.getShortName() + ".\n" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
