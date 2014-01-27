package fr.epita.sigl.miwa.st;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RMISocketFactory;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.application_locator.IApplicationLocator;

public class ConnectionHandler {
	private IApplicationLocator _appLocator;
	private String _serverIP;
	private static Logger log = Logger.getGlobal();
	
	public ConnectionHandler () {
		initConnection();
	}
	
	private void initConnection () {
		try {
			_serverIP = InetAddress.getLocalHost().getHostAddress();
			
		} catch (UnknownHostException e) {
			log.severe("Connection Handler : Failed to resolve server IP : \n" + e.getMessage());
		}
		String rmiAppLocator = "rmi://" + _serverIP
				+ "/ApplicationLocator";
			try {
				_appLocator = (IApplicationLocator) Naming
					.lookup(rmiAppLocator);
			} catch (MalformedURLException | RemoteException
				| NotBoundException e) {
			log.severe("Application Locator : Failed to establish connection with. \n" + e.getMessage());
			}
			try {
				RMISocketFactory.setSocketFactory(new TimedSocketFactory(5000));
			} catch (IOException e) {
				log.severe("failed to set RMIFactory.");
			}
	}

	public String getIPOfApp(EApplication app) {
		if (_appLocator == null)
			initConnection();
		if (_appLocator == null)
			return "";
		try {
			return _appLocator.getApplicationIP(app);
		} catch (RemoteException e) {
			log.warning("Application Locator : Failed to get IP of + " + app + " try to reconnect.\n" + e.getMessage());
			initConnection();
			try {
				return _appLocator.getApplicationIP(app);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				log.severe("Application Locator : Failed to get IP of + " + app + " for the second time.\n" + e.getMessage());
			}
		}
		return "";
	}
}
