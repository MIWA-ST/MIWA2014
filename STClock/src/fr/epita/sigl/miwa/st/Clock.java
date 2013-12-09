package fr.epita.sigl.miwa.st;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.epita.sigl.miwa.st.clock.IClock;
import fr.epita.sigl.miwa.st.clock.IClockClient;

public class Clock extends UnicastRemoteObject implements IClock {
	/**
	 * 
	 */

	private static final long serialVersionUID = -8347497431139736160L;
	private static final int _interval = 500;
	private static final Object _instanceLock = new Object();

	private static Clock _instance;

	private Map<String, IClockClient> _clients;
	private final Object _clientsLock = new Object();

	private Date _hour;
	private int _factor;
	private Set<MessageHandler> _messagesToDeliver;
	private Timer timer;

	static public Clock getInstance() {
		if (_instance == null) {
			synchronized (_instanceLock) {
				if (_instance == null) {
					try {
						_instance = new Clock();
						_instance.initConnection();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return _instance;
	}

	/* Remotes methods */
	@Override
	public Date getHour() throws RemoteException {
		return _hour;
	}

	@Override
	public void wakeMeUp(String sender, Date date, Object message)
			throws RemoteException {
		registerMessage(sender, date, message, RepeatFrequecy.NEVER);
	}

	@Override
	public void wakeMeUpEveryHours(String sender, Date nextOccurence,
			Object message) throws RemoteException {
		registerMessage(sender, nextOccurence, message, RepeatFrequecy.HOUR);
	}

	@Override
	public void wakeMeUpEveryDays(String sender, Date nextOccurence,
			Object message) throws RemoteException {
		registerMessage(sender, nextOccurence, message, RepeatFrequecy.DAY);
	}

	@Override
	public void wakeMeUpEveryWeeks(String sender, Date nextOccurence,
			Object message) throws RemoteException {
		registerMessage(sender, nextOccurence, message, RepeatFrequecy.WEEK);
	}

	/* !Remotes methods */

	public Clock() throws RemoteException {
	}

	private void start(Date begin, int factor) {
		setFactor(factor);
		_hour = begin;
		_messagesToDeliver = new HashSet<MessageHandler>();
		timer = new Timer();
		new Thread(timer).start();
	}

	public void kill() {
		if (timer != null)
			timer.stop();
	}

	public void setFactor(int factor) {
		_factor = factor;
	}

	/* Time management */
	private class Timer implements Runnable {

		private boolean toStop = false;

		public void stop() {
			toStop = true;
		}

		@Override
		public void run() {
			while (!toStop) {
				try {
					Thread.sleep(_interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				update();
			}

		}

	}

	private void update() {
		synchronized (_hour) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(_hour);
			cal.add(Calendar.MILLISECOND, _factor * 500);
			_hour = cal.getTime();
			checkMessages();
		}
	}

	/* !Time management */
	/* Message Handling */
	private void registerMessage(String sender, Date date, Object message,
			RepeatFrequecy frequency) {
		synchronized (_messagesToDeliver) {
			synchronized (_hour) {
				MessageHandler messageH = new MessageHandler(sender, date,
						message, frequency);
				_messagesToDeliver.add(messageH);
			}
		}
	}

	private void checkMessages() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (_messagesToDeliver) {
					Set<MessageHandler> toRemoveSet = new HashSet<Clock.MessageHandler>();
					for (MessageHandler message : _messagesToDeliver) {
						if (message._date.before(_hour)
								|| message._date.equals(_hour)) {
							new Thread(message).start();
							toRemoveSet.add(message);
						}
					}
					_messagesToDeliver.removeAll(toRemoveSet);
				}

			}
		}).start();
		;
	}

	private class MessageHandler implements Runnable {
		public String _sender;
		public Date _date;
		public Object _message;
		public RepeatFrequecy _frequency;

		public MessageHandler(String sender, Date date, Object message,
				RepeatFrequecy frequency) {
			_sender = sender;
			_message = message;
			_date = date;
			_frequency = frequency;
		}

		public void sendMessage() {
			try {
				final IClockClient receiver = getClientConnection(false);
				if (receiver != null)
					receiver.wakeUp(_date, _message);
			} catch (RemoteException e) {
				e.printStackTrace();
				System.err.println("#12093928 : Try to reconnect");
				final IClockClient receiver = getClientConnection(true);
				if (receiver != null) {
					try {
						receiver.wakeUp(_date, _message);
					} catch (RemoteException e1) {
						e1.printStackTrace();
						return;
					}
				}
			}
			if (!_frequency.equals(RepeatFrequecy.NEVER)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(_date);
				if (_frequency.equals(RepeatFrequecy.HOUR)) {
					cal.add(Calendar.HOUR, 1);
				} else if (_frequency.equals(RepeatFrequecy.DAY)) {
					cal.add(Calendar.DAY_OF_YEAR, 1);
				} else if (_frequency.equals(RepeatFrequecy.WEEK)) {
					cal.add(Calendar.WEEK_OF_YEAR, 1);
				}
				_date = cal.getTime();
				registerMessage(_sender, _date, _message, _frequency);
			}
		}

		private IClockClient getClientConnection(boolean reset) {
			final IClockClient receiver;
			synchronized (_clientsLock) {
				if (reset || !_clients.containsKey(_sender)) {
					try {
						String rmiClockClient = "rmi://"
								+ InetAddress.getLocalHost().getHostAddress()
								+ "/Clock" + _sender;
						try {
							receiver = (IClockClient) Naming
									.lookup(rmiClockClient);
							_clients.put(_sender, receiver);
						} catch (MalformedURLException | RemoteException
								| NotBoundException e) {
							e.printStackTrace();
							return null;
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
						return null;
					}
				} else {
					receiver = _clients.get(_sender);
				}
				return receiver;
			}
		}

		@Override
		public void run() {
			sendMessage();
		}
	}

	public enum RepeatFrequecy {
		NEVER, HOUR, DAY, WEEK;
	}

	/* !message handling */

	/* initConnection */
	private void initConnection() {
		try {
			String url = "rmi://" + InetAddress.getLocalHost().getHostAddress()
					+ "/Clock";
			try {
				// FIXME WithConf
				Naming.rebind(url, _instance);
			} catch (RemoteException | MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_clients = new HashMap<String, IClockClient>();
	}

	/* !initConnection */

	/* main */
	public static void main(String[] args) {
		Clock c = Clock.getInstance();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Entrez votre commande :");
		try {
			String s;
			String[] cmdArgs;

			do {
				s = br.readLine();
				cmdArgs = s.split(" ");
				if (cmdArgs.length < 1) {
					continue;
				}
				if (cmdArgs[0].equals("start")) {
					int factor = 1;
					Date start = new Date();
					if (cmdArgs.length >= 3) {
						if (cmdArgs[1].equals("-h")) {
							SimpleDateFormat formatter = new SimpleDateFormat(
									"MM/dd/yyyy-HH:mm:ss");
							start = formatter.parse(cmdArgs[2]);
						} else if (cmdArgs[1].equals("-f")) {
							factor = Integer.parseInt(cmdArgs[2]);
						}
						if (cmdArgs.length >= 5) {
							if (cmdArgs[3].equals("-h")) {
								SimpleDateFormat formatter = new SimpleDateFormat(
										"MM/dd/yyyy-HH:mm:ss");
								start = formatter.parse(cmdArgs[5]);
							} else if (cmdArgs[3].equals("-f")) {
								factor = Integer.parseInt(cmdArgs[5]);
							}
						}
					}
					if (c != null)
						c.kill();
					c.start(start, factor);
				} else if (cmdArgs[0].equals("factor")) {
					int factor = Integer.parseInt(cmdArgs[1]);
					c.setFactor(factor);
				} else if (cmdArgs[0].equals("quit")) {
					System.exit(0);
				} else {
					System.out
							.println("Commande inconnue, tapez help pour la liste des commandes disponibles");
				}
				System.out.println("Entrez votre commande :");
			} while (true);

		} catch (Exception e) {
			System.err.println("Erreur : " + e.getMessage());
		}
	}
	/* !main */
}
