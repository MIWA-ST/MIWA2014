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
import fr.epita.sigl.miwa.bo.object.Sale;
import fr.epita.sigl.miwa.bo.parser.DomParserCashRegister;
import fr.epita.sigl.miwa.bo.plug.PlugBusinessIntelligence;
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

//		DomParserCashRegister toto = new DomParserCashRegister();
		
//		toto.saleTicket(PlugCashRegister.saleTicket);
		
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
		System.out.println("***** demande de réassort envoyé à la GC");
		*/
		
		/*
		// BO => BI envoi des vente par catégorie
		AsyncMessageFactory.getInstance().getAsyncMessageManager().
		send(PlugBusinessIntelligence.categorizedSale, EApplication.BI);
		System.out.println("***** vente par catégorie envoyées à la BI");
		*/
		

		// BO => BI : Envoi des ventes détaillées
		FileManager.createFile("ventedetaille.xml", PlugBusinessIntelligence.detailedSale);
			AsyncFileFactory.getInstance().getFileManager().send("ventedetaille.xml", EApplication.BI);
			System.out.println("***** Ventes détaillées envoyés à la BI");


		//SyncMessHandler.getSyncMessSender().sendMessage(EApplication.GESTION_COMMERCIALE, "coucou");

//		FileManager.createFile("test.xml", PlugCashRegister.articleAndLocalPriceAndPromotion);
//		AsyncFileFactory.getInstance().getFileManager().send("test.xml", EApplication.CAISSE);
		
		
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
