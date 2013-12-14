package fr.epita.sigl.miwa.st;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.application_locator.IApplicationLocator;

public class ConfigurationContainer {

	private static final Logger log = Logger
			.getLogger(ConfigurationContainer.class.getName());
	private static ConfigurationContainer _instance;
	private static final Object _instanceLock = new Object();
	private static IApplicationLocator _applicationLocator;

	private Properties _prop = new Properties();

	private EApplication _currentApplication;
	private String _serverHostAddress;
	private String _applicationHostAddress;

	public static ConfigurationContainer getInstance() {
		if (_instance == null) {
			synchronized (_instanceLock) {
				if (_instance == null) {
					_instance = new ConfigurationContainer();
				}
			}
		}
		return _instance;
	}
	
	public String getIPOfApp(EApplication application) {
		try {
			return _applicationLocator.getApplicationIP(application);
		} catch (RemoteException e) {
			log.log(Level.WARNING, "Failed to get IP of " + application.getShortName() + " retrying.");
			e.printStackTrace();
			initAppLocator();
			try {
				return _applicationLocator.getApplicationIP(application);
			} catch (RemoteException e1) {
				log.log(Level.SEVERE, "Failed to get IP of " + application.getShortName() + ".");
				e1.printStackTrace();
			}
		}		
		return "";
	}

	private ConfigurationContainer() {
		try {
			_prop.load(new FileInputStream("conf/config.properties"));
			_currentApplication = EApplication.getFromShortName(_prop
					.getProperty("application"));
			_serverHostAddress = _prop.getProperty("server_ip");
			try {
				_applicationHostAddress = InetAddress.getLocalHost()
						.getHostAddress();
			} catch (UnknownHostException e1) {
				log.log(Level.SEVERE,
						"Properties Loading : Failed to get LocalHost");
				e1.printStackTrace();
			}

			if (_currentApplication == null) {
				log.log(Level.SEVERE,
						"Properties Loading : Failed to load the application name in the config.properties file.");
			}
			initAppLocator();
		} catch (IOException e) {
			log.log(Level.SEVERE,
					"Properties Loading : Failed to load properties file.");
			e.printStackTrace();
		}
	}
	
	private void initAppLocator() {
		String rmiAppLocator = "rmi://" + _serverHostAddress
				+ "/ApplicationLocator";
		try {
			_applicationLocator = (IApplicationLocator) Naming
					.lookup(rmiAppLocator);
		} catch (MalformedURLException | RemoteException
				| NotBoundException e) {
			log.log(Level.SEVERE,
					"Application Locator : Failed to establish connection with.");
			e.printStackTrace();
		}
		try {
			_applicationLocator.registerApplication(_currentApplication, _applicationHostAddress);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Properties getProp() {
		return _prop;
	}

	public EApplication getCurrentApplication() {
		return _currentApplication;
	}

	public String getServerHostAddress() {
		return _serverHostAddress;
	}

	public String getApplicationHostAddress() {
		return _applicationHostAddress;
	}
}
