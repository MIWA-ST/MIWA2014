package fr.epita.sigl.miwa.application;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlWriter {
	private String fileName;
	private DbHandler dbHandler;

	public XmlWriter(String fileName) {
		this.fileName = fileName;
		this.dbHandler = new DbHandler("jdbc:mysql://localhost/miwa", "root", "root");
	}

	public void generateFileForGC() {
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("XML");
			doc.appendChild(rootElement);

			Element articles = doc.createElement("ARTICLES");
			rootElement.appendChild(articles);

			for (Product p : dbHandler.getAllProducts()) {
				Element article = doc.createElement("ARTICLE");
				articles.appendChild(article);
				article.setAttribute("reference", p.getReference());
				article.setAttribute("prix_fournisseur", p.getBuyPrice());
				article.setAttribute("nb_min_commande", p.getNbMin());
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(this.fileName));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateFileForBI() {
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("XML");
			doc.appendChild(rootElement);
			
			Element header = doc.createElement("ENTETE");
			rootElement.appendChild(header);
			header.setAttribute("objet", "liste-article");
			header.setAttribute("source", "mdm");
			
			String format = "yyyy-MM-dd";
			java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(format); 
			java.util.Date date = new java.util.Date(); 
			header.setAttribute("date", formater.format(date));
			
			Element articles = doc.createElement("ARTICLES");
			rootElement.appendChild(articles);

			for (Product p : dbHandler.getAllProducts()) {
				Element article = doc.createElement("ARTICLE");
				articles.appendChild(article);
				article.setAttribute("reference", p.getReference());
				article.setAttribute("ean", p.getEAN());
				article.setAttribute("prix_fournisseur", p.getBuyPrice());
				article.setAttribute("prix_vente", p.getSellPrice());

				Element description = doc.createElement("DESCRIPTION");
				description.setTextContent(p.getDescription());
				article.appendChild(description);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(this.fileName));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
