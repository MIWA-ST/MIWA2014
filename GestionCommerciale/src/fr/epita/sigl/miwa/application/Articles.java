package fr.epita.sigl.miwa.application;

public class Articles {
private String ref_article;
private String nom;
private String prix_fournisseur;
private String prix_vente;
private String stock_max_entre;
private String stock_max_mag;
private String category;
public String getRef_article() {
	return ref_article;
}
public void setRef_article(String ref_article) {
	this.ref_article = ref_article;
}
public String getNom() {
	return nom;
}
public void setNom(String nom) {
	this.nom = nom;
}
public String getPrix_fournisseur() {
	return prix_fournisseur;
}
public void setPrix_fournisseur(String prix_fournisseur) {
	this.prix_fournisseur = prix_fournisseur;
}
public String getPrix_vente() {
	return prix_vente;
}
public void setPrix_vente(String prix_vente) {
	this.prix_vente = prix_vente;
}
public String getStock_max_entre() {
	return stock_max_entre;
}
public void setStock_max_entre(String stock_max_entre) {
	this.stock_max_entre = stock_max_entre;
}
public String getStock_max_mag() {
	return stock_max_mag;
}
public void setStock_max_mag(String stock_max_mag) {
	this.stock_max_mag = stock_max_mag;
}
public String getCategory() {
	return category;
}
public void setCategory(String category) {
	this.category = category;
}


}
