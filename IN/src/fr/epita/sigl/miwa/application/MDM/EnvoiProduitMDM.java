package fr.epita.sigl.miwa.application.MDM;

import java.util.Date;
import java.util.List;

public class EnvoiProduitMDM {
	private String objet;
	private String source;
	private Date date;
	private List<ArticleAVendreMDM> articles;
	
	public String getObjet() {
		return objet;
	}
	public void setObjet(String objet) {
		this.objet = objet;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<ArticleAVendreMDM> getArticles() {
		return articles;
	}
	public void setArticles(List<ArticleAVendreMDM> articles) {
		this.articles = articles;
	}
}
