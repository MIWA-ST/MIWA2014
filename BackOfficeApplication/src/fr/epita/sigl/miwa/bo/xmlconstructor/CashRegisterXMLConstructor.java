package fr.epita.sigl.miwa.bo.xmlconstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.ArticleAndLocalPriceAndPromotion;
import fr.epita.sigl.miwa.bo.object.ArticleList;
import fr.epita.sigl.miwa.bo.object.NodeAttribute;
import fr.epita.sigl.miwa.bo.object.Sale;

public class CashRegisterXMLConstructor extends XMLConstructor
{
	/* LOG : ***** 
	NOTRE PROPOSITION CORRIGEE
	BO => Caisse (Envoi article prix et promo local)
	fréquence = une fois par jour
	moment = ouverture de la caisse
	promotion = "" si pas de promotion sinon promotion entre 0 et 100

	ce qui a été corrigé :
	- il nous faut le nom de l'article, en plus de sa référence (son code barre)
	
	<ENTETE objet="article-prix-promo" source="bo" date="AAAAA-MM-JJ"/>
	<ARTICLES>
		<ARTICLE nomarticle="" refarticle="" prix="" promotion="" />
	</ARTICLES>
	*/
	public String articleAndLocalPriceAndPromotion(ArticleAndLocalPriceAndPromotion articleAndLocalPriceAndPromotion)
	{
		if (articleAndLocalPriceAndPromotion == null)
		{
			return null;
		}
		this.openNode("XML", null, 0);
		List<NodeAttribute> headerAttributes = new ArrayList<NodeAttribute>();
		headerAttributes.add(new NodeAttribute("objet", "article-prix-promo"));
		headerAttributes.add(new NodeAttribute("source", "bo"));
		System.out.println("toto : " + articleAndLocalPriceAndPromotion.date);
		headerAttributes.add(new NodeAttribute("date", new SimpleDateFormat("YYYY-MM-dd").format(articleAndLocalPriceAndPromotion.date)));
		this.openClosedNode("ENTETE", headerAttributes, 0);
		
		this.openNode("ARTICLES", null, 0);
		for (Article article : articleAndLocalPriceAndPromotion.articles)
		{
			List<NodeAttribute> articleAttributes = new ArrayList<NodeAttribute>();
			articleAttributes.add(new NodeAttribute("nomarticle", article.name));
			articleAttributes.add(new NodeAttribute("refarticle", article.reference));
			articleAttributes.add(new NodeAttribute("prix", article.salesPrice));
			articleAttributes.add(new NodeAttribute("promotion", article.promotion));
			this.openClosedNode("ARTICLE", articleAttributes, 1);
		}
		this.closeNode("ARTICLES", 0);
		this.closeNode("XML", 0);
		return this.xml;
	}
	
	/* LOG : ***** nom article, % promo, prix vente 
	NOTRE PROPOSITION CORRIGEE
	BO => Caisse (Envoi correctif prix)
	fréquence = en live

	ce qui a été corrigé :
	- il nous faut le nom de l'article, en plus de sa référence (son code barre)
	- les dates (de début et de fin) n'ont pas de sens : les promotions sont pour la journée en cours
	- le nom de l'entête ("objet") a été modifié pour que nous puissions identifier qu'il s'agit d'une mise à jour et non de l'envoi à l'ouverture de caisse
	-->
	<ENTETE objet="article-prix-promo-update" source="bo" date="AAAA-MM-JJ"/>
	<ARTICLES>
		<ARTICLE nomarticle="" refarticle="" prix="" promotion="" />
	</ARTICLES>
	*/
	public String articleAndLocalPriceAndPromotionUpdate(ArticleAndLocalPriceAndPromotion articleAndLocalPriceAndPromotion)
	{
		if (articleAndLocalPriceAndPromotion == null)
		{
			return null;
		}
		
		List<NodeAttribute> headerAttributes = new ArrayList<NodeAttribute>();
		headerAttributes.add(new NodeAttribute("objet", "article-prix-promo-update"));
		headerAttributes.add(new NodeAttribute("source", "bo"));
		headerAttributes.add(new NodeAttribute("date", new SimpleDateFormat("YYYY-MM-dd").format(articleAndLocalPriceAndPromotion.date)));
		this.openClosedNode("ENTETE", headerAttributes, 0);
		
		this.openNode("ARTICLES", null, 0);
		for (Article article : articleAndLocalPriceAndPromotion.articles)
		{
			List<NodeAttribute> articleAttributes = new ArrayList<NodeAttribute>();
			articleAttributes.add(new NodeAttribute("nomarticle", article.name));
			articleAttributes.add(new NodeAttribute("refarticle", article.reference));
			articleAttributes.add(new NodeAttribute("prix", article.salesPrice));
			articleAttributes.add(new NodeAttribute("promotion", article.promotion));
			this.openClosedNode("ARTICLE", articleAttributes, 1);
		}
		this.closeNode("ARTICLES", 0);
		
		return this.xml;
	}
	
	/*
	<ENTETE objet="facture-client" source="bo" date="AAAAA-MM-JJ"/>
	<FACTURE refclient="" montanttotal="" >
	    <ARTICLE refarticle="" quantite="" nvprix="" />
	    <ARTICLE refarticle="" quantite="" nvprix="" />
	</FACTURE>
	*/
	public String facture(Sale sale)
	{
		if (sale.articles == null)
		{
			return null;
		}
		this.openNode("XML", null, 0);
		List<NodeAttribute> headerAttributes = new ArrayList<NodeAttribute>();
		headerAttributes.add(new NodeAttribute("objet", "facture-client"));
		headerAttributes.add(new NodeAttribute("source", "bo"));
		headerAttributes.add(new NodeAttribute("date", new SimpleDateFormat("YYYY-MM-dd").format(sale.dateAndTime)));
		this.openClosedNode("ENTETE", headerAttributes, 0);
		
		List<NodeAttribute> factureAttributes = new ArrayList<NodeAttribute>();
		factureAttributes.add(new NodeAttribute("refclient", sale.customerNumber)); // NBA : changer en customernumber normalement, avec le parser !
		factureAttributes.add(new NodeAttribute("montanttotal", sale.total));
		this.openNode("FACTURE", factureAttributes, 0);
		for (Article article : sale.articles)
		{
			List<NodeAttribute> articleAttributes = new ArrayList<NodeAttribute>();
			articleAttributes.add(new NodeAttribute("refarticle", article.reference));
			articleAttributes.add(new NodeAttribute("quantite", article.quantity));
			articleAttributes.add(new NodeAttribute("nvprix", article.salesPrice));
			this.openClosedNode("ARTICLE", articleAttributes, 1);
		}
		this.closeNode("FACTURE", 0);
		this.closeNode("XML", 0);
		return this.xml;
	}
}
