package fr.epita.sigl.miwa.application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.epita.sigl.miwa.application.BDD.JdbcConnection;
import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.crm.TicketReduc;
import fr.epita.sigl.miwa.application.object.Article;
import fr.epita.sigl.miwa.application.object.Client;
import fr.epita.sigl.miwa.application.object.Critere;
import fr.epita.sigl.miwa.application.object.Group;
import fr.epita.sigl.miwa.application.object.Segmentation;
import fr.epita.sigl.miwa.application.object.TicketCaisse;
import fr.epita.sigl.miwa.application.object.TicketVente;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class XMLManager
{
	private static XMLManager instance = null;
	private DocumentBuilderFactory dBFactory;
	private DocumentBuilder dBuilder;
	
	public XMLManager() {
		try {
			dBFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dBFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			
			
		}
	}
	
	public static XMLManager getInstance()
	{
		if (instance == null)
			instance = new XMLManager();
		
		return instance;
	}
	
	
	public String dispatchXML(String message, String xml) throws SAXException, IOException, AsyncMessageException, ParseException
	{
		File file = new File (xml);

		// Parsage du fichier	
		Document criteriaFile = dBuilder.parse(file);
		String XMLReturn = "";
		
		NodeList headerNodes = criteriaFile.getElementsByTagName("ENTETE");
		String object = headerNodes.item(0).getAttributes().getNamedItem("objet").getNodeValue();

		switch (object) 
		{
			case "ticket-caisse":
				XMLReturn = getTicketCaisse(message, xml);
				break;
			case "ticket-client-fidelise":
				XMLReturn = getTicketClientFidelise(message, xml);
				break;
			case "segmentation-client":
				XMLReturn = getSegmentationClient(message, xml);
				break;

		}
		return XMLReturn;
	}
	
	
	public String getSegmentationClient(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		Segmentation segmentation = new Segmentation();
		
		File file = new File (xml);
		/*BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write(message);
		output.close();
		*/
		// Parsage du fichier	
		Document criteriaFile = dBuilder.parse(file);
		
		NodeList headerNodes = criteriaFile.getElementsByTagName("ENTETE");
		String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
		Date seqDate = (new SimpleDateFormat("YYYY-MM-dd")).parse(dateStr);
		
		segmentation.setDate(seqDate);
		
		
		ArrayList<Critere> criteres = new ArrayList<Critere>();

		NodeList groupsNodes = criteriaFile.getElementsByTagName("GROUPES");
		
		// Création des éléments Critere
		for (int i = 0; i < groupsNodes.getLength(); i++)
		{
			
			Node groupNode = groupsNodes.item(i);
			Group group = new Group();
			List<Critere> list = new  ArrayList<>();
			group.setCriteres(list);
			String tmpInfo;
			
			NodeList childNodes = groupNode.getChildNodes();
			for (int j = 0; j < childNodes.getLength(); j++) 
			{
				Node cNode = childNodes.item(j);
				if (cNode instanceof Element) 
				{
					String content = cNode.getLastChild().getTextContent().trim();
					if (cNode.getNodeName() == "CRITERE")
					{
						NodeList criteriasNodes = cNode.getChildNodes();
						for (int k = 0; k < criteriasNodes.getLength(); k++) 
						{
							Node criteriaNodes = criteriasNodes.item(k);
							Critere c = new Critere();
							String type = criteriaNodes.getAttributes().getNamedItem("type").getNodeValue();
							switch (type) 
							{
								case "age":
									c.setType(type);
									c.setMax(Integer.parseInt(criteriaNodes.getAttributes().getNamedItem("max").getNodeValue()));
									c.setMin(Integer.parseInt(criteriaNodes.getAttributes().getNamedItem("min").getNodeValue()));
									break;
								case "geographie":
									c.setType(type);
									c.setValue(criteriaNodes.getAttributes().getNamedItem("valeur").getNodeValue());
								    break;
								case "sexe":
									c.setType(type);
									c.setValue(criteriaNodes.getAttributes().getNamedItem("valeur").getNodeValue());
								    break;
								case "situation-familiale":
									c.setType(type);
									c.setValue(criteriaNodes.getAttributes().getNamedItem("valeur").getNodeValue());
								    break;
								case "enfant":
									c.setType(type);
									c.setValue(criteriaNodes.getAttributes().getNamedItem("valeur").getNodeValue());
								    break;
								case "fidelite":
									c.setType(type);
									c.setValue(criteriaNodes.getAttributes().getNamedItem("valeur").getNodeValue());
								    break;
							}
							group.getCriteres().add(c);
						}
					}
					else if (cNode.getNodeName() == "CLIENTS")
					{
						NodeList clientsNodes = cNode.getChildNodes();
						for (int k = 0; k < clientsNodes.getLength(); k++) 
						{
							Node clientNodes = clientsNodes.item(k);
							Critere c = new Critere();
							String type = clientNodes.getAttributes().getNamedItem("type").getNodeValue();
							// FIXME

						}
					}
				}
			}
		}
			
			
			/*tmpInfo = criteriaNode.getAttributes().getNamedItem("type").getNodeValue();
			criteria.setType(tmpInfo);
			if (tmpInfo.equalsIgnoreCase("age")) {
				String min = criteriaNode.getAttributes().getNamedItem("min").getNodeValue();
				String max = criteriaNode.getAttributes().getNamedItem("max").getNodeValue();
				criteria.setMax(Integer.valueOf(max));
				criteria.setMin(Integer.valueOf(min));
			}
			else if (tmpInfo.equalsIgnoreCase("geographie")) {
				String valeur = criteriaNode.getAttributes().getNamedItem("valeur").getNodeValue();
				criteria.setValue(valeur);
			}
			else if (tmpInfo.equalsIgnoreCase("sexe")) {
				String valeur = criteriaNode.getAttributes().getNamedItem("valeur").getNodeValue();
				criteria.setValue(valeur);
			}
			else if (tmpInfo.equalsIgnoreCase("situation-familiale")) {
				String valeur = criteriaNode.getAttributes().getNamedItem("valeur").getNodeValue();
				criteria.setValue(valeur);
			}
			else if (tmpInfo.equalsIgnoreCase("enfant")) {
				String valeur = criteriaNode.getAttributes().getNamedItem("valeur").getNodeValue();
				criteria.setValue(valeur);
			}
			else if (tmpInfo.equalsIgnoreCase("fidelite")) {
				String valeur = criteriaNode.getAttributes().getNamedItem("valeur").getNodeValue();
				criteria.setValue(valeur);
			}
			
			criteres.add(criteria);
		}
		segmentation.setCriteres(criteres);*/
		
		//Récupération des clients
		ArrayList<Client> clients = new ArrayList<Client>();
		NodeList mList = criteriaFile.getElementsByTagName("CLIENT");
		for (int temp = 0; temp < mList.getLength(); temp++)
		{
			//Récupéraction du noeud à traiter
			Node nNode = mList.item(temp);
			//Conversion en element
			Element eElement = (Element) nNode;
			
			Client a = new Client(Integer.parseInt(eElement.getAttribute("numero")));

			clients.add(a);
		}
		segmentation.setClients(clients);
		
		//DateFormat df = new SimpleDateFormat("yyyyMMdd");
		//segmentation.setDateBL(df.format(ClockClient.getClock().getHour()));
		
		//TODO sauvergarde en base
		//JdbcConnection.getInstance().insertCommandeInternet(command);
		
		//Construction du xml
		String bl = "<EXPEDITIONCLIENT>";
				/*	+ "<LIVRAISON>"
						+ "<NUMERO>" + segmentation.getCommandNumber() + "</NUMERO>"
						+ "<DATEBC>" + segmentation.getDateBC() + "</DATEBC>"
						+ "<DATEBL>" + segmentation.getDateBL() + "</DATEBL>";
						
		for (TicketReduc a : clients)
			bl += "<ARTICLE>"
					+ "<REFERENCE>" + a.getReference() + "</REFERENCE>"
					+ "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
					+ "<CATEGORIE>" + a.getCategory() + "</CATEGORIE>"
				+ "</ARTICLE>";*/
							
		bl += "</LIVRAISON></EXPEDITIONCLIENT>";
		
		return bl;
		
	}
	
	public String getDemandeCreationCompte(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		Client client = new Client();
		File file = new File (xml);
		Document compteClientFile = dBuilder.parse(file);	
		
		NodeList headerNodes = compteClientFile.getElementsByTagName("ENTETE");
		String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
		Date seqDate = (new SimpleDateFormat("YYYY-MM-dd")).parse(dateStr);
		client.setDate(seqDate);
		
		NodeList compteNodes = compteClientFile.getElementsByTagName("COMPTE");
		for (int i = 0; i < compteNodes.getLength(); i++)
		{
			Node infoNodes = compteNodes.item(i);
			client.setNom(infoNodes.getAttributes().getNamedItem("nom").getNodeValue());
			client.setPrenom(infoNodes.getAttributes().getNamedItem("prenom").getNodeValue());
			client.setAdresse(infoNodes.getAttributes().getNamedItem("adresse").getNodeValue());
			client.setCodePostal(infoNodes.getAttributes().getNamedItem("code_postal").getNodeValue());
			client.setMail(infoNodes.getAttributes().getNamedItem("email").getNodeValue());
			client.setTelephone(infoNodes.getAttributes().getNamedItem("telephone").getNodeValue());
			int lower = 1;
			int higher = 99999999;

			int random = (int)(Math.random() * (higher-lower)) + lower;
			client.setMatricule(random);
			JdbcConnection.getInstance().getConnection();
			JdbcConnection.getInstance().insertClientInternet(client);
		}	
		String bl = "<ENTETE objet=\"matricule-client\" source=\"crm\" date=\"AAAAA-MM-JJ\">"
				+ "<INFORMATION><CLIENT matricule=\"\" nom=\"" + client.getNom() + "\" prenom=\"" + client.getPrenom() + "\" />";
		bl += "</INFORMATION></ENTETE>";
		
		return bl;
	}
	
	public String getClientConnecteDemandeReduc(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		File file = new File (xml);
		Document compteClientFile = dBuilder.parse(file);	
		
		String bl = "<ENTETE objet=\"information-client\" source=\"crm\" date=\"AAAAA-MM-JJ\">"
				+ "<INFORMATIONS>";
		
		NodeList compteNodes = compteClientFile.getElementsByTagName("COMPTE");
		for (int i = 0; i < compteNodes.getLength(); i++)
		{
			Node infoNodes = compteNodes.item(i);
			String matricule = infoNodes.getAttributes().getNamedItem("matricule").getNodeValue();
			Client client = JdbcConnection.getInstance().GetClientInternet(matricule);
			bl += "<CLIENT matricule=\"" + client.getMatricule() + "\" />"
					+ "<PROMOTION article=\"\" fin=\"\" reduc=\"\" />";
		}
		
		bl += "</INFORMATIONS></ENTETE>";
		return bl;
	}
	
	public String getTicketClientFidelise(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		TicketVente ticketVente = new TicketVente();
		
		File file = new File (xml);
		/*BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write(message);
		output.close();
		*/
		// Parsage du fichier	
		Document ticketVenteFile = dBuilder.parse(file);
		
		NodeList headerNodes = ticketVenteFile.getElementsByTagName("ENTETE");
		String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
		Date seqDate = (new SimpleDateFormat("YYYY-MM-dd")).parse(dateStr);
		
		ticketVente.setDate(seqDate);
		List<Article> list = new  ArrayList<>();
		ticketVente.setArticle(list);
		

		NodeList ticketVenteNodes = ticketVenteFile.getElementsByTagName("TICKETVENTE");
		
		// Création des éléments ticketventes et articles
		for (int i = 0; i < ticketVenteNodes.getLength(); i++)
		{
			Node articleNodes = ticketVenteNodes.item(i);
			ticketVente.setRefclient(articleNodes.getAttributes().getNamedItem("refclient").getNodeValue());
			ticketVente.setMoyenpayement(articleNodes.getAttributes().getNamedItem("moyenpayement").getNodeValue());
			
			NodeList articlesNodes = ticketVenteFile.getElementsByTagName("ARTICLE");
			for (int j = 0; j < articlesNodes.getLength(); j++) 
			{
				Node artNodes = articlesNodes.item(j);
				Article article = new Article();
				article.setRef(artNodes.getAttributes().getNamedItem("refarticle").getNodeValue());
				article.setQuantite(Integer.parseInt(artNodes.getAttributes().getNamedItem("quantite").getNodeValue()));
				article.setPrix(Integer.parseInt(artNodes.getAttributes().getNamedItem("prix").getNodeValue()));
				ticketVente.getArticle().add(article);
			}
		}
		
		String bl = "<EXPEDITIONCLIENT>";
		/*	+ "<LIVRAISON>"
				+ "<NUMERO>" + segmentation.getCommandNumber() + "</NUMERO>"
				+ "<DATEBC>" + segmentation.getDateBC() + "</DATEBC>"
				+ "<DATEBL>" + segmentation.getDateBL() + "</DATEBL>";
				
		for (TicketReduc a : clients)
			bl += "<ARTICLE>"
					+ "<REFERENCE>" + a.getReference() + "</REFERENCE>"
					+ "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
					+ "<CATEGORIE>" + a.getCategory() + "</CATEGORIE>"
				+ "</ARTICLE>";*/
							
		bl += "</LIVRAISON></EXPEDITIONCLIENT>";
		
		return bl;
	}
	
	public String getTicketCaisse(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		TicketCaisse ticketCaisse = new TicketCaisse();
		
		File file = new File (xml);
		/*BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write(message);
		output.close();
		*/
		// Parsage du fichier	
		Document ticketCaisseFile = dBuilder.parse(file);
		
		NodeList headerNodes = ticketCaisseFile.getElementsByTagName("ENTETE");
		String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
		Date seqDate = (new SimpleDateFormat("YYYY-MM-dd")).parse(dateStr);
		
		ticketCaisse.setDate(seqDate);
		List<Article> list = new  ArrayList<>();
		ticketCaisse.setArticle(list);
		

		NodeList ticketCaisseNodes = ticketCaisseFile.getElementsByTagName("TICKETVENTE");
		
		// Création des éléments ticketventes et articles
		for (int i = 0; i < ticketCaisseNodes.getLength(); i++)
		{
			Node articleNodes = ticketCaisseNodes.item(i);
			ticketCaisse.setRefclient(articleNodes.getAttributes().getNamedItem("refclient").getNodeValue());
			ticketCaisse.setMoyenpayement(articleNodes.getAttributes().getNamedItem("moyenpayement").getNodeValue());
			
			NodeList articlesNodes = ticketCaisseFile.getElementsByTagName("ARTICLE");
			for (int j = 0; j < articlesNodes.getLength(); j++) 
			{
				Node artNodes = articlesNodes.item(j);
				Article article = new Article();
				article.setRef(artNodes.getAttributes().getNamedItem("refarticle").getNodeValue());
				article.setQuantite(Integer.parseInt(artNodes.getAttributes().getNamedItem("quantite").getNodeValue()));
				article.setPrix(Integer.parseInt(artNodes.getAttributes().getNamedItem("prix").getNodeValue()));
				ticketCaisse.getArticle().add(article);
			}
		}
		
		String bl = "<EXPEDITIONCLIENT>";
		/*	+ "<LIVRAISON>"
				+ "<NUMERO>" + segmentation.getCommandNumber() + "</NUMERO>"
				+ "<DATEBC>" + segmentation.getDateBC() + "</DATEBC>"
				+ "<DATEBL>" + segmentation.getDateBL() + "</DATEBL>";
				
		for (TicketReduc a : clients)
			bl += "<ARTICLE>"
					+ "<REFERENCE>" + a.getReference() + "</REFERENCE>"
					+ "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
					+ "<CATEGORIE>" + a.getCategory() + "</CATEGORIE>"
				+ "</ARTICLE>";*/
							
		bl += "</LIVRAISON></EXPEDITIONCLIENT>";
		
		return bl;
	}
	
	public String SendAfterFedelityAccount()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(ClockClient.getClock().getHour());
		String bl = "";
		
		bl = "<ENTETE objet='matricule-client' source='crm' date=" + date + "/>"
				+ "<INFORMATIONS>";
			
				/*	<CLIENT matricule="" nom="" prenom="" />
		</INFORMATIONS>*/
		return bl;
	}
	
	/*public String getCommandeFournisseur(String message, Document doc) throws AsyncMessageException
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
	}*/
}
