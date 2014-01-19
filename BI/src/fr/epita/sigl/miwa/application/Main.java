package fr.epita.sigl.miwa.application;

import java.util.Date;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.enums.EClockMessage;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws AsyncFileException,
	AsyncMessageException {
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		Conf.getInstance();
		SyncMessFactory.initSyncMessReceiver();	
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
		.initListener(new AsyncMessageListener());
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		/* CODE HERE */
		Date clockDate = ClockClient.getClock().getHour();
		// Réveil à 9h pour les statistiques sur les stocks
		Date reveilStock = new Date(clockDate.getYear(), clockDate.getMonth(), clockDate.getDay() + 1, 9, 0);
		ClockClient.getClock().wakeMeUpEveryDays(reveilStock, EClockMessage.STOCK.toString());
		// Réveil à 22h pour les ventes
		Date reveilVente = new Date(clockDate.getYear(), clockDate.getMonth(), clockDate.getDay() + 1, 23, 0);
		ClockClient.getClock().wakeMeUpEveryDays(reveilVente, EClockMessage.VENTE.toString());
		//Réveil à 23h pour la répartition des moyens de paiement
		Date reveilPayment = new Date(clockDate.getYear(), clockDate.getMonth(), clockDate.getDay() + 1, 23, 0);
		ClockClient.getClock().wakeMeUpEveryDays(reveilPayment, EClockMessage.REP_PAYMENT.toString());
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
		.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
