package fr.epita.sigl.miwa.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.ws.Holder;

import org.eclipse.swt.widgets.DateTime;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.ihm.Home;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	public static final BddAccess bdd = new BddAccess();
	public static ThreadVente ventealeatoires = new ThreadVente();
	public static ThreadIHM ihm = new ThreadIHM();
	public static boolean open = false;
	
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
			new BufferedReader(new InputStreamReader(System.in)).readLine();
			
			//connect BDD		
			bdd.connect();
			//je veux etre reveillé à 9H00
			Calendar dateouverture = Calendar.getInstance();
			dateouverture.setTime(ClockClient.getClock().getHour());
			Date nextOccurence = new Date();
			dateouverture.set(Calendar.HOUR_OF_DAY, 9);
			dateouverture.set(Calendar.MINUTE, 0);
			dateouverture.set(Calendar.SECOND, 0);
			dateouverture.set(Calendar.MILLISECOND, 0);
			if (dateouverture.get(Calendar.HOUR_OF_DAY) >= 9) 
			dateouverture.add((Calendar.DAY_OF_MONTH), 1);
			nextOccurence = dateouverture.getTime();
			ClockClient.getClock().wakeMeUpEveryDays(nextOccurence, "ouverture");
			System.out.println(ClockClient.getClock().getHour());
			//je veux etre reveillé à 21H00
			dateouverture = Calendar.getInstance();
			dateouverture.setTime(ClockClient.getClock().getHour());
			nextOccurence = new Date();
			dateouverture.set(Calendar.HOUR_OF_DAY, 21);
			dateouverture.set(Calendar.MINUTE, 0);
			dateouverture.set(Calendar.SECOND, 0);
			dateouverture.set(Calendar.MILLISECOND, 0);
			if (dateouverture.get(Calendar.HOUR_OF_DAY) >= 21) 
			dateouverture.add((Calendar.DAY_OF_MONTH), 1);
			nextOccurence = dateouverture.getTime();
			ClockClient.getClock().wakeMeUpEveryDays(nextOccurence, "fermeture");
			//Fin des réveilles
			new BufferedReader(new InputStreamReader(System.in)).readLine();	
			
			// TODO CHECK RECPTION MESSAGe, FILE DU BO + TRUC FIDELITE POUR MAJ PRIX
			//String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ENTETE objet=\"article-prix-promo-update\" source=\"bo\" date=\"2013-12-18\"/><ARTICLES><ARTICLE nomarticle=\"Chocapic\" refarticle=\"45678765434567\" prix=\"12\" promotion=\"50\" /></ARTICLES>";
		//XmlParser.ParseBOString(xml);	
			//fin de la clock
			
			// CI DESSOUS TEST MANUEL POUR LE PARSING XML DEPUIS LE BO VERS NOUS
			//AsyncFileFactory.getInstance().getFileManager().send("toto.xml", EApplication.CAISSE);
				//	String myXML = "<?xml version=\"1.0\"?><ARTICLES><ARTICLE nomarticle=\"toto\" refarticle=\"565644\" prix=\"54.76\" promotion=\"45\" /></ARTICLES>";
					//XmlParser.ParseBOString(myXML);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//SyncMessHandler.getSyncMessSender().sendMessage(EApplication.BI, "Coucou BI");
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
