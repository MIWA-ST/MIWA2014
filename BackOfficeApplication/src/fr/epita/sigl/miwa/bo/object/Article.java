package fr.epita.sigl.miwa.bo.object;

import java.util.List;

public class Article
{
	public String name = null;
	public String reference = null;
	public String category = null;
	public Integer quantity = null;
	public Float providerPrice = null;
	public Float salesPrice = null;
	public String ean = null;
	public String description = null;
	public List<Promotion> promotions = null; 

	public void print()
	{
		System.out.println("==========ARTICLE=BEGIN==========");
		System.out.println("NAME:" + this.name);
		System.out.println("REFERENCE:" + this.reference);
		System.out.println("CATEGORY:" + this.category);
		System.out.println("QUANTITY:" + this.quantity);
		System.out.println("PROVIDER PRICE:" + this.providerPrice);
		System.out.println("SALES PRICE:" + this.salesPrice);
		System.out.println("EAN:" + this.ean);
		System.out.println("DESCRIPTION:" + this.description);
		System.out.println("==========ARTICLE=>PROMOTIONS=BEGIN==========");
		if (this.promotions != null)
		{
			for (Promotion promotion : this.promotions)
			{
				promotion.print();
			}
		}
		System.out.println("==========ARTICLE=>PROMOTIONS=END==========");
		System.out.println("==========ARTICLE=END==========");
	}
}
