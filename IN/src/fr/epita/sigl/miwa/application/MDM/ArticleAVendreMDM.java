package fr.epita.sigl.miwa.application.MDM;

import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticlesGC;

public class ArticleAVendreMDM {
	private String reference;
	private String ean;
	private String categorie;
	private Integer prix_fournisseur;
	private Integer prix_vente;
	private String description;
	private List<PromotionArticleMDM> promotions = new ArrayList<PromotionArticleMDM>();
	
	public ArticleAVendreMDM()
	{
	}
	
	public ArticleAVendreMDM(String reference, String ean, String categorie,
			Integer prix_fournisseur, Integer prix_vente, String description, ArrayList<PromotionArticleMDM> promotions)
	{
		this.reference = reference;
		this.ean = ean;
		this.categorie = categorie;
		this.prix_fournisseur = prix_fournisseur;
		this.prix_vente = prix_vente;
		this.description = description;
		this.promotions = promotions;
	}
	
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
	public List<PromotionArticleMDM> getPromotions() {
		return promotions;
	}
	public void setPromotions(List<PromotionArticleMDM> promotions) {
		this.promotions = promotions;
	}
	
	public void print()
	{
		System.out.println("Article A Vendre - PRODUCTS : [");
		if (this.reference != null)
			System.out.println("	reference : " + this.reference);
		else
			System.out.println("	reference : NULL");
		
		if (this.ean != null)
			System.out.println("	ean : " + this.ean);
		else
			System.out.println("	ean : NULL");
		
		if (this.categorie != null)
			System.out.println("	categorie : " + this.categorie);
		else
			System.out.println("	categorie : NULL");
		
		if (this.description != null)
			System.out.println("	description : " + this.description);
		else
			System.out.println("	description : NULL");
		
		if (this.prix_fournisseur != null)
			System.out.println("	prix_fournisseur : " + this.prix_fournisseur);
		else
			System.out.println("	prix_fournisseur : NULL");
		
		if (this.prix_vente != null)
			System.out.println("	prix_vente : " + this.prix_vente);
		else
			System.out.println("	prix_vente : NULL");
		
		if (promotions != null || !promotions.isEmpty())
		{
			System.out.println("	PROMOTIONS : [");
			for(PromotionArticleMDM promotion : promotions)
				promotion.print();
			System.out.println("	]");
		}
		else
			System.out.println("	PROMOTIONS : NULL");
		System.out.println("]");
	}
}
