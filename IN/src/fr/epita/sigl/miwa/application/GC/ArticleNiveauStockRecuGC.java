package fr.epita.sigl.miwa.application.GC;

public class ArticleNiveauStockRecuGC {
	private String reference;
	private String quantite;
	
	public ArticleNiveauStockRecuGC()
	{
	}
	
	public ArticleNiveauStockRecuGC(String reference, String quantite)
	{
		this.reference = reference;
		this.quantite = quantite;
	}
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		if (reference != null && !reference.equals(""))
			result.append("***** 	Reference : " + reference + "\n");
		else
			result.append("***** 	Reference : NULL \n");
		
		if (quantite != null && !quantite.equals(""))
			result.append("***** 	Quantite : " + quantite + "\n");
		else
			result.append("***** 	Quantite : NULL \n\n");
		
		
		return result.toString();
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getQuantite() {
		return quantite;
	}

	public void setQuantite(String quantite) {
		this.quantite = quantite;
	}
}
