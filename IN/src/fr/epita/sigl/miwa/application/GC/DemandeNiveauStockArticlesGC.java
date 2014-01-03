package fr.epita.sigl.miwa.application.GC;

public class DemandeNiveauStockArticlesGC {
	private String reference;
	private Integer quantite;

	public DemandeNiveauStockArticlesGC(String reference, Integer quantite)
	{
		this.reference = reference;
		this.quantite = quantite;
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
