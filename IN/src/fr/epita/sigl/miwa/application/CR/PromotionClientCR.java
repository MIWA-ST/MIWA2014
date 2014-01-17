package fr.epita.sigl.miwa.application.CR;

import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticlesGC;

public class PromotionClientCR {
	private List<PromotionArticleCR> promotions = new ArrayList<PromotionArticleCR>();
	
	public List<PromotionArticleCR> getPromotions() {
		return promotions;
	}
	public void setPromotions(List<PromotionArticleCR> promotions) {
		this.promotions = promotions;
	}
	
	public void print()
	{
		System.out.println("Promotion Client : INFORMATIONS : [");
		
		if (promotions != null || !promotions.isEmpty())
		{
			System.out.println("	PROMOTIONS : [");
			for(PromotionArticleCR promotion : promotions)
				promotion.print();
			System.out.println("	]");
		}
		else
			System.out.println("	PROMOTIONS : NULL");
		System.out.println("]");
	}
}
