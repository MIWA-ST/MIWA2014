package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.epita.sigl.miwa.application.Articles;
import fr.epita.sigl.miwa.application.DemandeNiveauStock;
import fr.epita.sigl.miwa.application.DemandeReassort;
import fr.epita.sigl.miwa.application.JdbcConnection;
import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.Promotions;
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
	public void onMessage(String message, EApplication source) {
		try {
			String root = "";
			String content = "";

			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(message));

			Document doc = db.parse(is);
			root = doc.getFirstChild().getNodeName();
			
			if (source == EApplication.BACK_OFFICE) {
				if (root.toLowerCase().equals("REASSORT")) {
					LOGGER.info("BO envoi demande de reassort");
					
					//A faire envoyer à entrepot demande reassort => GOOD
					DemandeReassort demandereassort = XMLManager.getInstance().getdemandereassortfromBO(message, doc);
					
					//FIXME vérifier l'état des stocks et traiter une commande fournisseur ou pas
					//Commande fournisseur + envoi bon de commande fournisseur à l'entrepot => JE SAIS PAS QUOI METTRE EN PARAMETRE
					//content = XMLManager.getInstance().envoicommandefournisseurtoEntrepot(commande);
					//AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.ENTREPOT);
					//LOGGER.info("Envoi de la commande fournisseur à l'entrepot");
					
					content = XMLManager.getInstance().envoidemandereassorttoEntrepot(demandereassort);
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.ENTREPOT);
					LOGGER.info("Envoi de la demande de reassort à l'entrepot");
					
					
				}
				else if (root.toLowerCase().equals("DEMANDENIVEAUDESTOCK")) {
					
					XMLManager.getInstance().getniveauStockfromBO(message, doc);
					
					LOGGER.info("BO envoi niveau stock");
				}
			} else if (source == EApplication.ENTREPOT) {
				if (root.toLowerCase().equals("LIVRAISONSCOMMANDEFOURNISSEUR")) {
					XMLManager.getInstance().getbonlivraisonfromEntrepot(message, doc);
					
							
					//FIXME incrémenter les stocks
					
					LOGGER.info("Entrepot envoi bon de livraison fournisseur");
					
				}
				
				if (root.toLowerCase().equals("commande_internet")) {
					LOGGER.info("On envoie la commande internet à l'entrepot");
					
					
					content = XMLManager.getInstance().envoicommandeinternettoEntrepot(XMLManager.getInstance().getcommandeinternetfromInternet(message, doc));
					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.ENTREPOT);
					LOGGER.info("Envoi de la commande internet  à l'entrepot");				}
				
				else if (root.toLowerCase().equals("EXPEDITIONCLIENT")) {
					LOGGER.info("Entrepot envoi expedition client");
				}
			} else if (source == EApplication.INTERNET) {
				if (root.toLowerCase().equals("DEMANDENIVEAUDESTOCKINTERNET")) {
					LOGGER.info("On envoie les niveaux de stock à internet");
					
					JdbcConnection.getInstance().getConnection();
					DemandeNiveauStock demande = JdbcConnection.getInstance().envoiStock(dns);
					JdbcConnection.getInstance().closeConnection();
					
					content = XMLManager.getInstance().envoiniveaustocktoInternet(demande);
					
					AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.INTERNET);
					LOGGER.info("Envoi des stocks à internet");
				}
			} else if (source == EApplication.MDM) {
					//A faire envoyer au ref les prix des articles
						LOGGER.info("prix de vente des articles reçus par le référentiel");
						
						JdbcConnection.getInstance().getConnection();
						List<Articles> art = JdbcConnection.getInstance().envoiPrixArticle();
						JdbcConnection.getInstance().closeConnection();
						
						content = XMLManager.getInstance().envoiprixventetoRef(art);
						AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.MDM);	
						LOGGER.info("Envoi des prix de vente des articles au référentiel effectué");
					
					//Envoyer promotions au ref
						LOGGER.info("promotion des articles par le référentiel");
						
						JdbcConnection.getInstance().getConnection();
						List<Promotions> prom = JdbcConnection.getInstance().envoiPromotions();
						JdbcConnection.getInstance().closeConnection();
						
						content = XMLManager.getInstance().envoipromotoRef(prom);
						AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.MDM);	
						LOGGER.info("Envoi des promotions des articles au référentiel effectué");					
			}			
		} catch (AsyncMessageException | ParserConfigurationException
				| SAXException | IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}
	@Override
	public void onFile(File file, EApplication source) {
		LOGGER.severe(source + " : " + file.getAbsolutePath());
	}

}