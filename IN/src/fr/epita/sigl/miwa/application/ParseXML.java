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
import fr.epita.sigl.miwa.application.MDM.ProductsClientMDM;
import fr.epita.sigl.miwa.application.MDM.PromotionArticleMDM;

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
		// Parse du CRM
		else if (root.getName().equals("INFORMATIONS"))
			parseCRM();
		// Parse du MDM
		else if (root.getName().equals("PRODUCTS"))
			parseMDM();
	}
	
	public void parseMDM()
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
	
	public void parseCRM()
	{
		// Creation de l'objet correspondant
		PromotionClientCR promotionClient = new PromotionClientCR();
		
		// Récupération des informations du fichier XML
		Element solde = root.getChild("SOLDE");
		
		promotionClient.setSolde(Integer.parseInt(solde.getAttributeValue("restant")));
		Element promotions = root.getChild("PROMOTIONS");
		
		List<Element> listPromotions = promotions.getChildren("PROMOTION");
		for(Element e : listPromotions)
			promotionClient.getPromotions().add(new PromotionArticleCR(e.getAttributeValue("article"),
					e.getAttributeValue("fin"),
					Integer.parseInt(e.getAttributeValue("reduc"))));

		//promotionClient.print();
	}
	
	public void parseGC()
	{
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
}
