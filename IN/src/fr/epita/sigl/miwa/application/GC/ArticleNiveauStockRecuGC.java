package fr.epita.sigl.miwa.application.GC;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.epita.sigl.miwa.application.MiwaBDDIn;

public class ArticleNiveauStockRecuGC {
	private String reference;
	private Integer quantite;
	
	public ArticleNiveauStockRecuGC()
	{
	}
	
	public ArticleNiveauStockRecuGC(String reference, String quantite)
	{
		this.reference = reference;
		this.quantite = Integer.parseInt(quantite);
	}
	
	public void MAJBDD()
	{
		MiwaBDDIn bdd = MiwaBDDIn.getInstance();
		
		ResultSet rs = bdd.executeStatement_result("SELECT * FROM article WHERE reference='" + reference + "';");
		
		try {
			if (rs.next())
			{
				bdd.executeStatement("UPDATE article SET stock=" + quantite + " WHERE reference='" + reference + "';");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public Integer getQuantite() {
		return quantite;
	}

	public void setQuantite(String quantite) {
		this.quantite = Integer.parseInt(quantite);
	}
	
	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}
}
