package fr.epita.sigl.miwa.application;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;

import fr.epita.sigl.miwa.application.GC.DemandeNiveauStock;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticles;

// Parser des fichiers XML
public class ParseXML {
	private static Document document;
	private static Element root;
	private String filename;
	
	public ParseXML(String filename)
	{
		this.filename = filename;
	}
	
	@SuppressWarnings("deprecation")
	public void readXML()
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		File file = new File(filename);
		
		try {
			document = saxBuilder.build(file);
		} catch (Exception e) {
			System.out.println("Fichier introuvable : " + filename);
		}
		
		// Parse de la GC
		root = document.getRootElement();
		if (root.getName().equals("DEMANDENIVEAUDESTOCKINTERNET"))
			parseGC();
		else if (root.getName().equals("DEMANDENIVEAUDESTOCKINTERNET"))
			parseGC();
	}
	
	public void parseGC()
	{
		// Creation de l'objet correspondant
		DemandeNiveauStock demandeNiveauStock = new DemandeNiveauStock();
		
		// Récupération des informations du fichier XML
		demandeNiveauStock.setNumero(root.getChildText("NUMERO"));
		demandeNiveauStock.setDate(root.getChildText("DATE"));
		Element articles = root.getChild("ARTICLES");
		
		List<Element> listArticles = articles.getChildren("ARTICLE");
		for(Element e : listArticles)
			demandeNiveauStock.getArticles().add(new DemandeNiveauStockArticles(e.getChildText("REFERENCE"),
					Integer.parseInt(e.getChildText("QUANTITE"))));
		demandeNiveauStock.print();
	}
}
