package fr.epita.sigl.miwa.st;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClientToUse;
import fr.epita.sigl.miwa.st.clock.IClock;
import fr.epita.sigl.miwa.st.clock.IClockClient;

public class ClockClient extends UnicastRemoteObject implements IClockClient {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3086956773955378610L;
	/**
	 * @param args
	 */
	private static final Logger log = Logger.getLogger(ClockClient.class
			.getName());
	private static ClockClient _instance;
	private static final Object _lockInstance = new Object();
	private static IClock remoteClock;

	static public ClockClient getInstance() {
		if (_instance == null) {
			synchronized (_lockInstance) {
				if (_instance == null) {
					try {
						_instance = new ClockClient();
						_instance.initConnection();
					} catch (RemoteException e) {
						log.log(Level.SEVERE,
								"CLOCK CLIENT : Failed to build ClockClient");
						e.printStackTrace();
					}
				}
			}
		}
		return _instance;
	}

	public Date getHour() {
		try {
			return remoteClock.getHour();
		} catch (RemoteException e) {
			log.log(Level.WARNING,
					"CLOCK CLIENT : Failed to getHour, try to reinit the connection.");
			e.printStackTrace();
			initConnection();
			try {
				return remoteClock.getHour();
			} catch (RemoteException e1) {
				log.log(Level.SEVERE,
						"CLOCK CLIENT : Failed to getHour for the second time.");
				e1.printStackTrace();
				return null;
			}
		}
	}

	public void wakeMeUp(Date date, Object message) {
		EApplication app = ConfigurationContainer.getInstance()
				.getCurrentApplication();
		try {
			remoteClock.wakeMeUp(app, date, message);
		} catch (RemoteException e) {
			log.log(Level.WARNING,
					"CLOCK CLIENT : Failed to register a unique event, try to reinit the connection.");
			e.printStackTrace();
			initConnection();
			try {
				remoteClock.wakeMeUp(app, date, message);
			} catch (RemoteException e1) {
				log.log(Level.SEVERE,
						"CLOCK CLIENT : Failed to register a unique event for the second time.");
				e1.printStackTrace();
			}
		}
	}

	public void wakeMeUpEveryDays(Date nextOccurence, Object message) {
		EApplication app = ConfigurationContainer.getInstance()
				.getCurrentApplication();
		try {
			remoteClock.wakeMeUpEveryDays(app, nextOccurence, message);
		} catch (RemoteException e) {
			log.log(Level.WARNING,
					"CLOCK CLIENT : Failed to register a daily event, try to reinit the connection.");
			e.printStackTrace();
			initConnection();
			try {
				remoteClock.wakeMeUpEveryDays(app, nextOccurence, message);
			} catch (RemoteException e1) {
				log.log(Level.SEVERE,
						"CLOCK CLIENT : Failed to register a daily event for the second time.");
				e1.printStackTrace();
			}
		}
	}

	public void wakeMeUpEveryWeeks(Date nextOccurence, Object message) {
		EApplication app = ConfigurationContainer.getInstance()
				.getCurrentApplication();
		try {
			remoteClock.wakeMeUpEveryWeeks(app, nextOccurence, message);
		} catch (RemoteException e) {
			log.log(Level.WARNING,
					"CLOCK CLIENT : Failed to register a weekly event, try to reinit the connection.");
			e.printStackTrace();
			initConnection();
			try {
				remoteClock.wakeMeUpEveryWeeks(app, nextOccurence, message);
			} catch (RemoteException e1) {
				log.log(Level.SEVERE,
						"CLOCK CLIENT : Failed to register a weekly event for the second time.");
				e1.printStackTrace();
			}
		} // FIXME withConf
	}

	public void wakeMeUpEveryHours(Date nextOccurence, Object message) {
		EApplication app = ConfigurationContainer.getInstance()
				.getCurrentApplication();
		try {
			remoteClock.wakeMeUpEveryHours(app, nextOccurence, message);
		} catch (RemoteException e) {
			log.log(Level.WARNING,
					"CLOCK CLIENT : Failed to register a hourly event, try to reinit the connection.");
			e.printStackTrace();
			initConnection();
			try {
				remoteClock.wakeMeUpEveryHours(app, nextOccurence, message);
			} catch (RemoteException e1) {
				log.log(Level.SEVERE,
						"CLOCK CLIENT : Failed to register a hourly event for the second time.");
				e1.printStackTrace();
			}
		}
	}

	@Override
	public String wakeUp(Date date, Object message) throws RemoteException {
		ClockClientToUse.wakeUp(date, message);
		return null;
	}

	private ClockClient() throws RemoteException {
	}

	private void initConnection() {
		try {
			LocateRegistry.createRegistry(1099);
		} catch (RemoteException e1) {
			// FIXME Log
			e1.printStackTrace();
		}
		EApplication app = ConfigurationContainer.getInstance()
				.getCurrentApplication();
		String url;
		url = "rmi://"
				+ ConfigurationContainer.getInstance().getApplicationHostAddress()
				+ "/Clock" + app.getShortName();
		try {
			Naming.rebind(url, _instance);
		} catch (RemoteException | MalformedURLException e) {
			log.log(Level.SEVERE,
					"CLOCK CLIENT : Failed to (re)bind the connection.\n" + e.getMessage());
		}

		String rmiClockServer = "rmi://"
				+ ConfigurationContainer.getInstance().getServerHostAddress()
				+ "/Clock";
		try {
			remoteClock = (IClock) Naming.lookup(rmiClockServer);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			log.log(Level.SEVERE,
					"CLOCK CLIENT : Failed to contact Clock Server.\n" + e.getMessage());
		}
	}

	public static void main(String[] args) {
		ClockClient clock = ClockClient.getInstance();
			Date hour = clock.getHour();
			System.out.println("Au troisième top il sera exactement : "
					+ hour.toString());
		
		clock.wakeMeUp(hour, "INIT"); clock.wakeMeUpEveryDays(hour, "DAY");
		clock.wakeMeUpEveryWeeks(hour, "WEEK");		 
	}
}
