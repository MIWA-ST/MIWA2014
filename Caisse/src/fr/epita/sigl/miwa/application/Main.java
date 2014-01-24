package fr.epita.sigl.miwa.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;

import org.eclipse.swt.widgets.DateTime;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
			// fin de la clock

			// CI DESSOUS TEST MANUEL POUR LE PARSING XML DEPUIS LE BO VERS NOUS
			// AsyncFileFactory.getInstance().getFileManager().send("toto.xml",
			// EApplication.CAISSE);
			String myXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			myXML += "<ARTICLES>";
			myXML += "<ARTICLE nomarticle=\"Table\" refarticle=\"565644\" prix=\"200.99\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Chaise\" refarticle=\"565645\" prix=\"35.76\" promotion=\"15\" />";
			myXML += "<ARTICLE nomarticle=\"Stylo noir\" refarticle=\"565646\" prix=\"1.67\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Stylo bleu\" refarticle=\"565647\" prix=\"1.67\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Stylo rouge\" refarticle=\"565648\" prix=\"1.67\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Stylo vert\" refarticle=\"565649\" prix=\"1.67\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"MacBook Pro\" refarticle=\"565650\" prix=\"3600.45\" promotion=\"5\" />";
			myXML += "<ARTICLE nomarticle=\"Mac Pro\" refarticle=\"565651\" prix=\"13000.99\" promotion=\"5\" />";
			myXML += "<ARTICLE nomarticle=\"Tasses à café (pack de 50)\" refarticle=\"565652\" prix=\"7.90\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"petit skype en boite\" refarticle=\"565673\" prix=\"44.90\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Côte de porc\" refarticle=\"764567876\" prix=\"5.76\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Blanquette de veau\" refarticle=\"54676234\" prix=\"4.85\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Tomates\" refarticle=\"4564323456\" prix=\"2.86\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Melon\" refarticle=\"6545643\" prix=\"1.12\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Courgette\" refarticle=\"8743467\" prix=\"10.57\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Cerises\" refarticle=\"7643467654\" prix=\"3.86\" promotion=\"10\" />";
			myXML += "<ARTICLE nomarticle=\"Raisins\" refarticle=\"4564323456\" prix=\"2.87\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Cornichon\" refarticle=\"765434565\" prix=\"0.56\" promotion=\"10\" />";
			myXML += "<ARTICLE nomarticle=\"Conserve ravioli\" refarticle=\"54456432\" prix=\"4.96\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Pâtes\" refarticle=\"4567654345\" prix=\"6.96\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Sauce tomate\" refarticle=\"435676543\" prix=\"1.32\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Cigarettes\" refarticle=\"987654\" prix=\"5.65\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Voiture de courses\" refarticle=\"0987654\" prix=\"63123\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Baguette de pain\" refarticle=\"1234567\" prix=\"0.96\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Sucre\" refarticle=\"43234567\" prix=\"0.26\" promotion=\"10\" />";
			myXML += "<ARTICLE nomarticle=\"Sel\" refarticle=\"234789\" prix=\"0.26\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Gateau\" refarticle=\"987632\" prix=\"5.76\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Confiture\" refarticle=\"9876523456\" prix=\"6.43\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Chicoré\" refarticle=\"987645\" prix=\"2.54\" promotion=\"5\" />";
			myXML += "<ARTICLE nomarticle=\"Poulet\" refarticle=\"234576587\" prix=\"3.76\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Pizza\" refarticle=\"2345643\" prix=\"4.87\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Hachis parmentier\" refarticle=\"456545654\" prix=\"3.30\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Lasagne\" refarticle=\"1234321232\" prix=\"2.65\" promotion=\"10\" />";
			myXML += "<ARTICLE nomarticle=\"Galette\" refarticle=\"8987878\" prix=\"7.54\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Miel\" refarticle=\"765677\" prix=\"9.71\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Haricot vert\" refarticle=\"67876543456\" prix=\"2.12\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Carottes\" refarticle=\"45433456\" prix=\"3.61\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Patates\" refarticle=\"8767898765\" prix=\"2.76\" promotion=\"10\" />";
			myXML += "<ARTICLE nomarticle=\"Fromage\" refarticle=\"2345678\" prix=\"2.54\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Bonbons\" refarticle=\"876543\" prix=\"3.34\" promotion=\"5\" />";
			myXML += "<ARTICLE nomarticle=\"Cannettes\" refarticle=\"2346789\" prix=\"4.34\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"eau\" refarticle=\"9876543\" prix=\"1.10\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"coca\" refarticle=\"1234567\" prix=\"1.60\" promotion=\"10\" />";
			myXML += "<ARTICLE nomarticle=\"fanta\" refarticle=\"9876543\" prix=\".66\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"vodka\" refarticle=\"4567876543\" prix=\"16.20\" promotion=\"10\" />";
			myXML += "<ARTICLE nomarticle=\"redbull\" refarticle=\"23456432\" prix=\"5.45\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"ricard\" refarticle=\"987656787\" prix=\"18.20\" promotion=\"10\" />";
			myXML += "<ARTICLE nomarticle=\"saké\" refarticle=\"532871545\" prix=\"17.20\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Paté\" refarticle=\"38187358\" prix=\"3.98\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Jambon\" refarticle=\"3618738\" prix=\"4.21\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Saucisson\" refarticle=\"725854287\" prix=\"4.30\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Café\" refarticle=\"168361876\" prix=\"2.34\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Thé\" refarticle=\"1351753751\" prix=\"1.34\" promotion=\"5\" />";
			myXML += "<ARTICLE nomarticle=\"Malabar\" refarticle=\"51254715\" prix=\"1.45\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"fabien patou\" refarticle=\"37153715\" prix=\"660.20\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Chips\" refarticle=\"86286946\" prix=\"3.23\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"bretzel\" refarticle=\"5175487\" prix=\"5.10\" promotion=\"5\" />";
			myXML += "<ARTICLE nomarticle=\"olives\" refarticle=\"17517148614\" prix=\"2.56\" promotion=\"10\" />";
			myXML += "<ARTICLE nomarticle=\"Chèvre\" refarticle=\"117547154\" prix=\"3.90\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Kiri\" refarticle=\"73517537\" prix=\"2.87\" promotion=\"5\" />";
			myXML += "<ARTICLE nomarticle=\"Salade\" refarticle=\"87542875487\" prix=\"1.65\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"oignon\" refarticle=\"351546486\" prix=\"1.76\" promotion=\"5\" />";
			myXML += "<ARTICLE nomarticle=\"big mac\" refarticle=\"875482754872\" prix=\"4.30\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"couscous\" refarticle=\"2754752946\" prix=\"7.89\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"Coq au vin\" refarticle=\"74287548752\" prix=\"26.98\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"saumon\" refarticle=\"12121578\" prix=\"13.45\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"truite\" refarticle=\"6754287452\" prix=\"16.20\" promotion=\"0\" />";
			myXML += "<ARTICLE nomarticle=\"thon\" refarticle=\"6724826748\" prix=\"3.10\" promotion=\"5\" />";
			myXML += "<ARTICLE nomarticle=\"oasis\" refarticle=\"13751734875\" prix=\"2.67\" promotion=\"0\" />";
			myXML += "</ARTICLES>";
			//XmlParser.ParseBOString(myXML);
			
			
			/*DocumentBuilder db = null;
			try {
				db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(myXML));

			Document doc = null;
			try {
				doc = db.parse(is);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			XmlParser.ParseBOFile(doc);*/
			
			
			System.out.println("TEST !!!!!!!");
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
