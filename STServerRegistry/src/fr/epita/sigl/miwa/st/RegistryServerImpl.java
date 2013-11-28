package fr.epita.sigl.miwa.st;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class RegistryServerImpl extends UnicastRemoteObject {

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
		} catch (RemoteException e) {
			e.printStackTrace();
		
	}
	}

}
