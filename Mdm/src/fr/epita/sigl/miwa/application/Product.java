package fr.epita.sigl.miwa.application;

public class Product {
	private String reference;
	
	private String EAN;
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
	
	public Product(String EAN, String description, String buyPrice, String nbMin, String reference) {
		this.EAN = EAN;
		this.description = description;
		this.buyPrice = buyPrice;
		this.nbMin = nbMin;
		this.reference = reference;
	}

}
