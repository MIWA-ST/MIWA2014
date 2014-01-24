package fr.epita.sigl.miwa.bo.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleAndLocalPriceAndPromotion
{
	public Date date = null;
	public List<Article> articles = null;

	public String number = null;
	public String refclient = null;
	public String refshop = null;
	public String totalamount = null;

	public ArticleAndLocalPriceAndPromotion()
	{
		this.articles = new ArrayList<Article>();
	}
	
	public ArticleAndLocalPriceAndPromotion(ArticleList articles)
	{
		this.articles = new ArrayList<Article>();
		this.number = articles.number;
		this.refclient = articles.refclient;
		this.refshop = articles.refshop;
		this.totalamount = articles.totalamount;
		this.date = articles.date;
		
		for (Article artiste : articles.articles)
		{
			this.articles.add(artiste);
		}
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
