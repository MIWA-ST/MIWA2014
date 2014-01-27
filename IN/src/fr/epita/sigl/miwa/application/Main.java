package fr.epita.sigl.miwa.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.MDM.ProductsClientMDM;
import fr.epita.sigl.miwa.application.MO.PaiementCbMO;
import fr.epita.sigl.miwa.application.MO.PaiementCfMO;
import fr.epita.sigl.miwa.application.ParseXML.TYPE_LANGUAGE;
import fr.epita.sigl.miwa.application.BI.ArticleVenteBI;
import fr.epita.sigl.miwa.application.BI.CategorieVenteBI;
import fr.epita.sigl.miwa.application.BI.EnteteBI;
import fr.epita.sigl.miwa.application.BI.EnvoiInformationVentesBI;
import fr.epita.sigl.miwa.application.BI.EnvoiVenteDetailleeBI;
import fr.epita.sigl.miwa.application.BI.VenteBI;
import fr.epita.sigl.miwa.application.CR.CreationClientCR;
import fr.epita.sigl.miwa.application.CR.EnvoiEnteteCR;
import fr.epita.sigl.miwa.application.CR.EnvoiMatriculeCR;
import fr.epita.sigl.miwa.application.CR.ModifierUtilisateurCR;
import fr.epita.sigl.miwa.application.CR.SupprimerUtilisateurCR;
import fr.epita.sigl.miwa.application.GC.ArticleCommandeParClientGC;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticlesGC;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockGC;
import fr.epita.sigl.miwa.application.GC.EnvoiCommandeGC;
import fr.epita.sigl.miwa.application.clock.ClockClient;
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

	public static void main(String[] args) throws AsyncFileException,
			AsyncMessageException {
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		Conf.getInstance();
		SyncMessFactory.initSyncMessReceiver();	
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.initListener(new AsyncMessageListener());
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		/* CODE HERE */
		
		
		MiwaBDDIn bdd = MiwaBDDIn.getInstance();			
		bdd.connection();
		
		
		try {
			
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		// ENvoi des messages (wakeMeUp...)
			// Envoi des commandes Internet à la GC 1 fois par jour à 9h00
			Calendar dateouverture = Calendar.getInstance();
			dateouverture.setTime(ClockClient.getClock().getHour());
			Date nextOccurence = new Date();
			if (dateouverture.get(Calendar.HOUR_OF_DAY) >= 9) 
				dateouverture.add((Calendar.DAY_OF_MONTH), 1);
			dateouverture.set(Calendar.HOUR_OF_DAY, 9);
			dateouverture.set(Calendar.MINUTE, 0);
			dateouverture.set(Calendar.SECOND, 0);
			dateouverture.set(Calendar.MILLISECOND, 0);
			nextOccurence = dateouverture.getTime();
			ClockClient.getClock().wakeMeUpEveryDays(nextOccurence, "Commandes internet to GC");
			
			// Envoi des commandes Internet à BI tous les jours
				// Envoi des Informations ventes par catégorie toutes les 15 mn
			Calendar dateouverture_BI2 = Calendar.getInstance();
			dateouverture_BI2.setTime(ClockClient.getClock().getHour());
			Date nextOccurence_BI2 = new Date();
			dateouverture_BI2.add(Calendar.MINUTE, 15);
			nextOccurence_BI2 = dateouverture_BI2.getTime();
			ClockClient.getClock().wakeMeUp(nextOccurence_BI2, "Commandes internet to BI 1");
			
				// Envoi des ventes détaillées tous les jours à 21h00
			Calendar dateouverture_BI = Calendar.getInstance();
			dateouverture_BI.setTime(ClockClient.getClock().getHour());
			Date nextOccurence_BI = new Date();
			if (dateouverture_BI.get(Calendar.HOUR_OF_DAY) >= 21) 
				dateouverture_BI.add((Calendar.DAY_OF_MONTH), 1);
			dateouverture_BI.set(Calendar.HOUR_OF_DAY, 21);
			dateouverture_BI.set(Calendar.MINUTE, 0);
			dateouverture_BI.set(Calendar.SECOND, 0);
			dateouverture_BI.set(Calendar.MILLISECOND, 0);
			nextOccurence_BI = dateouverture_BI.getTime();
			ClockClient.getClock().wakeMeUpEveryDays(nextOccurence_BI, "Commandes internet to BI 2");
			
			
			
			Thread.sleep(40000);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
		new BufferedReader(new InputStreamReader(System.in)).readLine();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		bdd.close();
		
		
	//	SyncMessHandler.getSyncMessSender().sendMessage(
			//	EApplication.INTERNET, "Coucou IN");
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
