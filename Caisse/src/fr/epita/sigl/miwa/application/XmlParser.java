package fr.epita.sigl.miwa.application;

import java.io.File;
import java.io.StringReader;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class XmlParser {
	private static final Logger LOGGER = Logger.getLogger(XmlParser.class
			.getName());

	// fonction appelée quand le BO nous envoie un fichier
	// -> uniquement pour la liste des articles à réceptionner en début de
	// journée
	public static void ParseBOFile(File xmlFile) {
		LOGGER.info("***** Caisse : réception de fichier : la liste des produits de la part du back-office");
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			//doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("ARTICLE");

			Main.bdd.insert("delete from produit");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String nom = eElement.getAttribute("nomarticle");
					String ref = eElement.getAttribute("refarticle");
					String prix = eElement.getAttribute("prix");
					String promotion = eElement.getAttribute("promotion");

					Main.bdd.insert("insert into produit (produit_prix, produit_ref, produit_nom, produit_pourcentagepromo) values ("
							+ prix
							+ ",'"
							+ ref
							+ "','"
							+ nom
							+ "',"
							+ promotion
							+ ")");

					LOGGER.info("***** Caisse : nom du produit ajouté -> "
							+ nom);
					LOGGER.info("***** Caisse : référence du produit ajouté -> "
							+ ref);
					LOGGER.info("***** Caisse : prix du produit ajouté -> "
							+ prix);
					LOGGER.info("***** Caisse : promotion sur le produit -> "
							+ promotion);
				}
			}
		} catch (Exception e) {
			LOGGER.info("***** Caisse : erreur lors de la réception de la liste des produits de la part du back-office, format attendu pas respecté");
			e.printStackTrace();
		}

	}

	// fonction appelée quand le BO nous envoie un message
	// -> uniquement pour la mise à jour éventuelle des prix d'un article
	public static void ParseBOString(String xmlFile) {
		LOGGER.info("***** Caisse : réception de message : mise à jour de produit(s) de la part du back-office");

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(
					xmlFile)));
			//doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("ARTICLE");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String nom = eElement.getAttribute("nomarticle");
					String ref = eElement.getAttribute("refarticle");
					String prix = eElement.getAttribute("prix");
					String promotion = eElement.getAttribute("promotion");

					int n = Main.bdd
							.update("update produit set produit_prix = " + prix
									+ ", produit_nom = '" + nom
									+ "', produit_pourcentagepromo = "
									+ promotion + "where produit_ref = '" + ref + "'");
					if (n == 0)
						Main.bdd.insert("insert into produit (produit_prix, produit_ref, produit_nom, produit_pourcentagepromo) values ("
								+ prix
								+ ",'"
								+ ref
								+ "','"
								+ nom
								+ "',"
								+ promotion + ")");

					LOGGER.info("***** Caisse : nom du produit ajouté ou mis à jour -> "
							+ nom);
					LOGGER.info("***** Caisse : référence du produit ajouté ou mis à jour -> "
							+ ref);
					LOGGER.info("***** Caisse : prix du produit ajouté ou mis à jour -> "
							+ prix);
					LOGGER.info("***** Caisse : promotion sur le produit ajouté ou mis à jour -> "
							+ promotion);
				}
			}
		} catch (Exception e) {
			LOGGER.info("***** Caisse : erreur lors de la réception du message des produits à mettre à jour de la part du back-office, format attendu pas respecté");
			e.printStackTrace();
		}

	}

	public static String getUpdatedPrice(String xmlFile) {
		LOGGER.info("***** Caisse : réception de message : mise à jour du prix total du ticket pour un client fidélisé, de la part du back-office");

		String montant = "";
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(
					xmlFile)));
			//doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("FACTURE");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					montant = eElement.getAttribute("montanttotal");
					String ref = eElement.getAttribute("refclient");

					LOGGER.info("***** Caisse : nouveau montant total -> "
							+ montant);
					LOGGER.info("***** Caisse : référence client -> " + ref);
				}
			}
		} catch (Exception e) {
			LOGGER.info("***** Caisse : erreur lors de la réception du message des produits à mettre à jour de la part du back-office, format attendu pas respecté");
			e.printStackTrace();
		}

		if (montant == "")
			LOGGER.info("***** Caisse : erreur, le message des produits à mettre à jour a été reçu, mais le retour du back-office est incorrect");

		return montant;

	}

	public static Set<Produit> getUpdatedProducts(String xmlFile) {
		LOGGER.info("***** Caisse : réception de message : mise à jour du prix total du ticket pour un client fidélisé, de la part du back-office");

		Set<Produit> updatedProducts = new HashSet<Produit>();

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(
					xmlFile)));
			//doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("ARTICLE");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String ref = eElement.getAttribute("refarticle");
					String prix = eElement.getAttribute("nvprix");
					String quantite = eElement.getAttribute("quantite");
					String nom = "";
					String promo = "";
					String id = "";
					String prevPrix = "";

					ResultSet rs = Main.bdd
							.select("select produit_id, produit_nom, produit_pourcentagepromo, produit_prix from produit where produit_ref = '"
									+ ref + "'");

					while (rs.next()) {
						nom = rs.getString("produit_nom");
						promo = rs.getString("produit_pourcentagepromo");
						id = rs.getString("produit_id");
						prevPrix = rs.getString("produit_prix");
					}
					
					LOGGER.info("***** Caisse : ref article -> " + ref);
					LOGGER.info("***** Caisse : quantité -> " + quantite);
					LOGGER.info("***** Caisse : ancien prix -> " + prevPrix);
					LOGGER.info("***** Caisse : nouveau prix -> " + prix);

					Produit niou = new Produit(id, prix, ref, nom, promo);
					niou.setQuantite(Integer.parseInt(quantite));
					updatedProducts.add(niou);
				}
			}
		} catch (Exception e) {
			LOGGER.info("***** Caisse : erreur lors de la réception de message de mise à jour du prix total du ticket pour un client fidélisé, format attendu pas respecté par le back office");
			e.printStackTrace();
		}

		return updatedProducts;
	}

}
