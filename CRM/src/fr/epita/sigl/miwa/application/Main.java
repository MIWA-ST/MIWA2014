package fr.epita.sigl.miwa.application;

import java.io.File;
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
import fr.epita.sigl.miwa.application.object.Article;
import fr.epita.sigl.miwa.application.object.Client;
import fr.epita.sigl.miwa.application.object.Group;
import fr.epita.sigl.miwa.application.object.Promotion;
import fr.epita.sigl.miwa.application.object.TicketVente;
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
		Conf.getInstance();
		SyncMessFactory.initSyncMessReceiver();	
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.initListener(new AsyncMessageListener());
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		/* CODE HERE */
		//Client.clientsList = new ArrayList<>();
		
		// Automatisation base client
		//Date date = ClockClient.getClock().getHour();
		//ClockClient.getClock().wakeMeUpEveryDays(date, "baseclient");
		
		//XMLManager.getInstance().getSegmentationClient("coucou");
		Client.clientsList = new ArrayList<Client>();
		TicketVente.ticketVentesList = new ArrayList<TicketVente>();
		
		//Promotion promo = new Promotion(1, 1, 2, 10, 10, new Date("2014-02-06"));
		
		//Client client = new Client();
		
		JdbcConnection.getInstance().getConnection();
		JdbcConnection.getInstance().GetClients();
		//XMLManager.getInstance().getDemandeCreationCompte("lol", "creation compte internet.xml");
		/*XMLManager.getInstance().getCreationTypeCarte("Silver");
		XMLManager.getInstance().getCreationTypeCarte("Gold");
		XMLManager.getInstance().getDemandeCreationCompte("creation", "creation compte internet.xml");
		//XMLManager.getInstance().getTicketClientFidelise("lol", "BO ticket client fidelise.xml");
		XMLManager.getInstance().getSegmentationClient("lol", "segmentation-client.xml");
		XMLManager.getInstance().dispatchXML("coucou", "BO ticket caisse.xml");
		JdbcConnection.getInstance().GetClientInternet("57310395");
		XMLManager.getInstance().getClientConnecteDemandeReduc("reduc", "connexion client.xml");*/
		//System.out.println(ClockClient.getClock().getHour());
		
		
		//DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = factory.newDocumentBuilder();
		
		//Document doc = new DocumentBuilderFactory.newDocumentBuilder().parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <ENTETE objet=\"information-client\" source=\"crm\" date=\"2014/01/17\"> <CLIENTS>  <CLIENT id=\"01\" civilite=\"M\" naissance=\"AAAAA-MM-JJ\" codepostal=\"75000\" situationfam=\"Marie\" nbenfant=\"3\" typecarte=\"Super+\" /> </CLIENTS> </ENTETE>")));
		//Document doc = builder.parse(new InputSource(new StringReader()));  
		
		//SyncMessHandler.getSyncMessSender().sendMessage(
		//		EApplication.INTERNET, "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <ENTETE objet=\"matricule-client\" source=\"crm\" date=\"2014-01-17\"> <INFORMATIONS> <CLIENT matricule=\"0001\" nom=\"Doe\" prenom=\"John\" /> </INFORMATIONS></ENTETE>");
		
		//SyncMessHandler.getSyncMessSender().sendMessage(
		//		EApplication.INTERNET, 
		//"<ENTETE objet=\"information-client\" source=\"crm\" date=\"2014-01-18\"><INFORMATIONS><CLIENT matricule=\"50762119\" /><PROMOTIONS><PROMOTION article=\"2\" fin=\"2014-01-25\" reduc=\"10\" /></PROMOTIONS></INFORMATIONS></ENTETE>");
		//AsyncMessageFactory.getInstance().getAsyncMessageManager().send("Message Async vers GC", EApplication.GESTION_COMMERCIALE);
		
		//AsyncFileFactory.getInstance().getFileManager().send("bi.xml", EApplication.BI);
		
		//SyncMessHandler.getSyncMessSender().requestMessage(to, request)
		
		/* !CODE HERE */
		//JdbcConnection.getInstance().closeConnection();
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
