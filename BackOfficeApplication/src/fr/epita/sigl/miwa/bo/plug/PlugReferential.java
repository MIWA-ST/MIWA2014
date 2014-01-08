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
			"<ARTICLE reference='ART5999' ean='UIJS' categorie='CAT453' prix_fournisseur='64' prix_vente='100'>" +
				"<DESCRIPTION>Texte multi lignes de description du deuxième produit.</DESCRIPTION>" +
				"<PROMOTIONS>" +
					"<PROMOTION debut='2014-01-08' fin='2014-01-26' pourcent='50' />" +
					"<PROMOTION debut='2014-01-27' fin='2014-01-30' pourcent='70' />" +
				"</PROMOTIONS>" +
			"</ARTICLE>" +
		"</ARTICLES>" +
	"</XML>";
}
