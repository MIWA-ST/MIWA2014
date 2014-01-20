package fr.epita.sigl.miwa.application;

import java.util.Date;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
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
		
		Date d = ClockClient.getClock().getHour();
		System.out.println(d);
		
		// Pour se faire appeler à une certaine heure :
		// ClockClient.getClock().wakeMeUpEveryDays(new Date("23/12/2013"), "envoie_msg_BO");
		// ClockClient.getClock().wakeMeUpEveryDays(new Date ("23/12/2013 07:00"), "envoi_stocks");
	/*	String xml = "<COMMANDESFOURNISSEUR><COMMANDE><NUMERO>";
		xml += "0000009</NUMERO><DATEBC>20131225" + "</DATEBC><ARTICLES>";
			xml += "<ARTICLE><REFERENCE>11111" + 
					"</REFERENCE><QUANTITE>12"+ "</QUANTITE><CATEGORIE>" 
					+ "111</CATEGORIE></ARTICLE>";
		xml += "</ARTICLES></COMMANDE></COMMANDESFOURNISSEUR>";*/
		// Log > Afficher Commande fournisseur envoyée
		// Log > Ajouter quantité demandé / reçu pour voir si diff (en principe non mais c'est pou faire genre)
		// Log > Afficher bla bla reçu
				

		/*
		String xml = "<REASSORTSBO><REASSORT><NUMERO>0000099</NUMERO><REFBO>12345</REFBO><ADRESSEBO>Rue machin</ADRESSEBO><TELBO>09090909</TELBO><DATEBC>2014</DATEBC><ARTICLES>";
		xml += "<ARTICLE><REFERENCE>11111</REFERENCE><QUANTITE>8</QUANTITE><CATEGORIE>Meuble</CATEGORIE></ARTICLE>";
	xml += "</ARTICLES></REASSORT></REASSORTSBO>";
*/
	// Log > Ajout numéro commande / refBO / article / quantité
	//AsyncMessageFactory.getInstance().getAsyncMessageManager().send(xml, EApplication.ENTREPOT);

