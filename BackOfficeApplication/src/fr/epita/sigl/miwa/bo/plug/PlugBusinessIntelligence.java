package fr.epita.sigl.miwa.bo.plug;

import java.util.Date;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.CategorizedSale;
import fr.epita.sigl.miwa.bo.object.DetailedSale;
import fr.epita.sigl.miwa.bo.object.Sale;

public class PlugBusinessIntelligence {

	public static String detailedSale =
	"<XML>" +
	    "<ENTETE objet='ventes détaillées' source='bo' date='2013-12-22' />" +
	    "<VENTES-DETAILLEES lieu='MAG6793'>" +
	        "<VENTE numero_client='ALEX1234' montant='80' moyen_paiement='CB' dateHeure='2013-12-20 22:30:59'>" +
	            "<ARTICLES>" +
	                "<ARTICLE ref-article='ART3498' quantité='8' />" +
	            "</ARTICLES>" +
	        "</VENTE>" +
	    "</VENTES-DETAILLEES>" +
	"</XML>";
	
	public static String categorizedSale =
	"<XML>" +
	    "<ENTETE objet='ventes 15min' source='bo' date='2013-12-20 22:30:59' />" +
	    "<VENTES lieu='MAG67893'>" +
	        "<CATEGORIE ref-categorie='CAT8786' quantité_vendue='3' montant_fournisseur='12' montant_vente='16' />" +
	        "<CATEGORIE ref-categorie='CAT0973' quantité_vendue='2' montant_fournisseur='67' montant_vente='89' />" +       
	    "</VENTES>" +
	"</XML>";
	
	@SuppressWarnings("deprecation")
	public static DetailedSale detailedSaleObject()
	{
		DetailedSale ds = new DetailedSale();
		
		ds.date = new Date(2014 - 1900, 06, 25);
		ds.location = "MAG6547";
		
		Sale s1 = new Sale();
		s1.customerNumber = "s1cn";
		s1.total = "111";
		s1.paymentMeans = "cb";
		s1.dateAndTime = new Date(2014 - 1900, 03, 25, 22, 43, 8);
		
		Article a11 = new Article();
		a11.reference = "a11ref";
		a11.quantity = "11";
		s1.articles.add(a11);
		
		Article a12 = new Article();
		a12.reference = "a12ref";
		a12.quantity = "12";
		s1.articles.add(a12);
		
		ds.sales.add(s1);
		
		
		Sale s2 = new Sale();
		s2.customerNumber = "s2cn";
		s2.total = "222";
		s2.paymentMeans = "es";
		s2.dateAndTime = new Date(2014 - 1900, 04, 28, 20, 18, 26);;
		
		Article a21 = new Article();
		a21.reference = "a21ref";
		a21.quantity = "21";
		s2.articles.add(a21);
		
		ds.sales.add(s1);
		
		return ds;
	}
	
	@SuppressWarnings("deprecation")
	public static CategorizedSale categorizedSaleObject()
	{
		CategorizedSale cs = new CategorizedSale();
		
		cs.date = new Date(2014 - 1900, 03, 25);
		cs.location = "MAG536782";
		
		Article a1 = new Article();
		a1.category = "c1";
		a1.quantity = "1";
		a1.providerPrice = "11";
		a1.salesPrice ="111";
		cs.articles.add(a1);
		
		Article a2 = new Article();
		a1.category = "c2";
		a1.quantity = "2";
		a1.providerPrice = "22";
		a1.salesPrice ="222";
		cs.articles.add(a2);
		
		return cs;
	}
}