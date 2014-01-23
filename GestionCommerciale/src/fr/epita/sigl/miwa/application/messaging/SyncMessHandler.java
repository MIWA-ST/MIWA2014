package fr.epita.sigl.miwa.application.messaging;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.epita.sigl.miwa.application.DemandeNiveauStock;
import fr.epita.sigl.miwa.application.JdbcConnection;
import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.XMLManager;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.sync.ISyncMessSender;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class SyncMessHandler {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	/*
	 * A utiliser pour pouvoir envoyer des messages synchrones ou faire des
	 * request
	 */
	static public ISyncMessSender getSyncMessSender() {
		return SyncMessFactory.getSyncMessSender();
	}

	/*
	 * l'application sender vous envoie la string message. Vous ne devez faire
	 * aucun appel � cette fonction, seulement remplir le code Elle est
	 * automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public boolean receiveMessage(EApplication sender, String message) {
		
		System.out.println("TAGADAAAAAA");
		try {
			String root = "";
			String content = "";

			LOGGER.info("***** Message reçu par GC");

			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(message));

			Document doc = db.parse(is);
			root = doc.getFirstChild().getNodeName();
			if (sender == EApplication.BACK_OFFICE) {
				LOGGER.info("Message synchrone reçu du back office :" + message);

			} else if (sender == EApplication.INTERNET) {
				if (root.toLowerCase().equals("demandeniveaudestockinternet")) {
					LOGGER.severe("*****: demande des niveaux de stock depuis INTERNET");

					DemandeNiveauStock dns = XMLManager.getInstance()
							.getdemandeniveaustockfromInternet(message, doc);
					LOGGER.severe("*****: demande niveau de stock recu depuis internet : "
							+ dns.getCommandNumber());
					JdbcConnection.getInstance().getConnection();
					DemandeNiveauStock demande = JdbcConnection.getInstance()
							.envoiStock(dns);
					JdbcConnection.getInstance().closeConnection();
					int i = 0;
					while (i < demande.getArticles().size()) {
						LOGGER.info("*****: demande de stock pour article : "
								+ demande.getArticles().get(i) + " quantité :"
								+ demande.getQuantity().get(i));
					}

					content = XMLManager.getInstance()
							.envoiniveaustocktoInternet(demande);
					LOGGER.info("*****: XML généré : " + content);
					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.INTERNET);
					LOGGER.severe("*****: niveau de stock envoyé à Internet");

				}
			} else if (sender == EApplication.MDM) {
				LOGGER.info("Message synchrone reçu du référentiel :" + message);
			} else if (sender == EApplication.ENTREPOT) {
				LOGGER.info("Message synchrone reçu de l'entrepot :" + message);
			} else {
				LOGGER.info("Mesasage synchrone reçu de : " + sender.toString()
						+ " non traité.");
			}

			return false;
		} catch (Exception e) {

		}
		return false;
	}

	/*
	 * L'application sender vous demande request Vous devez lui renvoyer une
	 * string Vous ne devez faire aucun appel � cette fonction, seulement
	 * remplir le code Elle est automatiquement appel�e lorsqu'une application
	 * vous contacte
	 */
	@Deprecated
	static public String answerToRequestMessage(EApplication sender,
			String request) {
		System.out.println("TOTOOOOOOOOOOOOOOOOOO");
		// TODO Auto-generated method stub
		if (sender == EApplication.BACK_OFFICE) {
			LOGGER.info("Request synchrone reçu du back office :" + request);
		} else if (sender == EApplication.INTERNET) {
			LOGGER.info("Request synchrone reçu d'internet :" + request);
		} else if (sender == EApplication.MDM) {
			LOGGER.info("Request synchrone reçu du référentiel :" + request);
		} else if (sender == EApplication.ENTREPOT) {
			LOGGER.info("Request synchrone reçu de l'entrepot :" + request);
		} else {
			LOGGER.info("Request synchrone reçu de : " + sender.toString()
					+ " non traité.");
		}

		return null;
	}

	/*
	 * L'application sender vous envoie le XML xml Vous ne devez faire aucun
	 * appel � cette fonction, seulement remplir le code Elle est
	 * automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public boolean receiveXML(EApplication sender, Document xml) {
		// TODO Auto-generated method stub
		if (sender == EApplication.BACK_OFFICE) {
			LOGGER.info("XML synchrone reçu du back office :"
					+ xml.getDocumentURI());
		} else if (sender == EApplication.INTERNET) {
			LOGGER.info("XML synchrone reçu d'internet :"
					+ xml.getDocumentURI());
		} else if (sender == EApplication.MDM) {
			LOGGER.info("XML synchrone reçu du référentiel :"
					+ xml.getDocumentURI());
		} else if (sender == EApplication.ENTREPOT) {
			LOGGER.info("XML synchrone reçu de l'entrepot :"
					+ xml.getDocumentURI());
		} else {
			LOGGER.info("XML synchrone reçu de : " + sender.toString()
					+ " non traité.");
		}

		return false;
	}

	/*
	 * L'application sender vous demande un XML avec la requete request Vous ne
	 * devez faire aucun appel � cette fonction, seulement remplir le code Elle
	 * est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public Document answerToRequestXML(EApplication sender,
			String request) {
		// TODO Auto-generated method stub
		if (sender == EApplication.BACK_OFFICE) {
			LOGGER.info("Demande XML et request synchrone reçu du back office :"
					+ request);
		} else if (sender == EApplication.INTERNET) {
			LOGGER.info("Demande XML et request synchrone reçu d'internet :"
					+ request);
		} else if (sender == EApplication.MDM) {
			LOGGER.info("Demande XML et request synchrone reçu du référentiel :"
					+ request);
		} else if (sender == EApplication.ENTREPOT) {
			LOGGER.info("Demande XML et request synchrone reçu de l'entrepot :"
					+ request);
		} else {
			LOGGER.info("Demande XML et request synchrone reçu de : "
					+ sender.toString() + " non traité.");
		}

		return null;
	}

	private SyncMessHandler() {

	}
}
