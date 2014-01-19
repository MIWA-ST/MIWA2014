package fr.epita.sigl.miwa.application.statistics;

import java.util.Date;

public class StockStatistic {
	
	private Date dateTime;

	private String store;
	
	private String article;
	
	private boolean plein;
	
	private boolean vide;
	
	private boolean commande;
	

	public StockStatistic(Date dateTime, String store, String article,
			boolean plein, boolean vide, boolean commande) {
		super();
		this.dateTime = dateTime;
		this.store = store;
		this.article = article;
		this.plein = plein;
		this.vide = vide;
		this.commande = commande;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public boolean isPlein() {
		return plein;
	}

	public void setPlein(boolean plein) {
		this.plein = plein;
	}

	public boolean isVide() {
		return vide;
	}

	public void setVide(boolean vide) {
		this.vide = vide;
	}

	public boolean isCommande() {
		return commande;
	}

	public void setCommande(boolean commande) {
		this.commande = commande;
	}

}
