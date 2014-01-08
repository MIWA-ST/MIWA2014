package fr.epita.sigl.miwa.bo.plug;

import java.util.Date;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.RestockRequest;
import fr.epita.sigl.miwa.bo.object.RestockRequestReception;

public class PlugBusinessManagement {
	
	public static String restockRequest =
	"<REASSORT>" +
		"<NUMERO>CV398719873</NUMERO>" +
		"<REFBO>20131225</REFBO>" +
		"<ADRESSEBO>XXXXXX</ADRESSEBO>" + 
		"<TELBO>0133333333</TELBO>" +
		"<DATEBC>20130427</DATEBC>" + // AAAAMMJJ
		"<ARTICLES>" +
			"<ARTICLE>" +
				"<REFERENCE>AU736827</REFERENCE>" +
				"<QUANTITE>265000</QUANTITE>" +
				"<CATEGORIE>001</CATEGORIE>" +
			"</ARTICLE>" +
			"<ARTICLE>" +
				"<REFERENCE>AU736823</REFERENCE>" +
				"<QUANTITE>12</QUANTITE>" +
				"<CATEGORIE>001</CATEGORIE>" +
			"</ARTICLE>" +
		"<ARTICLES>" +
	"</REASSORT>";
			
	public static String restockRequestReception =
	"<RECEPTIONREASSORT>" +
		"<NUMEROCOMMANDE>CV398719873</NUMEROCOMMANDE>" +
		"<DATELIVRAISON>20131225</DATELIVRAISON>" + // AAAAMMJJ
		"<STATUT>TRUE</STATUT>" +
		"<COMMENTAIRE>Rien à signaler</COMMENTAIRE>" +
		"<ARTICLES>" +
			"<ARTICLE>" +
				"<REFERENCE>AU736827</REFERENCE>" +
				"<QUANTITE>265000</QUANTITE>" +
				"<CATEGORIE>001</CATEGORIE>" +
			"</ARTICLE>" +
			"<ARTICLE>" +
				"<REFERENCE>AU736823</REFERENCE>" +
				"<QUANTITE>12</QUANTITE>" +
				"<CATEGORIE>001</CATEGORIE>" +
			"</ARTICLE>" +
		"</ARTICLES>" +
	"</RECEPTIONREASSORT>";

	@SuppressWarnings("deprecation")
	public static RestockRequest restockRequestObject()
	{
		RestockRequest rr = new RestockRequest();
		
		rr.number = "CV398719873";
		rr.backOfficeReference = "20131225";
		rr.backOfficeAdresse = "XXXXXX";
		rr.backOfficePhone = "0133333333";
		rr.date = new Date(2014 - 1900, 03, 25);
		
		Article a1 = new Article();
		a1.reference = "AU736827";
		a1.quantity = "265000";
		a1.category = "001";
		rr.articles.add(a1);
		
		Article a2 = new Article();
		a2.reference = "AU736823";
		a2.quantity = "12";
		a2.category = "002";
		rr.articles.add(a2);
				
		return rr;
	}
	
	@SuppressWarnings("deprecation")
	public static RestockRequestReception restockRequestReceptionObject()
	{
		RestockRequestReception rrr = new RestockRequestReception();
		
		rrr.orderNumber = "CV398719873";
		rrr.deliveryDate = new Date(2014 - 1900, 03, 18);
		rrr.status = "TRUE";
		rrr.comment = "Rien à signaler";
		
		Article a1 = new Article();
		a1.reference = "AU736827";
		a1.quantity = "265000";
		a1.category = "001";
		rrr.articles.add(a1);
		
		Article a2 = new Article();
		a2.reference = "AU736823";
		a2.quantity = "12";
		a2.category = "002";
		rrr.articles.add(a2);
		
		return rrr;
	}
}