package fr.epita.sigl.miwa.bo.object;

import java.util.Date;
import java.util.List;

public class Sale
{
	public String customer = null;
	public String paymentMeans = null;
	public String total = null;
	public Date dateAndTime = null; // AAAA-MM-JJ HH:mm:ss
	public List<Article> articles = null;
	
	public void print()
	{
		System.out.println("==========SALE=BEGIN==========");
		System.out.println("CUSOMER:" + this.customer);
		System.out.println("PAYMENT MEANS:" + this.paymentMeans);
		System.out.println("TOTAL:" + this.total);
		if (this.dateAndTime != null)
		{
			System.out.println("DATE AND TIME:" + this.dateAndTime.toString());			
		}
		else
		{
			System.out.println("DATE AND TIME:");
		}
		System.out.println("==========SALE=>ARTICLES=BEGIN==========");
		if (this.articles != null)
		{
			for (Article article : this.articles)
			{
				article.print();
			}
		}
		System.out.println("==========SALE=>ARTICLES=END==========");
		System.out.println("==========SALE=END==========");
	}
}
