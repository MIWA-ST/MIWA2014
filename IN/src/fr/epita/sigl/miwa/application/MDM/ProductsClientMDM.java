package fr.epita.sigl.miwa.application.MDM;

import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.application.CR.PromotionArticleCR;

public class ProductsClientMDM {
	private List<ArticleAVendreMDM> products = new ArrayList<ArticleAVendreMDM>();

	public ProductsClientMDM()
	{
	}
	
	public List<ArticleAVendreMDM> getProducts() {
		return products;
	}

	public void setProducts(List<ArticleAVendreMDM> products) {
		this.products = products;
	}
	
	public void print()
	{
		for(ArticleAVendreMDM a : products)
			a.print();
	}
}
