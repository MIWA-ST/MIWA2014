package fr.epita.sigl.miwa.bo.xmlconstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.CategorizedSale;
import fr.epita.sigl.miwa.bo.object.DetailedSale;
import fr.epita.sigl.miwa.bo.object.NodeAttribute;
import fr.epita.sigl.miwa.bo.object.RestockRequestReception;
import fr.epita.sigl.miwa.bo.object.Sale;

public class BusinessIntelligenceXMLConstructor extends XMLConstructor
{
	/* LOG : *****
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

		this.openNode("XML", null, 0);
		
		List<NodeAttribute> headerAttributes = new ArrayList<NodeAttribute>();
		headerAttributes.add(new NodeAttribute("objet", "ventes détaillées"));
		headerAttributes.add(new NodeAttribute("source", "bo"));
		headerAttributes.add(new NodeAttribute("date", new SimpleDateFormat("YYYY-MM-dd").format(detailedSale.date)));
		this.openClosedNode("ENTETE", headerAttributes, 1);
		
		List<NodeAttribute> detailedSaleAttributes = new ArrayList<NodeAttribute>();
		detailedSaleAttributes.add(new NodeAttribute("lieu", detailedSale.location));
		this.openNode("VENTES-DETAILLEES", detailedSaleAttributes, 1);
		
		for (Sale sale : detailedSale.sales)
		{
			List<NodeAttribute> saleAttributes = new ArrayList<NodeAttribute>();
			saleAttributes.add(new NodeAttribute("numero_client", sale.customerNumber));
			saleAttributes.add(new NodeAttribute("montant", sale.total));
			saleAttributes.add(new NodeAttribute("moyen_paiement", sale.paymentMeans));
			saleAttributes.add(new NodeAttribute("dateHeure", new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(sale.dateAndTime)));
			this.openNode("VENTE", saleAttributes, 2);
			
			this.openNode("ARTICLES", null, 3);
			
			for (Article article : sale.articles)
			{
				List<NodeAttribute> articleAttributes = new ArrayList<NodeAttribute>();
				articleAttributes.add(new NodeAttribute("ref-article", article.reference));
				articleAttributes.add(new NodeAttribute("quantité", article.quantity));
				this.openClosedNode("ARTICLE", articleAttributes, 4);
			}
			
			this.closeNode("ARTICLES", 3);
			
			this.closeNode("VENTE", 2);
		}
		
		this.closeNode("VENTES-DETAILLEES", 1);
		
		this.closeNode("XML", 1);
		
		return this.xml;
	}
	
	/* LOG : *****
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
	    </VENTES>
	</XML>
	*/
	public String categorizedSale(CategorizedSale categorizedSale)
	{
		if (categorizedSale == null)
		{
			return null;
		}

		this.openNode("XML", null, 0);
		
		List<NodeAttribute> headerAttributes = new ArrayList<NodeAttribute>();
		headerAttributes.add(new NodeAttribute("objet", "ventes 15min"));
		headerAttributes.add(new NodeAttribute("source", "bo"));
		headerAttributes.add(new NodeAttribute("date", new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(categorizedSale.date)));
		this.openClosedNode("ENTETE", headerAttributes, 1);
		
		List<NodeAttribute> salesAttributes = new ArrayList<NodeAttribute>();
		salesAttributes.add(new NodeAttribute("lieu", categorizedSale.location));
		this.openNode("VENTES", salesAttributes, 1);
		
		for (Article article : categorizedSale.articles)
		{
			List<NodeAttribute> articleAttributes = new ArrayList<NodeAttribute>();
			articleAttributes.add(new NodeAttribute("ref-categorie", article.category));
			articleAttributes.add(new NodeAttribute("quantité_vendue", article.quantity));
			articleAttributes.add(new NodeAttribute("montant_fournisseur", article.providerPrice));
			articleAttributes.add(new NodeAttribute("montant_vente", article.salesPrice));
			this.openClosedNode("CATEGORIE", articleAttributes, 2);
		}
		
		this.closeNode("VENTES", 1);
		
		this.closeNode("XML", 0);
		
		return this.xml;
	}
}