package fr.epita.sigl.miwa.application;

public class StockEntrepot {
private Articles article;
private String quantity;
private String max_q;
private String identrepot;

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
public String getIdentrepot() {
	return identrepot;
}
public void setIdentrepot(String identrepot) {
	this.identrepot = identrepot;
}
public String getMax_q() {
	return max_q;
}
public void setMax_q(String max_q) {
	this.max_q = max_q;
}



}
