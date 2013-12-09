package fr.epita.sigl.miwa.st.application_locator;

import java.rmi.Remote;
import java.rmi.RemoteException;

import fr.epita.sigl.miwa.st.EApplication;

public interface IApplicationLocator extends Remote {
	public void registerApplication (EApplication application, String IPAddresse) throws RemoteException;
	public String getApplicationIP (EApplication application) throws RemoteException;
}
