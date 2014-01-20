package fr.epita.sigl.miwa.application.MDM;

import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.application.CR.PromotionArticleCR;

public class ProductsClientMDM {
	private List<ArticleAVendreMDM> articles = new ArrayList<ArticleAVendreMDM>();
	private ProductsClientEnteteMDM entete;

	public ProductsClientMDM()
	{
	}
	
	public ProductsClientEnteteMDM getEntete()
	{
		return entete;
	}
	
	public void setEntete(ProductsClientEnteteMDM entete)
	{
		this.entete = entete;
	}
	
	public List<ArticleAVendreMDM> getArticles() {
		return articles;
	}

	public void SetArticles(List<ArticleAVendreMDM> products) {
		this.articles = products;
	}
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("***** Article A Vendre - PRODUCTS : [\n");
		if (entete != null)
			result.append(entete.print_logger());
		else
			result.append("*****	Entete : [ NULL ]\n");
		
		if (articles != null && !articles.isEmpty())
		{
			Integer i = 0;
			for(ArticleAVendreMDM a : articles)
			{
				result.append("*****	Article ");
				result.append(++i);
				result.append(" : [\n");
				
				result.append(a.print_logger() + "\n");
				
				result.append("*****	]\n");
			}
			
		}
		else
			result.append("*****	Article : [ NULL ]\n");
		
		return result.toString();
	}
	
	public void print()
	{
		System.out.println("Article A Vendre - PRODUCTS : [");
		entete.print();
		Integer i = 0;
		for(ArticleAVendreMDM a : articles)
		{
			System.out.println("	Article " + ++i + " : [");
			a.print();
			System.out.println("		]");
		}
		System.out.println("	]");
	}
}
