/***********************************************************************
 * Module:  Product.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class Product
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

public class Product {
	private String reference;
	private Float buyingPrice;
	private Float sellingPrice;
   	private Float margin;
   	private String categoryName;

   	public Product() {
   	}

	public Product(String reference, Float buyingPrice, Float sellingPrice,
			Float margin, String categoryName) {
		this.reference = reference;
		this.buyingPrice = buyingPrice;
		this.sellingPrice = sellingPrice;
		this.margin = margin;
		this.categoryName = categoryName;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Float getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(Float buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public Float getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Float sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Float getMargin() {
		return margin;
	}

	public void setMargin(Float margin) {
		this.margin = margin;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}   
}