package fr.epita.sigl.miwa.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
import fr.epita.sigl.miwa.db.InitMysqlConnector;
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
		new BufferedReader(new InputStreamReader(System.in));	
		Date clockDate = ClockClient.getClock().getHour();
		System.out.println(clockDate);
	
		// Init MySQL connector
		InitMysqlConnector.init();
				
	    DocumentBuilder db;
		try 
		{
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputSource is = new InputSource();
		    // TEST Creation de compte fidélité
		    is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_compte_cf\" action=\"c\"><compte_cf><matricule_client>C987654321</matricule_client><BIC>ABCD</BIC><IBAN>EFGH</IBAN><id_type_cf>bronze</id_type_cf></compte_cf></monetique>"));
		    // TEST Modification de compte fidélité
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_compte_cf\" action=\"m\"><compte_cf matricule_client=\"C987654321\"><BIC>ABCD</BIC><IBAN>EFGH</IBAN><id_type_cf>silver</id_type_cf></compte_cf></monetique>"));
		    // TEST Suppression de compte fidélité
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_compte_cf\" action=\"s\"><compte_cf matricule_client=\"C987654321\"></compte_cf></monetique>"));	    
		    
		    // TEST Paiement CB
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"paiement_cb\"><montant>12.00</montant><cb><numero>XXXXXXXXXXXXXXXX</numero><date_validite>MMAA</date_validite><pictogramme>XXX</pictogramme></cb></monetique>"));
		    // TEST Paiement CF
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"paiement_cf\"><montant>150.50</montant><matricule_client>C123456789</matricule_client></monetique>"));

		    // TEST Ajout carte fidélité
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_type_carte\" action=\"c\"><type_cf><id>12GOLD</id><limite_mesuelle>100.00</limite_mesuelle><limite_totale>1200.00</limite_totale><nb_echelon>3</nb_echelon></type_cf></monetique>"));
		    // TEST Modification d'une carte fidélité
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_type_carte\" action=\"m\"><type_cf id=\"12GOLD\"><limite_mesuelle>500.00</limite_mesuelle><limite_totale>2000.00</limite_totale><nb_echelon>2</nb_echelon></type_cf></monetique>"));
		    
		    Document doc = db.parse(is);
			SyncMessHandler.getSyncMessSender().sendXML(EApplication.MONETIQUE, doc);
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		
		/*try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*AsyncMessageFactory.getInstance().getAsyncMessageManager().send(message, destination);
		SyncMessFactory.getSyncMessSender().sendMessage(to, message)
		ClockClient.getClock().wakeMeUp(date, message);*/
//		SyncMessHandler.getSyncMessSender().sendMessage(
//				EApplication.BI, "Coucou BI");
		/* !CODE HERE */
		
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
