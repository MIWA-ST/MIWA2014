package fr.epita.sigl.miwa.bo.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleList
{
	public Date date = null;
	public String refclient = null;
	public String totalamount = null;
	public List<Article> articles = null;
	
	public ArticleList()
	{
		this.articles = new ArrayList<Article>();
	}
	
	public void print()
	{
		System.out.println("==========ARTICLE=LIST=BEGIN==========");
		if (this.date != null)
		{
			System.out.println("DATE:" + this.date.toString());			
			System.out.println("REF CLIENT:" + this.refclient);
			System.out.println("TOTAL AMOUNT:" + this.totalamount);
		}
		else
		{
			System.out.println("DATE:");
		}
		System.out.println("==========ARTICLE=LIST=>ARTICLES=BEGIN==========");
		for (Article article : this.articles)
		{
			article.print();
		}
		System.out.println("==========ARTICLE=LIST=>ARTICLE=END==========");
		System.out.println("==========ARTICLE=LIST=END==========");
	}
}
