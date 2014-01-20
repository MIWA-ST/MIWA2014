package fr.epita.sigl.miwa.application.CR;

import java.util.ArrayList;
import java.util.List;

import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticlesGC;

public class PromotionClientCR {
	private EnteteCRM entete;
	private List<PromotionArticleCR> promotions = new ArrayList<PromotionArticleCR>();
	private String matricule;
	
	
	public EnteteCRM getEntete() {
		return entete;
	}
	public void setEntete(EnteteCRM entete) {
		this.entete = entete;
	}
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public List<PromotionArticleCR> getPromotions() {
		return promotions;
	}
	public void setPromotions(List<PromotionArticleCR> promotions) {
		this.promotions = promotions;
	}
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		result.append(entete.print_logger());
		result.append("***** INFORMATION CLIENT [\n");
		
		if (matricule == null || matricule.equals(""))
			result.append("***** 	Matricule : NULL \n");
		else
			result.append("***** 	Matricule : " + matricule + "\n");
		
		if (promotions != null || !promotions.isEmpty())
		{
			result.append("***** 	PROMOTIONS : [\n");
			for(PromotionArticleCR promotion : promotions)
				result.append(promotion.print_logger());
			System.out.println("*****	]\n");
		}
		else
			System.out.println("*****	PROMOTIONS : NULL\n");
		
		result.append("***** ]\n");
		
		return result.toString();
	}
	
	public void print()
	{
		entete.print();
		
		if (this.matricule == null || this.matricule.equals(""))
			System.out.println("Matricule : NULL");
		else
			System.out.println("Matricule : " + this.matricule);
		
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
