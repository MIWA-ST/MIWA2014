package fr.epita.sigl.miwa.application.clock;

import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.clock.ClockFactory;
import fr.epita.sigl.miwa.st.clock.IExposedClock;

public class ClockClient {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	/*
	 * R�cup�re l'horloge serveur pour faire des requ�tes dessus (getHour, wakeMeUp, ...)
	 */
	static public IExposedClock getClock() {
		return ClockFactory.getServerClock();
	}
	
	/*
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsque l'horloge vous contacte
	 */
	@Deprecated
	static public void wakeUp(Date date, Object message) throws AsyncMessageException {
		if (message instanceof String) {
			if (message.equals("Hello World!")) {
				System.out.println(date.toString() + " : Hello dear client!");
			} else if (message.equals("envoie_msg_BO")) {
				String content = "";
				try {
					DocumentBuilder db = DocumentBuilderFactory.newInstance()
							.newDocumentBuilder();
					InputSource is = new InputSource();
					LOGGER.info("On demande les niveaux de stock à Back office");
					// A faire envoi niveau de stock
					content = "<DEMANDENIVEAUDESTOCK><NUMERO>CV398719873</NUMERO><REFMAGASIN>PA218765</REFMAGASIN><DATE>20131225</DATE><ARTICLES><ARTICLE><REFERENCE>AU736827</REFERENCE></ARTICLE><ARTICLE><REFERENCE>AU736829</REFERENCE></ARTICLE></ARTICLES></DEMANDENIVEAUDESTOCK>";
					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.BACK_OFFICE);
					LOGGER.info("Envoi des stocks au back office");
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (message.equals("envoi_stocks")) {
				String content = "";
				try {
					DocumentBuilder db = DocumentBuilderFactory.newInstance()
							.newDocumentBuilder();
					InputSource is = new InputSource();
					LOGGER.info("On Envoi les niveaux de stock à BI");
					// A faire envoi niveau de stock
					content = "<XML>    <ENTETE objet=\"information stock\" source=\"GC\" date=\"1/01/2014\" />    <STOCKS lieu=\"Entrepot\">        <STOCK ref-article=\"11111\" stock=\"2\" commande=\"3\" max=\"10\" />        <STOCK ref-article=\"2222\" stock=\"0\" commande=\"1\" max=\"1\" />        <STOCK ref-article=\"33333\" stock=\"3\" commande=\"3\" max=\"99\" />    </STOCKS></XML>";
					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.BI);
					LOGGER.info("Envoi les stocks a la BI");
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				System.out.println(date.toString() + " : " + message);
			}
		}
	}	
}
