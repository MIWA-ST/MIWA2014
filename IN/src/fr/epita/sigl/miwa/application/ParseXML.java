package fr.epita.sigl.miwa.application;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;

import fr.epita.sigl.miwa.application.CR.EnteteCRM;
import fr.epita.sigl.miwa.application.CR.PromotionArticleCR;
import fr.epita.sigl.miwa.application.CR.PromotionClientCR;
import fr.epita.sigl.miwa.application.CR.ReceptionMatriculeCR;
import fr.epita.sigl.miwa.application.GC.ArticleNiveauStockRecuGC;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockGC;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticlesGC;
import fr.epita.sigl.miwa.application.GC.NiveauStockGC;
import fr.epita.sigl.miwa.application.MDM.ArticleAVendreMDM;
import fr.epita.sigl.miwa.application.MDM.ProductsClientEnteteMDM;
import fr.epita.sigl.miwa.application.MDM.ProductsClientMDM;
import fr.epita.sigl.miwa.application.MDM.PromotionArticleMDM;

// Parser des fichiers XML
public class ParseXML {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	private Document document;
	private Element root;
	private String filename;
	public static enum TYPE_LANGUAGE {
		FICHIER,
		STRING,
		DOCUMENT,
	}
	
	public ParseXML()
	{
	}
	
	public ParseXML(String filename)
	{
		this.filename = filename;
	}

	
	/*
		typeFlux :
			0 -> Fichier
			1 -> String
			2 -> Document
	 */
	public Boolean readXML(String flux, ParseXML.TYPE_LANGUAGE typeFlux)
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		
		
		if (typeFlux == ParseXML.TYPE_LANGUAGE.FICHIER)
		{
			File file = new File(flux);
			try {
				document = saxBuilder.build(file);
				
			} catch (JDOMException | IOException e) {
				LOGGER.info("***** " + e.getMessage());
				return  false;
			}
		}
		else if (typeFlux == ParseXML.TYPE_LANGUAGE.STRING)
		{
			if (flux != null && flux.equals("Client introuvable"))
			{
				LOGGER.info("***** Parsing du fichier CRM : Client introuvable.");
				return true;
			}
			else if (flux != null && flux.equals("OK MAJ"))
			{
				LOGGER.info("***** CRM : Mise à jour effectuée avec succès.");
				return true;
			}
			else if (flux != null && flux.equals("KO MAJ"))
			{
				LOGGER.info("***** CRM : Une erreure s'est produite lors de la mise à jour.");
				return true;
			}
			File file = new File("temp.xml");
			
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				
				out.write(flux);
				out.close();
				
				document = saxBuilder.build(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.info("***** Erreur à la création du fichier depuis le flux : " + e.getMessage());
				return false;
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				LOGGER.info("***** Erreur à la lecture du fichier depuis le flux : " + e.getMessage());
				return false;
			}
		}
		else
		{
			LOGGER.info("***** Type de flux inconnu.");
			return false;
		}
		