/*
		String xml = "<COMMANDESFOURNISSEUR><COMMANDE><NUMERO>";
		xml += "0000009</NUMERO><DATEBC>20131225" + "</DATEBC><ARTICLES>";
			xml += "<ARTICLE><REFERENCE>11111" + 
					"</REFERENCE><QUANTITE>12"+ "</QUANTITE><CATEGORIE>" 
					+ "111</CATEGORIE></ARTICLE>";
		xml += "</ARTICLES></COMMANDE></COMMANDESFOURNISSEUR>";
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(xml, EApplication.ENTREPOT);

*/		String xml;
		xml = "<commande_internet>" + "<commande>" + "<numero>125b76"
				 + "</numero>" + "<refclient>4242"
				 + "</refclient>" + "<datebc>20131212"
				 + "</datebc>" + "<datebl>20131225"
				+ "</datebl>" + "<adresseClient>12rue des oiseaux"
				 + "</adresseClient>" + "<nom>Hollande"
				 + "</nom>" + "<prenom>Francoise"
				 + "</prenom>" + "<articles>";

		
			xml += "<article>" + "<CATEGORIE>"
					+ "121234</CATEGORIE>" + "<reference>"
					+ "0193832436534653</reference>" + "<quantite>"
					+ "1234567890</quantite>"
					+ "</article>";
		
		xml += "</articles></commande></commande_internet>";
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(xml, EApplication.ENTREPOT);
/*
		String XMLENT_BLexp = "<EXPEDITIONCLIENT>";
				XMLENT_BLexp += "<LIVRAISON>";
				XMLENT_BLexp += "<NUMERO>CV398719873</NUMERO>";
				XMLENT_BLexp += "<DATEBC>20131225</DATEBC>";
				XMLENT_BLexp += "<DATEBL>20131225</DATEBL>";
				XMLENT_BLexp += "<ARTICLE>";
				XMLENT_BLexp += "<REFERENCE>AU736827</REFERENCE>";
				XMLENT_BLexp += "<QUANTITE>265000</QUANTITE>";
				XMLENT_BLexp += "<CATEGORIE>XXXXX</CATEGORIE>";
				XMLENT_BLexp += "</ARTICLE>";
				XMLENT_BLexp += "<ARTICLE>";
				XMLENT_BLexp += "<REFERENCE>AU736823</REFERENCE>";
				XMLENT_BLexp += "<QUANTITE>12</QUANTITE>";
				XMLENT_BLexp += "<CATEGORIE>XXXXX</CATEGORIE>";
				XMLENT_BLexp += "</ARTICLE>";
				XMLENT_BLexp += "</LIVRAISON>";
				XMLENT_BLexp += "</EXPEDITIONCLIENT>";
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(XMLENT_BLexp, EApplication.GESTION_COMMERCIALE);
		
		String XMLENT_BLliv = "<LIVRAISONSCOMMANDEFOURNISSEUR>";
		XMLENT_BLliv += "<LIVRAISON>";
		XMLENT_BLliv += "<NUMERO>CF398259873</NUMERO>";
		XMLENT_BLliv += "<DATEBC>20141226</DATEBC>";
		XMLENT_BLliv += "<DATEBL>20141226</DATEBL>";
		XMLENT_BLliv += "<ARTICLE>";
		XMLENT_BLliv += "<REFERENCE>AR736827</REFERENCE>";
		XMLENT_BLliv += "<QUANTITE>265000</QUANTITE>";
		XMLENT_BLliv += "<CATEGORIE>010</CATEGORIE>";
		XMLENT_BLliv += "</ARTICLE>";
		XMLENT_BLliv += "<ARTICLE>";
		XMLENT_BLliv += "<REFERENCE>AR736823</REFERENCE>";
		XMLENT_BLliv += "<QUANTITE>12</QUANTITE>";
		XMLENT_BLliv += "<CATEGORIE>010</CATEGORIE>";
		XMLENT_BLliv += "</ARTICLE>";
		XMLENT_BLliv += "</LIVRAISON>";
		XMLENT_BLliv += "</EXPEDITIONCLIENT>";
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(XMLENT_BLliv, EApplication.GESTION_COMMERCIALE);
		
		String XMLINT_EnvCom = "<commande_internet>";
		XMLINT_EnvCom += "<commande>";
		XMLINT_EnvCom += "<numero>XXXXXX</numero>";
		XMLINT_EnvCom += "<refclient>TH736749</refclient>";
		XMLINT_EnvCom += "<datebc>20131230</datebc>";
		XMLINT_EnvCom += "<datebl>20131230</datebl>";
		XMLINT_EnvCom += "<adresseClient>36 rue malte 92520 petaouchnok</adresse>";
		XMLINT_EnvCom += "<nom>Kaboul</nom>";
		XMLINT_EnvCom += "<prénom>George</prenom>";
		XMLINT_EnvCom += "<articles>";
		XMLINT_EnvCom += "<article>";
		XMLINT_EnvCom += "<CATEGORIE>001</CATEGORIE>";
		XMLINT_EnvCom += "<reference>AR736823</reference>";
		XMLINT_EnvCom += "<quantite>12</quantite>";
		XMLINT_EnvCom += "</article>";
		XMLINT_EnvCom += "</articles>";
		XMLINT_EnvCom += "</commande>";
		XMLINT_EnvCom += "</commande_internet>";
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(XMLINT_EnvCom, EApplication.GESTION_COMMERCIALE);
		
		String XMLINT_DemandeStock = "<DEMANDENIVEAUDESTOCKINTERNET>";
		XMLINT_DemandeStock += "<NUMERO>CV398719873</NUMERO>";
		XMLINT_DemandeStock += "<DATE>20131225</DATE>";
		XMLINT_DemandeStock += "<ARTICLES>";
		XMLINT_DemandeStock += "<ARTICLE>";
		XMLINT_DemandeStock += "<REFERENCE>AU736827</REFERENCE>";
		XMLINT_DemandeStock += "</ARTICLE>";
		XMLINT_DemandeStock += "<ARTICLE>";
		XMLINT_DemandeStock += "<REFERENCE>AU736829</REFERENCE>";
		XMLINT_DemandeStock += "</ARTICLE>";
		XMLINT_DemandeStock += "</ARTICLES>";
		XMLINT_DemandeStock += "</DEMANDENIVEAUDESTOCKINTERNET>";
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(XMLINT_DemandeStock, EApplication.GESTION_COMMERCIALE);
		
		String XMLBO_EnvStock = "<DEMANDENIVEAUDESTOCK>";
		XMLBO_EnvStock += "<NUMERO>CV398719873</NUMERO>";
		XMLBO_EnvStock += "<REFMAGASIN>PA218765</REFMAGASIN>";
		XMLBO_EnvStock += "<DATE>20131225</DATE>";
		XMLBO_EnvStock += "<ARTICLES>";
		XMLBO_EnvStock += "<ARTICLE>";
		XMLBO_EnvStock += "<REFERENCE>AU736827</REFERENCE>";
		XMLBO_EnvStock += "<QUANTITE>7</QUANTITE>";
		XMLBO_EnvStock += "<CAPACITE>10</CAPACITE>";
		XMLBO_EnvStock += "</ARTICLE>";
		XMLBO_EnvStock += "<ARTICLE>";
		XMLBO_EnvStock += "<REFERENCE>AU736829</REFERENCE>";
		XMLBO_EnvStock += "<QUANTITE>3</QUANTITE>";
		XMLBO_EnvStock += "<CAPACITE>20</CAPACITE>";
		XMLBO_EnvStock += "</ARTICLE>";
		XMLBO_EnvStock += "</ARTICLES>";
		XMLBO_EnvStock += "</DEMANDENIVEAUDESTOCK>";
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(XMLBO_EnvStock, EApplication.GESTION_COMMERCIALE);
		
		String XMLBO_DemandeRea = "<REASSORT>";
		XMLBO_DemandeRea += "<NUMERO>CV398719873</NUMERO>";
		XMLBO_DemandeRea += "<REFBO>20131225</REFBO>";
		XMLBO_DemandeRea += "<ADRESSEBO>XXXXXX</ADRESSEBO> ";
		XMLBO_DemandeRea += "<TELBO>0133333333</TELBO>";
		XMLBO_DemandeRea += "<DATEBC>20130427</DATEBC>";
		XMLBO_DemandeRea += "<ARTICLES>";
		XMLBO_DemandeRea += "<ARTICLE>";
		XMLBO_DemandeRea += "<REFERENCE>AU736827</REFERENCE>";
		XMLBO_DemandeRea += "<QUANTITE>265000</QUANTITE>";
		XMLBO_DemandeRea += "<CATEGORIE>001</CATEGORIE>";
		XMLBO_DemandeRea += "</ARTICLE>";
		XMLBO_DemandeRea += "<ARTICLE>";
		XMLBO_DemandeRea += "<REFERENCE>AU736823</REFERENCE>";
		XMLBO_DemandeRea += "<QUANTITE>12</QUANTITE>";
		XMLBO_DemandeRea += "<CATEGORIE>001</CATEGORIE>";
		XMLBO_DemandeRea += "</ARTICLE>";
		XMLBO_DemandeRea += "<ARTICLES>";
		XMLBO_DemandeRea += "</REASSORT>";
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(XMLBO_DemandeRea, EApplication.GESTION_COMMERCIALE);
		
		String XMLREF = "<XML>";
		XMLREF += "<ARTICLES>";
		XMLREF += "<ARTICLE reference=\"AU736829\" prix_fournisseur=\"20\" nb_min_commande=\"120\">";
		XMLREF += "<PROMOTIONS>";
		XMLREF += "<PROMOTION debut=\"20140601\" fin=\"20140610\" pourcent=\"20\" nb_min_promo=\"2\"/>";
		XMLREF += "</PROMOTIONS>";
		XMLREF += "</ARTICLE>";
		XMLREF += "</ARTICLES>";
		XMLREF += "</XML>";
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(XMLREF, EApplication.GESTION_COMMERCIALE);
		*/
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
