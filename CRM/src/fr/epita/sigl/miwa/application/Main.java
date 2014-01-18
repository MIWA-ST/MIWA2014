package fr.epita.sigl.miwa.application;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.epita.sigl.miwa.application.BDD.JdbcConnection;
import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
import fr.epita.sigl.miwa.application.object.Client;
import fr.epita.sigl.miwa.application.object.Group;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws AsyncFileException,
			AsyncMessageException, IOException, SAXException, ParseException {
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		/*Conf.getInstance();
		SyncMessFactory.initSyncMessReceiver();	
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.initListener(new AsyncMessageListener());*/
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		/* CODE HERE */
		//Client.clientsList = new ArrayList<>();
		
		//Date date = ClockClient.getClock().getHour();
		
		//XMLManager.getInstance().getSegmentationClient("coucou");
		Client.clientsList = new ArrayList<Client>();
		//XMLManager.getInstance().getDemandeCreationCompte("lol", "creation compte internet.xml");
		XMLManager.getInstance().getCreationTypeCarte("Silver");
		XMLManager.getInstance().getCreationTypeCarte("Gold");
		XMLManager.getInstance().dispatchXML("coucou", "BO ticket caisse.xml");
		JdbcConnection.getInstance().GetClientInternet("831356");
		//System.out.println(ClockClient.getClock().getHour());
		//ClockClient.getClock().wakeMeUpEveryDays(date, "baseclient");
		
		//DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = factory.newDocumentBuilder();
		
		//Document doc = new DocumentBuilderFactory.newDocumentBuilder().parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <ENTETE objet=\"information-client\" source=\"crm\" date=\"2014/01/17\"> <CLIENTS>  <CLIENT id=\"01\" civilite=\"M\" naissance=\"AAAAA-MM-JJ\" codepostal=\"75000\" situationfam=\"Marie\" nbenfant=\"3\" typecarte=\"Super+\" /> </CLIENTS> </ENTETE>")));
		//Document doc = builder.parse(new InputSource(new StringReader()));  
		
		SyncMessHandler.getSyncMessSender().sendMessage(
				EApplication.INTERNET, "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <ENETE objet=\"matricule-client\" source=\"crm\" date=\"AAAAA-MM-JJ\"/> <INFORMATIONS> <CLIENT matricule=\"0001\" nom=\"Doe\" prenom=\"John\" /> </INFORMATIONS>");
		
		//AsyncMessageFactory.getInstance().getAsyncMessageManager().send("Message Async vers GC", EApplication.GESTION_COMMERCIALE);
		
		//AsyncFileFactory.getInstance().getFileManager().send("bi.xml", EApplication.BI);
		
		
		
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
