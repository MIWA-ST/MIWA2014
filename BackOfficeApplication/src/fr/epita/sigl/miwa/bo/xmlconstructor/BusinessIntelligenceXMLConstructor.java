package fr.epita.sigl.miwa.bo.xmlconstructor;

import fr.epita.sigl.miwa.bo.object.CategorizedSale;
import fr.epita.sigl.miwa.bo.object.DetailedSale;
import fr.epita.sigl.miwa.bo.object.RestockRequestReception;

public class BusinessIntelligenceXMLConstructor extends XMLConstructor
{
	/*
	Objet : Ventes détaillées
	Provenance : Internet ou BO
	Destination : BI
	Fréquence : 1 fois par jour

	Champs du XML :
	    ENTETE
	        objet : ne pas modifier
	        source : mettre "bo" ou "internet" selon l'émetteur
	        date : format AAAA-MM-JJ
	    VENTES-DETAILLEES 
	        lieu : référence du magasin ou string "internet" (string; max 32)
	        numero_client : numéro du client (string; max 32) - si client inconnu, string vide (et pas null)
	        montant : montant total du ticket (numeric 2)
	        moyen_paiement : moyen de paiement utilisé (string; max 2) - "CB" pour carte bancaire, "CF" pour carte fidélité, "CQ" pour chèques ou "ES" pour espèces
	        dateHeure : date et heure de la vente (format AAAA-MM-JJ HH:mm:SS)
	        ref-article : référence de l'article (string; max 32)
	        quantité : quantité vendue d'article (int)
	<XML>
	    <ENTETE objet="ventes détaillées" source="bo" date="" />
	    <VENTES-DETAILLEES lieu="">
	        <VENTE numero_client="" montant="" moyen_paiement="" dateHeure="">
	            <ARTICLES>
	                <ARTICLE ref-article="" quantité="" />
	                <!-- plusieurs articles -->
	            </ARTICLES>
	        </VENTE>
	        <!-- plusieurs ventes -->
	    </VENTES-DETAILLEES>
	</XML>
	*/
	public String detailedSale(DetailedSale detailedSale)
	{
		if (detailedSale == null)
		{
			return null;
		}

		return this.xml;
	}
	
	
	/*
	Objet : Ventes par catégorie d'article
	Provenance : Internet ou BO
	Destination : BI
	Fréquence : toutes les 15 minutes

	Champs du XML :
	    Entête
	        objet : ne pas modifier
	        source : mettre "bo" ou "internet" selon l'émetteur
	        date : format AAAA-MM-JJ HH:mm:SS
	    Ventes
	        lieu : référence du magasin ou string "internet" (string; max 32)
	        ref-categorie : référence de la catégorie d'article (string; max 32)
	        quantité_vendue : quantité vendue d'articles de la catégorie (int)
	        montant_fournisseur : somme totale vendue en prix fournisseur (numeric 2)
	        montant_vente : somme totale vendue en prix de vente (numeric 2)
	<XML>
	    <ENTETE objet="ventes 15min" source="" date="" />
	    <VENTES lieu="">
	        <CATEGORIE ref-categorie="" quantité_vendue="" montant_fournisseur="" montant_vente="" />
	        <CATEGORIE ref-categorie="" quantité_vendue="" montant_fournisseur="" montant_vente="" />
	        <CATEGORIE ref-categorie="" quantité_vendue="" montant_fournisseur="" montant_vente="" />
	        <!-- plusieurs catégories -->
	    </STOCKS>
	</XML>
	*/
	public String categorizedSale(CategorizedSale categorizedSale)
	{
		if (categorizedSale == null)
		{
			return null;
		}

		return this.xml;
	}
}
