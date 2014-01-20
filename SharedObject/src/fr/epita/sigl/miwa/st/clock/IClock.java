package fr.epita.sigl.miwa.st.clock;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

import fr.epita.sigl.miwa.st.EApplication;


public interface IClock extends Remote {
	public Date getHour() throws RemoteException;
	public void wakeMeUp(EApplication sender, Date date, Object message) throws RemoteException;
	public void wakeMeUpEveryHours(EApplication sender, Date nextOccurence, Object message) throws RemoteException;
	public void wakeMeUpEveryDays(EApplication sender, Date nextOccurence, Object message) throws RemoteException;
	public void wakeMeUpEveryWeeks(EApplication sender, Date nextOccurence, Object message) throws RemoteException;
	public void removeSubscriptions(EApplication sender) throws RemoteException;
}
