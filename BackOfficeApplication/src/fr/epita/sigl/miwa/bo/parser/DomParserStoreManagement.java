
package fr.epita.sigl.miwa.bo.parser;

import java.util.List;
import org.w3c.dom.Node;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.ArticleList;
import fr.epita.sigl.miwa.bo.object.Sale;
import fr.epita.sigl.miwa.bo.util.Convert;

public class DomParserStoreManagement extends DomParser
{
	public DomParserStoreManagement()
	{
	}
	
	/*
	 * 
	CRM => BO (Panier avec réducs affectées)

	<ENTETE objet="facture-client" source="crm" date="AAAAA-MM-JJ"/>
	<FACTURE refclient="" montanttotal="" >
	    <ARTICLE refarticle="" quantite="" nvprix="" />
	    <ARTICLE refarticle="" quantite="" nvprix="" />
	</FACTURE>
	 
	 */
	public Sale saleTicket(String xml)
	{
		Sale sale = new Sale();
		
		String header = DomParserHelper.getHeader(xml);
		String body = DomParserHelper.getBody(xml);
		
		this.setXml(header);
		this.updateDoc();
	
		sale.dateAndTime= Convert.stringToDate(DomParserHelper.getNodeAttr("ENTETE", "date", this.doc.getChildNodes()), "AAAA-MM-JJ");
				
		this.setXml(body);
		this.updateDoc();
		

		Node saleNode = DomParserHelper.getNode("FACTURE", this.doc);
		sale.customer = DomParserHelper.getNodeAttr("refclient", saleNode);
		sale.total = DomParserHelper.getNodeAttr("montanttotal", saleNode);			

		List<Node> articleNodes = DomParserHelper.getNodes("ARTICLE", saleNode.getChildNodes());
		
		for (Node articleNode : articleNodes)
		{
			Article article = new Article();
		
			article.reference = DomParserHelper.getNodeAttr("refarticle", articleNode);
			article.quantity = DomParserHelper.getNodeAttr("quantite", articleNode);
			article.salesPrice = DomParserHelper.getNodeAttr("nvprix", articleNode);
			
			sale.articles.add(article);
		}
		
		return sale;
	}
	
	/*
	<!-- 
	GC => BO
		numero : CHAR(32) (Numéro de la demande)
		DATE : Datetime  (Date de la demande de niveau de stock)
		REFERENCE : CHAR(32)
	-->
	<DEMANDENIVEAUDESTOCK>
		<NUMERO>CV398719873</NUMERO>
		<REFMAGASIN>PA218765</REFMAGASIN>
		<DATE>20131225</DATE> <!-- AAAAMMJJ -->
		<ARTICLES>
			<ARTICLE>
				<REFERENCE>AU736827</REFERENCE>
			</ARTICLE>
			<ARTICLE>
				<REFERENCE>AU736829</REFERENCE>
			</ARTICLE>
		</ARTICLES>
	</DEMANDENIVEAUDESTOCK>
	*/
	public ArticleList stockLevel(String xml)
	{
		ArticleList articleList = new ArticleList();

		this.setXml(xml);
		this.updateDoc();
		
		Node stocklevelNode = DomParserHelper.getNode("DEMANDENIVEAUDESTOCK", this.doc);
		
		articleList.number = DomParserHelper.getNode("NUMERO", stocklevelNode).getTextContent();
		articleList.refshop = DomParserHelper.getNode("REFMAGASIN", stocklevelNode).getTextContent();
		articleList.date = Convert.stringToDate(DomParserHelper.getNode("DATE", stocklevelNode).getTextContent(), "AAAAMMJJ");
		
		Node articlesNode = DomParserHelper.getNode("ARTICLES", stocklevelNode);
		
		List<Node> articleNodes = DomParserHelper.getNodes("ARTICLE", articlesNode);
		
		for (Node articleNode : articleNodes)
		{
			
			Article article = new Article();
		
			article.reference = DomParserHelper.getNode("REFERENCE", articleNode).getTextContent();

			articleList.articles.add(article);
		}
		
		return articleList;
	}

}
