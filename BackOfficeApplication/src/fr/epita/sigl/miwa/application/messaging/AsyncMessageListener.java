package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.bo.file.FileManager;
import fr.epita.sigl.miwa.bo.object.ArticleAndLocalPriceAndPromotion;
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
import fr.epita.sigl.miwa.bo.plug.PlugCashRegister;
import fr.epita.sigl.miwa.bo.plug.PlugStoreManagement;
import fr.epita.sigl.miwa.bo.util.Convert;
import fr.epita.sigl.miwa.bo.xmlconstructor.CRMXMLConstructor;
import fr.epita.sigl.miwa.bo.xmlconstructor.CashRegisterXMLConstructor;
import fr.epita.sigl.miwa.bo.xmlconstructor.StoreManagementXMLConstructor;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.AsyncFileMessage;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
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
		//LOGGER.severe(message);
		Sale sale = null;
		ArticleList articleList = null;
		switch (source) {
		case CAISSE:
			System.out.println("***** Une vente réalisée envoyée par la Caisse a été reçue.");

			System.out.println("XML:" + message);
			DomParserCashRegister parserCashregister = new DomParserCashRegister();			
			sale = parserCashregister.saleTicket(message);
			
			System.out.println("****** Le client n°= " + sale.customerNumber + " a acheté " + sale.articles.size() + " articles en payant par " + sale.paymentMeans + "." );
			System.out.println("****** Fin du parsing.");	
			
			CRMXMLConstructor crmconstructor = new CRMXMLConstructor();
			try {
				AsyncMessageFactory.getInstance().getAsyncMessageManager().
				send(crmconstructor.loyalCustomerTicket(sale), EApplication.CRM);
			} catch (AsyncMessageException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
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
				System.out.println("****** Un bon de livraison a été envoyé à la gestion commerciale pour la livraison "+ deliv.number+".");
			}
			System.out.println("****** Fin de la cargaison.");
			break;
		case GESTION_COMMERCIALE:
			System.out.println("***** Une demande de niveau de stock envoyée par la GC a été reçue.");

			DomParserStoreManagement parserStoreManagement = new DomParserStoreManagement();			
			articleList = parserStoreManagement.stockLevel(message);
			
			if (articleList == null)
			{
				System.out.println("****** La demande de niveau de stock ne concerne aucun article. #miwafail"); 
			}
			else
			{
				System.out.println("****** La demande concerne " + articleList.articles.size()+ " articles.");
			}

			articleList = PlugStoreManagement.getstockLevel(articleList); // En attente de BDD pour mettre les vraies quantités
			StoreManagementXMLConstructor storemanagementconstructor = new StoreManagementXMLConstructor();
			try {
				AsyncMessageFactory.getInstance().getAsyncMessageManager().
				send(storemanagementconstructor.stockLevel(articleList), EApplication.GESTION_COMMERCIALE);
				System.out.println("***** Le niveau de stock a été envoyé à la Gestion Commerciale");
			} catch (AsyncMessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			/* MDM => BO : descente des articles */
			System.out.println("***** Le fichier du MDM a été reçu.");
			DomParserReferential parserReferential = new DomParserReferential();

			ArticleList articles = parserReferential.articleList(Convert.ReadFile(file));

			System.out.println("****** date = " + articles.date);
			System.out.println("****** nombre d'articles = " + articles.articles.size());
			System.out.println("****** Fin du parsing.");

			/* BO => Caisse : descente des articles */
			CashRegisterXMLConstructor cashregisterconstructor = new CashRegisterXMLConstructor();
			articles = PlugCashRegister.getpromotion(articles); // BDD : en attente pour mettre les promotions
			ArticleAndLocalPriceAndPromotion articlepromotionned = new ArticleAndLocalPriceAndPromotion(articles);
			String fileresponsename = file.getName().substring(0, file.getName().length()-4)+"response.xml";
			FileManager.createFile(fileresponsename, cashregisterconstructor.articleAndLocalPriceAndPromotion(articlepromotionned));
			try {
				AsyncFileFactory.getInstance().getFileManager().send(fileresponsename, EApplication.CAISSE);
				System.out.println("****** Le fichier <<"+ fileresponsename + ">> des articles a été redescendu vers la caisse.");
			} catch (AsyncFileException e) {
				System.out.println("****** Erreur pour redescente des articles vers la caisse.");
				e.printStackTrace();
			}

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
