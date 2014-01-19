/***********************************************************************
 * Module:  Promotion.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class Promotion
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

import java.util.*;

public class Promotion {
	private Integer id;
	private String productReference;
	private Date beginDate;
	private Date endDate;
	private Integer percentage;
	private String store;
	
	public Promotion() {
	}

	public Promotion(Integer id, String productReference, Date beginDate,
			Date endDate, Integer percentage, String store) {
		this.id = id;
		this.productReference = productReference;
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

	public String getProductReference() {
		return productReference;
	}

	public void setProductReference(String productReference) {
		this.productReference = productReference;
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