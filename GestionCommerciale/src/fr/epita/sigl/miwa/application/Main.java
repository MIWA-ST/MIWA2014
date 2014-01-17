package fr.epita.sigl.miwa.application;

import java.util.Date;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
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
		
		Date d = ClockClient.getClock().getHour();
		System.out.println(d);
		
		// Pour se faire appeler à une certaine heure :
		// ClockClient.getClock().wakeMeUpEveryDays(new Date("23/12/2013"), "envoie_msg_BO");
		// ClockClient.getClock().wakeMeUpEveryDays(new Date ("23/12/2013 07:00"), "envoi_stocks");
		String xml = "<COMMANDESFOURNISSEUR><COMMANDE><NUMERO>";
		xml += "0000007</NUMERO><DATEBC>20131225" + "</DATEBC><ARTICLES>";
			xml += "<ARTICLE><REFERENCE>11111" + 
					"</REFERENCE><QUANTITE>12"+ "</QUANTITE><CATEGORIE>" 
					+ "111</CATEGORIE></ARTICLE>";
		xml += "</ARTICLES></COMMANDE></COMMANDESFOURNISSEUR>";
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(xml, EApplication.ENTREPOT);
		
		try {
			Thread.sleep(1000000);
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