		if (document != null)
		{
			root = document.getRootElement();
			return true;
		}
		return false;
	}

	public Boolean readXML(File file, ParseXML.TYPE_LANGUAGE typeFlux)
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		
		try {
			document = saxBuilder.build(file);
			
		} catch (JDOMException | IOException e) {
			LOGGER.info("***** " + e.getMessage());
			return  false;
		}
			
		if (document != null)
		{
			root = document.getRootElement();
			return true;
		}
		return false;
	}
	
	public Boolean readXML(org.w3c.dom.Document document, ParseXML.TYPE_LANGUAGE typeFlux)
	{
		if (typeFlux == ParseXML.TYPE_LANGUAGE.DOCUMENT)
		{
			DOMBuilder dom = new DOMBuilder();
			
			this.document = dom.build(document);
			root = this.document.getRootElement();
			return true;
		}
		return false;
	}
	
	public Boolean parseMDM()
	{
		LOGGER.info("***** Message reçu du MDM :\n");
		if (root == null)
			return false;
		// Vérification du premier objet
		if (!root.getName().equals("XML"))
		{
			LOGGER.info("***** Erreur lors du parsing du flux MDM : la balise <XML> n'existe pas.");
			return false;
		}
		
		// Creation de l'objet correspondant
		ProductsClientMDM productsClient = new ProductsClientMDM();
		
		// Récupération des informations du fichier XML
		
			// Récupération de l'entete
		Element e1 = root.getChild("ENTETE");
		if (e1 == null)
		{
			LOGGER.info("***** Erreur lors du parsing du flux MDM : la balise <ENTETE> n'existe pas.");
			return false;
		}
		productsClient.setEntete(new ProductsClientEnteteMDM(e1.getAttributeValue("objet"),
															e1.getAttributeValue("source"),
															e1.getAttributeValue("date")));
		
		Element articlesProductClient = root.getChild("ARTICLES");
		
		if (articlesProductClient == null)
		{
			LOGGER.info("***** Erreur lors du parsing du flux MDM : la balise <ARTICLES> n'existe pas.");
			return false;
		}
		
		List<Element> listProducts = articlesProductClient.getChildren("ARTICLE");
		// Element product = root.getChild("ARTICLE");
		for(Element e : listProducts)
		{
			ArticleAVendreMDM articleAVendre = new ArticleAVendreMDM();
			
			articleAVendre.setReference(e.getAttributeValue("reference"));
			articleAVendre.setEan(e.getAttributeValue("ean"));
			articleAVendre.setCategorie(e.getAttributeValue("categorie"));
			articleAVendre.setPrix_fournisseur(e.getAttributeValue("prix_fournisseur"));
			articleAVendre.setPrix_vente(e.getAttributeValue("prix_vente"));
			
			articleAVendre.setDescription(e.getChildText("DESCRIPTION"));
			
			Element promitions = e.getChild("PROMOTIONS");
			List<Element> listPromotions = promitions.getChildren("PROMOTION");

			for(Element elt : listPromotions)
				articleAVendre.getPromotions().add(new PromotionArticleMDM(articleAVendre.getReference(), elt.getAttributeValue("debut"),
						elt.getAttributeValue("fin"),
						Integer.parseInt(elt.getAttributeValue("pourcent"))));
			
			productsClient.getArticles().add(articleAVendre);
		}
		
//		LOGGER.info(productsClient.print_logger());
		if (productsClient != null)
			productsClient.addBDD();
		else
			return false;
		return true;
	}

	public Boolean parseCRM()
	{
		LOGGER.info("***** Message reçu du CRM :\n");
		// Récupération des informations du fichier XML
		if (root == null)
			return false;
		if (!root.getName().equals("ENTETE"))
		{
			LOGGER.info("***** Erreur lors du parsing du flux CRM : la balise <ENTETE> n'existe pas.");
			return false;
		}
		
		// Récupération de l'entete
		EnteteCRM entete = new EnteteCRM(root.getAttributeValue("objet"),
				root.getAttributeValue("source"),
				root.getAttributeValue("date"));
		
		if (entete.getObjet() == null || entete.getObjet().equals(""))
		{
			LOGGER.info("***** Erreur lors du parsing du flux CRM : l'attribut \"objet\" n'est pas correct ou n'existe pas.");
			return false;
		}
		if (entete.getSource() == null || entete.getSource().equals(""))
		{
			LOGGER.info("***** Erreur lors du parsing du flux CRM : l'attribut \"source\" n'est pas correct ou n'existe pas.");
			return false;
		}
		if (entete.getDate() == null)
		{
			LOGGER.info("***** Erreur lors du parsing du flux CRM : l'attribut \"date\" n'est pas correct ou n'existe pas.");
			return false;
		}
		
		if (entete.getObjet().equals("information-client"))
		{
			// Information Client
			PromotionClientCR promotionClient = new PromotionClientCR();
			promotionClient.setEntete(entete);
			
			Element informations = root.getChild("INFORMATIONS");
			if (informations == null)
			{
				LOGGER.info("***** Erreur lors du parsing du flux CRM : la balise <INFORMATIONS> n'existe pas.");
				return false;
			}
			
			Element client = informations.getChild("CLIENT");
			if (client == null)
			{
				LOGGER.info("***** Erreur lors du parsing du flux CRM : la balise <CLIENT> n'existe pas.");
				return false;
			}
			
			promotionClient.setMatricule(client.getAttributeValue("matricule"));
			
			Element promotions = informations.getChild("PROMOTIONS");
			if (promotions == null)
			{
				LOGGER.info("***** Erreur lors du parsing du flux CRM : la balise <PROMOTIONS> n'existe pas.");
				return false;
			}
			
			List<Element> listPromotions = promotions.getChildren("PROMOTION");
			for(Element e : listPromotions)
				promotionClient.getPromotions().add(new PromotionArticleCR(e.getAttributeValue("article"),
						e.getAttributeValue("fin"),
						e.getAttributeValue("reduc")));
			
			LOGGER.info(promotionClient.print_logger());
			return true;
		}
		else if (entete.getObjet().equals("matricule-client"))
		{
			// Matricule Client
			ReceptionMatriculeCR receptionMatricule = new ReceptionMatriculeCR();
			receptionMatricule.setEntete(entete);
			
			Element informations = root.getChild("INFORMATIONS");
			if (informations == null)
			{
				LOGGER.info("***** Erreur lors du parsing du flux CRM : la balise <INFORMATIONS> n'existe pas.");
				return false;
			}
			
			Element client = informations.getChild("CLIENT");
			if (client == null)
			{
				LOGGER.info("***** Erreur lors du parsing du flux CRM : la balise <CLIENT> n'existe pas.");
				return false;
			}
			
			receptionMatricule.setMatricule(client.getAttributeValue("matricule"));
			receptionMatricule.setNom(client.getAttributeValue("nom"));
			receptionMatricule.setPrenom(client.getAttributeValue("prenom"));
			
			// LOGGER.info(receptionMatricule.print_logger());
			
			receptionMatricule.updateBDD();
			return true;
		}
		else
		{
			LOGGER.info("***** Erreur lors du parsing du flux CRM : l'attribut \"objet\" n'est pas reconnu.");
			return false;
		}
	}
	
	public Boolean parseGC()
	{
		LOGGER.info("***** Message reçu de la GC :\n");
		if (root == null)
			return false;
		
		if (!root.getName().equals("DEMANDENIVEAUDESTOCKINTERNET"))
		{
			LOGGER.info("***** Erreur lors du parsing du flux GC : la balise <DEMANDENIVEAUDESTOCKINTERNET> n'existe pas.");
			return false;
		}
		
		// Creation de l'objet correspondant
		NiveauStockGC niveauStock = new NiveauStockGC();
		
		// Récupération des informations du fichier XML
		niveauStock.setNumero(root.getChildText("NUMERO"));
		niveauStock.setDate(root.getChildText("DATE"));
		Element articles = root.getChild("ARTICLES");
		
		List<Element> listArticles = articles.getChildren("ARTICLE");
		for(Element e : listArticles)
			niveauStock.getArticles().add(new ArticleNiveauStockRecuGC(e.getChildText("REFERENCE"), e.getChildText("QUANTITE")));
		
		niveauStock.MAJBDD();
		
		 // LOGGER.info(niveauStock.print_logger());
		 return true;
	}

	public  Document getDocument() {
		return document;
	}

	public  void setDocument(Document document) {
		this.document = document;
	}

	public  Element getRoot() {
		return root;
	}

	public  void setRoot(Element root) {
		this.root = root;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
