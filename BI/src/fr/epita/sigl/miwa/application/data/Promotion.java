/***********************************************************************
 * Module:  Promotion.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class Promotion
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

import java.util.*;

public class Promotion {
	private Integer id;
	private Product product;
	private Date beginDate;
	private Date endDate;
	private Integer percentage;
	private String store;
	
	public Promotion() {
		super();
	}
	
	public Promotion(Integer id, Product product, Date beginDate, Date endDate,	Integer percentage,
			String store) {
		super();
		this.id = id;
		this.product = product;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.percentage = percentage;
		this.store = store;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

}