package fr.epita.sigl.miwa.application.MDM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.MiwaBDDIn;
import fr.epita.sigl.miwa.application.ParseXML;
import fr.epita.sigl.miwa.application.CR.PromotionArticleCR;

public class ProductsClientMDM {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
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
	
	public Boolean cleanTable()
	{
		MiwaBDDIn bdd = MiwaBDDIn.getInstance();
		
		return bdd.executeStatement("DELETE FROM article");
	}
	
	public Boolean addBDD()
	{
		for (ArticleAVendreMDM a : articles)
			if (!a.addBDD())
				return false;
		return true;
	}
	
	public Boolean getTable()
	{
		MiwaBDDIn bdd = MiwaBDDIn.getInstance();
		
		ResultSet rs = bdd.executeStatement_result("SELECT * FROM article");
		
		try {
			while (rs.next())
			{			
				ArticleAVendreMDM a = new ArticleAVendreMDM(rs.getString("reference"),
						rs.getString("ean"),
						rs.getString("categorie"),
						rs.getString("prix_fournisseur"),
						rs.getString("prix_vente"),
						rs.getString("description"));
				a.getPromotionsBDD();				
				
				articles.add(a);
			}
			
			return true;
		} catch (SQLException e) {
			LOGGER.info("***** Erreur SQL : " + e.getMessage());
			return false;
		}
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
		if (entete != null)
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
