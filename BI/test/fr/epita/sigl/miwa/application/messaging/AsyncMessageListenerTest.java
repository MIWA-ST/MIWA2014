package fr.epita.sigl.miwa.application.messaging;

import java.io.File;

import org.junit.Test;

import fr.epita.sigl.miwa.application.controller.BIController;
import fr.epita.sigl.miwa.st.EApplication;

public class AsyncMessageListenerTest {

	private AsyncMessageListener listener = new AsyncMessageListener();

	private BIController controller = BIController.getInstance();

	@Test
	public void manageCRMFileClientTest() {
		File file = new File("C:/Users/Laura/Desktop/miwa_env/workspace/MIWA2014/BI/test/mock/CRMClients.xml");
		listener.onFile(file , EApplication.CRM);
	}

	@Test
	public void manageCRMMessageSegmentationTest(){
		String message = "<XML><ENTETE objet='demande-segmentation-client' source='crm' date='2013-01-27'/><CRITERES><CRITERE type='age' max='60' min='30' /><CRITERE type='geographie' valeur='94' /><CRITERE type='situation-familiale' valeur='marie'/><CRITERE type='sexe' valeur='M' /><CRITERE type='enfant' valeur='TRUE' /><CRITERE type='fidelite' valeur='Gold' /></CRITERES></XML>";
		listener.onMessage(message, EApplication.CRM);
	}

	@Test
	public void manageMDMFileProductTest(){
		File file = new File("C:/Users/Laura/Desktop/miwa_env/workspace/MIWA2014/BI/test/mock/MDMProducts.xml");
		listener.onFile(file, EApplication.MDM);
	}

	@Test
	public void manageGCMessageStockTest(){
		String message = "<XML> <ENTETE objet='information stock' source='gc' date='2014-01-27' /><STOCKS><STOCK ref-article='1' stock='10' commande='false' max='500' lieu='Entrepot 1'/><STOCK ref-article='2' stock='10' commande='false' max='50' lieu='Magasin 1'/><STOCK ref-article='1' stock='10' commande='false' max='500' lieu='Magasin 1'/></STOCKS></XML>";
		listener.onMessage(message, EApplication.GESTION_COMMERCIALE);
	}

	@Test
	public void manageInternetMessageSaleTest(){
		String messageYesterday = "<XML><ENTETE objet='ventes 15min' source='internet' date='2014-01-26 16:56:12' /><VENTES lieu='internet'><CATEGORIE ref-categorie='Categorie 1' quantite_vendue='1' montant_fournisseur='2' montant_vente='3' /><CATEGORIE ref-categorie='Categorie 2' quantite_vendue='10' montant_fournisseur='5' montant_vente='6' /></VENTES></XML>";
		listener.onMessage(messageYesterday, EApplication.INTERNET);
		String messageToday = "<XML><ENTETE objet='ventes 15min' source='internet' date='2014-01-27 16:56:12' /><VENTES lieu='internet'><CATEGORIE ref-categorie='Categorie 1' quantite_vendue='10' montant_fournisseur='2' montant_vente='3' /><CATEGORIE ref-categorie='Categorie 2' quantite_vendue='4' montant_fournisseur='5' montant_vente='6' /></VENTES></XML>";
		listener.onMessage(messageToday, EApplication.INTERNET);
	}

	@Test
	public void manageInternetFileDetailSaleTest(){
		File file = new File("C:/Users/Laura/Desktop/miwa_env/workspace/MIWA2014/BI/test/mock/InternetDetailSales.xml");
		listener.onFile(file, EApplication.INTERNET);
	}

	@Test
	public void manageBOMessageSaleTest(){
		String messageYesterday = "<XML><ENTETE objet='ventes 15min' source='bo' date='2014-01-26 16:56:12' /><VENTES lieu='Magasin 1'><CATEGORIE ref-categorie='Categorie 1' quantite_vendue='1' montant_fournisseur='2' montant_vente='3' /><CATEGORIE ref-categorie='Categorie 2' quantite_vendue='10' montant_fournisseur='5' montant_vente='6' /></VENTES></XML>";
		listener.onMessage(messageYesterday, EApplication.BACK_OFFICE);
		String messageToday = "<XML><ENTETE objet='ventes 15min' source='bo' date='2014-01-27 16:56:12' /><VENTES lieu='Magasin 1'><CATEGORIE ref-categorie='Categorie 1' quantite_vendue='10' montant_fournisseur='2' montant_vente='3' /><CATEGORIE ref-categorie='Categorie 2' quantite_vendue='4' montant_fournisseur='5' montant_vente='6' /></VENTES></XML>";
		listener.onMessage(messageToday, EApplication.BACK_OFFICE);
	}

	@Test
	public void manageBOFileDetailSaleTest(){
		File file = new File("C:/Users/Laura/Desktop/miwa_env/workspace/MIWA2014/BI/test/mock/BODetailSales.xml");
		listener.onFile(file, EApplication.BACK_OFFICE);
	}

	@Test
	public void manageBOMessagePromotionTest(){
		String message = "<XML><ENTETE objet='promotions' source='bo' date='2014-01-27' /><PROMOTIONS lieu='Magasin 1'><PROMOTION ref-article='1' debut='2014-01-27 09:00:00' fin='2014-01-27 12:00:00' pourcent='5' /></PROMOTIONS></XML>";
		listener.onMessage(message, EApplication.BACK_OFFICE);
	}

	@Test
	public void generateStatisticPaymentTest(){
		controller.generatePaymentStatistics();
	}

	@Test
	public void generateStatisticSaleTest(){
		controller.generateSaleStatistic();
	}

	@Test
	public void generateStatisticStockTest(){
		controller.generateStockStatistic();
	}


}
