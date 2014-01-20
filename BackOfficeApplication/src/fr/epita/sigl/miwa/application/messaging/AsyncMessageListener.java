package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.bo.object.ArticleList;
import fr.epita.sigl.miwa.bo.object.Delivery;
import fr.epita.sigl.miwa.bo.object.DeliveryList;
import fr.epita.sigl.miwa.bo.object.RestockRequestReception;
import fr.epita.sigl.miwa.bo.object.Sale;
import fr.epita.sigl.miwa.bo.object.SalesTicket;
import fr.epita.sigl.miwa.bo.parser.DomParserCashRegister;
import fr.epita.sigl.miwa.bo.parser.DomParserStoreManagement;
import fr.epita.sigl.miwa.bo.parser.DomParserReferential;
import fr.epita.sigl.miwa.bo.parser.DomParserWarehouse;
import fr.epita.sigl.miwa.bo.util.Convert;
import fr.epita.sigl.miwa.bo.xmlconstructor.StoreManagementXMLConstructor;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileMessage;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	@Override
	public void onException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String message, EApplication source) {
		LOGGER.severe(message);
		Sale sale;
		switch (source) {
		case CAISSE:
//			System.out.println("***** Une vente réalisée envoyée par la Caisse a été reçue.");
//
//			DomParserCashRegister parserCashregister = new DomParserCashRegister();			
//			sale = parserCashregister.saleTicket(message);
//			
//			System.out.println("****** Le client = " + sale.customer + " a acheté " + sale.articles.size() + " articles en payant par " + sale.paymentMeans + "." );
//			System.out.println("****** Fin du parsing.");			
			break;
		
		case ENTREPOT:
			/* Entrepot => BO : Livraison reçue */
			System.out.println("***** Une cargaison a été reçue par l'entrepôt.");

			DomParserWarehouse parserWarehouse = new DomParserWarehouse();

			DeliveryList delivery = parserWarehouse.deliveryList(message);
			
			for (Delivery deliv : delivery.deliveries)
			{
				System.out.println("****** Une livraison contenant " + deliv.articles.size() + " articles a été reçue." );
				StoreManagementXMLConstructor storeManagementXMLConstructor = new StoreManagementXMLConstructor();
				
				try {
					AsyncMessageFactory.getInstance().getAsyncMessageManager().
						send(storeManagementXMLConstructor.restockRequestReception(deliv), EApplication.GESTION_COMMERCIALE);
				} catch (AsyncMessageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("****** Un bon de livraison a été envoyé à la gestion commerciale.");
			}
			System.out.println("****** Fin du parsing.");
			break;

		default:
			System.out.println("****** Message reçu d'une source inconnue. #miwaFail");
			break;
		}
		
		
		
	}

	@Override
	public void onFile(File file, EApplication source) {
		LOGGER.severe(source + " : " + file.getAbsolutePath());	
		switch (source) {
		case MDM:
			System.out.println("***** Le fichier du MDM a été reçu.");
			DomParserReferential parserReferential = new DomParserReferential();

			ArticleList articles = parserReferential.articleList(Convert.ReadFile(file));
			
			System.out.println("****** date = " + articles.date);
			System.out.println("****** nombre d'articles = " + articles.articles.size());
			System.out.println("****** Fin du parsing.");
			break;
		case CAISSE:
			/* FLUX ANNULE
			System.out.println("***** Les tickets du jour des caisses ont bien été reçus.");
			DomParserCashRegister parserCashregister = new DomParserCashRegister();
			
			SalesTicket tickets = parserCashregister.salesTicket(Convert.ReadFile(file));
			
			tickets.print(); // TEST
			
			System.out.println("****** date = " + tickets.date);
			System.out.println("****** nombre d'articles vendus aujourd'hui = " + tickets.sales.size());
			System.out.println("****** Fin du parsing.");
			*/
		default:
			System.out.println("****** Fichier reçu d'une source inconnue. #miwaFail");
			break;
		}
	}

}
