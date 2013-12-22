package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.XMLManager;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	@Override
	public void onException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String message, EApplication source)
	{
		try
		{
			String root = "";
			String content = "";
			
			if (source == EApplication.GESTION_COMMERCIALE)
			{	
			    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			    InputSource is = new InputSource();
			    is.setCharacterStream(new StringReader(message));
		
			    Document doc = db.parse(is);
			    root = doc.getFirstChild().getNodeName();
			    
				//Bon de commandes fournisseur >> Renvoyer BL à la GC
				if (root.toUpperCase().equals("COMMANDESFOURNISSEUR"))
				{
					LOGGER.info("Commande Fournisseur reçue de la Gestion Commerciale");
					//content = "<LIVRAISONSCOMMANDEFOURNISSEUR><LIVRAISON><NUMERO>CV398719873</NUMERO><DATEBC>20130427</DATEBC><DATEBL>20130427</DATEBL><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE><CATEGORIE>XXXX</CATEGORIE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE><CATEGORIE>XXXX</CATEGORIE></ARTICLE></LIVRAISON></LIVRAISONSCOMMANDEFOURNISSEUR>";
					
					content = XMLManager.getInstance().getCommandeFournisseur(message, doc);
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.GESTION_COMMERCIALE);	
					LOGGER.info("Envoi du BL fournisseur à la GC effectué");
				}
				//Commande internet >> Renvoyer BL à la GC
				else if (root.toLowerCase().equals("commande_internet"))
				{
					LOGGER.info("Commande Internet reçue de la Gestion Commerciale");	
					//content = "<EXPEDITIONCLIENT><LIVRAISON><NUMERO>CV398719873</NUMERO><DATEBC>20131225</DATEBC><DATEBL>20131225</DATEBL><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE><CATEGORIE>XXXXX</CATEGORIE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE><CATEGORIE>XXXXX</CATEGORIE></ARTICLE></LIVRAISON></EXPEDITIONCLIENT>";
				
					content = XMLManager.getInstance().getCommandeInternet(message, doc);
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.GESTION_COMMERCIALE);
					LOGGER.info("Livraison de la commande internet effectuée");
				}
				//Demande de réassort magasin >> livrer le BO
				else if (root.toUpperCase().equals("REASSORTSBO"))
				{
					LOGGER.info("Demande de réassort reçue de la Gestion Commerciale");
					//content = "<LIVRAISONS><LIVRAISON><NUMERO>CV398719873</NUMERO><REFMAGASIN>6876786</REFMAGASIN><DATEBC>20131225</DATEBC><DATEBL>20131225</DATEBL><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE></ARTICLE></LIVRAISON></LIVRAISONS>";
					
					content = XMLManager.getInstance().getReassortBO(message, doc);
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.BACK_OFFICE);
					
					LOGGER.info("Envoi réassort BO");
				}
				else
					LOGGER.info("Message inconnu reçu de la Gestion Commerciale");
			}
			else
			{
				LOGGER.severe("Message inconnu de " + source.getLongName() + " : ");
			}
		}
		catch (AsyncMessageException | ParserConfigurationException | SAXException | IOException e)
		{
			LOGGER.severe(e.getMessage());
		}
	}

	@Override
	public void onFile(File file, EApplication source) {
		LOGGER.severe(source + " : " + file.getAbsolutePath());		
	}

}
