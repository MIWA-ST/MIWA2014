package fr.epita.sigl.miwa.st.clock;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;


public interface IClockClient extends Remote {
	public String wakeUp(Date date, Object message, double appId) throws RemoteException;
}
