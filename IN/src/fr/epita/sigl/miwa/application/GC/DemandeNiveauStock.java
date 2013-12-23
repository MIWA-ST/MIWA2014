package fr.epita.sigl.miwa.application.GC;

import java.util.Date;
import java.util.List;

public class DemandeNiveauStock {
	// numero de la demande
	private Integer numero;
	private Date date;
	private List<DemandeNiveauStockArticles> articles;
	
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<DemandeNiveauStockArticles> getArticles() {
		return articles;
	}
	public void setArticles(List<DemandeNiveauStockArticles> articles) {
		this.articles = articles;
	}
}
