
package fr.epita.sigl.miwa.bo.parser;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Node;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.Payment;
import fr.epita.sigl.miwa.bo.object.Sale;
import fr.epita.sigl.miwa.bo.object.SalesTicket;
import fr.epita.sigl.miwa.bo.util.Convert;

public class DomParserCashRegister extends DomParser
{
	public DomParserCashRegister(String xml)
	{
		super(xml);
	}
	
	/*
		Caisse => BO (Envoi tickets de vente)
		fréquence = une fois par jour
		<ENTETE objet="ticket-vente-journee" source="caisse" date="AAAAA-MM-JJ"/>
		<VENTES>
			<VENTE client="" montanttotal="" dateheure="AAAAA-MM-JJ HH:mm:ss">
				<PAIEMENT type="" montant="" />
				<ARTICLE nomarticle="" refarticle="" quantite="" prix="" />
			</VENTE>
		</VENTES>
	*/
	public SalesTicket salesTicket()
	{
		SalesTicket salesTicket = new SalesTicket();
		
		String header = DomParserHelper.getHeader(this.xml);
		String body = DomParserHelper.getBody(this.xml);
		
		this.setXml(header);
		this.updateDoc();
	
		salesTicket.date = Convert.stringToDate(DomParserHelper.getNodeAttr("ENTETE", "date", this.doc.getChildNodes()), "AAAA-MM-JJ");
		
		this.setXml(body);
		this.updateDoc();
		
		List<Sale> sales = new ArrayList<>();
		
		List<Node> salesNode = DomParserHelper.getNodes("VENTE", this.doc.getChildNodes());
		
		for(Node saleNode : salesNode)
		{
			Sale sale = new Sale();
			sale.customer = DomParserHelper.getNodeAttr("client", saleNode);
			sale.total = DomParserHelper.getNodeAttr("montanttotal", saleNode);
			sale.paymentMeans = DomParserHelper.getNodeAttr("moyenpaiement", saleNode);			
			sale.dateAndTime = Convert.stringToDate(DomParserHelper.getNodeAttr("dateheure", saleNode), "AAAA-MM-JJ HH:mm:ss");
			
			Payment payment = new Payment();
			Node paymentNode = DomParserHelper.getNode("PAIEMENT", saleNode);
			payment.type = DomParserHelper.getNodeAttr("type", paymentNode);
			payment.amount = DomParserHelper.getNodeAttr("montant", paymentNode);
			
			sale.payment = payment; 
			
			List<Node> articlesNode = DomParserHelper.getNodes("ARTICLE", saleNode);
			
			List<Article> articles = new ArrayList<Article>();
			
			for (Node articleNode : articlesNode)
			{
				Article article = new Article();
			
				article.name = DomParserHelper.getNodeAttr("nomarticle", articleNode);
				article.reference = DomParserHelper.getNodeAttr("refarticle", articleNode);
				article.quantity = Integer.parseInt(DomParserHelper.getNodeAttr("quantite", articleNode));
				article.salesPrice = Float.parseFloat(DomParserHelper.getNodeAttr("prix", articleNode));
				
				articles.add(article);
			}
			sale.articles = articles;
			
			sales.add(sale);
		}
		
		salesTicket.sales = sales;
		
		salesTicket.print();
		
		return salesTicket;
	}
}
