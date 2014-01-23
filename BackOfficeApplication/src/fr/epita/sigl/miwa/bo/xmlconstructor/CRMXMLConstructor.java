package fr.epita.sigl.miwa.bo.xmlconstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.bo.object.Article;
import fr.epita.sigl.miwa.bo.object.NodeAttribute;
import fr.epita.sigl.miwa.bo.object.Sale;

public class CRMXMLConstructor extends XMLConstructor
{
	/* LOG : ***** 
	 * <ENETE objet="ticket-client-fidelise" source="bo" date="AAAAA-MM-JJ"/>
	 * <TICKETVENTE client="" moyenpayement="" >
	 * 		<ARTICLE refarticle="" quantite="" prix="" />
	 * 		<ARTICLE refarticle="" quantite="" prix="" />
	 * </TICKETVENTE>
	 */
	public String loyalCustomerTicket(Sale sale)
	{
		if (sale == null)
		{
			return null;
		}
		
		List<NodeAttribute> headerAttributes = new ArrayList<NodeAttribute>();
		headerAttributes.add(new NodeAttribute("objet", "ticket-client-fidelise"));
		headerAttributes.add(new NodeAttribute("source", "bo"));
		headerAttributes.add(new NodeAttribute("date", new SimpleDateFormat("YYYY-MM-dd").format(sale.dateAndTime)));
		this.openClosedNode("ENTETE", headerAttributes, 0);
		
		List<NodeAttribute> ticketAttributes = new ArrayList<NodeAttribute>();
		ticketAttributes.add(new NodeAttribute("client", sale.customerNumber));
		ticketAttributes.add(new NodeAttribute("moyenpayemen", sale.paymentMeans));
		this.openNode("TICKETVENTE", ticketAttributes, 0);
		

		for (Article article : sale.articles)
		{
			List<NodeAttribute> articleAttributes = new ArrayList<NodeAttribute>();
			articleAttributes.add(new NodeAttribute("refarticle", article.reference));
			articleAttributes.add(new NodeAttribute("quantite", article.quantity));
			articleAttributes.add(new NodeAttribute("prix", article.salesPrice));
			this.openClosedNode("ARTICLE", articleAttributes, 1);
		}

		
		this.closeNode("TICKETVENTE", 0);
		
		return this.xml;
	}

	public String saleTicket(String message) {

		return null;
	}
}
