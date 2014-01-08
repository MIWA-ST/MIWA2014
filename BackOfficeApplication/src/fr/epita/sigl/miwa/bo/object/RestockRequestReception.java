package fr.epita.sigl.miwa.bo.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestockRequestReception
{
	public String orderNumber = null;
	public String status = null;
	public String comment = null;
	public Date deliveryDate = null;
	public List<Article> articles = null;
	
	public RestockRequestReception()
	{
		 this.articles = new ArrayList<Article>();
	}

	public void print()
	{
		System.out.println("==========RESTOCK=REQUEST=RECEPTION=BEGIN==========");
		System.out.println("ORDER NUMBER:" + this.orderNumber);
		System.out.println("STATUS:" + this.status);
		System.out.println("COMMENT:" + this.comment);
		if (this.deliveryDate != null)
		{
			System.out.println("DELIVERY DATE:" + this.deliveryDate.toString());			
		}
		else
		{
			System.out.println("DELIVERY DATE:");
		}
		System.out.println("==========RESTOCK=REQUEST=RECEPTION=>ARTICLES=BEGIN==========");
		for (Article article : this.articles)
		{
			article.print();
		}
		System.out.println("==========RESTOCK=REQUEST=RECEPTION=>ARTICLES=END==========");
		System.out.println("==========RESTOCK=REQUEST=RECEPTION=END==========");
	}
}
