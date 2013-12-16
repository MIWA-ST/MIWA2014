package fr.epita.sigl.miwa.st;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.application_locator.IApplicationLocator;

public class ApplicationLocator extends UnicastRemoteObject implements IApplicationLocator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8632468378431197443L;
	private static Map<EApplication, String> _applicationMap;
	private static final Logger LOG = Logger.getGlobal();
	
	protected ApplicationLocator() throws RemoteException {
		super();
		_applicationMap = new HashMap<EApplication, String>();
	}

	@Override
	public void registerApplication(EApplication application, String IPAddress) {
		if (_applicationMap == null)
			_applicationMap = new HashMap<EApplication, String>();
		_applicationMap.put(application, IPAddress);
		LOG.info(application.getShortName() + " registred with IP " + IPAddress);
	}

	@Override
	public String getApplicationIP(EApplication application) {
		if (_applicationMap == null)
			return null;
		return _applicationMap.get(application);
	}
	
}
