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

	private static final Logger LOGGER = Logger
			.getLogger(ConfigurationContainer.class.getName());
	private static ConfigurationContainer _instance;
	private static final Object _instanceLock = new Object();
	private static IApplicationLocator _applicationLocator;

	// GLOBAL
	public static final String IS_LOCAL_KEY = "global.is_local";
	public static final String CLOCK_IS_LOCAL_KEY = "clock.is_local";
	public static final String LOCAL_REPOSITORY_KEY = "global.local_repository";
	public static final String CURRENT_APPLICATION_KEY = "application";

	// FTP
	public static final String FTP_HOST_KEY = "ftp.host";
	public static final String FTP_PORT_KEY = "ftp.port";
	public static final String FTP_USERNAME_KEY = "ftp.username";
	public static final String FTP_PASSWORD_KEY = "ftp.password";

	// JMS
	public static final String JMS_PROVIDER_URL_KEY = "jms.provider_url";
	public static final String JMS_INITIAL_CONTEXT_FACTORY_CLASS_KEY = "jms.initial_context_factory_class";
	public static final String JMS_CONNECTION_FACTORY_URL_KEY = "jms.connection_factory_url";


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
			LOGGER.log(Level.WARNING,
					"Failed to get IP of " + application.getShortName()
							+ " retrying.");
			e.printStackTrace();
			initAppLocator();
			try {
				return _applicationLocator.getApplicationIP(application);
			} catch (RemoteException e1) {
				LOGGER.severe("Failed to get IP of "
						+ application.getShortName() + ".");
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
				LOGGER.log(Level.SEVERE,
						"Properties Loading : Failed to get LocalHost");
				e1.printStackTrace();
			}

			if (_currentApplication == null) {
				LOGGER.log(
						Level.SEVERE,
						"Properties Loading : Failed to load the application name in the config.properties file.");
			}
			// TODO remove comment
			initAppLocator();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,
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
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			LOGGER.log(Level.SEVERE,
					"Application Locator : Failed to establish connection with.");
			e.printStackTrace();
		}
		try {
			_applicationLocator.registerApplication(_currentApplication,
					_applicationHostAddress);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String extractProperty(String key) throws ConfigurationException {
		String result = _prop.getProperty(key);
		if (result == null) {
			LOGGER.severe("Missing property " + key);
			throw new ConfigurationException("Missing property " + key);
		}
		return result;
	}

	public boolean isLocal() throws ConfigurationException {
		String isLocalStr = extractProperty(IS_LOCAL_KEY);
		return (isLocalStr.compareTo("true") == 0);
	}
	
	public boolean clockIsLocal() throws ConfigurationException {
		String isLocalStr = extractProperty(CLOCK_IS_LOCAL_KEY);
		return (isLocalStr.compareTo("true") == 0);
	}

	/**
	 * Retourne le chemin vers le répertoire local où les fichiers asynchrones
	 * vont être stockés
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public String getLocalRepository() throws ConfigurationException {
		return extractProperty(LOCAL_REPOSITORY_KEY);
	}

	public String getFTPHost() throws ConfigurationException {
		return extractProperty(FTP_HOST_KEY);
	}

	public int getFTPPort() throws ConfigurationException {
		String portTmp = extractProperty(FTP_PORT_KEY);
		Integer port = Integer.valueOf(portTmp);
		if (port == null) {
			LOGGER.severe("Could not convert the propertie " + FTP_PORT_KEY
					+ " to integer.");
			throw new ConfigurationException("Could not convert the propertie "
					+ FTP_PORT_KEY + " to integer.");
		}
		return port.intValue();
	}

	public String getFTPUsername() throws ConfigurationException {
		return extractProperty(FTP_USERNAME_KEY);
	}

	public String getFTPPassword() throws ConfigurationException {
		return extractProperty(FTP_PASSWORD_KEY);
	}
	
	public String getJMSProviderURL() throws ConfigurationException {
		return extractProperty(JMS_PROVIDER_URL_KEY);
	}
	
	public String getJMSInitialContextFactory() throws ConfigurationException {
		return extractProperty(JMS_INITIAL_CONTEXT_FACTORY_CLASS_KEY);
	}
	
	public String getJMSConnectionFactoryURL() throws ConfigurationException {
		return extractProperty(JMS_CONNECTION_FACTORY_URL_KEY);
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
