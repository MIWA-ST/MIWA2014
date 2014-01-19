/***********************************************************************
 * Module:  BIController.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class DetailSale
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

import java.util.*;

import fr.epita.sigl.miwa.application.enums.EPaiementType;

public class DetailSale {
	private Integer id;
	private EPaiementType paymentMean;
	private Date date;
	private Integer total;
	private String store;
	private Integer clientNb;
	private List<SoldProduct> productList;
	
	public DetailSale(){
		
	}
	public DetailSale(Integer id, EPaiementType paymentMean, Date date,
			Integer total, String store, Integer clientNb,
			List<SoldProduct> productList) {
		this.id = id;
		this.paymentMean = paymentMean;
		this.date = date;
		this.total = total;
		this.store = store;
		this.clientNb = clientNb;
		this.productList = productList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public EPaiementType getPaymentMean() {
		return paymentMean;
	}
	public void setPaymentMean(EPaiementType paymentMean) {
		this.paymentMean = paymentMean;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public Integer getClientNb() {
		return clientNb;
	}
	public void setClientNb(Integer clientNb) {
		this.clientNb = clientNb;
	}
	public List<SoldProduct> getProductList() {
		return productList;
	}
	public void setProductList(List<SoldProduct> productList) {
		this.productList = productList;
	}
}