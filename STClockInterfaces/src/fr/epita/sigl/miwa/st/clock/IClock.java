package fr.epita.sigl.miwa.st.clock;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;


public interface IClock extends Remote {
	public Date getHour() throws RemoteException;
	public void wakeMeUp(String sender, Date date, Object message) throws RemoteException;
	public void wakeMeUpEveryHours(String sender, Date nextOccurence, Object message) throws RemoteException;
	public void wakeMeUpEveryDays(String sender, Date nextOccurence, Object message) throws RemoteException;
	public void wakeMeUpEveryWeeks(String sender, Date nextOccurence, Object message) throws RemoteException;
}
