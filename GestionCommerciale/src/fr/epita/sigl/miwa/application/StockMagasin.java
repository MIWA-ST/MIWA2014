package fr.epita.sigl.miwa.application;

public class StockMagasin {
	private Articles article;
	private String quantity;
	private String max_q;
	private String idmag;
	
	public Articles getArticle() {
		return article;
	}
	public void setArticle(Articles article) {
		this.article = article;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getIdmag() {
		return idmag;
	}
	public void setIdmag(String idmag) {
		this.idmag = idmag;
	}
	public String getMax_q() {
		return max_q;
	}
	public void setMax_q(String max_q) {
		this.max_q = max_q;
	}
	
	
}
