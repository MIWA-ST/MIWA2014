package fr.epita.sigl.miwa.application.statistics;

public class StockStatistic {

	private String name;
	
	private String article;
	
	private boolean plein;
	
	private boolean vide;
	
	private boolean commande;

	public StockStatistic(String name, String article, boolean plein,
			boolean vide, boolean commande) {
		super();
		this.name = name;
		this.article = article;
		this.plein = plein;
		this.vide = vide;
		this.commande = commande;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
