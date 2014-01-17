package fr.epita.sigl.miwa.application;

import java.util.Date;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
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
		try {
			//ClockClient.getClock().wakeMeUp(new Date("12/13/2013 23:12:13"), "Test");
			EApplication to = EApplication.INTERNET;
			ClockClient.wakeUp(new Date("12/13/2013 23:12:13"), to.toString());

			
			ParseXML parser = new ParseXML("src/fr/epita/sigl/miwa/application/MDM/Envoi_produits_MDM_to_IN.xml");
			parser.readXML2();
			parser.parseMDM("stream");
			
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SyncMessHandler.getSyncMessSender().sendMessage(
				EApplication.INTERNET, "Coucou IN");
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
