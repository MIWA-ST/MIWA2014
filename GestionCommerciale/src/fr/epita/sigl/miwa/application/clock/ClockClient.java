package fr.epita.sigl.miwa.application.clock;

import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;

import fr.epita.sigl.miwa.application.DemandeNiveauStock;
import fr.epita.sigl.miwa.application.JdbcConnection;
import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.XMLManager;
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
	static public void wakeUp(Date date, Object message) {
		try {
		if (message instanceof String) {
			if (message.equals("BO")) {
				String content = "";
				DemandeNiveauStock demand = new DemandeNiveauStock();
				demand.setCommandNumber(ClockClient.getClock().getHour().toString());
				demand.setRefbo("0000001");
				JdbcConnection.getInstance().getConnection();
				demand.setArticles(JdbcConnection.getInstance().envoiPrixArticle());
				JdbcConnection.getInstance().closeConnection();
				content = XMLManager.getInstance().envoidemandeniveaudestocktoBO(demande);
			}
			else if ("BI")
			{
				
			}
			else {
				System.out.println(date.toString() + " : " + message);
			}
		}
		} catch (AsyncMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
