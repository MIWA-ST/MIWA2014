package fr.epita.sigl.miwa.application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class XMLManager
{
	private static XMLManager instance = null;
	
	public static XMLManager getInstance()
	{
		if (instance == null)
			return new XMLManager();
		
		return instance;
	}
	
	public String getCommandeInternet(String message) throws AsyncMessageException
	{
		CommandeInternet command = new CommandeInternet();
		
		//command.setCommandNumber(...);
		//command.setCustomerRef(...);
		//command.setCustomerLastname(...);
		//command.setCustomerFirstname(...);
		//command.setCustomerAddress(...);
		//command.setDateBC(...);
		
		List<Article> articles = new ArrayList<Article>();
		//TODO boucler sur les balises
		command.setArticles(articles);
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		command.setDateBL(df.format(Calendar.getInstance().getTime()));
		
		//TODO sauvergarde en base
		
		//Construction du xml
		String bl = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<EXPEDITIONCLIENT>"
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
		
		return bl;
	}
	
	public String getCommandeFournisseur(String message) throws AsyncMessageException
	{
		LivraisonFournisseur command = new LivraisonFournisseur();
		
		//command.setCommandNumber(...);
		//command.setDateBC(...);
		
		List<Article> articles = new ArrayList<Article>();
		//TODO boucler sur les balises
		command.setArticles(articles);
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		command.setDateBL(df.format(Calendar.getInstance().getTime()));
		
		//TODO sauvergarde en base
		
		//Construction du xml
		String bl = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<LIVRAISONSCOMMANDEFOURNISSEUR>"
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
		
		return bl;
	}
	
	public String getReassortBO(String message) throws AsyncMessageException
	{
		ReassortBO command = new ReassortBO();
		
		//command.setCommandNumber(...);
		//command.setBackOfficeRef(...);
		//command.setBackOfficeAddress(...);
		//command.setBackOfficePhone(...);
		//command.setDateBC(...);
		
		List<Article> articles = new ArrayList<Article>();
		//TODO boucler sur les balises
		command.setArticles(articles);
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		command.setDateBL(df.format(Calendar.getInstance().getTime()));
		
		//TODO sauvergarde en base
		
		//Construction du xml
		String bl = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<LIVRAISONS>"
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
		
		return bl;
	}
}
