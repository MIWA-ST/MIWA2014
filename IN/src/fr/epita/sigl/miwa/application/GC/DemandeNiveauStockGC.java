package fr.epita.sigl.miwa.application.GC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.w3c.dom.Document;

import fr.epita.sigl.miwa.application.ParseXML;

public class DemandeNiveauStockGC {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	// numero de la demande
	private String numero;
	private String date;
	private List<DemandeNiveauStockArticlesGC> articles = new ArrayList<DemandeNiveauStockArticlesGC>();
	
	public DemandeNiveauStockGC()
	{
		
	}
	
	public DemandeNiveauStockGC(String numero, String date, List<DemandeNiveauStockArticlesGC> articles)
	{
		this.numero = numero;
		this.date = date;
		this.articles = articles;
	}
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		LOGGER.info("***** Envoi d'un message à GC : demande niveaux de stock Internet");
		
		result.append("<DEMANDENIVEAUDESTOCKINTERNET>");
		
		result.append("<numero>" + this.numero + "</numero>");
		result.append("<date>" + this.date + "</date>");
		result.append("<ARTICLES>");
		
		for (DemandeNiveauStockArticlesGC a : articles)
			result.append(a.sendXML());
		
		result.append("</ARTICLES>");

		result.append("</DEMANDENIVEAUDESTOCKINTERNET>");
		
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public List<DemandeNiveauStockArticlesGC> getArticles() {
		return articles;
	}
	public void setArticles(List<DemandeNiveauStockArticlesGC> articles) {
		this.articles = articles;
	}
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("***** DEMANDENIVEAUDESTOCKINTERNET : [\n");
		if (this.numero != null)
			result.append("***** 	Numero : " + this.numero + "\n");
		else
			result.append("*****	Numero : NULL\n");
		
		if (this.date != null)
			result.append("***** 	Date : " + this.date + "\n");
		else
			result.append("*****	Date : NULL\n");
		
		if (articles != null || !articles.isEmpty())
		{
			result.append("***** 	Articles : [\n");
			for(DemandeNiveauStockArticlesGC article : articles)
				result.append(article.print_logger());
			result.append("*****		]\n");
		}
		else
			System.out.println("	Articles : NULL");
		
		return result.toString();
	}
	
	public void print()
	{
		System.out.println("DEMANDENIVEAUDESTOCKINTERNET : [");
		if (this.numero != null)
			System.out.println("	Numero : " + this.numero);
		else
			System.out.println("	Numero : NULL");
		
		if (this.numero != null)
			System.out.println("	Date : " + this.date);
		else
			System.out.println("	Date : NULL");
		
		if (articles != null || !articles.isEmpty())
		{
			System.out.println("	Articles : [");
			for(DemandeNiveauStockArticlesGC article : articles)
				article.print();
			System.out.println("	]");
		}
		else
			System.out.println("	Articles : NULL");
		System.out.println("]");
	}
}
