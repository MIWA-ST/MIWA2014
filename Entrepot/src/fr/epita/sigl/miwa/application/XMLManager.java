package fr.epita.sigl.miwa.application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import fr.epita.sigl.miwa.application.BDD.JdbcConnection;
import fr.epita.sigl.miwa.application.bo.Article;
import fr.epita.sigl.miwa.application.bo.CommandeInternet;
import fr.epita.sigl.miwa.application.bo.LivraisonFournisseur;
import fr.epita.sigl.miwa.application.bo.ReassortBO;
import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class XMLManager
{
	private static XMLManager instance = null;
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	public static XMLManager getInstance()
	{
		if (instance == null)
			instance = new XMLManager();
		
		return instance;
	}
	
	public String getCommandeInternet(Document doc) throws AsyncMessageException
	{
		CommandeInternet command = new CommandeInternet();
		
		String cmd = doc.getElementsByTagName("numero").item(0).getTextContent();
		command.setCommandNumber(cmd);
		command.setCustomerRef(doc.getElementsByTagName("refclient").item(0).getTextContent());
		command.setCustomerLastname(doc.getElementsByTagName("nom").item(0).getTextContent());
		command.setCustomerFirstname(doc.getElementsByTagName("prenom").item(0).getTextContent());
		command.setCustomerAddress(doc.getElementsByTagName("adresseClient").item(0).getTextContent());
		command.setDateBC(doc.getElementsByTagName("datebc").item(0).getTextContent());
		
		List<Article> articles = new ArrayList<Article>();
		NodeList nList = doc.getElementsByTagName("article");
		for (int temp = 0; temp < nList.getLength(); temp++) 
		{
			Article a = new Article();
			
			//Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			//Conversion en element
			Element eElement = (Element) nNode;

			String ref = eElement.getElementsByTagName("reference").item(0).getTextContent();
			String qte = eElement.getElementsByTagName("quantite").item(0).getTextContent();
			a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0).getTextContent());
			a.setReference(ref);
			a.setQuantity(qte);

			LOGGER.info("*****Commande Internet " + cmd + ", Commande article ref : " + ref + ", quantité : " + qte);
			
			articles.add(a);
		}
		command.setArticles(articles);
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		command.setDateBL(df.format(ClockClient.getClock().getHour()));
		
		//TODO sauvergarde en base
		JdbcConnection.getInstance().insertCommandeInternet(command);
		
		//Construction du xml
		String bl = "<EXPEDITIONCLIENT>"
					+ "<LIVRAISON>"
						+ "<NUMERO>" + command.getCommandNumber() + "</NUMERO>"
						+ "<DATEBC>" + command.getDateBC() + "</DATEBC>"
						+ "<DATEBL>" + command.getDateBL() + "</DATEBL>";
						
		for (Article a : articles)
			bl += "<ARTICLE>"
					+ "<REFERENCE>" + a.getReference() + "</REFERENCE>"
					+ "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
					+ "<CATEGORIE>" + a.getCategory() + "</CATEGORIE>"
				+ "</ARTICLE>";
							
		bl += "</LIVRAISON></EXPEDITIONCLIENT>";
		
		LOGGER.info("*****Commande Internet " + cmd + " : Bon de Livraison prêt à envoi");
		
		return bl;
	}
	
	public String getCommandeFournisseur(Document doc) throws AsyncMessageException
	{
		LivraisonFournisseur command = new LivraisonFournisseur();
		
		String cmd = doc.getElementsByTagName("NUMERO").item(0).getTextContent();
		command.setCommandNumber(cmd);
		command.setDateBC(doc.getElementsByTagName("DATEBC").item(0).getTextContent());
		
		List<Article> articles = new ArrayList<Article>();
		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp = 0; temp < nList.getLength(); temp++) 
		{
			Article a = new Article();
			
			//Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			//Conversion en element
			Element eElement = (Element) nNode;

			String ref = eElement.getElementsByTagName("REFERENCE").item(0).getTextContent();
			String qte = eElement.getElementsByTagName("QUANTITE").item(0).getTextContent();
			a.setReference(ref);
			a.setQuantity(qte);
			a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0).getTextContent());
			 
			LOGGER.info("*****Commande Fournisseur " + cmd + ", Approvisionnement article ref : " + ref + ", quantité : " + qte);
			
			articles.add(a);
		}
		command.setArticles(articles);
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		command.setDateBL(df.format(ClockClient.getClock().getHour()));
		
		
		LOGGER.info("*****Livraison Fournisseur reçue !");
		//TODO sauvergarde en base
		JdbcConnection.getInstance().insertLivraisonFournisseur(command);
		
		//Construction du xml
		String bl = "<LIVRAISONSCOMMANDEFOURNISSEUR>"
					+ "<LIVRAISON>"
						+ "<NUMERO>" + command.getCommandNumber() + "</NUMERO>"
						+ "<DATEBC>" + command.getDateBC() + "</DATEBC>"
						+ "<DATEBL>" + command.getDateBL() + "</DATEBL>";
		
		for (Article a : articles)
			bl += "<ARTICLE>"
					+ "<REFERENCE>" + a.getReference() + "</REFERENCE>"
					+ "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
					+ "<CATEGORIE>" + a.getCategory() + "</CATEGORIE>"
				+ "</ARTICLE>";
							
		bl+= "</LIVRAISON></LIVRAISONSCOMMANDEFOURNISSEUR>";
		
		LOGGER.info("*****Commande Fournisseur " + cmd + " : Bon de Livraison prêt à envoi");
		
		return bl;
	}
	
	public String getReassortBO(Document doc) throws AsyncMessageException
	{
		ReassortBO command = new ReassortBO();
		
		String cmd = doc.getElementsByTagName("NUMERO").item(0).getTextContent();
		command.setCommandNumber(cmd);
		command.setBackOfficeRef(doc.getElementsByTagName("REFBO").item(0).getTextContent());
		command.setBackOfficeAddress(doc.getElementsByTagName("ADRESSEBO").item(0).getTextContent()); 
		command.setBackOfficePhone(doc.getElementsByTagName("TELBO").item(0).getTextContent());
		command.setDateBC(doc.getElementsByTagName("DATEBC").item(0).getTextContent());
		
		List<Article> articles = new ArrayList<Article>();
		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp = 0; temp < nList.getLength(); temp++) 
		{			
			Article a = new Article();
			
			//Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			//Conversion en element
			Element eElement = (Element) nNode;

			String ref = eElement.getElementsByTagName("REFERENCE").item(0).getTextContent();
			String qte = eElement.getElementsByTagName("QUANTITE").item(0).getTextContent();
			
			LOGGER.info("*****Commande réassort BO " + cmd + ", sortie article ref : " + ref + ", quantité : " + qte);
			
			a.setReference(ref);
			a.setQuantity(qte);
			a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0).getTextContent());
			 
			articles.add(a);
		}
		command.setArticles(articles);
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		command.setDateBL(df.format(ClockClient.getClock().getHour()));
		
		//TODO sauvergarde en base
		JdbcConnection.getInstance().insertReassortBO(command);
		
		//Construction du xml
		String bl = "<LIVRAISONS>"
					+ "<LIVRAISON>"
						+ "<NUMERO>" + command.getCommandNumber() + "</NUMERO>"
						+ "<REFMAGASIN>"+ command.getBackOfficeRef() + "</REFMAGASIN>"
						+ "<DATEBC>" + command.getDateBC() + "</DATEBC>"
						+ "<DATEBL>" + command.getDateBL() + "</DATEBL>";
		
		for (Article a : articles)
			bl += "<ARTICLE>"
					+ "<REFERENCE>" + a.getReference() + "</REFERENCE>"
					+ "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
				+ "</ARTICLE>";
		
		bl += "</LIVRAISON></LIVRAISONS>";
		
		LOGGER.info("*****Commande Réassort BO " + cmd + " : Bon de Livraison prêt à envoi");
		
		return bl;
	}
}
