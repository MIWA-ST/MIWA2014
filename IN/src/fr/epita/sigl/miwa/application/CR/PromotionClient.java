package fr.epita.sigl.miwa.application.CR;

import java.util.List;

public class PromotionClient {
	private Integer solde;
	private List<PromotionArticle> promotions;
	
	
	public Integer getSolde() {
		return solde;
	}
	public void setSolde(Integer solde) {
		this.solde = solde;
	}
	public List<PromotionArticle> getPromotions() {
		return promotions;
	}
	public void setPromotions(List<PromotionArticle> promotions) {
		this.promotions = promotions;
	}
}
