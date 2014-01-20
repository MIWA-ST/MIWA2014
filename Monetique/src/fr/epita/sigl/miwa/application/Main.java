package fr.epita.sigl.miwa.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.db.InitMysqlConnector;
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
		System.out.println(clockDate);
		
		// Lancement paiement fin de mois
		ClockClient.getClock().wakeMeUpEveryWeeks(getLastDayInMonth(), "Prélèvement des crédits fidélité en fin de mois");
		
		try {
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (IOException e) {
			System.out.println("LLALA ERROR");
			e.printStackTrace();
		}	
	
		// Init MySQL connector
		InitMysqlConnector.init();
				
	    /*DocumentBuilder db;
		try 
		{
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputSource is = new InputSource();
		    
		    // TEST Creation de compte fidélité
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_compte_cf\" action=\"c\"><compte_cf><matricule_client>C987654321</matricule_client><BIC>ABCD</BIC><IBAN>EFGH</IBAN><id_type_cf>bronze</id_type_cf></compte_cf></monetique>"));
		    // TEST Modification de compte fidélité
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_compte_cf\" action=\"m\"><compte_cf matricule_client=\"C987654321\"><BIC>ABCD</BIC><IBAN>EFGH</IBAN><id_type_cf>silver</id_type_cf></compte_cf></monetique>"));
		    // TEST Suppression de compte fidélité
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_compte_cf\" action=\"s\"><compte_cf matricule_client=\"C987654321\"></compte_cf></monetique>"));	    
		    
		    // TEST Paiement CB
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"paiement_cb\"><montant>12.00</montant><cb><numero>XXXXXXXXXXXXXXXX</numero><date_validite>MMAA</date_validite><pictogramme>XXX</pictogramme></cb></monetique>"));
		    // TEST Paiement CF	    
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"paiement_cf\"><montant>150.50</montant><matricule_client>C123456789</matricule_client></monetique>"));
		    
		    // TEST Ajout carte fidélité
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_type_carte\" action=\"c\"><type_cf><id>12GOLD</id><limite_mensuelle>100.00</limite_mensuelle><limite_totale>1200.00</limite_totale><nb_echelon>3</nb_echelon></type_cf></monetique>"));
		    // TEST Modification d'une carte fidélité
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_type_carte\" action=\"m\"><type_cf id=\"12GOLD\"><limite_mensuelle>500.00</limite_mensuelle><limite_totale>2000.00</limite_totale><nb_echelon>2</nb_echelon></type_cf></monetique>"));
		    // TEST Suppression d'une carte fidélité
		    //is.setCharacterStream(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"cms_type_carte\" action=\"s\"><type_cf id=\"bronze\"><nouvel_id>12GOLD</nouvel_id></type_cf></monetique>"));

		    Document doc = db.parse(is);
			SyncMessHandler.getSyncMessSender().sendXML(EApplication.MONETIQUE, doc);
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}*/

		
		//ClockClient.getClock().wakeMeUp(date, message);
		/* !CODE HERE */
		
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}
	
	public static Date getLastDayInMonth() 
	{
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(new Date());
		  int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		  int month = new Date().getMonth();
		  int year = new Date().getYear();
		  
		  String s = String.valueOf(maxDay) + String.valueOf(month) + String.valueOf(year);
		  SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		  
		  Date d = null;
		  try 
		  {
			d = sdf.parse(s);
		  } 
		  catch (ParseException e) 
		  {
			e.printStackTrace();
		  }
		  return d;
	}

}
