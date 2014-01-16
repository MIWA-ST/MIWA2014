/***********************************************************************
 * Module:  SoldProduct.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class SoldProduct
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

public class SoldProduct {
	private Product product;
	private DetailSale detailSale;
	private Integer quantity;
	
	public SoldProduct() {
		super();
	}
	
	public SoldProduct(Product product, DetailSale detailSale, Integer quantity) {
		super();
		this.product = product;
		this.detailSale = detailSale;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public DetailSale getDetailSale() {
		return detailSale;
	}

	public void setDetailSale(DetailSale detailSale) {
		this.detailSale = detailSale;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}