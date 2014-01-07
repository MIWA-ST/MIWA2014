package fr.epita.sigl.miwa.bo.object;

import java.util.Date;
import java.util.List;

public class Delivery
{
	public String number = null;
	public String shopReference = null;
	public Date orderDate = null; // AAAAMMJJ
	public Date deliveryDate = null; // AAAAMMJJ
	public List<Article> articles = null;
	
	public void print()
	{
		System.out.println("==========DELIVERY=BEGIN==========");
		System.out.println("NUMBER:" + this.number);
		System.out.println("SHOP REFERENCE:" + this.shopReference);
		if (this.orderDate != null)
		{
			System.out.println("ORDER DATE:" + this.orderDate.toString());			
		}
		else
		{
			System.out.println("ORDER DATE:");
		}
		if (this.deliveryDate != null)
		{
			System.out.println("DELIVERY DATE:" + this.deliveryDate.toString());			
		}
		else
		{
			System.out.println("DELIVERY DATE:");
		}
		System.out.println("==========DELIVERY=>ARTICLES=BEGIN==========");
		if (this.articles != null)
		{
			for (Article article : this.articles)
			{
				article.print();
			}
		}
		System.out.println("==========DELIVERY=>ARTICLES=END==========");
		System.out.println("==========DELIVERY=END==========");
	}
}
