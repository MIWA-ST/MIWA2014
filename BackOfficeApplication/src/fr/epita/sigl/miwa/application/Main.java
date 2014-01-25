package fr.epita.sigl.miwa.application;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.jgroups.util.Buffer;

import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.bo.db.JdbcConnection;
import fr.epita.sigl.miwa.bo.db.Mapper;
import fr.epita.sigl.miwa.bo.file.FileManager;
import fr.epita.sigl.miwa.bo.object.ArticleList;
import fr.epita.sigl.miwa.bo.object.Sale;
import fr.epita.sigl.miwa.bo.parser.DomParserCashRegister;
import fr.epita.sigl.miwa.bo.parser.DomParserStoreManagement;
import fr.epita.sigl.miwa.bo.plug.PlugBusinessIntelligence;
import fr.epita.sigl.miwa.bo.plug.PlugReferential;
import fr.epita.sigl.miwa.bo.plug.PlugStoreManagement;
import fr.epita.sigl.miwa.bo.plug.PlugCashRegister;
import fr.epita.sigl.miwa.bo.util.Test;
import fr.epita.sigl.miwa.bo.xmlconstructor.CashRegisterXMLConstructor;
import fr.epita.sigl.miwa.bo.xmlconstructor.StoreManagementXMLConstructor;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileMessage;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;;

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
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		// BO => GC demande de réassort
		AsyncMessageFactory.getInstance().getAsyncMessageManager().
		send(PlugStoreManagement.restockRequest, EApplication.GESTION_COMMERCIALE);
		System.out.println("***** Bouchon : Demande de réassort envoyée à la GC"); // mettre + d'infos
		*/
		
		/*
		// BO => Caisse : mock pour simuler la descente des articles du matin vers la caisse censé être envoyés par le mdm
		System.out.println("***** Bouchon : Envoi des articles matinaux vers la caisse.");
		AsyncFileFactory.getInstance().getFileManager().send("outputFileBOresponse.xml", EApplication.CAISSE);
		System.out.println("***** Bouchon : Articles matinaux.");
		*/

		/*
		// BDD : faire des clock pour modifier les promo en live
		System.out.println("***** Bouchon : Envoi des correctifs vers la caisse.");
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send(PlugCashRegister.articleAndLocalPriceAndPromotion, EApplication.CAISSE);
		System.out.println("***** Bouchon : Correctifs envoyés.");
		*/

		/*
		// BO => BI envoi des vente par catégorie
		AsyncMessageFactory.getInstance().getAsyncMessageManager().
		send(PlugBusinessIntelligence.categorizedSale, EApplication.BI);
		System.out.println("***** vente par catégorie envoyées à la BI");
		*/

		/*
		// BO => BI : Envoi des ventes détaillées
		FileManager.createFile("ventedetaille.xml", PlugBusinessIntelligence.detailedSale);
			AsyncFileFactory.getInstance().getFileManager().send("ventedetaille.xml", EApplication.BI);
			System.out.println("***** Ventes détaillées envoyées à la BI");
		*/

		/*
		// BO => GC envoi niveau de stock
		AsyncMessageFactory.getInstance().getAsyncMessageManager().
		send(PlugStoreManagement.stockLevel, EApplication.GESTION_COMMERCIALE);
		System.out.println("***** niveau de stock envoyé à la GC");
		*/
		
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
