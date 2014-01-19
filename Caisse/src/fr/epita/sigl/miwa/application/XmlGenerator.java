package fr.epita.sigl.miwa.application;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class XmlGenerator {
	private static final Logger LOGGER = Logger.getLogger(XmlGenerator.class
			.getName());

	public static String GetCurrentClockDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ClockClient.getClock().getHour());
		String theDate = Integer.toString(cal.get(Calendar.YEAR)) + "-"
				+ Integer.toString(cal.get(Calendar.MONTH) + 1) + "-"
				+ Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

		return theDate;
	}

	public static boolean CheckCbPaymentWithMo(String total, String numCB,
			String date, String picto) {
		String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"paiement_cb\"><montant>"
				+ total
				+ "</montant><cb><numero>"
				+ numCB
				+ "</numero><date_validite>"
				+ date
				+ "</date_validite><pictogramme>"
				+ picto
				+ "</pictogramme></cb></monetique>";

		DocumentBuilder db = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(message));

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

/*// pour écrire un DOM dans un fichier
TransformerFactory factory = TransformerFactory.newInstance();
Transformer transformer = null;
try {
	transformer = factory.newTransformer();
} catch (TransformerConfigurationException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
Result result = new StreamResult(new File("test.xml"));
Source source = new DOMSource(doc);
try {
	transformer.transform(source, result);
} catch (TransformerException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}*/
		
		LOGGER.info("***** Caisse : envoi d'une demande de paiement CB vers la monétique");
		boolean resultat = SyncMessFactory.getSyncMessSender().sendXML(
				EApplication.MONETIQUE, doc);
		
		if (resultat)
			LOGGER.info("***** Caisse : le paiement CB a été approuvé par la monétique");
		else
			LOGGER.info("***** Caisse : erreur, le paiement CB n'a pas été approuvé par la monétique");
		
		return resultat;
	}

	public static void SendTicketToBO(Set<Produit> produits, String idClient,
			String typePaiement) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ENTETE objet=\"ticket-caisse\" source=\"caisse\" date=\""
				+ GetCurrentClockDate()
				+ "\"/><TICKETVENTE refclient="
				+ "\""
				+ idClient + "\"" + " moyenpayement=\"" + typePaiement + "\">";

		Iterator<Produit> e = produits.iterator();
		Produit current = new Produit();

		while (e.hasNext()) {
			current = e.next();
			xml += "<ARTICLE refarticle=\"" + current.getId()
					+ "\" quantite=\"" + current.getQuantite() + "\" prix=\""
					+ current.getPrix() + "\" />";
		}

		xml += "</TICKETVENTE>";

		try {
			AsyncMessageFactory.getInstance().getAsyncMessageManager()
					.send(xml, EApplication.BACK_OFFICE);
			LOGGER.info("***** Caisse : ticket de fin de vente envoyé au back-office");
		} catch (AsyncMessageException e1) {
			LOGGER.info("***** Caisse : erreur, l'envoi du ticket de fin de vente au back-office a généré une exception");
		}
	}

}
