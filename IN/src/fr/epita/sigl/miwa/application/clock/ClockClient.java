package fr.epita.sigl.miwa.application.clock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.ParseXML;
import fr.epita.sigl.miwa.application.BI.EnteteBI;
import fr.epita.sigl.miwa.application.BI.EnvoiInformationVentesBI;
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
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				Random rnd = new Random();
				Integer r = rnd.nextInt(3);
				
				DemandeNiveauStockGC testDmandeNiveauStock = new DemandeNiveauStockGC("CV65" + r, df.format(date));
				
				testDmandeNiveauStock.MiseAJourStock();
				LOGGER.info("***** Envoi d'un message à GC : mise à jour des stocks Internet.");
			}
			else if (message.equals("Commandes internet to BI 1"))
			{
				Calendar dateouverture_BI2 = Calendar.getInstance();
				dateouverture_BI2.setTime(date);
				Date nextOccurence_BI2 = new Date();
				dateouverture_BI2.add(Calendar.MINUTE, 15);
				nextOccurence_BI2 = dateouverture_BI2.getTime();
				ClockClient.getClock().wakeMeUp(nextOccurence_BI2, "Commandes internet to BI 1");
				
				EnvoiInformationVentesBI ventes = new EnvoiInformationVentesBI();
				EnteteBI entete = new EnteteBI("ventes 15min", "internet", date);
				
				ventes.setEntete(entete);
				ventes.getBDD();
				try {
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(ventes.sendXML(), EApplication.BI);
					LOGGER.info("***** Envoi d'un message à BI : envoi des ventes par catégories internet.");
				} catch (AsyncMessageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
					e.printStackTrace();
				}
			}
			else {
				System.out.println(date.toString() + " : " + message);
			}
		}
	}	
}
