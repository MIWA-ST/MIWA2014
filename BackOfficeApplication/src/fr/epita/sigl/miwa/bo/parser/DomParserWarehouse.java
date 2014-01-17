package fr.epita.sigl.miwa.bo.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.Delivery;
import fr.epita.sigl.miwa.bo.object.DeliveryList;
import fr.epita.sigl.miwa.bo.object.Payment;
import fr.epita.sigl.miwa.bo.object.Sale;
import fr.epita.sigl.miwa.bo.object.SalesTicket;
import fr.epita.sigl.miwa.bo.util.Convert;

public class DomParserWarehouse extends DomParser
{
	public DomParserWarehouse()
	{
	}
	
	/* LOG : ***** 
	Entrepôt => BO
	numero : CHAR(32)
	refmagasin: CHAR(32)
	DATEBC : Datetime
	DATEBL : Datetime
	reference : CHAR(32)
	quantite : Numeric(10)
	<LIVRAISONS>
		<LIVRAISON>
			<NUMERO>CV398719873</NUMERO>
			<REFMAGASIN>6876786</REFMAGASIN>
			<DATEBC>20131225</DATEBC> <!-- AAAAMMJJ Date de commande -->
			<DATEBL>20131225</DATEBL> <!-- AAAAMMJJ Date de livraison -->
			<ARTICLE>
				<REFERENCE>AU736827</REFERENCE>
				<QUANTITE>265000</QUANTITE>
			</ARTICLE>
			<ARTICLE>
				<REFERENCE>AU736823</REFERENCE>
				<QUANTITE>12</QUANTITE>
			</ARTICLE>
		</LIVRAISON>
	</LIVRAISONS>
	*/
	public DeliveryList deliveryList(String xml)
	{
		DeliveryList deliveryList  = new DeliveryList();
	
		this.setXml(xml);
		this.updateDoc();
		
		List<Node> deliverieNodes = DomParserHelper.getNodes("LIVRAISON", this.doc.getChildNodes());
		
		for(Node deliverieNode : deliverieNodes)
		{
			Delivery delivery = new Delivery();
			
			delivery.number = DomParserHelper.getNode("NUMERO", deliverieNode).getTextContent();
			delivery.shopReference = DomParserHelper.getNode("REFMAGASIN", deliverieNode).getTextContent();
			delivery.orderDate =  Convert.stringToDate(DomParserHelper.getNode("DATEBC", deliverieNode).getTextContent(), "AAAAMMJJ");
			delivery.deliveryDate =  Convert.stringToDate(DomParserHelper.getNode("DATEBL", deliverieNode).getTextContent(), "AAAAMMJJ");
			
			List<Node> articleNodes = DomParserHelper.getNodes("ARTICLE", deliverieNode);
			
			for (Node articleNode : articleNodes)
			{
				Article article = new Article();
				
				article.reference = DomParserHelper.getNode("REFERENCE", articleNode).getTextContent();
				article.quantity = DomParserHelper.getNode("QUANTITE", articleNode).getTextContent();
				
				delivery.articles.add(article);
			}
			
			deliveryList.deliveries.add(delivery);
		}
		
		return deliveryList;
	}
}
