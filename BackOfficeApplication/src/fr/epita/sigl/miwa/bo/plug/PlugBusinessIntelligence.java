package fr.epita.sigl.miwa.bo.plug;

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
	
	public static String fifteenMinuteSale =
	"<XML>" +
	    "<ENTETE objet='ventes 15min' source='bo' date='2013-12-20 22:30:59' />" +
	    "<VENTES lieu='MAG67893'>" +
	        "<CATEGORIE ref-categorie='CAT8786' quantité_vendue='3' montant_fournisseur='12' montant_vente='16' />" +
	        "<CATEGORIE ref-categorie='CAT0973' quantité_vendue='2' montant_fournisseur='67' montant_vente='89' />" +       
	    "</VENTES>" +
	"</XML>";
}