package fr.epita.sigl.miwa.bo.plug;

public class PlugReferential {
	public static String articles =
	"<XML>" +
	"<ENTETE objet='liste-article' source='mdm' date='2013-12-22'/>" +
		"<ARTICLES>" +
			"<ARTICLE reference='ART5678' ean='RUED' categorie='CAT898' prix_fournisseur='46' prix_vente='70'>" +
				"<DESCRIPTION>Texte multi lignes de description du produit.</DESCRIPTION>" +
				"<PROMOTIONS>" +
					"<PROMOTION debut='2014-01-08' fin='2014-01-26' pourcent='50' />" +
				"</PROMOTIONS>" +
			"</ARTICLE>" +
		"</ARTICLES>" +
	"</XML>";
}
