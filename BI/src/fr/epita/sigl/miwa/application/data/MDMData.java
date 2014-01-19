package fr.epita.sigl.miwa.application.data;

import java.util.List;

public class MDMData {

	private List<Promotion> promotions;
	
	private List<Product> products;

	public MDMData() {
	}
	
	public MDMData(List<Product> products, List<Promotion> promotions) {
		this.promotions = promotions;
		this.products = products;
	}

	public List<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
