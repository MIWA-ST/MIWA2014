package fr.epita.sigl.miwa.bo.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleAndLocalPriceAndPromotion
{
	public Date date = null;
	public List<Article> articles = null;
	
	public ArticleAndLocalPriceAndPromotion()
	{
		this.articles = new ArrayList<Article>();
	}
	public void print()
	{
		System.out.println("***** ==========ARTICLE=AND=LOCAL=PRICE=AND=PROMOTION=BEGIN==========");
		if (this.date != null)
		{
			System.out.println("***** DATE:" + this.date.toString());			
		}
		else
		{
			System.out.println("***** DATE:");
		}
		System.out.println("***** ==========ARTICLE=AND=LOCAL=PRICE=AND=PROMOTION=>ARTICLES=BEGIN==========");
		for (Article article : this.articles)
		{
			article.print();
		}
		System.out.println("***** ==========ARTICLE=AND=LOCAL=PRICE=AND=PROMOTION=>ARTICLES=END==========");
		System.out.println("***** ==========ARTICLE=AND=LOCAL=PRICE=AND=PROMOTION=END==========");
	}
}
