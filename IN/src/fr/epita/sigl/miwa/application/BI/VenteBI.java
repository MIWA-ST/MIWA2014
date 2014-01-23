package fr.epita.sigl.miwa.application.BI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VenteBI {
	private String numero_client;
	private Integer montant;
	private String moyen_paiement;
	private Date dateHeure;
	private List<ArticleVenteBI> articles = new ArrayList<ArticleVenteBI>();
	
	public VenteBI()
	{
		
	}
	
	public VenteBI(String numero_client, Integer montant, String moyen_paiement, Date dateHeure, List<ArticleVenteBI> articles)
	{
		this.numero_client = numero_client;
		this.montant = montant;
		this.moyen_paiement = moyen_paiement;
		this.dateHeure = dateHeure;
		this.articles = articles;
	}
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		result.append("<VENTE numero_client=\"" + numero_client + "\" montant=\"" + montant + "\" moyen_paiement=\"" + moyen_paiement + 
		"\" dateHeure=\"" + df.format(dateHeure) + "\">");

		result.append("<ARTICLES>");
		for (ArticleVenteBI a : articles)
			result.append(a.sendXML());
		result.append("</ARTICLES>");
		
		result.append("</VENTE>");
		
		return result.toString();
	}
	
	public String getNumero_client() {
		return numero_client;
	}
	public void setNumero_client(String numero_client) {
		this.numero_client = numero_client;
	}
	public Integer getMontant() {
		return montant;
	}
	public void setMontant(Integer montant) {
		this.montant = montant;
	}
	public String getMoyen_paiement() {
		return moyen_paiement;
	}
	public void setMoyen_paiement(String moyen_paiement) {
		this.moyen_paiement = moyen_paiement;
	}
	public Date getDateHeure() {
		return dateHeure;
	}
	public void setDateHeure(Date dateHeure) {
		this.dateHeure = dateHeure;
	}

	public List<ArticleVenteBI> getArticles() {
		return articles;
	}

	public void setArticles(List<ArticleVenteBI> articles) {
		this.articles = articles;
	}
}
