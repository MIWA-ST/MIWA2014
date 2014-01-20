package fr.epita.sigl.miwa.application.MDM;

import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticlesGC;

public class ArticleAVendreMDM {
	private String reference;
	private String ean;
	private String categorie;
	private Float prix_fournisseur;
	private Float prix_vente;
	private String description;
	private List<PromotionArticleMDM> promotions = new ArrayList<PromotionArticleMDM>();
	
	public ArticleAVendreMDM()
	{
	}
	
	public ArticleAVendreMDM(String reference, String ean, String categorie,
			String prix_fournisseur, String prix_vente, String description, ArrayList<PromotionArticleMDM> promotions)
	{
		this.reference = reference;
		this.ean = ean;
		this.categorie = categorie;
		if (prix_fournisseur != null && !prix_fournisseur.equals(""))
			this.prix_fournisseur = Float.parseFloat(prix_fournisseur);
		else
			this.prix_fournisseur = 0.f;
		if (prix_vente != null && !prix_vente.equals(""))
			this.prix_vente = Float.parseFloat(prix_vente);
		else
			this.prix_vente = 0.f;
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
	public Float getPrix_fournisseur() {
		return prix_fournisseur;
	}
	public void setPrix_fournisseur(Float prix_fournisseur) {
		this.prix_fournisseur = prix_fournisseur;
	}
	public Float getPrix_vente() {
		return prix_vente;
	}
	public void setPrix_vente(Float prix_vente) {
		this.prix_vente = prix_vente;
	}
	
	public void setPrix_vente(String prix_vente) {
		if (prix_vente != null && !prix_vente.equals(""))
			this.prix_vente = Float.parseFloat(prix_vente);
		else
			this.prix_vente = 0.f;
	}
	
	public void setPrix_fournisseur(String prix_fournisseur) {
		if (prix_fournisseur != null && !prix_fournisseur.equals(""))
			this.prix_fournisseur = Float.parseFloat(prix_fournisseur);
		else
			this.prix_fournisseur = 0.f;
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
	
//	public String print_logger()
//	{
//		String result = "";
//		
//		if (this.reference != null && !this.reference.equals(""))
//			result = 
//		
//		return result;
//	}
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("***** 		reference : ");
		if (this.reference != null && !this.reference.equals(""))
			result.append(this.reference + "\n");
		else
			result.append("NULL\n");
		
		result.append("***** 		ean : ");
		if (this.ean != null && !this.ean.equals(""))
			result.append(this.ean + "\n");
		else
			result.append("NULL\n");
		
		result.append("***** 		categorie : ");
		if (this.categorie != null && !this.categorie.equals(""))
			result.append(this.categorie + "\n");
		else
			result.append("NULL\n");
		
		result.append("***** 		description : ");
		if (this.description != null && !this.description.equals(""))
			result.append(this.description + "\n");
		else
			result.append("NULL\n");
		
		result.append("***** 		prix_fournisseur : ");
		if (this.prix_fournisseur != null)
			result.append(this.prix_fournisseur + "\n");
		else
			result.append("NULL\n");
		
		result.append("***** 		prix_vente : ");
		if (this.prix_vente != null)
			result.append(this.prix_vente + "\n");
		else
			result.append("NULL\n");
		
		if (promotions != null && !promotions.isEmpty())
		{
			Integer i = 0;
			for(PromotionArticleMDM a : promotions)
			{
				result.append("***** 	PROMOTIONS : [\n");
				
				result.append(a.print_logger() + "\n");
				
				result.append("***** 	]\n");
			}
		}
		else
			result.append("***** 	PROMOTIONS : [ NULL ]\n");
		
		return result.toString();
	}
	
	public void print()
	{
		if (this.reference != null && !this.reference.equals(""))
			System.out.println("	reference : " + this.reference);
		else
			System.out.println("	reference : NULL");
		
		if (this.ean != null && !this.ean.equals(""))
			System.out.println("	ean : " + this.ean);
		else
			System.out.println("	ean : NULL");
		
		if (this.categorie != null && !this.categorie.equals(""))
			System.out.println("	categorie : " + this.categorie);
		else
			System.out.println("	categorie : NULL");
		
		if (this.description != null && !this.description.equals(""))
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
			System.out.println("			]");
		}
		else
			System.out.println("	PROMOTIONS : NULL");
	}
}
