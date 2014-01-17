/***********************************************************************
 * Module:  SoldProduct.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class SoldProduct
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

public class SoldProduct {
	private String productRef;
	private Integer quantity;
	
	public SoldProduct() {
	}
	
	public SoldProduct(String productRef, Integer quantity) {
		this.productRef = productRef;
		this.quantity = quantity;
	}

	public String getProductRef() {
		return productRef;
	}

	public void setProductRef(String productRef) {
		this.productRef = productRef;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}