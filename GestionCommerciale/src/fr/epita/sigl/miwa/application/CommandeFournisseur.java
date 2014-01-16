package fr.epita.sigl.miwa.application;

import java.util.Date;
import java.util.List;

public class CommandeFournisseur {
private String numero_commande;
private Date bon_commande;
private Date bon_livraion;
private List<Articles> articles;
private List<String> quantity;

public String getNumero_commande() {
	return numero_commande;
}
public void setNumero_commande(String numero_commande) {
	this.numero_commande = numero_commande;
}
public Date getBon_commande() {
	return bon_commande;
}
public void setBon_commande(Date bon_commande) {
	this.bon_commande = bon_commande;
}
public Date getBon_livraion() {
	return bon_livraion;
}
public void setBon_livraion(Date bon_livraion) {
	this.bon_livraion = bon_livraion;
}
public List<Articles> getArticles() {
	return articles;
}
public void setArticles(List<Articles> articles) {
	this.articles = articles;
}
public List<String> getquantity() {
	return quantity;
}
public void setquantity(List<String> quantity) {
	this.quantity = quantity;
}


}
