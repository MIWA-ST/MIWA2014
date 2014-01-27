package fr.epita.sigl.miwa.application;

import java.nio.channels.AsynchronousByteChannel;
import java.util.Calendar;
import java.util.Date;
//import java.util.logging.Logger;





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
	 //private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

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
		Calendar calen = Calendar.getInstance();
		calen.setTime(d);
		calen.set(Calendar.HOUR_OF_DAY, 7);
		calen.set(Calendar.MINUTE, 0);
		ClockClient.getClock().wakeMeUpEveryDays(calen.getTime(), "BI");
		calen.set(Calendar.HOUR_OF_DAY, 4);
		ClockClient.getClock().wakeMeUpEveryDays(calen.getTime(), "BO");
		// Pour se faire appeler à une certaine heure :
		 ClockClient.getClock().wakeMeUpEveryDays(new Date("23/12/2013"),
		 "envoie_msg_BO");
		// ClockClient.getClock().wakeMeUpEveryDays(new Date
		// ("23/12/2013 07:00"), "envoi_stocks");

		//SyncMessHandler.answerToRequestMessage(EApplication.INTERNET, "<DEMANDENIVEAUDESTOCKINTERNET><numero>CV6598</numero><date>20131225</date><ARTICLES><ARTICLE><REFERENCE>05280cc5-6804-4687-a39b-6c0446df</REFERENCE></ARTICLE><ARTICLE><REFERENCE>073e5d55-02f2-4e65-99c9-3e498ac8</REFERENCE></ARTICLE><ARTICLE><REFERENCE>096252df-1961-4133-a9a4-aa9debda</REFERENCE></ARTICLE><ARTICLE><REFERENCE>0be5ef2f-95cf-4873-9904-35dcd9a3</REFERENCE></ARTICLE><ARTICLE><REFERENCE>1462d20b-3ee6-4193-b6e1-b5d50a06</REFERENCE></ARTICLE><ARTICLE><REFERENCE>34e1adf9-0172-42da-9755-e2863f02</REFERENCE></ARTICLE><ARTICLE><REFERENCE>38d0da04-0468-4bb5-ac85-3a46de6c</REFERENCE></ARTICLE><ARTICLE><REFERENCE>3a2877da-e754-445b-808c-7f9d064d</REFERENCE></ARTICLE><ARTICLE><REFERENCE>582271b6-d466-4179-9fa9-b9111ef3</REFERENCE></ARTICLE><ARTICLE><REFERENCE>76436bdc-936c-4975-9f63-2a1e1c4a</REFERENCE></ARTICLE><ARTICLE><REFERENCE>8063e4a1-bdeb-44bf-82ec-fdb35b07</REFERENCE></ARTICLE><ARTICLE><REFERENCE>81aab51f-8943-4af2-914b-442b9260</REFERENCE></ARTICLE><ARTICLE><REFERENCE>8311ec7f-d9ad-4310-9de7-c20b0863</REFERENCE></ARTICLE><ARTICLE><REFERENCE>877a203f-649f-4baa-9a2f-2e1196b4</REFERENCE></ARTICLE><ARTICLE><REFERENCE>9d078ed6-3297-49d0-a4a6-6383d068</REFERENCE></ARTICLE><ARTICLE><REFERENCE>aa2f8d93-db2f-4f45-96ad-8b900914</REFERENCE></ARTICLE><ARTICLE><REFERENCE>d57a6a84-ec63-4458-a055-a893c855</REFERENCE></ARTICLE><ARTICLE><REFERENCE>dc07244a-a4b0-47e7-adb1-0969b8da</REFERENCE></ARTICLE><ARTICLE><REFERENCE>e9ff6366-acdb-404b-89a9-6257a186</REFERENCE></ARTICLE><ARTICLE><REFERENCE>fb72b352-7ad9-4c52-b783-7b7227c2</REFERENCE></ARTICLE></ARTICLES></DEMANDENIVEAUDESTOCKINTERNET>");
		 
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
