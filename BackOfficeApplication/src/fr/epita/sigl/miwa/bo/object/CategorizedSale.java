package fr.epita.sigl.miwa.bo.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategorizedSale
{
	public Date date = null;
	public String location = null;
	public List<Article> articles = null;
	
	public CategorizedSale()
	{
		this.articles = new ArrayList<Article>();
	}
	
	public void print()
	{
		System.out.println("==========CATEGORIZED=SALE=BEGIN==========");
		if (this.date != null)
		{
			System.out.println("DATE:" + this.date.toString());			
		}
		else
		{
			System.out.println("DATE:");
		}
		System.out.println("LOCATION:" + this.location);
		System.out.println("==========CATEGORIZED=SALE=>SALES=BEGIN==========");
		for (Article article : this.articles)
		{
			article.print();
		}
		System.out.println("==========CATEGORIZED=SALE=>SALES=END==========");
		System.out.println("==========CATEGORIZED=SALE=END==========");
	}
}
