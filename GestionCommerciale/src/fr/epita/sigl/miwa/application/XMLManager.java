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

//import fr.epita.sigl.miwa.application.BDD.JdbcConnection;
//import fr.epita.sigl.miwa.application.bo.Article;
//import fr.epita.sigl.miwa.application.bo.CommandeInternet;
//import fr.epita.sigl.miwa.application.bo.LivraisonFournisseur;
//import fr.epita.sigl.miwa.application.bo.ReassortBO;
import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class XMLManager {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	private static XMLManager instance = null;

	public static XMLManager getInstance() {
		if (instance == null)
			instance = new XMLManager();

		return instance;
	}

	// public String getCommandeInternet(String message, Document doc)
	// throws AsyncMessageException {
	// CommandeInternet command = new CommandeInternet();
	//
	// command.setCommandNumber(doc.getElementsByTagName("numero").item(0)
	// .getTextContent());
	// command.setCustomerRef(doc.getElementsByTagName("refclient").item(0)
	// .getTextContent());
	// command.setCustomerLastname(doc.getElementsByTagName("nom").item(0)
	// .getTextContent());
	// command.setCustomerFirstname(doc.getElementsByTagName("prenom").item(0)
	// .getTextContent());
	// command.setCustomerAddress(doc.getElementsByTagName("adresseClient")
	// .item(0).getTextContent());
	// command.setDateBC(doc.getElementsByTagName("datebc").item(0)
	// .getTextContent());
	//
	// List<Article> articles = new ArrayList<Article>();
	// NodeList nList = doc.getElementsByTagName("article");
	// for (int temp = 0; temp < nList.getLength(); temp++) {
	// Article a = new Article();
	//
	// // Récupéraction du noeud à traiter
	// Node nNode = nList.item(temp);
	// // Conversion en element
	// Element eElement = (Element) nNode;
	//
	// a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0)
	// .getTextContent());
	// a.setReference(eElement.getElementsByTagName("reference").item(0)
	// .getTextContent());
	// a.setQuantity(eElement.getElementsByTagName("quantite").item(0)
	// .getTextContent());
	//
	// articles.add(a);
	// }
	// command.setArticles(articles);
	//
	// DateFormat df = new SimpleDateFormat("yyyyMMdd");
	// command.setDateBL(df.format(ClockClient.getClock().getHour()));
	//
	// // TODO sauvergarde en base
	// // JdbcConnection.getInstance().insertCommandeInternet(command);
	//
	// // // Construction du xml
	// // String bl = "<EXPEDITIONCLIENT>" + "<LIVRAISON>" + "<NUMERO>"
	// // + command.getCommandNumber() + "</NUMERO>" + "<DATEBC>"
	// // + command.getDateBC() + "</DATEBC>" + "<DATEBL>"
	// // + command.getDateBL() + "</DATEBL>";
	// //
	// // for (Article a : articles)
	// // bl += "<ARTICLE>" + "<REFERENCE>" + a.getReference()
	// // + "</REFERENCE>" + "<QUANTITE>" + a.getQuantity()
	// // + "</QUANTITE>" + "<CATEGORIE>" + a.getCategory()
	// // + "</CATEGORIE>" + "</ARTICLE>";
	// //
	// // bl += "</LIVRAISON></EXPEDITIONCLIENT>";
	// //
	// // return bl;
	// }

	public List<Articles> getprixfournisseurs(String message, Document doc)
			throws AsyncMessageException {
		LOGGER.severe("TOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO1");

		Node listarticles = doc.getElementsByTagName("ARTICLES").item(0);

		List<Articles> articles = new ArrayList<Articles>();
		NodeList nList = listarticles.getChildNodes();

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Articles a = new Articles();
			LOGGER.severe("TOAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			// Récupéraction du noeud à traiter
			Node nNode = nList.item(temp);
			// Conversion en element
			Element eElement = (Element) nNode;
			a.setRef_article(eElement.getAttribute("reference"));
			a.setPrix_fournisseur(eElement.getAttribute("prix_fournisseur"));
			float pv = Float.parseFloat(eElement
					.getAttribute("prix_fournisseur"))
					* Float.parseFloat("1.1");
			a.setPrix_vente(Float.toString(pv));
			articles.add(a);
			List<PromoFournisseur> promosf = new ArrayList<PromoFournisseur>();
			NodeList nList2 = doc.getElementsByTagName("PROMOTION");
			for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
				PromoFournisseur p = new PromoFournisseur();
				// Récupéraction du noeud à traiter
				Node nNode2 = nList2.item(temp2);
				// Conversion en element
				Element eElement2 = (Element) nNode2;
				p.setDateDebut(eElement2.getAttribute("debut"));
				p.setDateFin(eElement2.getAttribute("fin"));
				p.setPourcentage(eElement2.getAttribute("pourcent"));
				p.setMinquantite(eElement2.getAttribute("nb_min_promo"));
				p.setRef_article(a.getRef_article());
				promosf.add(p);
				LOGGER.severe("J4AI UN ARTICLE");
			}
		}
		for (Articles articles2 : articles) {
			JdbcConnection.getInstance().getConnection();
			LOGGER.severe(articles2.getRef_article());
			JdbcConnection.getInstance().insertArticle(articles2);
			JdbcConnection.getInstance().closeConnection();
		}
		return articles;
		// FIXME sauvegarder les pomo et les prix des articles
	}

	public DemandeReassort getconfirmationreassortfromBO(String message,
			Document doc) {
		DemandeReassort demand = new DemandeReassort();
		demand.setCommandNumber(doc.getElementsByTagName("NUMEROCOMMANDE")
				.item(0).getTextContent());
		demand.setDateBL(doc.getElementsByTagName("DATELIVRAISON").item(0)
				.getTextContent());
		demand.setTraite(doc.getElementsByTagName("STATUT").item(0)
				.getTextContent());

		List<Articles> articles = new ArrayList<Articles>();
		List<String> quantities = new ArrayList<String>();
		NodeList nList = doc.getElementsByTagName("ARTICLE");
		for (int temp2 = 0; temp2 < nList.getLength(); temp2++) {
			Node nNode = nList.item(temp2);
			Element eElement = (Element) nNode;
			Articles a = new Articles();
			a.setRef_article(eElement.getElementsByTagName("REFERENCE").item(0)
					.getTextContent());
			a.setCategory(eElement.getElementsByTagName("CATEGORIE").item(0)
					.getTextContent());
			articles.add(a);
			quantities.add(eElement.getElementsByTagName("QUANTITE").item(0)
					.getTextContent());
		}
		demand.setArticles(articles);
		demand.setQuantity(quantities);
		JdbcConnection.getInstance().getConnection();
		JdbcConnection.getInstance().insertDemandeReassort(demand);
		JdbcConnection.getInstance().closeConnection();
		return demand;
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
		demand.setQuantity(quantities);
		// FIXME SAVEBDD
		JdbcConnection.getInstance().getConnection();
		JdbcConnection.getInstance().insertDemandeReassort(demand);
		JdbcConnection.getInstance().closeConnection();
		return demand;
	}

	public DemandeNiveauStock getdemandeniveaustockfromInternet(String message,
			Document doc) throws AsyncMessageException {

		DemandeNiveauStock demande = new DemandeNiveauStock();
		demande.setCommandNumber(doc.getElementsByTagName("NUMERO").item(0)
				.getTextContent());
		demande.setDatedemand(doc.getElementsByTagName("DATE").item(0)
				.getTextContent());

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
		JdbcConnection.getInstance().getConnection();
		JdbcConnection.getInstance().insertDemandeNiveauStock(demande);
		JdbcConnection.getInstance().closeConnection();
		return demande;

	}

	public CommandeFournisseur getbonlivraisonfromEntrepot(String message,
			Document doc) {

		CommandeFournisseur commande = new CommandeFournisseur();
		commande.setNumero_commande(doc.getElementsByTagName("NUMERO").item(0)
				.getTextContent());
		commande.setBon_commande(doc.getElementsByTagName("DATEBC").item(0)
				.getTextContent());
		commande.setBon_livraion(doc.getElementsByTagName("DATEBL").item(0)
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
		commande.setArticles(articles);
		commande.setquantity(quantities);
		// FIXME SAVEBDD
		JdbcConnection.getInstance().getConnection();
		JdbcConnection.getInstance().insertCommandeFournisseur(commande);
		JdbcConnection.getInstance().closeConnection();
		return commande;
	}

	public CommandeInternet getexpeditionclientfromEntrepot(String message,
			Document doc) throws AsyncMessageException {
		CommandeInternet command = new CommandeInternet();
		command.setCommandNumber(doc.getElementsByTagName("NUMERO").item(0)
				.getTextContent());
		command.setDateBC(doc.getElementsByTagName("DATEBC").item(0)
				.getTextContent());
		command.setDateBL(doc.getElementsByTagName("DATEBL").item(0)
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
		command.setArticles(articles);
		command.setquantity(quantities);
		JdbcConnection.getInstance().getConnection();
		JdbcConnection.getInstance().insertCommandeInternet(command);
		JdbcConnection.getInstance().closeConnection();
		return command;
	}

	public CommandeInternet getcommandeinternetfromInternet(String message,
			Document doc) throws AsyncMessageException {

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
		JdbcConnection.getInstance().getConnection();
		JdbcConnection.getInstance().insertCommandeInternet(commande);
		JdbcConnection.getInstance().closeConnection();
		return commande;
	}

	public List<StockMagasin> getniveauStockfromBO(String message, Document doc)
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
		for (StockMagasin stockMagasin : stocks) {
			JdbcConnection.getInstance().getConnection();
			JdbcConnection.getInstance().insertStockMagasin(stockMagasin);
			JdbcConnection.getInstance().closeConnection();

		}
		return stocks;
		// FIXME sauvegarder chaque stock dans la bdd
	}

	// ENVOI !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	public String envoipromotoRef(List<Promotions> promos) {
		String xml = "<PROMOTIONS>";
		for (Promotions promotions : promos) {
			
			xml += "<PROMOTION datedebut=\"" + promotions.getBegin()
					+ "\" datefin=\"" + promotions.getEnd()
					+ "\" promotion_pourcentage=\""
					+ promotions.getPourcentage() + "\"><ARTICLES>"
					+ "<ARTICLE reference=\""
					+ promotions.getRef_article()
					+ "\" /></ARTICLES></PROMOTION>";
		}

		xml += "</PROMOTIONS>";
		return xml;

	}

	public String envoiprixventetoRef(List<Articles> articles) {
		String xml = "<PRIXVENTE><ARTICLES>";
		for (Articles article : articles) {
			xml += "<ARTICLE reference=\"" + article.getRef_article()
					+ "\" prix_vente=\"" + article.getPrix_vente() + "\"/>";
		}
		xml += "</ARTICLES></PRIXVENTE>";
		return xml;
	}

	public String envoiniveaustocktoInternet(DemandeNiveauStock demande) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String xml = "<DEMANDENIVEAUDESTOCKINTERNET><NUMERO>"
				+ demande.getCommandNumber() + "</NUMERO><DATE>"
				+ df.format(ClockClient.getClock().getHour())
				+ "</DATE><ARTICLES>";
		int i = 0;
		while (i < demande.getArticles().size()) {
			xml += "<ARTICLE><REFERENCE>" + demande.getArticles().get(i)
					+ "</REFERENCE>";
			xml += "<QUANTITE>" + demande.getQuantity().get(i)
					+ "</QUANTITE></ARTICLE>";
			i++;
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
				+ commande.getDateBL() + "</datebl>" + "<adresseClient>"
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
			i++;
		}
		xml += "</articles></commande></commande_internet>";

		return xml;

	}

	public String envoicommandefournisseurtoEntrepot(
			CommandeFournisseur commande) {

		String xml = "<COMMANDESFOURNISSEUR><COMMANDE><NUMERO>";
		xml += commande.getNumero_commande() + "</NUMERO><DATEBC>"
				+ commande.getBon_commande() + "</DATEBC><ARTICLES>";
		int i = 0;
		while (i < commande.getArticles().size()) {
			xml += "<ARTICLE><REFERENCE>"
					+ commande.getArticles().get(i).getRef_article()
					+ "</REFERENCE><QUANTITE>" + commande.getquantity().get(i)
					+ "</QUANTITE><CATEGORIE>"
					+ commande.getArticles().get(i).getCategory()
					+ "</CATEGORIE></ARTICLE>";
			i++;
		}
		xml += "</ARTICLES></COMMANDE></COMMANDESFOURNISSEUR>";
		return xml;
	}

	public String envoidemandereassorttoEntrepot(DemandeReassort demand) {
		String xml = "<REASSORTSBO><REASSORT><NUMERO>"
				+ demand.getCommandNumber() + "</NUMERO><REFBO>"
				+ demand.getBackOfficeRef() + "</REFBO><ADRESSEBO>"
				+ demand.getBackOfficeAddress() + "</ADRESSEBO><TELBO>"
				+ demand.getBackOfficePhone() + "</TELBO><DATEBC>"
				+ demand.getDateBC() + "</DATEBC><ARTICLES>";
		int i = 0;
		while (i < demand.getArticles().size()) {
			xml += "<ARTICLE><REFERENCE>"
					+ demand.getArticles().get(i).getRef_article()
					+ "</REFERENCE><QUANTITE>" + demand.getQuantity().get(i)
					+ "</QUANTITE><CATEGORIE>"
					+ demand.getArticles().get(i).getCategory()
					+ "</CATEGORIE></ARTICLE>";
			i++;
		}
		xml += "</ARTICLES></REASSORT></REASSORTSBO>";
		return xml;
	}

	public String envoidemandeniveaudestocktoBO(DemandeNiveauStock demande) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String xml = "<DEMANDENIVEAUDESTOCK><NUMERO>"
				+ demande.getCommandNumber() + "</NUMERO><REFMAGASIN>"
				+ demande.getRefbo() + "</REFMAGASIN><DATE>"
				+ df.format(ClockClient.getClock().getHour())
				+ "</DATE><ARTICLES>";
		int i = 0;
		while (i < demande.getArticles().size()) {
			xml += "<ARTICLE><REFERENCE>" + demande.getArticles().get(i)
					+ "</REFERENCE>";
			i++;
		}
		xml += "</ARTICLES></DEMANDENIVEAUDESTOCK>";

		return xml;

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
