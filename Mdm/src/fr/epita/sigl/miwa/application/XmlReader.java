package fr.epita.sigl.miwa.application;

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


				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {

					if (qName.equalsIgnoreCase("ARTICLE")) {
						String ref = attributes.getValue("reference");
						Integer sellPrice = Integer.parseInt(attributes.getValue("prix_vente"));
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


				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {

					if (qName.equalsIgnoreCase("ARTICLE")) {
						String ref = attributes.getValue("reference");
						Integer sellPrice = Integer.parseInt(attributes.getValue("prix_vente"));
						dbHandler.updateProduct(ref, sellPrice);
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
