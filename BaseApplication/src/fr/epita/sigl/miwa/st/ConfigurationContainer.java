package fr.epita.sigl.miwa.st;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationContainer {

	private static final Logger log = Logger.getLogger(ConfigurationContainer.class.getName());
	private static ConfigurationContainer _instance;
	private static final Object _instanceLock = new Object();
	
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
	
	private ConfigurationContainer() {
		try {
			_prop.load(new FileInputStream("conf/config.properties"));
			_currentApplication = EApplication.getFromShortName(_prop.getProperty("application"));
			_serverHostAddress = _prop.getProperty("server_ip");
			
			try {
				_applicationHostAddress = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				log.log(Level.SEVERE, "Properties Loading : Failed to get LocalHost");
				e1.printStackTrace();
			}

			if (_currentApplication == null) {
				log.log(Level.SEVERE, "Properties Loading : Failed to load the application name in the config.properties file.");
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, "Properties Loading : Failed to load properties file.");
			e.printStackTrace();
		}
		
		System.out.println(_currentApplication.getShortName());
		System.out.println("server address : " + _serverHostAddress);
		System.out.println("app address : " + _applicationHostAddress);
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
