package fr.epita.sigl.miwa.application.data;

import java.util.ArrayList;
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
	
	public List<ProductCategory> getCategories() {
		ArrayList<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		
		for (Product product : this.products) {
			// Est-ce que la catégorie du produit existe déjà dans la liste en construction ?
			Boolean categoryExists = false;
			
			for (ProductCategory pCat : productCategoryList) {	
				// Si la catégorie existe déjà
				if (pCat.getName().equalsIgnoreCase(product.getCategoryName())) {
					pCat.getProduct().add(product);
					categoryExists = true;
					break;
				}
			}
			
			// Si la catégorie n'existe pas
			if (!categoryExists) {
				ProductCategory productCategory = new ProductCategory();
				
				productCategory.setName(product.getCategoryName());
				productCategory.setProduct(new ArrayList<Product>());
				productCategory.getProduct().add(product);
				
				productCategoryList.add(productCategory);
			}
		}
		
		return productCategoryList;
	}
}
