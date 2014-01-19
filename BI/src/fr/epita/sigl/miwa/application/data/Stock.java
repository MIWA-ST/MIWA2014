/***********************************************************************
 * Module:  Stock.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class Stock
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

public class Stock {
	private Integer id;
	private String productRef;
	private Boolean ordered;
	private Integer stockQty;
	private Integer maxQty;
	private String store;
	
	public Stock() {
	}

	public Stock(Integer id, String productRef, Boolean ordered,
			Integer stockQty, Integer maxQty, String store) {
		super();
		this.id = id;
		this.productRef = productRef;
		this.ordered = ordered;
		this.stockQty = stockQty;
		this.maxQty = maxQty;
		this.store = store;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductRef() {
		return productRef;
	}

	public void setProductRef(String productRef) {
		this.productRef = productRef;
	}

	public Boolean getOrdered() {
		return ordered;
	}

	public void setOrdered(Boolean ordered) {
		this.ordered = ordered;
	}

	public Integer getStockQty() {
		return stockQty;
	}

	public void setStockQty(Integer stockQty) {
		this.stockQty = stockQty;
	}

	public Integer getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}
	
}