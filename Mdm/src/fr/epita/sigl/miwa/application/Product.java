package fr.epita.sigl.miwa.application;

import java.util.ArrayList;

public class Product {
	
	private ArrayList<Promotion> promotionList;
	private ArrayList<PromotionForGC> promotionGCList; 
	
	private String categorie;
	private ArrayList<Promotion> promoList;
	private String description;
	//Description longue
	private String long_desc;
	//Prix TTC
	private String buyPrice;
	private String nbMin;
	private String name;
	//Ajout, suppression, maj
	private String modification;
	
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	private String reference;
	
	public ArrayList<Promotion> getPromoList() {
		return promoList;
	}
	public void setPromoList(ArrayList<Promotion> promoList) {
		this.promoList = promoList;
	}
	private String sellPrice;
	
	public String getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}
	// Numéro du fournisseur (1 ou 2)
	private Integer providerNumber;
	
	private String EAN;
	public Integer getProviderNumber() {
		return providerNumber;
	}
	public void setProviderNumber(Integer origine) {
		this.providerNumber = origine;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getEAN() {
		return EAN;
	}
	public void setEAN(String eAN) {
		EAN = eAN;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}
	public String getNbMin() {
		return nbMin;
	}
	public void setNbMin(String nbMin) {
		this.nbMin = nbMin;
	}
	
	public Product(String EAN, String description, String buyPrice, String nbMin, String reference, Integer origine) {
		this.EAN = EAN;
		this.description = description;
		this.buyPrice = buyPrice;
		this.nbMin = nbMin;
		this.reference = reference;
		this.providerNumber = origine;
		promotionList = new ArrayList<Promotion>();
		promotionGCList = new ArrayList<PromotionForGC>();
		this.categorie = "Nourriture";
	}
	
	public Product(String ref) {
		this.reference = ref;
	}
	
	public ArrayList<PromotionForGC> getPromotionGCList() {
		return promotionGCList;
	}
	public void setPromotionGCList(ArrayList<PromotionForGC> promotionGCList) {
		this.promotionGCList = promotionGCList;
	}
	public ArrayList<Promotion> getPromotionList() {
		return promotionList;
	}
	public void setPromotionList(ArrayList<Promotion> promotionList) {
		this.promotionList = promotionList;
	}
}
