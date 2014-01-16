package fr.epita.sigl.miwa.application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import fr.epita.sigl.miwa.application.BDD.JdbcConnection;
//import fr.epita.sigl.miwa.application.bo.Article;
//import fr.epita.sigl.miwa.application.bo.CommandeInternet;
//import fr.epita.sigl.miwa.application.bo.LivraisonFournisseur;
//import fr.epita.sigl.miwa.application.bo.ReassortBO;
import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class XMLManager {
	private static XMLManager instance = null;

	public static XMLManager getInstance() {
		if (instance == null)
			instance = new XMLManager();

		return instance;
	}

	public String getCommandeInternet(String message, Document doc)
			throws AsyncMessageException {
		CommandeInternet command = new CommandeInternet();

		command.setCommandNumber(doc.getElementsByTagName("numero").item(0)
				.getTextContent());
		command.setCustomerRef(doc.getElementsByTagName("refclient").item(0)
				.getTextContent());
		command.setCustomerLastname(doc.getElementsByTagName("nom").item(0)
				.getTextContent());
		command.setCustomerFirstname(doc.getElementsByTagName("prenom").item(0)
				.getTextContent());
		command.setCustomerAddress(doc.getElementsByTagName("adresseClient")
				.item(0).getTextContent());
		command.setDateBC(doc.getElementsByTagName("datebc").item(0)
				.getTextContent());

		List<Article> articles = new ArrayList<Article>();
		NodeList nList = doc.getElementsByTagName("article");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Article a = new Article();

			// Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			// Conversion en element
			Element eElement = (Element) nNode;

			a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0)
					.getTextContent());
			a.setReference(eElement.getElementsByTagName("reference").item(0)
					.getTextContent());
			a.setQuantity(eElement.getElementsByTagName("quantite").item(0)
					.getTextContent());

			articles.add(a);
		}
		command.setArticles(articles);

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		command.setDateBL(df.format(ClockClient.getClock().getHour()));

		// TODO sauvergarde en base
		// JdbcConnection.getInstance().insertCommandeInternet(command);

		// Construction du xml
		String bl = "<EXPEDITIONCLIENT>" + "<LIVRAISON>" + "<NUMERO>"
				+ command.getCommandNumber() + "</NUMERO>" + "<DATEBC>"
				+ command.getDateBC() + "</DATEBC>" + "<DATEBL>"
				+ command.getDateBL() + "</DATEBL>";

		for (Article a : articles)
			bl += "<ARTICLE>" + "<REFERENCE>" + a.getReference()
					+ "</REFERENCE>" + "<QUANTITE>" + a.getQuantity()
					+ "</QUANTITE>" + "<CATEGORIE>" + a.getCategory()
					+ "</CATEGORIE>" + "</ARTICLE>";

		bl += "</LIVRAISON></EXPEDITIONCLIENT>";

		return bl;
	}

	public List<Articles> getprixfournisseurs(String message, Document doc)
			throws AsyncMessageException {
		List<Articles> articles = new ArrayList<Articles>();
		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Articles a = new Articles();

			// Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			// Conversion en element
			Element eElement = (Element) nNode;
			a.setRef_article(eElement.getAttribute("reference"));
			a.setPrix_fournisseur(eElement.getAttribute("prix_fournisseur"));
			
			List<PromoFournisseur> promosf = new ArrayList<PromoFournisseur>();
			NodeList nList2 = doc.getElementsByTagName("PROMOTION");
			for (int temp2 = 0; temp2 < nList.getLength(); temp2++) {
				PromoFournisseur p = new PromoFournisseur();
				/ Récupéraction du noeud à traiter
				Node nNode2 = nList2.item(temp2);
				// Conversion en element
				Element eElement2 = (Element) nNode2;
				p.setDateDebut(eElement2.getAttribute("debut"));
				p.setDateFin(eElement2.getAttribute("fin"));
				p.setPourcentage(eElement2.getAttribute("pourcent"));
				p.setMinquantite(eElement2.getAttribute("nb_min_promo"));
				p.setRef_article(a.getRef_article());
				promosf.add(p);
			}
		}
		
		// FIXME sauvegarder les pomo et les prix des articles
	}

	public DemandeReassort getdemandereassortfromBO(String message, Document doc)
			throws AsyncMessageException {
		DemandeReassort demand = new DemandeReassort();

		demand.setCommandNumber(doc.getElementsByTagName("NUMERO").item(0)
				.getTextContent());
		demand.setBackOfficeRef(doc.getElementsByTagName("REFBO").item(0)
				.getTextContent());
		demand.setBackOfficeAddress(doc.getElementsByTagName("ADRESSEBO")
				.item(0).getTextContent());
		demand.setBackOfficePhone(doc.getElementsByTagName("TELBO").item(0)
				.getTextContent());
		demand.setDateBC(doc.getElementsByTagName("DATEBC").item(0)
				.getTextContent());

		List<Articles> articles = new ArrayList<Articles>();
		List<String> quantities = new ArrayList<String>();
		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Articles a = new Articles();

			// Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			// Conversion en element
			Element eElement = (Element) nNode;

			a.setRef_article(eElement.getElementsByTagName("REFERENCE").item(0)
					.getTextContent());

			quantities.add(eElement.getElementsByTagName("QUANTITE").item(0)
					.getTextContent());
			a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0)
					.getTextContent());
			articles.add(a);
		}
		demand.setArticles(articles);
		demand.setQuatity(quantities);
		// FIXME SAVEBDD
		return demand;
	}

	public void getdemandeniveaustockfromInternet(String message,
			Document doc) throws AsyncMessageException {
		
		DemandeNiveauStock demande = new DemandeNiveauStock();
		demande.setCommandNumber(doc.getElementsByTagName("NUMERO").item(0).getTextContent());
		demande.setDatedemand(doc.getElementsByTagName("DATE").item(0).getTextContent());
	
		List<Articles> articles = new ArrayList<Articles>();
		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Articles a = new Articles();
			// Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			// Conversion en element
			Element eElement = (Element) nNode;
			a.setRef_article(eElement.getElementsByTagName("REFERENCE").item(0)
					.getTextContent());
			articles.add(a);
		}
		demande.setArticles(articles);
		
		//FIXME SAVE BDD

	}

	public void getbonlivraisonfromEntrepot(String message, Document doc)
			throws AsyncMessageException, DOMException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		CommandeFournisseur commande = new CommandeFournisseur();
		commande.setNumero_commande(doc.getElementsByTagName("NUMERO").item(0)
				.getTextContent());
		commande.setBon_commande(df.parse(doc.getElementsByTagName("DATEBC")
				.item(0).getTextContent()));
		commande.setBon_livraion(df.parse(doc.getElementsByTagName("DATEBL")
				.item(0).getTextContent()));

		List<Articles> articles = new ArrayList<Articles>();
		List<String> quantities = new ArrayList<String>();
		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Articles a = new Articles();

			// Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			// Conversion en element
			Element eElement = (Element) nNode;

			a.setRef_article(eElement.getElementsByTagName("REFERENCE").item(0)
					.getTextContent());

			quantities.add(eElement.getElementsByTagName("QUANTITE").item(0)
					.getTextContent());
			a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0)
					.getTextContent());
			articles.add(a);
		}
		commande.setArticles(articles);
		commande.setquantity(quantities);
		// FIXME SAVEBDD
	}

	public String getexpeditionclientfromEntrepot(String message, Document doc)
			throws AsyncMessageException {

	}

	public String getcommandeinternetfromInternet(String message, Document doc)
			throws AsyncMessageException {

		CommandeInternet commande = new CommandeInternet();

		commande.setCommandNumber(doc.getElementsByTagName("numero").item(0)
				.getTextContent());
		commande.setCustomerRef(doc.getElementsByTagName("refclient").item(0)
				.getTextContent());
		commande.setDateBC(doc.getElementsByTagName("datebc").item(0)
				.getTextContent());
		commande.setDateBL(doc.getElementsByTagName("datebl").item(0)
				.getTextContent());
		commande.setCustomerAddress(doc.getElementsByTagName("adresseClient")
				.item(0).getTextContent());
		commande.setCustomerLastname(doc.getElementsByTagName("nom").item(0)
				.getTextContent());
		commande.setCustomerFirstname(doc.getElementsByTagName("prenom")
				.item(0).getTextContent());

		List<Articles> articles = new ArrayList<Articles>();
		List<String> quantities = new ArrayList<String>();
		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Articles a = new Articles();

			// Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			// Conversion en element
			Element eElement = (Element) nNode;

			a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0)
					.getTextContent());
			a.setRef_article(eElement.getElementsByTagName("REFERENCE").item(0)
					.getTextContent());

			quantities.add(eElement.getElementsByTagName("QUANTITE").item(0)
					.getTextContent());

			articles.add(a);
		}
		commande.setArticles(articles);
		commande.setquantity(quantities);
		// FIXME SAVEBDD

	}

	public void getniveauStockfromBO(String message, Document doc)
			throws AsyncMessageException {
		List<StockMagasin> stocks = new ArrayList<StockMagasin>();
		String idmagasin = doc.getElementsByTagName("REFMAGASIN").item(0)
				.getTextContent();

		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			StockMagasin s = new StockMagasin();
			Articles a = new Articles();

			// Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			// Conversion en element
			Element eElement = (Element) nNode;
			a.setRef_article(eElement.getElementsByTagName("REFERENCE").item(0)
					.getTextContent());
			a.setStock_max_mag(eElement.getElementsByTagName("CAPACITE")
					.item(0).getTextContent());
			s.setArticle(a);
			s.setQuantity(eElement.getElementsByTagName("QUANTITE").item(0)
					.getTextContent());
			s.setIdmag(idmagasin);
			stocks.add(s);
		}
		// FIXME sauvegarder chaque stock dans la bdd
	}

	// ENVOI !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	public String envoipromotoRef(List<Promotions> promos) {
	 String xml = "<PROMOTIONS>";
	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	 for (Promotions promotions : promos) {
		 xml += "<PROMOTION datedebut=\"" + df.format(promotions.getBegin()) + "\" datefin=\"" + df.format(promotions.getEnd())
				 + "\" promotion_pourcentage=\"" + promotions.getPourcentage() + "\"></ARTICLES>"+
				"<ARTICLE reference=\"" +promotions.getArticle().getRef_article() + "\" /></ARTICLES></PROMOTION>";
				}
	 
	 xml += "</PROMOTIONS>";
	 return xml;
			 
	}

	public String envoiprixventetoRef(List<Articles> articles) {
		String xml = "<PRIXVENTE></ARTICLES>";
		for (Articles article : articles) {
			xml += "<ARTICLE reference=\"" +article.getRef_article() + 
					"\" prix_vente=\"" + article.getPrix_vente() + "\"/>";	
		}
		xml += "</ARTICLES></PRIXVENTE>";
		return xml;
	}

	public String envoiniveaustocktoInternet(DemandeNiveauStock demande) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String xml = "<DEMANDENIVEAUDESTOCKINTERNET><NUMERO>" +demande.getCommandNumber()
				+ "</NUMERO><DATE>" 
				+ df.format(ClockClient.getClock().getHour()) + "</DATE><ARTICLES>";
		int i = 0;
		while (i< demande.getArticles().size())
		{
			xml += "<ARTICLE><REFERENCE>" + demande.getArticles().get(i)  + "</REFERENCE>";
			xml += "<QUANTITE>" + demande.getQuantity().get(i) + "</QUANTITE></ARTICLE>";
					
		}
		xml += "</ARTICLES></DEMANDENIVEAUDESTOCKINTERNET>";
		
		return xml;
		
	}

	public String envoicommandeinternettoEntrepot(CommandeInternet commande) {
		int i = 0;
		String xml = "<commande_internet>" + "<commande>" + "<numero>"
				+ commande.getCommandNumber() + "</numero>" + "<refclient>"
				+ commande.getCustomerRef() + "</refclient>" + "<datebc>"
				+ commande.getDateBC() + "</datebc>" + "<datebl>"
				+ commande.getDateBL() + "</datebl>" + "<adresseClient"
				+ commande.getCustomerAddress() + "</adresseClient>" + "<nom>"
				+ commande.getCustomerLastname() + "</nom>" + "<prenom>"
				+ commande.getCustomerFirstname() + "</prenom>" + "<articles>";

		while (i < commande.getArticles().size()) {
			xml += "<article>" + "<CATEGORIE>"
					+ commande.getArticles().get(i).getCategory()
					+ "</CATEGORIE>" + "<reference>"
					+ commande.getArticles().get(i).getRef_article()
					+ "</reference>" + "<quantite>"
					+ commande.getquantity().get(i) + "</quantite>"
					+ "</article>";
		}
		xml += "</articles></commande></commande_internet>";

		return xml;

	}

	public String envoicommandefournisseurtoEntrepot(
			CommandeFournisseur commande) {
		return null;
	}

	public String envoidemandereassorttoEntrepot(DemandeReassort demand) {
		return null;
	}

	public String envoidemandeniveaudestocktoBO(DemandeNiveauStock demande) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String xml = "<DEMANDENIVEAUDESTOCK><NUMERO>" +demande.getCommandNumber()
				+ "</NUMERO><REFMAGASIN>" + demande.getRefbo() + "</REFMAGASIN><DATE>" 
				+ df.format(ClockClient.getClock().getHour()) + "</DATE><ARTICLES>";
		int i = 0;
		while (i< demande.getArticles().size())
		{
			xml += "<ARTICLE><REFERENCE>" + demande.getArticles().get(i)  + "</REFERENCE>";
					
		}
		xml += "</ARTICLES></DEMANDENIVEAUDESTOCK>";
		
		return xml;
		
		return null;
	}

	public String envoiStockToBI(StockEntrepot entrepot, StockMagasin magasin) {
		return null;
	}

	// public String getCommandeFournisseur(String message, Document doc) throws
	// AsyncMessageException
	// {
	// LivraisonFournisseur command = new LivraisonFournisseur();
	//
	// command.setCommandNumber(doc.getElementsByTagName("NUMERO").item(0).getTextContent());
	// command.setDateBC(doc.getElementsByTagName("DATEBC").item(0).getTextContent());
	//
	// List<Article> articles = new ArrayList<Article>();
	// NodeList nList = doc.getElementsByTagName("ARTICLE");
	// for (int temp = 0; temp < nList.getLength(); temp++)
	// {
	// Article a = new Article();
	//
	// //Récupéraction du noeud à traiter
	// Node nNode = nList.item(temp);
	// //Conversion en element
	// Element eElement = (Element) nNode;
	//
	// a.setReference(eElement.getElementsByTagName("REFERENCE").item(0).getTextContent());
	// a.setQuantity(eElement.getElementsByTagName("QUANTITE").item(0).getTextContent());
	// a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0).getTextContent());
	//
	// articles.add(a);
	// }
	// command.setArticles(articles);
	//
	// DateFormat df = new SimpleDateFormat("yyyyMMdd");
	// command.setDateBL(df.format(ClockClient.getClock().getHour()));
	//
	// //TODO sauvergarde en base
	// //JdbcConnection.getInstance().insertLivraisonFournisseur(command);
	//
	// //Construction du xml
	// String bl = "<LIVRAISONSCOMMANDEFOURNISSEUR>"
	// + "<LIVRAISON>"
	// + "<NUMERO>" + command.getCommandNumber() + "</NUMERO>"
	// + "<DATEBC>" + command.getDateBC() + "</DATEBC>"
	// + "<DATEBL>" + command.getDateBL() + "</DATEBL>";
	//
	// for (Article a : articles)
	// bl += "<ARTICLE>"
	// + "<REFERENCE>" + a.getReference() + "</REFERENCE>"
	// + "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
	// + "<CATEGORIE>" + a.getCategory() + "</CATEGORIE>"
	// + "</ARTICLE>";
	//
	// bl+= "</LIVRAISON></LIVRAISONSCOMMANDEFOURNISSEUR>";
	//
	// return bl;
	// }
	//
	// public String getReassortBO(String message, Document doc) throws
	// AsyncMessageException
	// {
	// ReassortBO command = new ReassortBO();
	//
	// command.setCommandNumber(doc.getElementsByTagName("NUMERO").item(0).getTextContent());
	// command.setBackOfficeRef(doc.getElementsByTagName("REFBO").item(0).getTextContent());
	// command.setBackOfficeAddress(doc.getElementsByTagName("ADRESSEBO").item(0).getTextContent());
	// command.setBackOfficePhone(doc.getElementsByTagName("TELBO").item(0).getTextContent());
	// command.setDateBC(doc.getElementsByTagName("DATEBC").item(0).getTextContent());
	//
	// List<Article> articles = new ArrayList<Article>();
	// NodeList nList = doc.getElementsByTagName("ARTICLE");
	// for (int temp = 0; temp < nList.getLength(); temp++)
	// {
	// Article a = new Article();
	//
	// //Récupéraction du noeud à traiter
	// Node nNode = nList.item(temp);
	// //Conversion en element
	// Element eElement = (Element) nNode;
	//
	// a.setReference(eElement.getElementsByTagName("REFERENCE").item(0).getTextContent());
	// a.setQuantity(eElement.getElementsByTagName("QUANTITE").item(0).getTextContent());
	// a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0).getTextContent());
	//
	// articles.add(a);
	// }
	// command.setArticles(articles);
	//
	// DateFormat df = new SimpleDateFormat("yyyyMMdd");
	// command.setDateBL(df.format(ClockClient.getClock().getHour()));
	//
	// //TODO sauvergarde en base
	// //JdbcConnection.getInstance().insertReassortBO(command);
	//
	// //Construction du xml
	// String bl = "<LIVRAISONS>"
	// + "<LIVRAISON>"
	// + "<NUMERO>" + command.getCommandNumber() + "</NUMERO>"
	// + "<REFMAGASIN>"+ command.getBackOfficeRef() + "</REFMAGASIN>"
	// + "<DATEBC>" + command.getDateBC() + "</DATEBC>"
	// + "<DATEBL>" + command.getDateBL() + "</DATEBL>";
	//
	// for (Article a : articles)
	// bl += "<ARTICLE>"
	// + "<REFERENCE>" + a.getReference() + "</REFERENCE>"
	// + "<QUANTITE>" + a.getQuantity() + "</QUANTITE>"
	// + "</ARTICLE>";
	//
	// bl += "</LIVRAISON></LIVRAISONS>";
	//
	// return bl;
	// }
}
