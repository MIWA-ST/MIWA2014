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
import fr.epita.sigl.miwa.application.CommandeFournisseur;
import fr.epita.sigl.miwa.application.CommandeInternet;
import fr.epita.sigl.miwa.application.DemandeNiveauStock;
import fr.epita.sigl.miwa.application.DemandeReassort;
import fr.epita.sigl.miwa.application.JdbcConnection;
import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.Promotions;
import fr.epita.sigl.miwa.application.StockMagasin;
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

			LOGGER.info("***** Message reçu par GC");

			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(message));

			Document doc = db.parse(is);
			root = doc.getFirstChild().getNodeName();

			if (source == EApplication.BACK_OFFICE) {
				if (root.toLowerCase().equals("reassort")) {
					LOGGER.severe("*****: Reception demande de reassort depuis BO");

					// A faire envoyer à entrepot demande reassort => GOOD
					DemandeReassort demandereassort = XMLManager.getInstance()
							.getdemandereassortfromBO(message, doc);
					LOGGER.severe("*****: Demande de réassort recu depuis BO :"
							+ demandereassort.getCommandNumber());
					for (Articles a : demandereassort.getArticles()) {
						LOGGER.info("*****: Article : " + a.getRef_article());
					}
					// FIXME vérifier l'état des stocks et traiter une commande
					// fournisseur ou pas
					// Commande fournisseur + envoi bon de commande fournisseur
					// à l'entrepot => JE SAIS PAS QUOI METTRE EN PARAMETRE
					// content =
					// XMLManager.getInstance().envoicommandefournisseurtoEntrepot(commande);
					// AsyncMessageFactory.getInstance().getAsyncMessageManager().send(content,
					// EApplication.ENTREPOT);
					// LOGGER.info("Envoi de la commande fournisseur à l'entrepot");

					// Demande Reassort
					// Stock Entrepot Suffisant ?
					// Si oui > Suite / decrément
					// Si non > Commande Fournisseur / incrémente
					LOGGER.severe("*****:Préparation de l'envoi de la demande de réassort à l'ENTREPOT : "
							+ demandereassort.getCommandNumber());
					content = XMLManager.getInstance()
							.envoidemandereassorttoEntrepot(demandereassort);
					LOGGER.info("*****:XML généré : " + content);
					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.ENTREPOT);
					LOGGER.severe("*****:demande de réassort envoyé à l'entrepot :"
							+ demandereassort.getCommandNumber());

				} else if (root.toLowerCase().equals("demandeniveaudestock")) {
					LOGGER.severe("*****: Reception des niveaux de stock depuis BO");
					List<StockMagasin> stocks = XMLManager.getInstance()
							.getniveauStockfromBO(message, doc);
					for (StockMagasin stockMagasin : stocks) {
						LOGGER.info("*****: Article :"
								+ stockMagasin.getArticle().getRef_article()
								+ " Quantité :" + stockMagasin.getQuantity());
					}

				} else if (root.toLowerCase().equals("receptionreassort")) {
					LOGGER.severe("*****: Reception confirmation réassort BO");
					DemandeReassort demand = XMLManager.getInstance()
							.getconfirmationreassortfromBO(message, doc);
					LOGGER.info("*****: Confirmation réassort depuis BO :"
							+ demand.getCommandNumber());
					int i = 0;
					while (i < demand.getArticles().size()) {
						LOGGER.info("*****: Confirmation Article :"
								+ demand.getArticles().get(i) + " quantite :"
								+ demand.getQuantity().get(i));
						i++;
					}
					LOGGER.severe("*****: Fin Confirmation réassort depuis BO :"
							+ demand.getCommandNumber());
				}
			} else if (source == EApplication.ENTREPOT) {
				if (root.toLowerCase().equals("livraisonscommandefournisseur")) {
					LOGGER.severe("*****: Reception bon de livraison commande fournisseur depuis entrepot");
					CommandeFournisseur cmd = XMLManager.getInstance()
							.getbonlivraisonfromEntrepot(message, doc);
					LOGGER.severe("*****: Bon de livraison commande fournisseur recu : "
							+ cmd.getNumero_commande());
					for (Articles a : cmd.getArticles()) {
						LOGGER.info("*****: Bon de livraison avec article :"
								+ a.getRef_article());
					}
					LOGGER.severe("*****: FIN Bon de livraison commande fournisseur recu : "
							+ cmd.getNumero_commande());
					// FIXME incrémenter les stocks
				}

				else if (root.toLowerCase().equals("expeditionclient")) {
					LOGGER.severe("*****: Reception expédition client depuis ENTREPOT");
					CommandeInternet cmd = XMLManager.getInstance()
							.getexpeditionclientfromEntrepot(message, doc);
					LOGGER.severe("*****: Bon d'expédition client recu : "
							+ cmd.getCommandNumber());
					for (Articles a : cmd.getArticles()) {
						LOGGER.info("*****: Expedition client avec article :"
								+ a.getRef_article());
					}
					LOGGER.severe("*****: FIN expedition client recu : "
							+ cmd.getCommandNumber());
				}

			} else if (source == EApplication.INTERNET) {
				if (root.toLowerCase().equals("demandeniveaudestockinternet")) {
					LOGGER.severe("*****: demande des niveaux de stock depuis INTERNET");

					DemandeNiveauStock dns = XMLManager.getInstance()
							.getdemandeniveaustockfromInternet(message, doc);
					LOGGER.severe("*****: demande niveau de stock recu depuis internet : "
							+ dns.getCommandNumber());
					JdbcConnection.getInstance().getConnection();
					DemandeNiveauStock demande = JdbcConnection.getInstance()
							.envoiStock(dns);
					JdbcConnection.getInstance().closeConnection();
					int i = 0;
					while (i < demande.getArticles().size()) {
						LOGGER.info("*****: demande de stock pour article : "
								+ demande.getArticles().get(i) + " quantité :"
								+ demande.getQuantity().get(i));
					}

					content = XMLManager.getInstance()
							.envoiniveaustocktoInternet(demande);
					LOGGER.info("*****: XML généré : " + content);
					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.INTERNET);
					LOGGER.severe("*****: niveau de stock envoyé à Internet");

				} else if (root.toLowerCase().equals("commande_internet")) {
					CommandeInternet commandeint = XMLManager.getInstance()
							.getcommandeinternetfromInternet(message, doc);
					LOGGER.severe("*****:Commande Internet recu depuis internet numéro : "
							+ commandeint.getCommandNumber());
					for (Articles a : commandeint.getArticles()) {
						LOGGER.info("*****: Article : " + a.getRef_article());
					}

					LOGGER.severe("*****:Préparation envoi commande internet à ENTREPOT : "
							+ commandeint.getCommandNumber());
					content = XMLManager.getInstance()
							.envoicommandeinternettoEntrepot(commandeint);
					LOGGER.info("*****: XML généré : " + content);
					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.ENTREPOT);
					LOGGER.severe("*****:Commande internet envoyé à ENTREPOT : "
							+ commandeint.getCommandNumber());
				}
			} else if (source == EApplication.MDM) {
				LOGGER.severe("*****: Reception des prix fournisseur par le referentiel");
				if (root.toLowerCase().equals("xml")) {
					List<Articles> pf = XMLManager.getInstance()
							.getprixfournisseurs(message, doc);
					for (Articles articles : pf) {
						LOGGER.info("*****: Article"
								+ articles.getRef_article()
								+ " Prix fournisseur :"
								+ articles.getPrix_fournisseur());
					}

					LOGGER.severe("*****: Envoi des prix de vente au REFERENTIEL");
					JdbcConnection.getInstance().getConnection();
					List<Articles> art = JdbcConnection.getInstance()
							.envoiPrixArticle();
					JdbcConnection.getInstance().closeConnection();
					for (Articles articles : art) {
						LOGGER.info("*****: Article"
								+ articles.getRef_article() + " Prix vente :"
								+ articles.getPrix_vente());
					}

					content = XMLManager.getInstance().envoiprixventetoRef(art);
					LOGGER.info("*****: XML généré :" + content);
					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.MDM);
					LOGGER.severe("*****: prix de vente des articles envoyés au référentiel");

					// Envoyer promotions au ref
					LOGGER.severe("*****:Envoi des promotions au référentiel");

					JdbcConnection.getInstance().getConnection();
					List<Promotions> prom = JdbcConnection.getInstance()
							.envoiPromotions();
					JdbcConnection.getInstance().closeConnection();
					for (Promotions promotions : prom) {

						LOGGER.info("*****: Promotion article:"
								+ promotions.getRef_article() + "date debut:"
								+ promotions.getBegin() + " date de fin :"
								+ promotions.getEnd() + "remise :"
								+ promotions.getPourcentage());
					}
					content = XMLManager.getInstance().envoipromotoRef(prom);
					LOGGER.info("*****: XML généré :" + content);
					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.MDM);
					LOGGER.severe("*****: Promotions envoyées au REFERENTIEL");
				}
			}

		} catch (AsyncMessageException | ParserConfigurationException
				| SAXException | IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}

	@Override
	public void onFile(File file, EApplication source) {
		LOGGER.info("***** Fichier reçu par GC");
		LOGGER.severe(source + " : " + file.getAbsolutePath());
	}

}