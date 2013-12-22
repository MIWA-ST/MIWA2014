package fr.epita.sigl.miwa.application.messaging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
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
					
					//A faire envoyer à entrepot demande reassort
					
					//Commande fournisseur + envoi bon de commande fournisseur à l'entrepot
					
				}
				else if (root.toLowerCase().equals("DEMANDENIVEAUDESTOCK")) {
					LOGGER.info("BO envoi niveau stock");
				}
			} else if (source == EApplication.ENTREPOT) {
				if (root.toLowerCase().equals("LIVRAISONSCOMMANDEFOURNISSEUR")) {
					LOGGER.info("Entrepot envoi bon de livraison fournisseur");
				}
				else if (root.toLowerCase().equals("EXPEDITIONCLIENT")) {
					LOGGER.info("Entrepot envoi expedition client");
				}
			} else if (source == EApplication.INTERNET) {
				if (root.toLowerCase().equals("DEMANDENIVEAUDESTOCKINTERNET")) {
					LOGGER.info("Internet demande les niveaux de stock");
					// A faire envoi niveau de stock
					content = "";

					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.INTERNET);
					LOGGER.info("Envoi des stocks à internet");
				}

			}else if (source == EApplication.MDM) {
				//if (root.toLowerCase().equals("ARTICLES")) {
					//LOGGER.info("Ref envoi prix fournisseurs");
					
					//A faire envoyer au ref les prix des articles
					if (root.toUpperCase().equals("PRIXVENTE"))
					{
						LOGGER.info("prix de vente des articles reçus par le référentiel");
						content = "<XML><PRIXVENTE></ARTICLES><ARTICLE reference=\"AU736827\" prix_vente=\"15\"/></ARTICLES></ARTICLES><ARTICLE reference=\"BZ758887\" prix_vente=\"80\"/></ARTICLES></PRIXVENTE></XML>";
						
						//content = XMLManager.getInstance().getCommandeFournisseur(message, doc);
						AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.MDM);	
						LOGGER.info("Envoi des prix des articles au référentiel effectué");
					}
					
					//Envoyer promotions au ref
					if (root.toUpperCase().equals("PROMOTION"))
					{
						LOGGER.info("promotion des articles par le référentiel");
						content = "<XML><PROMOTIONS><PROMOTION datedebut=\"2012-09-17\" datefin=\"2012-09-25\" promotion_pourcentage=\"23\"></ARTICLES><ARTICLE reference=\"FR145687\" /></ARTICLES></PROMOTION><PROMOTION datedebut=\"2013-11-01\" datefin=\"2013-12-30\" promotion_pourcentage=\"50\"></ARTICLES><ARTICLE reference=\"FD123456\" /></ARTICLES></ARTICLES><ARTICLE reference=\"GT789562\" /></ARTICLES></PROMOTION></PROMOTIONS></XML>";
						
						//content = XMLManager.getInstance().getCommandeFournisseur(message, doc);
						AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content, EApplication.MDM);	
						LOGGER.info("Envoi des promotions des articles au référentiel effectué");
					}
					
				//}
			
			}
		} catch (AsyncMessageException | ParserConfigurationException
				| SAXException | IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}

	// if (source == EApplication.GESTION_COMMERCIALE)
	// {
	// //
	// if (root.toUpperCase().equals("COMMANDESFOURNISSEUR"))
	// {
	// LOGGER.info("Commande Fournisseur reÃ§ue de la Gestion Commerciale");
	// content =
	// "<LIVRAISONSCOMMANDEFOURNISSEUR><LIVRAISON><NUMERO>CV398719873</NUMERO><DATEBC>20130427</DATEBC><DATEBL>20130427</DATEBL><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE><CATEGORIE>XXXX</CATEGORIE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE><CATEGORIE>XXXX</CATEGORIE></ARTICLE></LIVRAISON></LIVRAISONSCOMMANDEFOURNISSEUR>";
	//
	// //content = XMLManager.getInstance().getCommandeFournisseur(message,
	// doc);
	// AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content,
	// EApplication.GESTION_COMMERCIALE);
	// LOGGER.info("Envoi du BL fournisseur Ã  la GC effectuÃ©");
	// }
	// //Commande internet >> Renvoyer BL Ã  la GC
	// else if (root.toLowerCase().equals("commande_internet"))
	// {
	// LOGGER.info("Commande Internet reÃ§ue de la Gestion Commerciale");
	// content =
	// "<EXPEDITIONCLIENT><LIVRAISON><NUMERO>CV398719873</NUMERO><DATEBC>20131225</DATEBC><DATEBL>20131225</DATEBL><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE><CATEGORIE>XXXXX</CATEGORIE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE><CATEGORIE>XXXXX</CATEGORIE></ARTICLE></LIVRAISON></EXPEDITIONCLIENT>";
	//
	// //content = XMLManager.getInstance().getCommandeInternet(message, doc);
	// AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content,
	// EApplication.GESTION_COMMERCIALE);
	// LOGGER.info("Livraison de la commande internet effectuÃ©e");
	// }
	// //Demande de rÃ©assort magasin >> livrer le BO
	// else if (root.toUpperCase().equals("REASSORTSBO"))
	// {
	// LOGGER.info("Demande de rÃ©assort reÃ§ue de la Gestion Commerciale");
	// content =
	// "<LIVRAISONS><LIVRAISON><NUMERO>CV398719873</NUMERO><REFMAGASIN>6876786</REFMAGASIN><DATEBC>20131225</DATEBC><DATEBL>20131225</DATEBL><ARTICLE><REFERENCE>AU736827</REFERENCE><QUANTITE>265000</QUANTITE></ARTICLE><ARTICLE><REFERENCE>AU736823</REFERENCE><QUANTITE>12</QUANTITE></ARTICLE></LIVRAISON></LIVRAISONS>";
	//
	// //content = XMLManager.getInstance().getReassortBO(message, doc);
	// AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content,
	// EApplication.BACK_OFFICE);
	//
	// LOGGER.info("Envoi rÃ©assort BO");
	// }
	// else
	// LOGGER.info("Message inconnu reÃ§u de la Gestion Commerciale");
	// }
	// else
	// {
	// LOGGER.severe("Message inconnu de " + source.getLongName() + " : ");
	// }
	// }
	// catch (AsyncMessageException | ParserConfigurationException |
	// SAXException | IOException e)
	// {
	// LOGGER.severe(e.getMessage());
	// }
	// }

	@Override
	public void onFile(File file, EApplication source) {
		LOGGER.severe(source + " : " + file.getAbsolutePath());
	}

}