package fr.epita.sigl.miwa.application;


import java.util.List;

public class CommandeFournisseur {
private String numero_commande;
private String bon_commande;
private String bon_livraion;
private List<Articles> articles;
private List<String> quantity;
private String traitee = "false";

public String getNumero_commande() {
	return numero_commande;
}
public void setNumero_commande(String numero_commande) {
	this.numero_commande = numero_commande;
}
public String getBon_commande() {
	return bon_commande;
}
public void setBon_commande(String bon_commande) {
	this.bon_commande = bon_commande;
}
public String getBon_livraion() {
	return bon_livraion;
}
public void setBon_livraion(String bon_livraion) {
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
public String getTraitee() {
	return traitee;
}
public void setTraitee(String traitee) {
	this.traitee = traitee;
}


}
