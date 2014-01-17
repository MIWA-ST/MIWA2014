package fr.epita.sigl.miwa.application;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;

import fr.epita.sigl.miwa.application.CR.PromotionArticleCR;
import fr.epita.sigl.miwa.application.CR.PromotionClientCR;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockGC;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticlesGC;
import fr.epita.sigl.miwa.application.MDM.ArticleAVendreMDM;
import fr.epita.sigl.miwa.application.MDM.ProductsClientEnteteMDM;
import fr.epita.sigl.miwa.application.MDM.ProductsClientMDM;
import fr.epita.sigl.miwa.application.MDM.PromotionArticleMDM;

// Parser des fichiers XML
public class ParseXML {
	private Document document;
	private Element root;
	private String filename;
	
	public ParseXML()
	{
	}
	
	public ParseXML(String filename)
	{
		this.filename = filename;
	}
	
	public String createXML(String destination, String feedName)
	{
		if (destination.equals("BI"))
			createXMLBI(feedName);
		
		return null;
	}
	
	public String createXMLBI(String feedName)
	{
		if (feedName.equals("ventes détaillées"))
		{
			
		}
		
		return null;
	}
	
	public void readXML(String inputStream)
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		
		if (document == null)
		{
			if (inputStream == null || !inputStream.equals(""))
			{
				try {
					File file = new File(filename);
					document = saxBuilder.build(file);
				} catch (Exception e) {
					System.out.println("Fichier introuvable : " + filename);
				}
			} else
				try {
					document = saxBuilder.build(inputStream);
				} catch (JDOMException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		root = document.getRootElement();
		
		// Parse de la GC
		if (root.getName().equals("DEMANDENIVEAUDESTOCKINTERNET"))
			parseGC("");
		// Parse du CRM
		else if (root.getName().equals("INFORMATIONS"))
			parseCRM("");
		// Parse du MDM
		else if (root.getName().equals("PRODUCTS"))
			parseMDM("");
		else
			System.out.println("Parse error. File : " + filename);
	}

	public void readXML2(String inputStream)
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		
		if (document == null)
		{
			if (inputStream == null || !inputStream.equals(""))
			{
				try {
					File file = new File(filename);
					document = saxBuilder.build(file);
				} catch (Exception e) {
					System.out.println("Fichier introuvable : " + filename);
				}
			} else
				try {
					document = saxBuilder.build(inputStream);
				} catch (JDOMException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		root = document.getRootElement();
	}
	
	public void parseMDM(String stream)
	{
		if (stream.equals("stream"))
		{
			if (!root.getName().equals("XML"))
			{
				System.out.println("Parse error. File : " + filename);
				return;
			}
		}
		// Creation de l'objet correspondant
		ProductsClientMDM productsClient = new ProductsClientMDM();
		
		// Récupération des informations du fichier XML
		
		// Récupération de l'entete
		Element e1 = root.getChild("ENTETE");
		productsClient.setEntete(new ProductsClientEnteteMDM(e1.getAttributeValue("objet"),
															e1.getAttributeValue("source"),
															e1.getAttributeValue("date")));
		
		Element articlesProductClient = root.getChild("ARTICLES");
		
		List<Element> listProducts = articlesProductClient.getChildren("ARTICLE");
		// Element product = root.getChild("ARTICLE");
		for(Element e : listProducts)
		{
			ArticleAVendreMDM articleAVendre = new ArticleAVendreMDM();
			
			articleAVendre.setReference(e.getAttributeValue("reference"));
			articleAVendre.setEan(e.getAttributeValue("ean"));
			articleAVendre.setCategorie(e.getAttributeValue("categorie"));
			articleAVendre.setPrix_fournisseur(Integer.parseInt(e.getAttributeValue("prix_fournisseur")));
			articleAVendre.setPrix_vente(Integer.parseInt(e.getAttributeValue("prix_vente")));
			
			articleAVendre.setDescription(e.getChildText("DESCRIPTION"));
			
			Element promitions = e.getChild("PROMOTIONS");
			List<Element> listPromotions = promitions.getChildren("PROMOTION");

			for(Element elt : listPromotions)
				articleAVendre.getPromotions().add(new PromotionArticleMDM(elt.getAttributeValue("debut"),
						elt.getAttributeValue("fin"),
						Integer.parseInt(elt.getAttributeValue("pourcent"))));
			
			productsClient.getArticles().add(articleAVendre);
		}
		productsClient.print();
	}
	
/*	public void parseMDM()
	{
		// Creation de l'objet correspondant
		ProductsClientMDM productsClient = new ProductsClientMDM();
		
		// Récupération des informations du fichier XML
		
		List<Element> listProducts = root.getChildren("PRODUCT");
		// Element product = root.getChild("PRODUCT");
		for(Element e : listProducts)
		{
			ArticleAVendreMDM articleAVendre = new ArticleAVendreMDM();
			
			articleAVendre.setReference(e.getAttributeValue("reference"));
			articleAVendre.setEan(e.getAttributeValue("ean"));
			articleAVendre.setCategorie(e.getAttributeValue("categorie"));
			articleAVendre.setPrix_fournisseur(Integer.parseInt(e.getAttributeValue("prix_fournisseur")));
			articleAVendre.setPrix_vente(Integer.parseInt(e.getAttributeValue("prix_vente")));
			
			articleAVendre.setDescription(e.getChildText("DESCRIPTION"));
			
			Element promitions = e.getChild("PROMOTIONS");
			List<Element> listPromotions = promitions.getChildren("PROMOTION");

			for(Element elt : listPromotions)
				articleAVendre.getPromotions().add(new PromotionArticleMDM(elt.getAttributeValue("debut"),
						elt.getAttributeValue("fin"),
						Integer.parseInt(elt.getAttributeValue("percent"))));
			
			productsClient.getProducts().add(articleAVendre);
		}
		productsClient.print();
	}
	*/
	
	public void parseCRM(String stream)
	{
		if (stream.equals("stream"))
		{
			Element e = root.getChild("INFORMATIONS");
			if (e == null || e.getName().equals(""))
			{
				System.out.println("Parse error. File : " + filename);
				return;
			}
		}
		// Creation de l'objet correspondant
		PromotionClientCR promotionClient = new PromotionClientCR();
		
		// Récupération des informations du fichier XML
//		Element solde = root.getChild("SOLDE");
//		
//		promotionClient.setSolde(Integer.parseInt(solde.getAttributeValue("restant")));
		Element promotions = root.getChild("PROMOTIONS");
		
		List<Element> listPromotions = promotions.getChildren("PROMOTION");
		for(Element e : listPromotions)
			promotionClient.getPromotions().add(new PromotionArticleCR(e.getAttributeValue("article"),
					e.getAttributeValue("fin"),
					Integer.parseInt(e.getAttributeValue("reduc"))));

		//promotionClient.print();
	}
	
	public void parseGC(String stream)
	{
		if (stream.equals("stream"))
		{
			if (!root.getName().equals("DEMANDENIVEAUDESTOCKINTERNET"))
			{
				System.out.println("Parse error. File : " + filename);
				return;
			}
		}
		// Creation de l'objet correspondant
		DemandeNiveauStockGC demandeNiveauStock = new DemandeNiveauStockGC();
		
		// Récupération des informations du fichier XML
		demandeNiveauStock.setNumero(root.getChildText("NUMERO"));
		demandeNiveauStock.setDate(root.getChildText("DATE"));
		Element articles = root.getChild("ARTICLES");
		
		List<Element> listArticles = articles.getChildren("ARTICLE");
		for(Element e : listArticles)
			demandeNiveauStock.getArticles().add(new DemandeNiveauStockArticlesGC(e.getChildText("REFERENCE"),
					Integer.parseInt(e.getChildText("QUANTITE"))));
		// demandeNiveauStock.print();
	}

	public  Document getDocument() {
		return document;
	}

	public  void setDocument(Document document) {
		document = document;
	}

	public  Element getRoot() {
		return root;
	}

	public  void setRoot(Element root) {
		root = root;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
