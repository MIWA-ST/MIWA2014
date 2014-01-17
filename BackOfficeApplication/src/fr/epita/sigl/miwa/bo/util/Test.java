package fr.epita.sigl.miwa.bo.util;

import fr.epita.sigl.miwa.bo.object.ArticleList;
import fr.epita.sigl.miwa.bo.object.DeliveryList;
import fr.epita.sigl.miwa.bo.object.SalesTicket;
import fr.epita.sigl.miwa.bo.parser.DomParserCashRegister;
import fr.epita.sigl.miwa.bo.parser.DomParserReferential;
import fr.epita.sigl.miwa.bo.parser.DomParserWarehouse;
import fr.epita.sigl.miwa.bo.plug.PlugBusinessIntelligence;
import fr.epita.sigl.miwa.bo.plug.PlugBusinessManagement;
import fr.epita.sigl.miwa.bo.plug.PlugCashRegister;
import fr.epita.sigl.miwa.bo.plug.PlugReferential;
import fr.epita.sigl.miwa.bo.plug.PlugWarehouse;
import fr.epita.sigl.miwa.bo.xmlconstructor.BusinessIntelligenceXMLConstructor;
import fr.epita.sigl.miwa.bo.xmlconstructor.StoreManagementXMLConstructor;
import fr.epita.sigl.miwa.bo.xmlconstructor.CashRegisterXMLConstructor;

public class Test
{
	public static void all()
	{
		parseArticleList();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		parseSalesTicket();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		parseDeliveryList();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		constructArticleAndLocalPriceAndPromotionXML();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		constructRestockRequestReceptionXML();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		constructRestockRequestXML();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		constructDetailedSalesXML();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		constructcategorizedSalesXML();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	}
	
	public static void parseArticleList()
	{
		System.out.println("*****************************");
		System.out.println("* TEST - PARSE ARTICLE LIST *");
		System.out.println("*****************************");
		
		DomParserReferential parser = new DomParserReferential();
		ArticleList articleList = parser.articleList(PlugReferential.articleList);
		articleList.print();
	}
	
	public static void parseSalesTicket()
	{
		System.out.println("*****************************");
		System.out.println("* TEST - PARSE SALES TICKET *");
		System.out.println("*****************************");
		
		DomParserCashRegister parser = new DomParserCashRegister();
		SalesTicket salesTicket = parser.salesTicket(PlugCashRegister.salesTicket);
		salesTicket.print();
	}
	
	public static void parseDeliveryList()
	{
		System.out.println("*************************************");
		System.out.println("* TEST - PARSE DELIVERY LIST TICKET *");
		System.out.println("*************************************");
		
		DomParserWarehouse parser = new DomParserWarehouse();
		DeliveryList deliveryList = parser.deliveryList(PlugWarehouse.deliveryList);
		deliveryList.print();
	}
	
	public static void constructArticleAndLocalPriceAndPromotionXML()
	{
		System.out.println("*************************************************************");
		System.out.println("* TEST - CONSTRUCT ARTICLE AND LOCAL PRICE AND PROMOTION XML*");
		System.out.println("*************************************************************");
		
		CashRegisterXMLConstructor xmlConstructor = new CashRegisterXMLConstructor();
		System.out.println(xmlConstructor.articleAndLocalPriceAndPromotion(PlugCashRegister.articleAndLocalPriceAndPromotionObject()));
	}
	
	public static void constructRestockRequestReceptionXML()
	{
		System.out.println("*************************************************");
		System.out.println("* TEST - CONSTRUCT RESTOCK REQUEST RECEPTION XML*");
		System.out.println("*************************************************");
		
		StoreManagementXMLConstructor xmlConstructor = new StoreManagementXMLConstructor();
		System.out.println(xmlConstructor.restockRequestReception(PlugBusinessManagement.restockRequestReceptionObject()));
	}
	
	public static void constructRestockRequestXML()
	{
		System.out.println("***************************************");
		System.out.println("* TEST - CONSTRUCT RESTOCK REQUEST XML*");
		System.out.println("***************************************");
		
		StoreManagementXMLConstructor xmlConstructor = new StoreManagementXMLConstructor();
		System.out.println(xmlConstructor.restockRequest(PlugBusinessManagement.restockRequestObject()));
	}
	
	public static void constructDetailedSalesXML()
	{
		System.out.println("**************************************");
		System.out.println("* TEST - CONSTRUCT DETAILED SALES XML*");
		System.out.println("**************************************");
		
		BusinessIntelligenceXMLConstructor xmlConstructor = new BusinessIntelligenceXMLConstructor();
		System.out.println(xmlConstructor.detailedSale(PlugBusinessIntelligence.detailedSaleObject()));
	}
	
	public static void constructcategorizedSalesXML()
	{
		System.out.println("*****************************************");
		System.out.println("* TEST - CONSTRUCT CATEGORIZED SALES XML*");
		System.out.println("*****************************************");
		
		BusinessIntelligenceXMLConstructor xmlConstructor = new BusinessIntelligenceXMLConstructor();
		System.out.println(xmlConstructor.categorizedSale(PlugBusinessIntelligence.categorizedSaleObject()));
	}
}
