package fr.epita.sigl.miwa.application.clock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.ParseXML;
import fr.epita.sigl.miwa.application.BI.EnteteBI;
import fr.epita.sigl.miwa.application.BI.EnvoiVenteDetailleeBI;
import fr.epita.sigl.miwa.application.GC.ArticleCommandeParClientGC;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticlesGC;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockGC;
import fr.epita.sigl.miwa.application.GC.EnvoiCommandeGC;
import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
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
		if (message instanceof String) {
			if (message.equals("Commandes internet to GC")) {
				// Mise à joru des niveaux de stock de tous les articles
				DemandeNiveauStockGC testDmandeNiveauStock = new DemandeNiveauStockGC("CV6598", "20131225");
				
				testDmandeNiveauStock.MiseAJourStock();
				
				
				
//				List<ArticleCommandeParClientGC> articlesCommande = new ArrayList<ArticleCommandeParClientGC>();
//				
//				articlesCommande.add(new ArticleCommandeParClientGC("Categorie 1", "05280cc5-6804-4687-a39b-6c0446df", "77"));
//				articlesCommande.add(new ArticleCommandeParClientGC("Categorie 2", "073e5d55-02f2-4e65-99c9-3e498ac8", "59"));
//				articlesCommande.add(new ArticleCommandeParClientGC("Categorie 3", "096252df-1961-4133-a9a4-aa9debda", "99"));
//				
//				EnvoiCommandeGC testEnvoiCommande = new EnvoiCommandeGC("CV56E8",
//						"Fd595SD",
//						"20121223",
//						"20140102",
//						"ADRESSE CLIENTE !!!",
//						"HADDAD",
//						"CHAWQUI",
//						articlesCommande);
//				System.out.println(testEnvoiCommande.sendXML());
//				
//				
//				try {
//					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(testEnvoiCommande.sendXML(), EApplication.GESTION_COMMERCIALE);
//				} catch (AsyncMessageException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			else if (message.equals("Commandes internet to BI 1"))
			{
				Calendar dateouverture_BI2 = Calendar.getInstance();
				dateouverture_BI2.setTime(date);
				Date nextOccurence_BI2 = new Date();
				dateouverture_BI2.add(Calendar.MINUTE, 15);
				nextOccurence_BI2 = dateouverture_BI2.getTime();
				ClockClient.getClock().wakeMeUp(nextOccurence_BI2, "Commandes internet to BI 1");
			}
			else if (message.equals("Commandes internet to BI 2"))
			{
				EnvoiVenteDetailleeBI ventes = new EnvoiVenteDetailleeBI();
				EnteteBI entete = new EnteteBI("ventes detaillees", "internet", date);
				
				
				
				ventes.setEntete(entete);
				ventes.generateVentes();
				ventes.getBDD();
				LOGGER.info("***** Envoi d'un message à BI : envoi des ventes détaillés internet.");
				try {
					AsyncFileFactory.getInstance().getFileManager().send(ventes.createFileXML(), EApplication.BI);
				} catch (AsyncFileException e) {
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
