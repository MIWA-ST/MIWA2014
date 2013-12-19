package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

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
		String root = "";
		String content = "";
		
		if (source == EApplication.GESTION_COMMERCIALE)
		{
			//prendre la première balise
			//root = ...

			//Bon de commandes fournisseur >> Renvoyer BL à la GC
			if (root.toUpperCase().equals("COMMANDESFOURNISSEUR"))
			{
				LOGGER.info("Commande Fournisseur reçue de la Gestion Commerciale");
				content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><LIVRAISONSCOMMANDEFOURNISSEUR><LIVRAISON><NUMERO>CV398719873</NUMERO><DATEBC>20130427</DATEBC><DATEBL>20130427</DATEBL><ARTICLES><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE><CATEGORIE>XXXX</CATEGORIE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE><CATEGORIE>XXXX</CATEGORIE></ARTICLE><ARTICLES></LIVRAISON></LIVRAISONCOMMANDEFOURNISSEUR>";
				
				try
				{
					//content = XMLManager.getInstance().getCommandeFournisseur(message);
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.GESTION_COMMERCIALE);	
					LOGGER.info("Envoi du BL fournisseur à la GC effectué");
				}
				catch (AsyncMessageException e)
				{
					LOGGER.severe(e.getMessage());
				}
			}
			//Commande internet >> Renvoyer BL à la GC
			else if (root.toLowerCase().equals("commande_internet"))
			{
				LOGGER.info("Commande Internet reçue de la Gestion Commerciale");	
				content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><EXPEDITIONCLIENT><LIVRAISON><NUMERO>CV398719873</NUMERO><DATEBC>20131225</DATEBC><DATEBL>20131225</DATEBL><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE><CATEGORIE>XXXXX</CATEGORIE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE><CATEGORIE>XXXXX</CATEGORIE></ARTICLE></LIVRAISON></EXPEDITIONCLIENT>";
			
				try
				{
					//content = XMLManager.getInstance().getCommandeInternet(message);
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.GESTION_COMMERCIALE);
					LOGGER.info("Livraison de la commande internet effectuée");
				}
				catch (AsyncMessageException e)
				{
					LOGGER.severe(e.getMessage());
				}
			}
			//Demande de réassort magasin >> livrer le BO
			else if (root.toUpperCase().equals("REASSORTSBO"))
			{
				LOGGER.info("Demande de réassort reçue de la Gestion Commerciale");
				content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><LIVRAISONS><LIVRAISON><NUMERO>CV398719873</NUMERO><REFMAGASIN>6876786</REFMAGASIN><DATEBC>20131225</DATEBC><DATEBL>20131225</DATEBL><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE></ARTICLE></LIVRAISON></LIVRAISONS>";
				
				try
				{
					//content = XMLManager.getInstance().getReassortBO(message);
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.BACK_OFFICE);
					
					LOGGER.info("Envoi réassort BO");
				}
				catch (AsyncMessageException e)
				{
					LOGGER.severe(e.getMessage());
				}
			}
			else
				LOGGER.info("Message inconnu reçu de la Gestion Commerciale");
		}
		else
		{
			LOGGER.severe("Message inconnu de " + source.getLongName() + " : ");
			LOGGER.severe(message);	
		}
	}

	@Override
	public void onFile(File file, EApplication source) {
		LOGGER.severe(source + " : " + file.getAbsolutePath());		
	}

}
