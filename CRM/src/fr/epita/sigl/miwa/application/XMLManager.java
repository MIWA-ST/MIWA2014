package fr.epita.sigl.miwa.application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.epita.sigl.miwa.application.BDD.JdbcConnection;
import fr.epita.sigl.miwa.application.crm.TicketReduc;
import fr.epita.sigl.miwa.application.crm.LivraisonFournisseur;
import fr.epita.sigl.miwa.application.crm.ReassortBO;
import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.object.Client;
import fr.epita.sigl.miwa.application.object.Critere;
import fr.epita.sigl.miwa.application.object.Segmentation;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class XMLManager
{
	private static XMLManager instance = null;
	
	public static XMLManager getInstance()
	{
		if (instance == null)
			instance = new XMLManager();
		
		return instance;
	}
	
	public String getSegmentationClient(String message, Document doc) throws AsyncMessageException
	{
		Segmentation segmentation = new Segmentation();
		
		/*segmentation.setCommandNumber(doc.getElementsByTagName("numero").item(0).getTextContent());
		segmentation.setCustomerRef(doc.getElementsByTagName("refclient").item(0).getTextContent());
		segmentation.setCustomerLastname(doc.getElementsByTagName("nom").item(0).getTextContent());
		segmentation.setCustomerFirstname(doc.getElementsByTagName("prenom").item(0).getTextContent());
		segmentation.setCustomerAddress(doc.getElementsByTagName("adresseClient").item(0).getTextContent());
		segmentation.setDateBC(doc.getElementsByTagName("datebc").item(0).getTextContent());*/
		
		// Récupération des critères
		ArrayList<Critere> criteres = new ArrayList<Critere>();
		NodeList nList = doc.getElementsByTagName("CRITERE");
		for (int temp = 0; temp < nList.getLength(); temp++) 
		{
			
			//Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			//Conversion en element
			Element eElement = (Element) nNode;

			Critere a = new Critere( eElement.getAttribute("type"));
			//a = eElement.getAttribute("");
			
			// TODO check si min/max ou valeur
			String tmp= eElement.getAttribute("type");
			
			if (tmp == null)
			{
				a.setMin(Integer.parseInt(eElement.getAttribute("min")));
				a.setMax(Integer.parseInt(eElement.getAttribute("max")));				
			}
			else
			{
				a.setValue(eElement.getAttribute("valeur"));
			}
			
			criteres.add(a);
		}
		segmentation.setCriteres(criteres);
		
		//Récuération des clients
		ArrayList<Client> clients = new ArrayList<Client>();
		NodeList mList = doc.getElementsByTagName("CLIENT");
		for (int temp = 0; temp < mList.getLength(); temp++) 
		{
			//Récupéraction du noeud à traiter
			Node nNode = mList.item(temp);
			//Conversion en element
			Element eElement = (Element) nNode;
			
			Client a = new Client(Integer.parseInt(eElement.getAttribute("numero")));

			/*a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0).getTextContent());
			a.setReference(eElement.getElementsByTagName("reference").item(0).getTextContent());
			a.setQuantity(eElement.getElementsByTagName("quantite").item(0).getTextContent());*/

			clients.add(a);
		}
		segmentation.setClients(clients);
		
		/*DateFormat df = new SimpleDateFormat("yyyyMMdd");
		segmentation.setDateBL(df.format(ClockClient.getClock().getHour()));
		*/
		//TODO sauvergarde en base
		//JdbcConnection.getInstance().insertCommandeInternet(command);
		
		//Construction du xml
		/*String bl = "<EXPEDITIONCLIENT>"
					+ "<LIVRAISON>"
						+ "<NUMERO>" + segmentation.getCommandNumber() + "</NUMERO>"
						+ "<DATEBC>" + segmentation.getDateBC() + "</DATEBC>"
						+ "<DATEBL>" + segmentation.getDateBL() + "</DATEBL>";
						
		for (TicketReduc a : clients)
			bl += "<ARTICLE>"
					+ "<REFERENCE>" + a.getReference() + "</REFERENCE>"
					+ "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
					+ "<CATEGORIE>" + a.getCategory() + "</CATEGORIE>"
				+ "</ARTICLE>";
							
		bl += "</LIVRAISON></EXPEDITIONCLIENT>";*/
		
		//return bl;
		
		return null;
	}
	
	public String getCommandeFournisseur(String message, Document doc) throws AsyncMessageException
	{
		LivraisonFournisseur command = new LivraisonFournisseur();
		
		command.setCommandNumber(doc.getElementsByTagName("NUMERO").item(0).getTextContent());
		command.setDateBC(doc.getElementsByTagName("DATEBC").item(0).getTextContent());
		
		List<TicketReduc> articles = new ArrayList<TicketReduc>();
		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp = 0; temp < nList.getLength(); temp++) 
		{
			TicketReduc a = new TicketReduc();
			
			//Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			//Conversion en element
			Element eElement = (Element) nNode;

			a.setReference(eElement.getElementsByTagName("REFERENCE").item(0).getTextContent());
			a.setQuantity(eElement.getElementsByTagName("QUANTITE").item(0).getTextContent());
			a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0).getTextContent());
			 
			articles.add(a);
		}
		command.setArticles(articles);
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		command.setDateBL(df.format(ClockClient.getClock().getHour()));
		
		//TODO sauvergarde en base
		//JdbcConnection.getInstance().insertLivraisonFournisseur(command);
		
		//Construction du xml
		String bl = "<LIVRAISONSCOMMANDEFOURNISSEUR>"
					+ "<LIVRAISON>"
						+ "<NUMERO>" + command.getCommandNumber() + "</NUMERO>"
						+ "<DATEBC>" + command.getDateBC() + "</DATEBC>"
						+ "<DATEBL>" + command.getDateBL() + "</DATEBL>";
		
		for (TicketReduc a : articles)
			bl += "<ARTICLE>"
					+ "<REFERENCE>" + a.getReference() + "</REFERENCE>"
					+ "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
					+ "<CATEGORIE>" + a.getCategory() + "</CATEGORIE>"
				+ "</ARTICLE>";
							
		bl+= "</LIVRAISON></LIVRAISONSCOMMANDEFOURNISSEUR>";
		
		return bl;
	}
	
	public String getReassortBO(String message, Document doc) throws AsyncMessageException
	{
		ReassortBO command = new ReassortBO();
		
		command.setCommandNumber(doc.getElementsByTagName("NUMERO").item(0).getTextContent());
		command.setBackOfficeRef(doc.getElementsByTagName("REFBO").item(0).getTextContent());
		command.setBackOfficeAddress(doc.getElementsByTagName("ADRESSEBO").item(0).getTextContent()); 
		command.setBackOfficePhone(doc.getElementsByTagName("TELBO").item(0).getTextContent());
		command.setDateBC(doc.getElementsByTagName("DATEBC").item(0).getTextContent());
		
		List<TicketReduc> articles = new ArrayList<TicketReduc>();
		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp = 0; temp < nList.getLength(); temp++) 
		{
			TicketReduc a = new TicketReduc();
			
			//Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			//Conversion en element
			Element eElement = (Element) nNode;

			a.setReference(eElement.getElementsByTagName("REFERENCE").item(0).getTextContent());
			a.setQuantity(eElement.getElementsByTagName("QUANTITE").item(0).getTextContent());
			a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0).getTextContent());
			 
			articles.add(a);
		}
		command.setArticles(articles);
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		command.setDateBL(df.format(ClockClient.getClock().getHour()));
		
		//TODO sauvergarde en base
		//JdbcConnection.getInstance().insertReassortBO(command);
		
		//Construction du xml
		String bl = "<LIVRAISONS>"
					+ "<LIVRAISON>"
						+ "<NUMERO>" + command.getCommandNumber() + "</NUMERO>"
						+ "<REFMAGASIN>"+ command.getBackOfficeRef() + "</REFMAGASIN>"
						+ "<DATEBC>" + command.getDateBC() + "</DATEBC>"
						+ "<DATEBL>" + command.getDateBL() + "</DATEBL>";
		
		for (TicketReduc a : articles)
			bl += "<ARTICLE>"
					+ "<REFERENCE>" + a.getReference() + "</REFERENCE>"
					+ "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
				+ "</ARTICLE>";
		
		bl += "</LIVRAISON></LIVRAISONS>";
		
		return bl;
	}
}
