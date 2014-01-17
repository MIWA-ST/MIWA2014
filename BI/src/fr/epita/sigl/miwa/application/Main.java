package fr.epita.sigl.miwa.application;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.enums.EClockMessage;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.application.parser.BIParser;
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
		try {
//			Date clockDate = ClockClient.getClock().getHour();
//			// Réveil à 9h pour les statistiques sur les stocks
//			ClockClient.getClock().wakeMeUpEveryDays(new Date(clockDate.getYear(), clockDate.getMonth(), clockDate.getDay() + 1, 9, 0), EClockMessage.STOCK);
//			// Réveil à 22h pour l'évolution des ventes
//			ClockClient.getClock().wakeMeUpEveryDays(new Date(clockDate.getYear(), clockDate.getMonth(), clockDate.getDay() + 1, 22, 0), EClockMessage.EVOL_VENTE);
//			// Réveil à 23h pour la répartition des ventes
//			ClockClient.getClock().wakeMeUpEveryDays(new Date(clockDate.getYear(), clockDate.getMonth(), clockDate.getDay() + 1, 23, 0), EClockMessage.REP_VENTE);
//			//Réveil à 23h pour la répartition des moyens de paiement
//			ClockClient.getClock().wakeMeUpEveryDays(new Date(clockDate.getYear(), clockDate.getMonth(), clockDate.getDay() + 1, 23, 0), EClockMessage.REP_PAYMENT);
			Thread.sleep(3000000000L);
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
