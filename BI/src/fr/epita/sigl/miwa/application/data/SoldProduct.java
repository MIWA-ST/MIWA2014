/***********************************************************************
 * Module:  SoldProduct.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class SoldProduct
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

public class SoldProduct {
	private Product product;
	private Integer quantity;
	
	public SoldProduct() {
	}
	
	public SoldProduct(Product product, Integer quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}