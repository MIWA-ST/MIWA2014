package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
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
				content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><COMMANDESFOURNISSEUR><COMMANDE><NUMERO>CV398719873</NUMERO><DATEBC>20130427</DATEBC><ARTICLES><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE><CATEGORIE>XXXX</CATEGORIE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE><CATEGORIE>XXXX</CATEGORIE></ARTICLE><ARTICLES></COMMANDE></COMMANDESFOURNISSEUR>";
				
				try
				{
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.GESTION_COMMERCIALE);
					LOGGER.info("Envoi du BL fournisseur à la GC effectué");
				}
				catch (AsyncMessageException e)
				{
					LOGGER.severe(e.getMessage());
				}
			}
			//Demande de réassort magasin >> livrer le BO
			else if (root.toLowerCase().equals("commande_internet"))
			{
				LOGGER.info("Commande Internet reçue de la Gestion Commerciale");	
				content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><commande_internet><commande><numero>XXXXXX</numero><refclient>REF</refclient><datebc>AAAAMMJJ</datebc><datebl>AAAAMMJJ</datebl><adresseClient>XXXX</adresse><nom>XXX</nom><prénom>XXX</prenom><articles><article><CATEGORIE>001</CATEGORIE><reference>REF</reference><quantite>XX</quantite></article></articles></commande></commande_internet>";
			
				try
				{
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.GESTION_COMMERCIALE);
					LOGGER.info("Livraison de la commande internet effectuée");
				}
				catch (AsyncMessageException e)
				{
					LOGGER.severe(e.getMessage());
				}
			}
			//Commande internet >> Renvoyer BL à la GC
			else if (root.toUpperCase().equals("REASSORTSBO"))
			{
				LOGGER.info("Demande de réassort reçue de la Gestion Commerciale");
				content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REASSORTSBO><REASSORT><NUMERO>5398719873</NUMERO><REFBO>201</REFBO><ADRESSEBO>66 coucou ca va</ADRESSEBO><TELBO>0133333333</TELBO><DATEBC>20130427</DATEBC><ARTICLES><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE><CATEGORIE>001</CATEGORIE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE><CATEGORIE>001</CATEGORIE></ARTICLE><ARTICLES></REASSORT></REASSORTSBO>";
				
				try
				{
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.BACK_OFFICE);
					LOGGER.info("Envoi BL réassort envoyé à la GC");
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
