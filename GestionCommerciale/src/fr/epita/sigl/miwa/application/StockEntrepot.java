package fr.epita.sigl.miwa.application;

import java.util.List;

public class StockEntrepot {
private Articles article;
private String ref_article;
private String quantity;
private String max_q;
private String identrepot;


public String getRef_article() {
	return ref_article;
}
public void setRef_article(String ref_article) {
	this.ref_article = ref_article;
}
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

public boolean stock_suffisant(Articles art, String quant_dem) {
	int demande = 0;
	int stock = 0;
	
	JdbcConnection.getInstance().getConnection();
	List<StockEntrepot> lis = JdbcConnection.getInstance().envoi_all_stock();
	JdbcConnection.getInstance().closeConnection();

	int index = 0;
	while (lis.get(index).getRef_article() != this.getRef_article()) {
		index++;
	}
	
	this.quantity = lis.get(index).getQuantity();
	
	stock = Integer.parseInt(this.quantity);
	demande = Integer.parseInt(quant_dem);
	
	if (stock >= demande)
		return true;
	else
		return false;
}

public void ajout_stock (Articles art, String ajout) {
	JdbcConnection.getInstance().getConnection();
	int n_qt = Integer.parseInt(quantity) + Integer.parseInt(ajout);
	String nouvelle_qt = String.valueOf(n_qt);
	quantity = nouvelle_qt;
	JdbcConnection.getInstance().modif_stock(art, nouvelle_qt);;
	JdbcConnection.getInstance().closeConnection();
}

public void retrait_stock (Articles art, String retrait) {
	JdbcConnection.getInstance().getConnection();
	int n_qt = Integer.parseInt(quantity) + Integer.parseInt(retrait);
	String nouvelle_qt = String.valueOf(n_qt);
	quantity = nouvelle_qt;
	JdbcConnection.getInstance().modif_stock(art, nouvelle_qt);;
	JdbcConnection.getInstance().closeConnection();	
}
}
