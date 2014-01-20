package fr.epita.sigl.miwa.application.GC;

public class DemandeNiveauStockArticlesGC {
	private String reference;
	

	public DemandeNiveauStockArticlesGC(String reference)
	{
		this.reference = reference;
	}
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("<ARTICLE>");
		
		result.append("<REFERENCE>" + this.reference + "</REFERENCE>");
		
		result.append("<ARTICLE>");		
		
		return result.toString();
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
		
		return result.toString();
	}
	
	public void print()
	{
		if (this.reference != null)
			System.out.println("		REFERENCE : " + this.reference);
		else
			System.out.println("		REFERENCE : NULL");
	}
}
