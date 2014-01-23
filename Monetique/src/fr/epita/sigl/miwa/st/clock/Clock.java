package fr.epita.sigl.miwa.st.clock;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.db.DbHandler;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.ConfigurationException;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.clock.IClock;
import fr.epita.sigl.miwa.st.clock.IClockClient;
import fr.epita.sigl.miwa.st.clock.IExposedClock;

class Clock extends UnicastRemoteObject implements IClockClient, IExposedClock {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3086956773955378610L;
	/**
	 * @param args
	 */
	private static final Logger log = Logger.getLogger(Clock.class
			.getName());
	private static Clock _instance;
	private static final Object _lockInstance = new Object();
	private static IClock remoteClock;
	
	private static Integer counter = 0;

	static public Clock getInstance() {
		if (_instance == null) {
			synchronized (_lockInstance) {
				if (_instance == null) {
					try {
						_instance = new Clock();
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
		EApplication app = Conf.getInstance()
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
		EApplication app = Conf.getInstance()
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
		EApplication app = Conf.getInstance()
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
		}
	}

	public void wakeMeUpEveryHours(Date nextOccurence, Object message) {
		EApplication app = Conf.getInstance()
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
		class OneShotTask implements Runnable {
			Date date;
			Object message;
			OneShotTask(Date date, Object message) {
				this.date = date;
				this.message = message;
			}
			public void run() {
				try {
					ClockClient.wakeUp(date, message);
				} catch (Exception e) {
					log.severe("wakeUp : error in functional code");
				}
			}
		}
		/*
				// Paiement fidélité en fin de mois
		//ClockClient.wakeUp(date, message);
		
		if (counter == 0)
		{		
			log.info("***** REQUEST -> Paiement of fidelity credits the : " + date);
			
			Map<Integer, Float> comptes = new HashMap<>();
			
			DbHandler dbHandler = new DbHandler();
			try 
			{
				// Connexion à la BDD
				Connection connection = dbHandler.open();

				log.info("***** REQUEST -> Fetching in-debts accounts.");			
					
				// Fetching account
				PreparedStatement ps = connection.prepareStatement("SELECT ID_FIDELITY_CREDIT_ACCOUNT FROM fidelity_credit_account WHERE TOTAL_CREDIT_AMOUNT - TOTAL_REPAID_CREDIT__AMOUNT > 0;");
				ResultSet res = ps.executeQuery();
				
				while (res.next())
				{
					comptes.put(res.getInt("ID_FIDELITY_CREDIT_ACCOUNT"), 0f);
					
					log.info("***** The account with ID : " + res.getInt("ID_FIDELITY_CREDIT_ACCOUNT") + " has debts to pay this month.");
				}

				// Foreach compte select montant somme des credit fid en prenant les echelons en compte
				for (Integer c : comptes.keySet()) 
				{
					ps = connection.prepareStatement("SELECT SUM(AMOUNT / ECHELON_NB) FROM fidelity_credit WHERE IS_REPAID = false AND ID_FIDELITY_CREDIT_ACCOUNT = ?;");
					ps.setInt(1, c);
					res = ps.executeQuery();
					if (res.next())
					{
						comptes.put(c, res.getFloat(1));
						
						log.info("***** The account with ID : " + c + " has " + res.getFloat(1)+ "€ to pay.");
					}
				}

				for (Integer c : comptes.keySet()) 
				{
					if (!getBankPaiement())
					{
						ps = connection.prepareStatement("UPDATE fidelity_credit_account SET IS_BLACKLISTED = true, BLAKLISTED_DATE = NOW() WHERE ID_FIDELITY_CREDIT_ACCOUNT = ?;");
						ps.setInt(1, c);
						ps.executeUpdate();
						
						log.info("***** Fidelity account with ID : " + c + "is now blacklisted because bank refuses paiement.");

					}
					else
					{
						ps = connection.prepareStatement("UPDATE fidelity_credit_account SET IS_BLACKLISTED = false, BLAKLISTED_DATE = NOW(), TOTAL_REPAID_CREDIT__AMOUNT = TOTAL_REPAID_CREDIT__AMOUNT + ? WHERE ID_FIDELITY_CREDIT_ACCOUNT = ?;");
						ps.setFloat(1, comptes.get(c));
						ps.setInt(2, c);
						ps.executeUpdate();
						
						log.info("***** Fidelity account with ID : " + c + "have been updated.");
						
						ps = connection.prepareStatement("UPDATE fidelity_credit SET REPAID_AMOUNT = REPAID_AMOUNT + (AMOUNT / ECHELON_NB) WHERE ID_FIDELITY_CREDIT_ACCOUNT = ? AND IS_REPAID = false;");
						ps.setInt(1, c);
						ps.executeUpdate();
						
						ps = connection.prepareStatement("UPDATE fidelity_credit SET IS_REPAID = true WHERE ID_FIDELITY_CREDIT_ACCOUNT = ? AND IS_REPAID = false AND REPAID_AMOUNT >= AMOUNT;");
						ps.setInt(1, c);
						ps.executeUpdate();
						
						log.info("***** Fidelity credit with ID : " + c + "have been updated.");
					}
				}	
			} 
			catch (SQLException e) 
			{
				System.err.println("ERROR : " + e.getMessage());		
				return "ERROR";
			}
			finally
			{
				log.info("***** REQUEST DONE");
				counter++;
				dbHandler.close();
			}				

		}
		else
		{
			counter = (counter + 1) % 4;
		}*/

		Thread thread = new Thread(new OneShotTask(date, message));
		thread.start();
		return null;
	}

	private Clock() throws RemoteException {
	}

	private void initConnection() {
		EApplication app = Conf.getInstance()
				.getCurrentApplication();
		try {
			if (!Conf.getInstance().clockIsLocal()) {
				try {
					LocateRegistry.createRegistry(1099);
				} catch (RemoteException e1) {
					log.severe("Failed to create Registry" + e1.getMessage());
				}
			}
		} catch (ConfigurationException e1) {
			log.severe("Failed to get isLocal conf : " + e1.getMessage());
		}
		String url = "rmi://"
				+ Conf.getInstance()
						.getApplicationHostAddress() + "/Clock"
				+ app.getShortName();
		try {
			Naming.rebind(url, _instance);
			log.info("Clock" + app.getShortName() + " registred.");
		} catch (RemoteException | MalformedURLException e) {
			log.log(Level.SEVERE,
					"CLOCK CLIENT : Failed to (re)bind the connection.\n"
							+ e.getMessage());
		}

		String rmiClockServer = "rmi://"
				+ Conf.getInstance().getServerHostAddress()
				+ "/Clock";
		try {
			remoteClock = (IClock) Naming.lookup(rmiClockServer);
			log.info("Server Clock found.");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			log.log(Level.SEVERE,
					"CLOCK CLIENT : Failed to contact Clock Server.\n"
							+ e.getMessage());
		}
		try {
			try {
				remoteClock.removeSubscriptions(app);
			} catch (RemoteException e) {
				log.severe("Clock : Failed to remove subscriptions");
				e.printStackTrace();
			}
		} catch (Exception e2) {
			log.severe("Clock : Failed to remove subscriptions 2");
			e2.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Clock clock = Clock.getInstance();

		Date hour = clock.getHour();
		System.out.println("Au troisi�me top il sera exactement : "
				+ hour.toString());

		clock.wakeMeUp(hour, "INIT");
		clock.wakeMeUpEveryDays(hour, "DAY");
		clock.wakeMeUpEveryHours(hour, "HOUR");
	}
	
	private static boolean getBankPaiement() 
	{
		log.info("***** Bank Paiement service call started.");		
		Random rnd = new Random();
		Integer myRnd = rnd.nextInt(100);
		
		log.info("***** Bank Paiement service call stopped with : " + (myRnd < 70) + ".");
		return myRnd < 70;
	}

}
