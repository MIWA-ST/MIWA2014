/***********************************************************************
 * Module:  Product.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class Product
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

public class Product {
	private Integer id;
	private Integer reference;
	private Integer buyingPrice;
	private Integer sellingPrice;
   	private Integer margin;

   	public Product() {
   	}

	public Product(Integer id, Integer reference, Integer buyingPrice, Integer sellingPrice,
			Integer margin) {
		this.id = id;
		this.reference = reference;
		this.buyingPrice = buyingPrice;
		this.sellingPrice = sellingPrice;
		this.margin = margin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReference() {
		return reference;
	}

	public void setReference(Integer reference) {
		this.reference = reference;
	}

	public Integer getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(Integer buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public Integer getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Integer sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Integer getMargin() {
		return margin;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}   
}