/***********************************************************************
 * Module:  Sale.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class Sale
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

import java.util.*;

public class Sale {
   private Date dateTime;
   private String store;
   private Integer soldQty;
   private String productCategory;
   private Integer supplierTotal;
   private Integer salesTotal;
   
	public Sale() {
	}
	
	public Sale(Date dateTime, String store, Integer soldQty, String productCategory,
			Integer supplierTotal, Integer salesTotal) {
		this.dateTime = dateTime;
		this.store = store;
		this.soldQty = soldQty;
		this.productCategory = productCategory;
		this.supplierTotal = supplierTotal;
		this.salesTotal = salesTotal;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Integer getSoldQty() {
		return soldQty;
	}

	public void setSoldQty(Integer soldQty) {
		this.soldQty = soldQty;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Integer getSupplierTotal() {
		return supplierTotal;
	}

	public void setSupplierTotal(Integer supplierTotal) {
		this.supplierTotal = supplierTotal;
	}

	public Integer getSalesTotal() {
		return salesTotal;
	}

	public void setSalesTotal(Integer salesTotal) {
		this.salesTotal = salesTotal;
	}  

}