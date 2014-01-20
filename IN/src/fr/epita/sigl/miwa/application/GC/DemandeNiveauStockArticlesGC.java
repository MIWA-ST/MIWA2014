package fr.epita.sigl.miwa.application.GC;

public class DemandeNiveauStockArticlesGC {
	private String reference;
	private Integer quantite;

	public DemandeNiveauStockArticlesGC(String reference, String quantite)
	{
		this.reference = reference;
		if (quantite != null && !quantite.equals(""))
			this.quantite = Integer.parseInt(quantite);
		else
			this.quantite = 0;
	}
	
	public Integer getQuantite() {
		return quantite;
	}

	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		if (this.reference != null)
			result.append("***** 		REFERENCE : " + this.reference + "\n");
		else
			result.append("*****		REFERENCE : NULL\n");
		
		if (this.quantite != null)
			result.append("***** 		QUANTITE : " + this.quantite + "\n");
		else
			result.append("*****		QUANTITE : NULL\n");
		
		return result.toString();
	}
	
	public void print()
	{
		if (this.reference != null)
			System.out.println("		REFERENCE : " + this.reference);
		else
			System.out.println("		REFERENCE : NULL");
		
		if (this.quantite != null)
			System.out.println("		QUANTITE : " + this.quantite);
		else
			System.out.println("		QUANTITE : NULL");
	}
}
