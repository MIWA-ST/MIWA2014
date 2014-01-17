/***********************************************************************
 * Module:  ProductCategory.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class ProductCategory
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

import java.util.*;

public class ProductCategory {
	private Integer id;
	private String name;
   
	public List<Product> product;

	public ProductCategory() {
	}

	public ProductCategory(Integer id, String name, List<Product> product) {
		this.id = id;
		this.name = name;
		this.product = product;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}
}