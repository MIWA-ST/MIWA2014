/***********************************************************************
 * Module:  ProductCategory.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class ProductCategory
 ***********************************************************************/
package fr.epita.sigl.miwa.application.data;

import java.util.*;

public class ProductCategory {
	private String name;
   
	public List<Product> product;

	public ProductCategory() {
	}

	public ProductCategory(String name, List<Product> product) {
		this.name = name;
		this.product = product;
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