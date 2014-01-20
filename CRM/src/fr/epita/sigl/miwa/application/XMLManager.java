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
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
import fr.epita.sigl.miwa.application.object.Article;
import fr.epita.sigl.miwa.application.object.CarteFidelite;
import fr.epita.sigl.miwa.application.object.Client;
import fr.epita.sigl.miwa.application.object.Critere;
import fr.epita.sigl.miwa.application.object.Group;
import fr.epita.sigl.miwa.application.object.Segmentation;
import fr.epita.sigl.miwa.application.object.TicketVente;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class XMLManager
{
	private static XMLManager instance = null;
	private DocumentBuilderFactory dBFactory;
	private DocumentBuilder dBuilder;
	
	private static final Logger LOGGER = Logger.getLogger(XMLManager.class.getName());
	
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
		LOGGER.info("***** Chargement du gestionnaire de XML");
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
			case "creation_compte":
				XMLReturn = getDemandeCreationCompte(message, xml);
				break;
			case "modif_compte":
				XMLReturn = getDemandeModifCompte(message, xml);
				break;
			case "suppression_compte":
				XMLReturn = getDemandeSupprCompte(message, xml);
				break;

		}
		return XMLReturn;
	}
	
	public String getSegmentationClient(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Analyse du fichier XML: Segmentation client");
		Segmentation segmentation = new Segmentation();
		
		File file = new File (xml);
		/*BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write(message);
		output.close();
		*/
		// Parsage du fichier
		Document criteriaFile = dBuilder.parse(file);
		LOGGER.info("***** Parsage du fichier");
		
		NodeList headerNodes = criteriaFile.getElementsByTagName("ENTETE");
		String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
		Date seqDate = (new SimpleDateFormat("YYYY-MM-dd")).parse(dateStr);
		LOGGER.info("***** Demande effectuée le: " + seqDate);
		
		segmentation.setDate(seqDate);
		List<Critere> list2 = new ArrayList<Critere>();
		segmentation.setCriteres(list2);
		

		NodeList groupsNodes = criteriaFile.getElementsByTagName("GROUPE");
		
		// Création des éléments Critere
		for (int i = 0; i < groupsNodes.getLength(); i++)
		{
			LOGGER.info("***** Analyse du groupe " + i);
			//Node groupNode = groupsNodes.item(i);
			Element groupNode = (Element)groupsNodes.item(i);
			Group group = new Group();
			List<Critere> list = new  ArrayList<>();
			group.setCriteres(list);
			
			NodeList childNodes = groupNode.getChildNodes();
			for (int j = 0; j < childNodes.getLength(); j++) 
			{
				Node cNode = childNodes.item(j);
				if (cNode instanceof Element)
				{
					if (cNode.getNodeName() == "CRITERES")
					{
						NodeList criteriasNodes = groupNode.getElementsByTagName("CRITERE");
						LOGGER.info("***** Nombre de critères: " + criteriasNodes.getLength());
						for (int k = 0; k < criteriasNodes.getLength(); k++) 
						{
							Node criteriaNodes = criteriasNodes.item(k);
							Critere c = new Critere();
							String tmp = criteriaNodes.getNodeName();
							String type = criteriaNodes.getAttributes().getNamedItem("type").getNodeValue();
							LOGGER.info("***** Analyse du critère "+ k +": " + type);
							switch (type) 
							{
								case "age":
									c.setType(type);
									c.setMax(Integer.parseInt(criteriaNodes.getAttributes().getNamedItem("max").getNodeValue()));
									c.setMin(Integer.parseInt(criteriaNodes.getAttributes().getNamedItem("min").getNodeValue()));
									LOGGER.info("***** L'âge est compris entre : " + c.getMin() + " et " + c.getMax());
									break;
								case "geographie":
									c.setType(type);
									c.setValue(criteriaNodes.getAttributes().getNamedItem("departement").getNodeValue());
									LOGGER.info("***** Le département est: " + c.getValue());
								    break;
								case "sexe":
									c.setType(type);
									c.setValue(criteriaNodes.getAttributes().getNamedItem("sexe").getNodeValue());
									LOGGER.info("***** Le sexe est: " + c.getValue());
								    break;
								case "situation-familiale":
									c.setType(type);
									c.setValue(criteriaNodes.getAttributes().getNamedItem("situation").getNodeValue());
									LOGGER.info("***** La situation familiale est: " + c.getValue());
								    break;
								case "enfant":
									c.setType(type);
									c.setValue(criteriaNodes.getAttributes().getNamedItem("enfant").getNodeValue());
									LOGGER.info("***** Le nombre d'enfants est de: " + c.getValue());
								    break;
								case "fidelite":
									c.setType(type);
									c.setValue(criteriaNodes.getAttributes().getNamedItem("carte").getNodeValue());
									LOGGER.info("***** La fidélité est: " + c.getValue());
								    break;
							}
							group.getCriteres().add(c);
							segmentation.addCritere(c);
						}
					}
					else if (cNode.getNodeName() == "CLIENTS")
					{
						NodeList clientsNodes = groupNode.getElementsByTagName("CLIENT");
						LOGGER.info("***** Nombre de clients: " + clientsNodes.getLength());
						for (int k = 0; k < clientsNodes.getLength(); k++) 
						{
							Element clientNodes = (Element)clientsNodes.item(k);
							Client c = new Client();
							c.setMatricule(Integer.parseInt(clientNodes.getAttributes().getNamedItem("numero").getNodeValue()));
							c.articlesList = new ArrayList<>();
							LOGGER.info("***** Analyse du client " + k + ": " + c.getMatricule());
							
							NodeList articlessNodes = clientNodes.getElementsByTagName("CATEGORIE");
							for (int l = 0; l < articlessNodes.getLength(); l++) 
							{
								Node articleNodes = articlessNodes.item(l);
								Article a = new Article();
								a.setRef(articleNodes.getAttributes().getNamedItem("ref").getNodeValue());
								a.setQuantite(Integer.parseInt(articleNodes.getAttributes().getNamedItem("achat").getNodeValue()));
								c.articlesList.add(a);
								LOGGER.info("***** Catégorie d'article: ref:" + a.getRef() + " - achat:" + a.getQuantite());
							}
							segmentation.addClient(c);
						}
					}
				}
			}
		}
		
		//DateFormat df = new SimpleDateFormat("yyyyMMdd");
		//segmentation.setDateBL(df.format(ClockClient.getClock().getHour()));
		
		//TODO sauvergarde en base
		//JdbcConnection.getInstance().insertCommandeInternet(command);
		
		//Construction du xml
		String bl = "<EXPEDITIONCLIENT>";
		
		return bl;
		
	}
	

	public String getSendClientBI()
	{
		LOGGER.info("***** Creation du XML poir le BI avec la base client");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String bl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><XML><ENTETE objet=\"information-client\" source=\"crm\" date=\"" + df.format(ClockClient.getClock().getHour()) + "\"/>"
				+ "<CLIENTS>";
		
		for (int i = 0; i < Client.clientsList.size(); i++)
		{
			Client c = Client.clientsList.get(i);
			LOGGER.info("***** Ajout d'un client dans le XML : " + c.getMatricule());
			bl += "<CLIENT id=\"" + c.getMatricule() + "\" civilite=\"" + c.getCivilite()
					+ "\" naissance=\"" + c.getDate() + "\" codepostal=\"" + c.getCodePostal() 
					+ "\" situationfam=\"" + c.getSituation() + "\" nbenfant=\"" + c.getNbenfant() + "\" typecarte=\"" + c.getCarteFed().getType() + "\" />";
		}
		
		bl += "<CLIENTS><XML>";
		LOGGER.info("***** Envoi des clients au BI");
		
		return bl;
	}

	public String getDemandeSegmentationClient(String message) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Préparation d'une demande de segmentation client");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String bl = "<ENTETE objet=\"demande-segmentation-client\" source=\"crm\" date=\"" + df.format(ClockClient.getClock().getHour());
		//TODO: heure
		bl += "\"/><CRITERES>";
		int lower = 15;
		int higher = 80;

		int random = (int)(Math.random() * (higher-lower)) + lower;
		
		bl += "<CRITERE type=\"age\" max=" + (random - 10) + " min=" + (random + 10) + "/>"
				+ "<CRITERE type=\"geographie\" valeur=" + (random + 3) + "/>";
				
		if (random%2 == 0)
		{
			bl += "<CRITERE type=\"situation-familiale\" valeur=\"célibataire\"/>"
					+ "<CRITERE type=\"sexe\" valeur=\"M\"/>";
		}
		else
		{
			bl += "<CRITERE type=\"situation-familiale\" valeur=\"marié\"/>"
					+ "<CRITERE type=\"sexe\" valeur=\"F\"/>";
		}
		if (random%3 == 0)
		{
			bl += "<CRITERE type=\"enfant\" valeur=TRUE/>"
					+ "<CRITERE type=\"fidelite\" valeur=1/>";
		}
		else
		{
			bl += "<CRITERE type=\"enfant\" valeur=FALSE/>"
					+ "<CRITERE type=\"fidelite\" valeur=2/>";
		}	
		
		bl += "</CRITERES>";
		return bl;
	}
	
	public String getDemandeCreationCompte(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Analyse du flux XML: création d'un compte fidélité");
		Client client = new Client();
		File file = new File (xml);
		Document compteClientFile = dBuilder.parse(file);	
		LOGGER.info("***** Début du parsage du fichier");
		
		NodeList headerNodes = compteClientFile.getElementsByTagName("ENTETE");
		String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
		Date seqDate = (new SimpleDateFormat("YYYY-MM-dd")).parse(dateStr);
		LOGGER.info("***** Demande effectuée le: " + seqDate);
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
			client.setCivilite(infoNodes.getAttributes().getNamedItem("civilite").getNodeValue());
			client.setSituation(infoNodes.getAttributes().getNamedItem("situation").getNodeValue());
			client.setNaissance(infoNodes.getAttributes().getNamedItem("naissance").getNodeValue());
			client.setNbenfant(Integer.parseInt(infoNodes.getAttributes().getNamedItem("nbenfant").getNodeValue()));
			client.setIBAN(infoNodes.getAttributes().getNamedItem("iban").getNodeValue());
			client.setBIC(infoNodes.getAttributes().getNamedItem("bic").getNodeValue());

			LOGGER.info("***** Recherche des informations clients");
			LOGGER.info("***** " + client.toString());
			
			int lower = 1;
			int higher = 99999999;

			int random = (int)(Math.random() * (higher-lower)) + lower;
			client.setMatricule(random);
			Client.clientsList.add(client);
			JdbcConnection.getInstance().insertClientInternet(client);
			LOGGER.info("***** Enregistrement en BDD sous le matricule: " + client.getMatricule());
		}	
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String bl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ENTETE objet=\"matricule-client\" source=\"crm\" date=\"" + df.format(ClockClient.getClock().getHour()) + "\">"
				+ "<INFORMATION><CLIENT matricule=\"" + client.getMatricule() + "\" nom=\"" + client.getNom() + "\" prenom=\"" + client.getPrenom() + "\" />";
		bl += "</INFORMATION></ENTETE>";
		
		// Retour des info à Internet
		SyncMessHandler.getSyncMessSender().sendMessage(
				EApplication.INTERNET, bl);
		LOGGER.info("***** Envoi de la réponse auprès d'Internet");
		
		// Creation des comptes chez la monétique
		if (SyncMessHandler.getSyncMessSender().sendMessage(
				EApplication.MONETIQUE, getCreationCompteCreditFed(client)) == false)
		{
			LOGGER.warning("Impossible de contacter la monétique pour la création d'un compte crédit carte");
		}
		LOGGER.info("***** Création du compte crédit " + client.getMatricule() + " effectué auprès de la Monétique");
		return bl;
	}
	
	
	public String getDemandeModifCompte(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Analyse du flux XML: modification d'un compte fidélité");
		Client client = new Client();
		File file = new File (xml);
		Document compteClientFile = dBuilder.parse(file);	
		LOGGER.info("***** Début du parsage du fichier");
		
		NodeList headerNodes = compteClientFile.getElementsByTagName("ENTETE");
		String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
		Date seqDate = (new SimpleDateFormat("YYYY-MM-dd")).parse(dateStr);
		LOGGER.info("***** Demande effectuée le: " + seqDate);
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
			client.setMatricule(Integer.parseInt(infoNodes.getAttributes().getNamedItem("matricule").getNodeValue()));
			LOGGER.info("***** Recherche des informations clients");
			LOGGER.info("***** " + client.toString());
			for (int j = 0 ; j < Client.clientsList.size(); j++)
			{
				if (Client.clientsList.get(j).getMatricule() == client.getMatricule())
				{
					Client.clientsList.remove(j);
					break;
				}
			}
			Client.clientsList.add(client);
			JdbcConnection.getInstance().updateClientInternet(client);
			LOGGER.info("***** Modification effectuée en BDD");
		}
		String bl = "true";
		
		// Retour des info à Internet
		SyncMessHandler.getSyncMessSender().sendMessage(
				EApplication.INTERNET, bl);
		LOGGER.info("***** Confirmation effectuée auprès d'Internet");
		return bl;
	}
	
	
	public String getDemandeSupprCompte(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Analyse du flux XML: suppression d'un compte fidélité");
		File file = new File (xml);
		Document compteClientFile = dBuilder.parse(file);
		LOGGER.info("***** Début du parsage du fichier");
		
		NodeList headerNodes = compteClientFile.getElementsByTagName("ENTETE");
		String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
		Date seqDate = (new SimpleDateFormat("YYYY-MM-dd")).parse(dateStr);
		LOGGER.info("***** Demande effectuée le: " + seqDate);
		int matricule = 0;
		
		NodeList compteNodes = compteClientFile.getElementsByTagName("COMPTE");
		for (int i = 0; i < compteNodes.getLength(); i++)
		{
			Node infoNodes = compteNodes.item(i);
			matricule = Integer.parseInt(infoNodes.getAttributes().getNamedItem("matricule").getNodeValue());
			LOGGER.info("***** Demande de suppression du compte " + matricule);
			for (int j = 0 ; j < Client.clientsList.size(); j++)
			{
				if (Client.clientsList.get(j).getMatricule() == matricule)
				{
					Client.clientsList.remove(j);
					break;
				}
			}
			JdbcConnection.getInstance().deleteClientInternet(matricule);
			LOGGER.info("***** Suppression effectuée en BDD");
		}
		String bl = "true";
		
		// Retour des info à Internet
		SyncMessHandler.getSyncMessSender().sendMessage(
				EApplication.INTERNET, bl);
		LOGGER.info("***** Confirmation de la suppression auprès d'Internet");
		
		// Suppression des comptes chez la monétique
		if (matricule != 0)
		{
			if (SyncMessHandler.getSyncMessSender().sendMessage(
					EApplication.MONETIQUE, getSupprCompteCreditFed(matricule)) == false)
			{
				LOGGER.warning("Impossible de contacter la monétique pour la suppression d'un compte crédit");
			}
			LOGGER.info("***** Suppression du compte crédit " + matricule + " effectué auprès de la Monétique");
		}
			return bl;
	}
	
	
	public String getCreationTypeCarte(String type) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Création du type de carte: " + type);
		CarteFidelite fed = new CarteFidelite(type);
		if (type == "Silver")
		{
			fed.setEchellon(3);
			fed.setLimite_m(3000);
			fed.setLimite_tot(10000);
		}
		else if (type == "Gold")
		{
			fed.setEchellon(5);
			fed.setLimite_m(5000);
			fed.setLimite_tot(15000);
		}
		JdbcConnection.getInstance().insertCarteFed(fed);
		
		String bl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<monetique service=\"cms_type_carte\" action=\"c\">" +
					"<type_cf>" +
					"<id>" + fed.getType() + "</id>" +
					"<limite_mesuelle>" + fed.getLimite_m() + "</limite_mesuelle>" +
					"<limite_totale>" + fed.getLimite_tot() + "</limite_totale>" +
					"<nb_echelon>" + fed.getEchellon() + "</nb_echelon>" +
					"</type_cf>" +
					"</monetique>";
		
		return bl;
	}
	
	public String getModifTypeCarte(Client client) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Modification du type de carte");
		CarteFidelite fed = new CarteFidelite("Silver");
		fed.setEchellon(3);
		fed.setLimite_m(4000);
		fed.setLimite_tot(10000);
		JdbcConnection.getInstance().updateCarteFed(fed);
		
		client.setCarteFed(fed);
		
		String bl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<monetique service=\"cms_type_carte\" action=\"m\">" +
					"<type_cf id=\"" + fed.getType() + "\">" +
					"<limite_mesuelle>" + fed.getLimite_m() + "</limite_mesuelle>" +
					"<limite_totale>" + fed.getLimite_tot() + "</limite_totale>" +
					"<nb_echelon>" + fed.getEchellon() + "</nb_echelon>" +
					"</type_cf>" +
					"</monetique>";
		
		return bl;
	}
	
	public String getSupprTypeCarte(String type) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		JdbcConnection.getInstance().deleteCarteFed(type);
		LOGGER.info("***** Suppression du type de carte fidélité: " + type);
		String bl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<monetique service=\"cms_type_carte\" action=\"s\">" +
					"<type_cf id=\"" + type + "\">" +
					"<nouvel_id></nouvel_id>" +
					"</type_cf>" +
					"</monetique>";
		LOGGER.info("***** Envoi du XML vers Monétique");
		return bl;
	}
	
	
	public String getCreationCompteCreditFed(Client client) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Création du compte crédit pour le client: " + client.getMatricule());
		CarteFidelite fed = new CarteFidelite("Silver");
		fed.setEchellon(3);
		fed.setLimite_m(3000);
		fed.setLimite_tot(10000);
		
		client.setCarteFed(fed);
		
		String bl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<monetique service=\"cms_compte_cf\" action=\"c\">" +
					"<compte_cf>" +
					"<matricule_client>" + client.getMatricule() + "</matricule_client>" +
					"<BIC>" + client.getBIC() + "</BIC>" +
					"<IBAN>" + client.getIBAN() + "</IBAN>" +
					"<id_type_cf>" + fed.getType() + "</id_type_cf>" +
					"</compte_cf>" +
					"</monetique>";
		LOGGER.info("***** Envoi du XML vers Monétique");
		return bl;
	}
	
	public String getModifCompteCreditFed(Client client) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Modification du compte crédit du client " + client.getMatricule());
		CarteFidelite fed = new CarteFidelite("Gold");
		
		client.setCarteFed(fed);
		
		String bl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<monetique service=\"cms_compte_cf\" action=\"m\">" +
					"<compte_cf matricule_client=\"" + client.getMatricule() + "\">" +
					"<BIC>" + client.getBIC() + "</BIC>" +
					"<IBAN>" + client.getIBAN() + "</IBAN>" +
					"<id_type_cf>" + fed.getType() + "</id_type_cf>" +
					"</compte_cf>" +
					"</monetique>";
		LOGGER.info("***** Envoi du XML vers Monétique");
		return bl;
	}
	
	public String getSupprCompteCreditFed(int mat) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Suppression du compte crédit: " + mat);
		String bl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<monetique service=\"cms_compte_cf\" action=\"s\">" +
					"<compte_cf matricule_client=\"" + Integer.toString(mat) + "\">" +
					"</compte_cf>" +
					"</monetique>";
		LOGGER.info("***** Envoi du XML vers Monétique");
		return bl;
	}
	
	
	public String getClientConnecteDemandeReduc(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Analyse du flux XML: Client Connecté (Internet)");
		File file = new File (xml);
		Document compteClientFile = dBuilder.parse(file);	
		LOGGER.info("***** Parsage du XML");
		
		String bl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ENTETE objet=\"information-client\" source=\"crm\" date=\"AAAAA-MM-JJ\">"
				+ "<INFORMATIONS>";
		
		NodeList compteNodes = compteClientFile.getElementsByTagName("COMPTE");
		for (int i = 0; i < compteNodes.getLength(); i++)
		{
			Node infoNodes = compteNodes.item(i);
			String matricule = infoNodes.getAttributes().getNamedItem("matricule").getNodeValue();
			LOGGER.info("***** Client connecté:" + matricule);
			Client client = JdbcConnection.getInstance().GetClientInternet(matricule);
			if (client != null)
				LOGGER.info("***** Client retrouvé en BDD:" + client.getNom() + " " + client.getPrenom());
			else
				LOGGER.info("***** ERREUR: Client inconnu de la part du CRM");
			bl += "<CLIENT matricule=\"" + client.getMatricule() + "\" />"
					+ "<PROMOTION article=\"\" fin=\"\" reduc=\"\" />";
		}
		
		bl += "</INFORMATIONS></ENTETE>";
		LOGGER.info("***** Envoi des promotions");
		return bl;
	}
	
	public String getTicketClientFidelise(String message, String xml) throws AsyncMessageException, IOException, SAXException, ParseException
	{
		LOGGER.info("***** Analyse du flux XML: ticket de caisse après vente");
		TicketVente ticketVente = new TicketVente();
		
		File file = new File (xml);
		/*BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write(message);
		output.close();
		*/
		// Parsage du fichier	
		Document ticketVenteFile = dBuilder.parse(file);
		LOGGER.info("***** Parsage du ticket de caisse");
		
		NodeList headerNodes = ticketVenteFile.getElementsByTagName("ENTETE");
		String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
		Date seqDate = (new SimpleDateFormat("YYYY-MM-dd")).parse(dateStr);
		LOGGER.info("***** Ticket de caisse du " + dateStr);
		
		ticketVente.setDate(seqDate);
		List<Article> list = new  ArrayList<>();
		ticketVente.setArticle(list);
		

		NodeList ticketVenteNodes = ticketVenteFile.getElementsByTagName("TICKETVENTE");
		
		// Création des éléments ticketventes et articles
		for (int i = 0; i < ticketVenteNodes.getLength(); i++)
		{
			Element articleNodes = (Element)ticketVenteNodes.item(i);
			ticketVente.setRefclient(articleNodes.getAttributes().getNamedItem("refclient").getNodeValue());
			ticketVente.setMoyenpayement(articleNodes.getAttributes().getNamedItem("moyenpayement").getNodeValue());
			LOGGER.info("***** Client " + i + ": " + ticketVente.getRefclient() + " - " + ticketVente.getMoyenpayement());
			
			NodeList articlesNodes = articleNodes.getElementsByTagName("ARTICLE");
			for (int j = 0; j < articlesNodes.getLength(); j++) 
			{
				Node artNodes = articlesNodes.item(j);
				Article article = new Article();
				article.setRef(artNodes.getAttributes().getNamedItem("refarticle").getNodeValue());
				article.setQuantite(Integer.parseInt(artNodes.getAttributes().getNamedItem("quantite").getNodeValue()));
				article.setPrix(Integer.parseInt(artNodes.getAttributes().getNamedItem("prix").getNodeValue()));
				ticketVente.getArticle().add(article);
				LOGGER.info("******** Article " + j + ": " + article.getRef() + " - " + article.getQuantite() + " - " + article.getPrix());
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
		LOGGER.info("***** Analyse du flux XML: ticket de caisse avant vente");
		TicketVente ticketVente = new TicketVente();
		
		File file = new File (xml);
		/*BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write(message);
		output.close();
		*/
		// Parsage du fichier	
		Document ticketVenteFile = dBuilder.parse(file);
		LOGGER.info("***** Parsage du ticket de caisse temporaire");
		
		NodeList headerNodes = ticketVenteFile.getElementsByTagName("ENTETE");
		String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
		LOGGER.info("***** Ticket de caisse du " + dateStr);
		Date seqDate = (new SimpleDateFormat("YYYY-MM-dd")).parse(dateStr);
		
		ticketVente.setDate(seqDate);
		List<Article> list = new  ArrayList<>();
		List<Article> listReduc = new  ArrayList<>();
		ticketVente.setArticle(list);
		int totalPrice = 0;
		

		NodeList ticketVenteNodes = ticketVenteFile.getElementsByTagName("TICKETVENTE");
		
		// Création des éléments ticketventes et articles
		for (int i = 0; i < ticketVenteNodes.getLength(); i++)
		{
			Node articleNodes = ticketVenteNodes.item(i);
			
			ticketVente.setRefclient(articleNodes.getAttributes().getNamedItem("refclient").getNodeValue());
			ticketVente.setMoyenpayement(articleNodes.getAttributes().getNamedItem("moyenpayement").getNodeValue());
			LOGGER.info("***** Client " + i + ": " + ticketVente.getRefclient() + " - " + ticketVente.getMoyenpayement());
			
			NodeList articlesNodes = ticketVenteFile.getElementsByTagName("ARTICLE");
			for (int j = 0; j < articlesNodes.getLength(); j++) 
			{
				Node artNodes = articlesNodes.item(j);
				Article article = new Article();
				Article articleReduc = new Article();
				article.setRef(artNodes.getAttributes().getNamedItem("refarticle").getNodeValue());
				article.setQuantite(Integer.parseInt(artNodes.getAttributes().getNamedItem("quantite").getNodeValue()));
				article.setPrix(Integer.parseInt(artNodes.getAttributes().getNamedItem("prix").getNodeValue()));
				LOGGER.info("******** Article " + j + ": " + article.getRef() + " - " + article.getQuantite() + " - " + article.getPrix());
				ticketVente.getArticle().add(article);
				articleReduc.setRef(article.getRef());
				articleReduc.setQuantite(article.getQuantite());
				articleReduc.setPrix(article.getPrix() - 1);
				listReduc.add(articleReduc);
				LOGGER.info("********* Application de la réduction : " + articleReduc.getPrix());
				totalPrice = totalPrice + (article.getPrix() - 1);
			}
		}
		LOGGER.info("***** Montant total : " + totalPrice);
		LOGGER.info("***** Construction du nouveau ticket de vente");
		
		Date date = null;
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//(df.format(ClockClient.getClock().getHour()));
		
		String bl = "<ENTETE objet=\"facture-client\" source=\"crm\" date=\"" + date + "\"/>" +
						"<FACTURE refclient=\"" + ticketVente.getRefclient() + "\" montanttotal=\"" + totalPrice + "\" >";
						   for (int k = 0; k < listReduc.size(); k++)
						   {
							   bl = bl + "<ARTICLE refarticle=\"" + listReduc.get(k).getRef() + "\" quantite=\"" + listReduc.get(k).getQuantite() + "\" nvprix=\"" + listReduc.get(k).getPrix() + "\" />";
						   }
						bl = bl + "</FACTURE>";
		
		return bl;
	}
}
