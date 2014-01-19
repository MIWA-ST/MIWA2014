package fr.epita.sigl.miwa.application;

import java.io.File;
import java.io.StringReader;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParser {

	/*SAXParserFactory factory = SAXParserFactory.newInstance();
	   SAXParser saxParser = factory.newSAXParser();

	   DefaultHandler handler = new DefaultHandler() {


	    public void startElement(String uri, String localName,String qName, 
	      Attributes attributes) throws SAXException {

	     if (qName.equalsIgnoreCase("ARTICLE")) {
	    	 
	      String nom = attributes.getValue("nomarticle");
	      String ref = attributes.getValue("refarticle");
	      String prix = attributes.getValue("prix");
	      String promotion = attributes.getValue("promotion");
	      
	      System.out.println("nom = " + nom);
			System.out.println("ref = " + ref);
			System.out.println("prix = " + prix);
			System.out.println("promo = " + promotion);
	      //Integer sellPrice = Integer.parseInt(attributes.getValue("prix_vente"));

	      int n = 1;
		try {
			n = Main.bdd.update("update produit set produit_prix = " + prix + ", produit_nom = '" + nom + "', produit_pourcentagepromo = " + promotion + "where produit_ref = " + ref);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			if (n == 0)
				try {
					Main.bdd.insert("insert into produit (produit_prix, produit_ref, produit_nom, produit_pourcentagepromo) values ('" + nom + "'," + ref + "," + prix + "," + promotion + ")");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	     }
	    }
	   };

	   saxParser.parse(xmlFile, handler);*/
	
	
	// fonction appelée quand le BO nous envoie un fichier
	// -> uniquement pour la liste des articles à réceptionner en début de
	// journée
	public static void ParseBOFile(File xmlFile) {
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
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

					System.out.println("nom = " + nom);
					System.out.println("ref = " + ref);
					System.out.println("prix = " + prix);
					System.out.println("promo = " + promotion);
					
					int n = Main.bdd.update("update produit set produit_prix = " + prix + ", produit_nom = '" + nom + "', produit_pourcentagepromo = " + promotion + "where produit_ref = " + ref);
					if (n == 0)
						Main.bdd.insert("insert into produit (produit_prix, produit_ref, produit_nom, produit_pourcentagepromo) values (" + prix + "," + ref + ",'" + nom + "'," + promotion + ")");
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
					
					System.out.println("nom = " + nom);
					System.out.println("ref = " + ref);
					System.out.println("prix = " + prix);
					System.out.println("promo = " + promotion);

					Main.bdd.insert("insert into produit (produit_prix, produit_ref, produit_nom, produit_pourcentagepromo) values ("
							+ prix
							+ ","
							+ ref
							+ ",'"
							+ nom
							+ "',"
							+ promotion
							+ ")");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
