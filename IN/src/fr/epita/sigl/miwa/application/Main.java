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
			MiwaBDDIn bdd = MiwaBDDIn.getInstance();			
			bdd.connection();
			EApplication to = EApplication.INTERNET;
			
			ParseXML parser = new ParseXML();
			
			parser.readXML("src/fr/epita/sigl/miwa/application/MDM/Envoi_produits_MDM_to_IN.xml",
					ParseXML.TYPE_LANGUAGE.FICHIER);
			
			parser.parseMDM();
			

			
			
			
//			bdd.executeStatement("INSERT INTO article VALUES('Ref1', 'ean', 'catgorie', 33, 37, 'DEISJSLJEFLKSJESLKDJF SLDKJFLSKDJ\n SDLKFJ');");
//			bdd.executeStatement("INSERT INTO article VALUES('Ref2', 'ean', 'catgorie', 33, 37, 'DEISJSLJEFLKSJESLKDJF SLDKJFLSKDJ\n SDLKFJ');");
//			bdd.executeStatement("INSERT INTO article VALUES('Ref3', 'ean', 'catgorie', 33, 37, 'DEISJSLJEFLKSJESLKDJF SLDKJFLSKDJ\n SDLKFJ');");
//			Date s = new Date();
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			bdd.executeStatement("INSERT INTO promotion VALUES('Ref3', 33, 'sdfsdf', 'sdfsdf');");
//			bdd.executeStatement("INSERT INTO promotion VALUES('Ref3', 66, '" + df.format(s) + "', '" + df.format(s) + "');");
//			bdd.executeStatement("INSERT INTO promotion VALUES('Ref3', 66, '" + df.format(s) + "', '" + df.format(s) + "');");
			
//			ProductsClientMDM produitClient = new ProductsClientMDM();
//			produitClient.getTable();
//			
//			produitClient.print();
			
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
//			articles.add(new ArticleCommandeParClientGC("Categorie 1", "01", "77"));
//			articles.add(new ArticleCommandeParClientGC("Categorie 2", "02", "59"));
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
//			
//			
//			AsyncMessageFactory.getInstance().getAsyncMessageManager().send(testEnvoiCommande.sendXML(), EApplication.GESTION_COMMERCIALE);
//			

			
			List<DemandeNiveauStockArticlesGC> articles = new ArrayList<DemandeNiveauStockArticlesGC>();
			
			articles.add(new DemandeNiveauStockArticlesGC("01"));
			
			DemandeNiveauStockGC testDmandeNiveauStock = new DemandeNiveauStockGC("CV6598", "20131225", articles);
			String result = SyncMessHandler.getSyncMessSender().requestMessage(EApplication.GESTION_COMMERCIALE, testDmandeNiveauStock.sendXML());
			
			ParseXML parserGC = new ParseXML();
			parserGC.readXML(result,
					ParseXML.TYPE_LANGUAGE.STRING);
			
			
			parserGC.parseGC();
			
			
			

			
			
			// Test de la MO
//			PaiementCfMO paiementCF = new PaiementCfMO("93.93", "68523705");
//			SyncMessHandler.getSyncMessSender().sendXML(EApplication.MONETIQUE, paiementCF.sendXMLDocument());
			
			
//			PaiementCbMO paiementCB = new PaiementCbMO("93.93", "1234569856985214", "1116", "586");
//			Boolean retr = SyncMessHandler.getSyncMessSender().sendXML(EApplication.MONETIQUE, paiementCB.sendXMLDocument());
//			LOGGER.info(retr.toString());
			
			
//			ArticleVenteBI a1 = new ArticleVenteBI("TEST1", 33);
//			ArticleVenteBI a2 = new ArticleVenteBI("TEST2", 22);
//			ArticleVenteBI a3 = new ArticleVenteBI("TEST3", 11);
//			
//			List<ArticleVenteBI> articles = new ArrayList<ArticleVenteBI>();
//			articles.add(a1);
//			articles.add(a2);
//			articles.add(a3);
//			
//			VenteBI v1 = new VenteBI("32AB", 37, "CF", new Date(), articles);
//			List<VenteBI> ventes = new ArrayList<VenteBI>();
//			ventes.add(v1);
//			EnteteBI enteteBI1 = new EnteteBI("ventes detaillees", "internet", new Date());
//			EnvoiVenteDetailleeBI envoiVenteBI = new EnvoiVenteDetailleeBI(enteteBI1, ventes);
//			
//			System.out.println(envoiVenteBI.sendXML());
//			
//			EnteteBI enteteBI2 = new EnteteBI("ventes 15min", "internet", new Date());
//			CategorieVenteBI c1 = new CategorieVenteBI("TEST1", 1, 2, 3);
//			CategorieVenteBI c2 = new CategorieVenteBI("TEST2", "4", "5", "6");
//			
//			List<CategorieVenteBI> categories = new ArrayList<CategorieVenteBI>();
//			
//			categories.add(c1);
//			categories.add(c2);
//			
//			EnvoiInformationVentesBI envoiInfoVenteBI = new EnvoiInformationVentesBI(enteteBI2, categories);
//			System.out.println(envoiInfoVenteBI.sendXML());
			
			
			// Test BI
			
//			EnvoiInformationVentesBI ventes = new EnvoiInformationVentesBI();
//			
//			EnteteBI entete = new EnteteBI("ventes 15min", "internet", new Date());
//			
//			
//			ventes.setEntete(entete);
//			ventes.getBDD();
//			System.out.println(ventes.sendXML());
//			AsyncMessageFactory.getInstance().getAsyncMessageManager().send(ventes.sendXML(), EApplication.BI);
			
			// Test BI 2
			
//			EnvoiVenteDetailleeBI ventes = new EnvoiVenteDetailleeBI();
//			
//			EnteteBI entete = new EnteteBI("ventes detaillees", "internet", new Date());
//			ventes.setEntete(entete);
//			
//			ventes.getBDD();
//			System.out.println(ventes.sendXML());
			
			
			
			//new BufferedReader(new InputStreamReader(System.in)).readLine();
			
			// ENvoi des messages (wakeMeUp...)
			
			bdd.close();
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
