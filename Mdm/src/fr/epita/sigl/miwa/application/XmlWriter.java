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

	public void generateFileForG() {
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


}
