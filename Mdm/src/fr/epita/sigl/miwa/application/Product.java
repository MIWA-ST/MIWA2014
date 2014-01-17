package fr.epita.sigl.miwa.application;

import java.util.ArrayList;

public class Product {
	
	private String categorie;
	private ArrayList<Promotion> promoList;
	
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
	private String description;
	private String buyPrice;
	private String nbMin;
	
	public Product(String EAN, String description, String buyPrice, String nbMin, String reference, Integer origine) {
		this.EAN = EAN;
		this.description = description;
		this.buyPrice = buyPrice;
		this.nbMin = nbMin;
		this.reference = reference;
		this.providerNumber = origine;
		this.promoList = new ArrayList<Promotion>();
	}

}
