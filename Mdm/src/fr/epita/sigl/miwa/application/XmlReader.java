package fr.epita.sigl.miwa.application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XmlReader {
	private String filename;
	private DbHandler dbHandler;

	public XmlReader(String filename) {
		this.filename = filename;
		this.dbHandler = new DbHandler("jdbc:mysql://localhost/miwa", "root", "root");
	}

	public void parseProducts() {
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {
				boolean firstline = true;

				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {

					if (qName.equalsIgnoreCase("ARTICLE")) {
						String ref = attributes.getValue("reference");
						Integer sellPrice = Integer.parseInt(attributes.getValue("prix_vente"));

						if (firstline) {
							final Logger LOGGER = Logger.getLogger(CsvParser.class.getName());
							LOGGER.severe("***** " + "Parsing du premier prix de la GC (Exemple) : Référence du produit=" + ref + " / Prix de vente du produit (TTC)=" + sellPrice);
							firstline = false;
						}
						dbHandler.updateProduct(ref, sellPrice);
					}
				}
			};

			saxParser.parse(this.filename, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parsePromotions() {
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {
				ArrayList<Product> productList = new ArrayList<>();
				Promotion promo = null;


				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("PROMOTION")) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						
						Date startDate = null;
						Date endDate = null;
						try {
							startDate = formatter.parse(attributes.getValue("datedebut"));
							endDate = formatter.parse(attributes.getValue("datefin"));
						} catch (ParseException e) {
							e.printStackTrace();
						}

						Integer rebate = Integer.parseInt(attributes.getValue("promotion_pourcentage"));
						promo = new Promotion(startDate, endDate, rebate);
					}

					if (qName.equalsIgnoreCase("ARTICLE")) {
						String ref = attributes.getValue("reference");
						Product p = new Product(ref);
						productList.add(p);
					}
				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {

					if (qName.equalsIgnoreCase("PROMOTION")) {
						dbHandler.createNewPromotion(promo, productList);
						productList = new ArrayList<>();
						promo = null;
					}

				}
			};

			saxParser.parse(this.filename, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseDelta() {
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {


				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {

					if (qName.equalsIgnoreCase("PRODUCT")) {
						String name = attributes.getValue("name");
						String description = attributes.getValue("description");
						Integer priceTTC = Integer.parseInt(attributes.getValue("priceTTC"));
						//dbHandler.updateProduct(ref, sellPrice);
					}

					if (qName.equalsIgnoreCase("DESCRIPTION")) {
						String long_desc = attributes.getValue("name");
						//dbHandler.updateProduct(ref, sellPrice);
					}
				}
			};

			saxParser.parse(this.filename, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}