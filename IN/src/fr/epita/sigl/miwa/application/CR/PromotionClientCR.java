package fr.epita.sigl.miwa.application.CR;

import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticlesGC;

public class PromotionClientCR {
	private Integer solde;
	private List<PromotionArticleCR> promotions = new ArrayList<PromotionArticleCR>();
	
	
	public Integer getSolde() {
		return solde;
	}
	public void setSolde(Integer solde) {
		this.solde = solde;
	}
	public List<PromotionArticleCR> getPromotions() {
		return promotions;
	}
	public void setPromotions(List<PromotionArticleCR> promotions) {
		this.promotions = promotions;
	}
	
	public void print()
	{
		System.out.println("Promotion Client : INFORMATIONS : [");
		if (this.solde != null)
			System.out.println("	Solde : " + this.solde);
		else
			System.out.println("	Solde : NULL");
		
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
