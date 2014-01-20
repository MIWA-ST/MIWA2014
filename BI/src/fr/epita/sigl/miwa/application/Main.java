package fr.epita.sigl.miwa.application;

import java.util.Calendar;
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
		Calendar reveilStock = Calendar.getInstance();
		reveilStock.setTime(clockDate);
		reveilStock.add(Calendar.DAY_OF_MONTH, 1);
		reveilStock.set(Calendar.HOUR_OF_DAY, 10);
		reveilStock.set(Calendar.MINUTE, 0);
		LOGGER.info("***** Enregistrement auprès de la Clock pour la génération des statistiques de stock à " + reveilStock.getTime());
		ClockClient.getClock().wakeMeUpEveryDays(reveilStock.getTime(), EClockMessage.STOCK.toString());
		// Réveil à 22h pour les ventes
		Calendar reveilVente = Calendar.getInstance();
		reveilVente.setTime(clockDate);
		reveilVente.add(Calendar.DAY_OF_MONTH, 1);
		reveilVente.set(Calendar.HOUR_OF_DAY, 23);
		reveilVente.set(Calendar.MINUTE, 0);
		LOGGER.info("***** Enregistrement auprès de la Clock pour la génération des statistiques de ventes à " + reveilVente.getTime());
		ClockClient.getClock().wakeMeUpEveryDays(reveilVente.getTime(), EClockMessage.VENTE.toString());
		//Réveil à 23h pour la répartition des moyens de paiement
		Calendar reveilPayment = Calendar.getInstance();
		reveilPayment.setTime(clockDate);
		reveilPayment.add(Calendar.DAY_OF_MONTH, 1);
		reveilPayment.set(Calendar.HOUR_OF_DAY, 23);
		reveilPayment.set(Calendar.MINUTE, 15);
		LOGGER.info("***** Enregistrement auprès de la Clock pour la génération des statistiques de paiement à " + reveilPayment.getTime());
		ClockClient.getClock().wakeMeUpEveryDays(reveilPayment.getTime(), EClockMessage.REP_PAYMENT.toString());
		try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
		.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
