package fr.epita.sigl.miwa.application.MDM;

import java.util.List;

public class ArticleAVendre {
	private String reference;
	private String ean;
	private String categorie;
	private Integer prix_fournisseur;
	private Integer prix_vente;
	private String description;
	private List<PromotionArticle> promotions;
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public Integer getPrix_fournisseur() {
		return prix_fournisseur;
	}
	public void setPrix_fournisseur(Integer prix_fournisseur) {
		this.prix_fournisseur = prix_fournisseur;
	}
	public Integer getPrix_vente() {
		return prix_vente;
	}
	public void setPrix_vente(Integer prix_vente) {
		this.prix_vente = prix_vente;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<PromotionArticle> getPromotions() {
		return promotions;
	}
	public void setPromotions(List<PromotionArticle> promotions) {
		this.promotions = promotions;
	}
}
