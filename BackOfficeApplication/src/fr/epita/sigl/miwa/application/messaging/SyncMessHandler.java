package fr.epita.sigl.miwa.application.messaging;

import org.w3c.dom.Document;

import fr.epita.sigl.miwa.bo.object.ArticleList;
import fr.epita.sigl.miwa.bo.object.Sale;
import fr.epita.sigl.miwa.bo.parser.DomParserCashRegister;
import fr.epita.sigl.miwa.bo.parser.DomParserReferential;
import fr.epita.sigl.miwa.bo.parser.DomParserStoreManagement;
import fr.epita.sigl.miwa.bo.plug.PlugStoreManagement;
import fr.epita.sigl.miwa.bo.xmlconstructor.CRMXMLConstructor;
import fr.epita.sigl.miwa.bo.xmlconstructor.CashRegisterXMLConstructor;
import fr.epita.sigl.miwa.bo.xmlconstructor.StoreManagementXMLConstructor;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.sync.ISyncMessSender;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class SyncMessHandler {
	
	/* 
	 * A utiliser pour pouvoir envoyer des messages synchrones ou faire des request
	 */
	static public ISyncMessSender getSyncMessSender() {
		return SyncMessFactory.getSyncMessSender();
	}

	/*
	 * l'application sender vous envoie la string message.
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public boolean receiveMessage(EApplication sender, String message) {
		System.out.println(message);
		Sale sale = new Sale();
		switch (sender) {
		case CAISSE:
			/* Caisse => BO : Vente en cours en demande de fidélisation */
			System.out.println("***** Une vente en cours de réalisation est remontée par la Caisse.");

			DomParserCashRegister parserCashregister = new DomParserCashRegister();
			
			sale = parserCashregister.saleTicket(message);
			
			System.out.println("****** Le client " + sale.customerNumber + " souhaite avoir accès à des promotions ciblées." );
			System.out.println("****** Fin du parsing.");
			
			break;
		
		case CRM:
			/* CRM => BO : Vente en cours avec fidélisation effectuée */
			System.out.println("***** Un panier a pu être retourné par le CRM.");

			// ATTENTION : Mauvais dom parser ! Créer le domparserCRM !!
			DomParserStoreManagement parserStoremanagement2 = new DomParserStoreManagement();
			sale  = parserStoremanagement2.saleTicket(message);

			System.out.println("****** Le panier du client = " + sale.customerNumber + " a été redescendu par le CRM." );
			System.out.println("****** Fin du parsing.");
			
			// BO => Caisse : On redescend le panier envoyé par le CRM à la Caisse.
			CashRegisterXMLConstructor cashregisterconstructor = new CashRegisterXMLConstructor();
			getSyncMessSender().sendMessage(EApplication.CAISSE, cashregisterconstructor.facture(sale));
			break;

		case GESTION_COMMERCIALE:
			/* GESCO => BO : Demande de niveau de stock */
			System.out.println("***** Une demande de niveau de stock de la part de la Gestion Commerciale vient d'être reçue.");

			DomParserStoreManagement parserStoremanagement = new DomParserStoreManagement();
			ArticleList articles = parserStoremanagement.stockLevel(message);

			System.out.println("****** La gestion commerciale souhaite avoir le niveau de stock de " + articles.articles.size() + " article(s)." );
			System.out.println("****** Fin du parsing.");

			// Fonction temporaire qui met de fausses quantités, attente de BDD
			PlugStoreManagement.getstockLevel(articles);

			// BO ==> GESTION COMMERCIALE : on renvoit le niveau de stock
			StoreManagementXMLConstructor storemanagementconstructor = new StoreManagementXMLConstructor();
			getSyncMessSender().sendMessage(EApplication.GESTION_COMMERCIALE, storemanagementconstructor.stockLevel(articles));

		default:
			break;
		}
		
		return false;
	}

	/*
	 * L'application sender vous demande request
	 * Vous devez lui renvoyer une string
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public String answerToRequestMessage(EApplication sender, String request){
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	* L'application sender vous envoie le XML xml
	* Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	* Elle est automatiquement appel�e lorsqu'une application vous contacte
	*/
	@Deprecated
	static public boolean receiveXML(EApplication sender, Document xml){
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * L'application sender vous demande un XML avec la requete request
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public Document answerToRequestXML(EApplication sender, String request){
		// TODO Auto-generated method stub
		return null;
	}
	
	private SyncMessHandler() {
		
	}
}
