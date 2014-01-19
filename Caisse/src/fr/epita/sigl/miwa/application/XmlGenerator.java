package fr.epita.sigl.miwa.application;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class XmlGenerator {
	private static final Logger LOGGER = Logger.getLogger(XmlGenerator.class.getName());

	public String GetCurrentClockDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ClockClient.getClock().getHour());
		String theDate = Integer.toString(cal.get(Calendar.YEAR)) + "-"
				+ Integer.toString(cal.get(Calendar.MONTH)) + "-"
				+ Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

		return theDate;
	}

	public void SendTicketToBO(Set<Produit> produits, String idClient,
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
			AsyncMessageFactory.getInstance().getAsyncMessageManager().send(xml, EApplication.BACK_OFFICE);
			LOGGER.info("***** Caisse : ticket de fin de vente envoyé au back-office");
		} catch (AsyncMessageException e1) {
			LOGGER.info("***** Caisse : erreur, l'envoi du ticket de fin de vente au back-office a généré une exception");
		}
		// TODO SEND THIS TICKET TO BO
	}

}
