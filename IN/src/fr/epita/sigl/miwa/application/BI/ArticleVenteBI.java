package fr.epita.sigl.miwa.application.BI;

public class ArticleVenteBI {
	private String categorie;
	private String ref_article;
	private Integer quantite;
	
	public ArticleVenteBI()
	{
		
	}

	public ArticleVenteBI(String ref_article, Integer quantite, String categorie)
	{
		this.ref_article = ref_article;
		this.quantite = quantite;
		this.categorie = categorie;
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

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
}
