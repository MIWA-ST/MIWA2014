package fr.epita.sigl.miwa.application;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class ReadXMLFile {

	// fonction appelée quand le BO nous envoie un fichier
	// -> uniquement pour la liste des articles à réceptionner en début de
	// journée
	public static void ParseBOFile(File xmlFile) {
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("ARTICLE");

			System.out.println("----------------------------");

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
							.update("update from produit set produit_prix = "
									+ prix + ", produit_nom = " + nom
									+ ", produit_pourcentagepromo = "
									+ promotion + "where produit_ref = " + ref);
					if (n == 0)
						Main.bdd.insert("insert into produit (produit_prix, produit_ref, produit_nom, produit_pourcentagepromo) values ("
								+ nom
								+ ","
								+ ref
								+ ","
								+ prix
								+ ","
								+ promotion + ")");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// fonction appelée quand le BO nous envoie un message
	// -> uniquement pour la mise à jour éventuelle des prix d'un article
	public static void ParseBOString(String xmlFile) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(
					xmlFile)));

			doc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("ARTICLE");

			System.out.println("----------------------------");

			Main.bdd.insert("delete from produit");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String nom = eElement.getAttribute("nomarticle");
					String ref = eElement.getAttribute("refarticle");
					String prix = eElement.getAttribute("prix");
					String promotion = eElement.getAttribute("promotion");

					Main.bdd.insert("insert into produit (produit_prix, produit_ref, produit_nom, produit_pourcentagepromo) values ("
							+ nom
							+ ","
							+ ref
							+ ","
							+ prix
							+ ","
							+ promotion
							+ ")");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
