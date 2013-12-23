package fr.epita.sigl.miwa.application.GC;

import java.util.Date;
import java.util.List;

public class EnvoiCommande {
	// numero commande
	private Integer numero;
	private String refclient;
	private Date datebc;
	private Date datebl;
	private String adresseClient;
	private String nom;
	private String prenom;
	// liste des articles commandes par le client
	private List<ArticleCommandeParClient> articles;
	
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getRefclient() {
		return refclient;
	}
	public void setRefclient(String refclient) {
		this.refclient = refclient;
	}
	public Date getDatebc() {
		return datebc;
	}
	public void setDatebc(Date datebc) {
		this.datebc = datebc;
	}
	public Date getDatebl() {
		return datebl;
	}
	public void setDatebl(Date datebl) {
		this.datebl = datebl;
	}
	public String getAdresseClient() {
		return adresseClient;
	}
	public void setAdresseClient(String adresseClient) {
		this.adresseClient = adresseClient;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public List<ArticleCommandeParClient> getArticles() {
		return articles;
	}
	public void setArticles(List<ArticleCommandeParClient> articles) {
		this.articles = articles;
	}
}
