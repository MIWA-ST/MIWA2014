package fr.epita.sigl.miwa.application.data;

import java.util.ArrayList;
import java.util.List;

public class MDMData {

	private List<Promotion> promotions;
	
	private List<ProductCategory> categories;

	public MDMData(List<ProductCategory> categories, List<Promotion> promotions) {
		this.promotions = promotions;
		this.categories = categories;
	}

	public List<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}

	public List<ProductCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<ProductCategory> categories) {
		this.categories = categories;
	}
	
	public List<Product> getProducts(){
		List<Product> products = new ArrayList<Product>();
		for (ProductCategory category : categories){
			products.addAll(category.getProduct());
		}
		return products;
	}
}
