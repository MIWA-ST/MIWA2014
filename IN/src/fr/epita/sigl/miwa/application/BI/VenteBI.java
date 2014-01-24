package fr.epita.sigl.miwa.application.BI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.epita.sigl.miwa.application.MiwaBDDIn;

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
	
	public void addBDD()
	{
		MiwaBDDIn bdd = MiwaBDDIn.getInstance();
		
		String art = "";
		for (ArticleVenteBI a : articles)
		{
			if (art.equals(""))
				art += a.getRef_article() + "---" + a.getQuantite();
			else
				art += "///" + a.getRef_article() + "---" + a.getQuantite();
		}
			
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(dateHeure);
		bdd.executeStatement("INSERT INTO vente VALUES('"+numero_client+"', "+montant+", '"+moyen_paiement+"', '"+date+"', '"+art+"');");
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
	
	public Boolean getArticlesBDD(String articlesBDD)
	{
		if (articlesBDD != null)
		{
			String articlesQuantite[] = articlesBDD.split("///");
			for (String s : articlesQuantite)
			{
				String art[] = s.split("---");
				if (art.length != 2)
					return false;
				articles.add(new ArticleVenteBI(art[0], Integer.parseInt(art[1])));
			}
			return true;
		}
		else
			return false;
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
	public void setDateHeure(String dateHeure) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.dateHeure = df.parse(dateHeure);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<ArticleVenteBI> getArticles() {
		return articles;
	}

	public void setArticles(List<ArticleVenteBI> articles) {
		this.articles = articles;
	}
}
