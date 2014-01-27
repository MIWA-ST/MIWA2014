package fr.epita.sigl.miwa.application.GC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.w3c.dom.Document;

import fr.epita.sigl.miwa.application.ParseXML;

public class EnvoiCommandeGC {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	// numero commande
	private String numero;
	private String refclient;
	private String datebc;
	private String datebl;
	private String adresseClient;
	private String nom;
	private String prenom;

	// liste des articles commandes par le client
	private List<ArticleCommandeParClientGC> articles = new ArrayList<ArticleCommandeParClientGC>();
	
	public EnvoiCommandeGC(String numero, String refclient, String datebc, String datebl, String adresseClient, String nom, String prenom, List<ArticleCommandeParClientGC> articles)
	{
		this.numero = numero;
		this.refclient = refclient;
		this.datebc = datebc;
		this.datebl = datebl;
		this.adresseClient = adresseClient;
		this.nom = nom;
		this.prenom = prenom;
		this.articles = articles;
	}
	
	public EnvoiCommandeGC() {
		// TODO Auto-generated constructor stub
	}

	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		LOGGER.info("***** Envoi d'un message à GC : envoi des commandes Internet");
		
		result.append("<commande_internet><commande>");
		
		result.append("<numero>" + this.numero + "</numero>");
		result.append("<refclient>" + this.refclient + "</refclient>");
		result.append("<datebc>" + this.datebc + "</datebc>");
		result.append("<datebl>" + this.datebl + "</datebl>");
		result.append("<adresseClient>" + this.adresseClient + "</adresseClient>");
		result.append("<nom>" + this.nom + "</nom>");
		result.append("<prenom>" + this.prenom + "</prenom>");
		
		result.append("<articles>");
		
		for (ArticleCommandeParClientGC a : articles)
			result.append(a.sendXML());
		
		result.append("</articles>");
		
		result.append("</commande></commande_internet>");

		
		return result.toString();
	}
	
	public Document sendXMLDocument()
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		
		File file = new File("temp.xml");
		
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file));
			out.write(this.sendXML());
			out.close();
			
			org.jdom2.Document documentJdom = saxBuilder.build(file);
			DOMOutputter outputDocument = new DOMOutputter();
			
			return outputDocument.output(documentJdom);
		} catch (IOException | JDOMException e) {
			LOGGER.info("***** Erreur lors de la création du flux XML : " + e.getMessage());
		}
		return null;
	}
	
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getRefclient() {
		return refclient;
	}
	public void setRefclient(String refclient) {
		this.refclient = refclient;
	}
	public String getDatebc() {
		return datebc;
	}
	public void setDatebc(String datebc) {
		this.datebc = datebc;
	}
	public String getDatebl() {
		return datebl;
	}
	public void setDatebl(String datebl) {
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
	public List<ArticleCommandeParClientGC> getArticles() {
		return articles;
	}
	public void setArticles(List<ArticleCommandeParClientGC> articles) {
		this.articles = articles;
	}
}
