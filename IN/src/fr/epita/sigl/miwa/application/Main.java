package fr.epita.sigl.miwa.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.MO.PaiementCbMO;
import fr.epita.sigl.miwa.application.MO.PaiementCfMO;
import fr.epita.sigl.miwa.application.ParseXML.TYPE_LANGUAGE;
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
		
		try {
			//ClockClient.getClock().wakeMeUp(new Date("12/13/2013 23:12:13"), "Test");
			EApplication to = EApplication.INTERNET;
			
//			ParseXML parser = new ParseXML();
//			
//			parser.readXML("src/fr/epita/sigl/miwa/application/GC/Envoi_stock_GC_to_IN.xml",
//					ParseXML.TYPE_LANGUAGE.FICHIER);
//			
//			parser.parseGC();
			
//			ClockClient.getClock().wakeMeUp(date, message);
			
//			AsyncMessageFactory.getInstance().getAsyncMessageManager().send("<XML><ENTETE objet=\"connection_client\" source=\"Internet\" date=\"2014-01-23\" /><COMPTE matricule=\"50762119\"></COMPTE></XML>", EApplication.CRM);
			//SyncMessHandler.getSyncMessSender().sendMessage(EApplication.CRM, "<XML><ENTETE objet=\"connection_client\" source=\"Internet\" date=\"2014-01-23\" /><COMPTE matricule=\"50762119\"></COMPTE></XML>");
			
			// Test demande info client CRM
//			EnvoiMatriculeCR test = new EnvoiMatriculeCR("58749566");
//			String result = SyncMessHandler.getSyncMessSender().requestMessage(EApplication.CRM, test.sendXML());
			
//			CreationClientCR testCreationCRM = new CreationClientCR(new EnvoiEnteteCR("creation_compte", "Internet", ClockClient.getClock().getHour()),
//					"HADDAD",
//					"CHAWQUI",
//					"93 RUE DU BOSSS",
//					"93170",
//					"chawqui.haddad@gmail.com",
//					"0630189798",
//					"M",
//					"Marie",
//					"1989-12-09",
//					"IBAN",
//					"BIC",
//					"3");
//			String result = SyncMessHandler.getSyncMessSender().requestMessage(EApplication.CRM, testCreationCRM.sendXML());
			
			
//			SupprimerUtilisateurCR deleteTest = new SupprimerUtilisateurCR(new EnvoiEnteteCR("suppression_compte", "Internet", ClockClient.getClock().getHour()), "58749566");
//			String result = SyncMessHandler.getSyncMessSender().requestMessage(EApplication.CRM, deleteTest.sendXML());
			
//			System.out.println(result);
//			
//			ModifierUtilisateurCR modifierTest = new ModifierUtilisateurCR(new EnvoiEnteteCR("modifier_compte", "Internet", ClockClient.getClock().getHour()), "58749566", "BOUDERBALA", "Chawqui", "ADRESSE", "code_postal", "email", "213265413", "civilite", "situation", "2014-12-13", "65465", "2313123", "12321");
//			String result2 = SyncMessHandler.getSyncMessSender().requestMessage(EApplication.CRM, modifierTest.sendXML());
			
			
			// Test Envoi des commandes GC
//			List<ArticleCommandeParClientGC> articles = new ArrayList<ArticleCommandeParClientGC>();
//			
//			articles.add(new ArticleCommandeParClientGC("Categorie 1", "08deb933-bb39-411d-b0ee-59b28812", "77"));
//			articles.add(new ArticleCommandeParClientGC("Categorie 2", "292f8025-58bf-498a-a092-1404d30f", "59"));
////			articles.add(new ArticleCommandeParClientGC("Categorie 3", "Referenc0e", "99"));
//			
//			EnvoiCommandeGC testEnvoiCommande = new EnvoiCommandeGC("CV56E8",
//					"Fd595SD",
//					"20121223",
//					"20140102",
//					"ADRESSE CLIENTE !!!",
//					"HADDAD",
//					"CHAWQUI",
//					articles);
//			System.out.println(testEnvoiCommande.sendXML());
//			AsyncMessageFactory.getInstance().getAsyncMessageManager().send(testEnvoiCommande.sendXML(), EApplication.GESTION_COMMERCIALE);
			

			
//			List<DemandeNiveauStockArticlesGC> articles = new ArrayList<DemandeNiveauStockArticlesGC>();
//			
//			articles.add(new DemandeNiveauStockArticlesGC("042ef636-acbd-40b6-afcd-3c82236c"));
//			
//			DemandeNiveauStockGC testDmandeNiveauStock = new DemandeNiveauStockGC("CV6598", "20131225", articles);
//			boolean result = SyncMessHandler.getSyncMessSender().sendMessage(EApplication.DEBUG, testDmandeNiveauStock.sendXML());
//			
//			
			
			
			// Test de la MO
//			PaiementCfMO paiementCF = new PaiementCfMO("93.93", "68523705");
//			SyncMessHandler.getSyncMessSender().sendXML(EApplication.MONETIQUE, paiementCF.sendXMLDocument());
			
			
//			PaiementCbMO paiementCB = new PaiementCbMO("93.93", "1234569856985214", "1116", "586");
//			Boolean retr = SyncMessHandler.getSyncMessSender().sendXML(EApplication.MONETIQUE, paiementCB.sendXMLDocument());
//			LOGGER.info(retr.toString());
			
			
			
			
			//new BufferedReader(new InputStreamReader(System.in)).readLine();
			
			// ENvoi des messages (wakeMeUp...)
			
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	SyncMessHandler.getSyncMessSender().sendMessage(
			//	EApplication.INTERNET, "Coucou IN");
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
