package fr.epita.sigl.miwa.application;

import java.util.ArrayList;
import java.util.List;

public class Product {
	
	public String getLong_desc() {
		return long_desc;
	}
	public void setLong_desc(String long_desc) {
		this.long_desc = long_desc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModification() {
		return modification;
	}
	public void setModification(String modification) {
		this.modification = modification;
	}
	private ArrayList<Promotion> promotionList;
	private ArrayList<PromotionForGC> promotionGCList; 
	
	private String categorie;
	private ArrayList<Promotion> promoList;
	private String description;
	//Description longue
	private String long_desc;
	//Prix TTC
	private float buyPrice;
	private int nbMin;
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
	private float sellPrice;
	
	public float getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(float sellPrice) {
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
	public float getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(float buyPrice) {
		this.buyPrice = buyPrice;
	}
	public int getNbMin() {
		return nbMin;
	}
	public void setNbMin(int nbMin) {
		this.nbMin = nbMin;
	}
	
	public Product(String EAN, String description, float buyPrice, int nbMin, String reference, Integer origine) {
		this.EAN = EAN;
		this.description = description;
		this.buyPrice = buyPrice;
		this.nbMin = nbMin;
		this.reference = reference;
		this.providerNumber = origine;
		promotionList = new ArrayList<Promotion>();
		promotionGCList = new ArrayList<PromotionForGC>();
		List<String> categorieList = new ArrayList<String>();
		categorieList.add("Nourriture");
		categorieList.add("Meuble");
		categorieList.add("Electromenager");
		categorieList.add("Loisirs");
		
		this.categorie = categorieList.get((int) (Math.random() * 3));
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
