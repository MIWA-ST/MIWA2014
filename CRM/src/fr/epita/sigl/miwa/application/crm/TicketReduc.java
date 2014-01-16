package fr.epita.sigl.miwa.application.crm;

import java.util.List;

public class TicketReduc
{
	private String client;
	private int montantTotal;
	private String date;
	private List<ArticleReduc> articles;
	
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public int getMontantTotal() {
		return montantTotal;
	}
	public void setMontantTotal(int montantTotal) {
		this.montantTotal = montantTotal;
	}
	public String getDate() {
		return date;
	}
	public void setDateBC(String date) {
		this.date = date;
	}
	public List<ArticleReduc> getArticles() {
		return articles;
	}
	public void setArticles(List<ArticleReduc> articles) {
		this.articles = articles;
	}
}
