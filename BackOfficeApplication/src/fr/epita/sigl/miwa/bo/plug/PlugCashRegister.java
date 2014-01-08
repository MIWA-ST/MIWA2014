package fr.epita.sigl.miwa.bo.plug;

import java.util.Date;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.ArticleAndLocalPriceAndPromotion;

public class PlugCashRegister {
	
	public static String articleAndLocalPriceAndPromotion =
	"<ENTETE objet='article-prix-promo' source='bo' date='2013-12-18'/>" +
	"<ARTICLES>" +
		"<ARTICLE nomarticle='poire' refarticle='P456' prix='11' promotion='25' />" +
	"</ARTICLES>";
	
	public static String articlePricePromotionUpdate =
	"<ENTETE objet='article-prix-promo-update' source='bo' date='2013-12-18'/>" +
	"<ARTICLES>" +
		"<ARTICLE nomarticle='fraise' refarticle='F324' prix='12' promotion='50' />" +
	"</ARTICLES>";
	
	public static String salesTicket =
	"<ENTETE objet='ticket-vente-journee' source='caisse' date='2013-12-18'/>" +
	"<VENTES>" +
		"<VENTE client='Alex' montanttotal='200' dateheure='2013-12-22 20:22:21'>" +
			"<PAIEMENT type='cb' montant='200' />" +
			"<ARTICLE nomarticle='pomme' refarticle='P123' quantite='1' prix='200' />" +
		"</VENTE>" +
	"</VENTES>";
	
	public static ArticleAndLocalPriceAndPromotion articleAndLocalPriceAndPromotionObject()
	{
		ArticleAndLocalPriceAndPromotion a = new ArticleAndLocalPriceAndPromotion();
		a.date = new Date(2013 - 1900, 11, 20);
		
		Article a1 = new Article();
		a1.name = "n1";
		a1.reference = "r1";
		a1.salesPrice="p1";
		a1.promotion="pr1";
		a.articles.add(a1);
		
		Article a2 = new Article();
		a2.name = "n2";
		a2.reference = "r2";
		a2.salesPrice="p2";
		a2.promotion="pr2";
		a.articles.add(a2);

		return a;
	}
}
