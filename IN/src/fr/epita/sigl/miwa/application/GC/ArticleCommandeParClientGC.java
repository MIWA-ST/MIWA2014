package fr.epita.sigl.miwa.application.GC;

public class ArticleCommandeParClientGC {
	private String categorie;
	private String reference;
	private Integer quantite;
	
	public ArticleCommandeParClientGC(String categorie, String reference, String quantite)
	{
		this.categorie = categorie;
		this.reference = reference;
		if (quantite != null && !quantite.equals(""))
			this.quantite = Integer.parseInt(quantite);
		else
			this.quantite = Integer.parseInt(quantite);
	}
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("<article>");
		
		result.append("<CATEGORIE>" + this.categorie + "</CATEGORIE>");
		result.append("<reference>" + this.reference + "</reference>");
		result.append("<quantite>" + this.quantite + "</quantite>");
		
		result.append("</article>");
		
		return result.toString();
	}
	
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public Integer getQuantite() {
		return quantite;
	}
	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}
}
