package fr.epita.sigl.miwa.application.BI;

public class ArticleVenteBI {
	private String ref_article;
	private Integer quantite;
	
	public ArticleVenteBI()
	{
		
	}

	public ArticleVenteBI(String ref_article, Integer quantite)
	{
		this.ref_article = ref_article;
		this.quantite = quantite;
	}
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		
		result.append("<ARTICLE ref-article=\"" + ref_article + "\" quantite=\"" + quantite + "\" />");
		
		return result.toString();
	}
	
	public String getRef_article() {
		return ref_article;
	}

	public void setRef_article(String ref_article) {
		this.ref_article = ref_article;
	}

	public Integer getQuantite() {
		return quantite;
	}

	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}
}
