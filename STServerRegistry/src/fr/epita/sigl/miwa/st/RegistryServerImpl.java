package fr.epita.sigl.miwa.st;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.async.file.FTPHelper;

public class RegistryServerImpl extends UnicastRemoteObject {
	
	private static final Logger log = Logger.getLogger(RegistryServerImpl.class
			.getName());

	protected RegistryServerImpl(int arg0, RMIClientSocketFactory arg1,
			RMIServerSocketFactory arg2) throws RemoteException {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(1099);
			runAppLocator();
			
			try {
				new BufferedReader(new InputStreamReader(System.in)).readLine();
			} catch (IOException e) {
				log.log(Level.SEVERE, "ERREUR REGISTRY READLINE.");
				e.printStackTrace();
			}
		} catch (RemoteException e) {
			log.log(Level.SEVERE, "ERREUR REGISTRY : RemoteException.");
			e.printStackTrace();

		}
	}
	
	private static void runAppLocator () {

		ApplicationLocator locator = null;
		try {
			locator = new ApplicationLocator();
		} catch (RemoteException e1) {
			log.log(Level.SEVERE,
					"APP LOCATOR : Failed to build.");
			e1.printStackTrace();
			return;
		}
		try {
			String url = "rmi://" + InetAddress.getLocalHost().getHostAddress()
					+ "/ApplicationLocator";
			try {
				// FIXME WithConf
				Naming.rebind(url, locator);
			} catch (RemoteException | MalformedURLException e) {
				log.log(Level.SEVERE,
						"APP LOCATOR : Failed to register URL Or Remote exception");
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			log.log(Level.SEVERE,
					"APP LOCATOR : Failed to register UnknownHost exception");
			e.printStackTrace();
		}
	}

}
