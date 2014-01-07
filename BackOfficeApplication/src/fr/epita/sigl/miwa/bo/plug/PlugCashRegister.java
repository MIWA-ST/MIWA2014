package fr.epita.sigl.miwa.bo.plug;

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
}
