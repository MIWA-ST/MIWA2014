package fr.epita.sigl.miwa.application.GC;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.MiwaBDDIn;
import fr.epita.sigl.miwa.application.ParseXML;

public class NiveauStockGC {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	private String numero;
	private String date;
	private List<ArticleNiveauStockRecuGC> articles = new ArrayList<ArticleNiveauStockRecuGC>();
	
	public NiveauStockGC()
	{
	}
	
	public NiveauStockGC(String numero, String date, List<ArticleNiveauStockRecuGC> articles)
	{
		this.numero = numero;
		this.date = date;
		this.articles = articles;
	}

	public void MAJBDD()
	{
		for (ArticleNiveauStockRecuGC a : articles)
			a.MAJBDD();
	}
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		LOGGER.info("***** Reception d'un message de la GC : reception des stocks suite à demande");
		
		result.append("***** DEMANDENIVEAUDESTOCKINTERNET : [\n");
		if (this.numero != null)
			result.append("***** 	Numero : " + this.numero + "\n");
		else
			result.append("*****	Numero : NULL\n");
		
		if (this.date != null)
			result.append("***** 	Date : " + this.date + "\n");
		else
			result.append("*****	Date : NULL\n");
		
		if (articles != null || !articles.isEmpty())
		{
			result.append("***** 	Articles : [\n");
			for(ArticleNiveauStockRecuGC article : articles)
				result.append(article.print_logger());
			result.append("*****		]\n");
		}
		else
			result.append("*****	Articles : NULL");
		
		return result.toString();
	}
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<ArticleNiveauStockRecuGC> getArticles() {
		return articles;
	}

	public void setArticles(List<ArticleNiveauStockRecuGC> articles) {
		this.articles = articles;
	}
}
