
package fr.epita.sigl.miwa.bo.parser;

import java.util.List;
import org.w3c.dom.Node;

import fr.epita.sigl.miwa.bo.object.Article;
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

		sale.print();
		
		return sale;
	}

}
