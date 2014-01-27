package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import fr.epita.sigl.miwa.application.StockEntrepot;
import fr.epita.sigl.miwa.application.StockMagasin;
import fr.epita.sigl.miwa.application.XMLManager;
import fr.epita.sigl.miwa.application.clock.ClockClient;
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
			System.out.println("*");
			System.out.println("**");
			System.out.println("*****");
			System.out.println("*******");
			System.out.println("*********");
			System.out.println("*******");
			System.out.println("*****");
			System.out.println("**");
			System.out.println("*");


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
					JdbcConnection.getInstance().getConnection();
					List<StockEntrepot> stock = JdbcConnection.getInstance()
							.envoi_all_stock();
					JdbcConnection.getInstance().closeConnection();

					List<Articles> articlefournisseur = new ArrayList<Articles>();
					List<String> quantitefoutnisseur = new ArrayList<String>();
					int j = 0;
					while (j < demandereassort.getArticles().size()) {
						int i = 0;
						boolean finded = false;
						Articles currentarticle = demandereassort.getArticles()
								.get(j);
						while (i < stock.size()) {
							StockEntrepot currentstock = stock.get(i);
							if (stock.get(i).getRef_article()
									.equals(currentarticle.getRef_article())) {
								finded = true;
								if (Integer
										.parseInt(currentstock.getQuantity()) <= Integer
										.parseInt(demandereassort.getQuantity()
												.get(j))) {
									int quantity = Integer
											.parseInt(currentstock
													.getQuantity())
											- Integer.parseInt(demandereassort
													.getQuantity().get(j)) + 20;
									articlefournisseur.add(currentarticle);
									quantitefoutnisseur.add(Integer
											.toString(quantity));
								}
							}
							i++;
							if (!finded) {
								StockEntrepot newstock = new StockEntrepot();
								newstock.setRef_article(demandereassort
										.getArticles().get(j).getRef_article());
								newstock.setQuantity("0");
								JdbcConnection.getInstance().getConnection();
								JdbcConnection.getInstance()
										.insertStockEntrepot(newstock);
								JdbcConnection.getInstance().closeConnection();
								int quantity = Integer.parseInt(demandereassort
										.getQuantity().get(j)) + 30;
								articlefournisseur.add(demandereassort
										.getArticles().get(j));
								quantitefoutnisseur.add(Integer
										.toString(quantity));
							}
						}

						if (!articlefournisseur.isEmpty()) {
							CommandeFournisseur commandef = new CommandeFournisseur();
							commandef.setArticles(articlefournisseur);
							commandef.setquantity(quantitefoutnisseur);
							commandef.setTraitee("FALSE");
							commandef.setNumero_commande(ClockClient.getClock()
									.getHour().toString());
							DateFormat df = new SimpleDateFormat("yyyyMMdd");
							commandef.setBon_commande(df.format(ClockClient
									.getClock().getHour()));
							content = XMLManager.getInstance()
									.envoicommandefournisseurtoEntrepot(
											commandef);
							AsyncMessageFactory.getInstance()
									.getAsyncMessageManager()
									.send(content, EApplication.ENTREPOT);
						}
						j++;
					}

					LOGGER.severe("*****:Préparation de l'envoi de la demande de réassort à l'ENTREPOT : "
							+ demandereassort.getCommandNumber());
					content = XMLManager.getInstance()
							.envoidemandereassorttoEntrepot(demandereassort);
					LOGGER.info("*****:XML généré : " + content);
					AsyncMessageFactory.getInstance().getAsyncMessageManager()
							.send(content, EApplication.ENTREPOT);
					LOGGER.severe("*****:demande de réassort envoyé à l'entrepot :"
							+ demandereassort.getCommandNumber());
					// decrémenter stock
					int i = 0;
					while (i < demandereassort.getArticles().size()) {
						boolean finded = false;
						int y = 0;
						while (y < stock.size()
								&& stock.get(y)
										.getRef_article()
										.equals(demandereassort.getArticles()
												.get(i).getRef_article())) {
							finded = true;
							int newquantity = Integer.parseInt(stock.get(y)
									.getQuantity())
									- Integer.parseInt(demandereassort
											.getQuantity().get(i));
							stock.get(y).setQuantity(
									Integer.toString(newquantity));
							JdbcConnection.getInstance().getConnection();
							JdbcConnection.getInstance().modif_stock(
									stock.get(y).getRef_article(),
									stock.get(y).getQuantity());
							JdbcConnection.getInstance().closeConnection();
							y++;
						}
						if (!finded) {
							StockEntrepot newstock = new StockEntrepot();
							newstock.setRef_article(demandereassort
									.getArticles().get(i).getRef_article());
							newstock.setQuantity("0");
							JdbcConnection.getInstance().getConnection();
							JdbcConnection.getInstance().insertStockEntrepot(
									newstock);
							JdbcConnection.getInstance().closeConnection();
						}
						i++;
					}

				} else if (root.toLowerCase().equals("demandeniveaudestock")) {
					LOGGER.severe("*****: Reception des niveaux de stock depuis BO");
					System.out.println("*****: message de BO : "+ message);
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
								+ demand.getArticles().get(i).getRef_article()
								+ " quantite :" + demand.getQuantity().get(i));
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
					JdbcConnection.getInstance().getConnection();
					List<StockEntrepot> stock = JdbcConnection.getInstance()
							.envoi_all_stock();
					JdbcConnection.getInstance().closeConnection();
					
					int i = 0;
					while (i < cmd.getArticles().size()) {
						boolean finded = false;
						int y = 0;
						while (y < stock.size()
								&& stock.get(y).getRef_article()
										.equals(cmd.getArticles()
												.get(i).getRef_article())) {
							finded = true;
							int newquantity = Integer.parseInt(stock.get(y)
									.getQuantity())
									+ Integer.parseInt(cmd
											.getquantity().get(i));
							stock.get(y).setQuantity(
									Integer.toString(newquantity));
							JdbcConnection.getInstance().getConnection();
							JdbcConnection.getInstance().modif_stock(
									stock.get(y).getRef_article(),
									stock.get(y).getQuantity());
							JdbcConnection.getInstance().closeConnection();
							y++;
						}
						if (!finded) {
							StockEntrepot newstock = new StockEntrepot();
							newstock.setRef_article(cmd.getArticles().get(i).getRef_article());
							newstock.setQuantity(cmd.getquantity().get(i));
							JdbcConnection.getInstance().getConnection();
							JdbcConnection.getInstance().insertStockEntrepot(
									newstock);
							JdbcConnection.getInstance().closeConnection();
						}
						i++;
					}
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
					
					//DECREMENTER STOCK
					JdbcConnection.getInstance().getConnection();
					List<StockEntrepot> stock = JdbcConnection.getInstance()
							.envoi_all_stock();
					JdbcConnection.getInstance().closeConnection();
					int i = 0;
					while (i < cmd.getArticles().size()) {
						boolean finded = false;
						int y = 0;
						while (y < stock.size()
								&& stock.get(y).getRef_article()
										.equals(cmd.getArticles()
												.get(i).getRef_article())) {
							finded = true;
							int newquantity = Integer.parseInt(stock.get(y)
									.getQuantity())
									- Integer.parseInt(cmd.getquantity().get(i));
							stock.get(y).setQuantity(
									Integer.toString(newquantity));
							JdbcConnection.getInstance().getConnection();
							JdbcConnection.getInstance().modif_stock(
									stock.get(y).getRef_article(),
									stock.get(y).getQuantity());
							JdbcConnection.getInstance().closeConnection();
							y++;
						}
						if (!finded) {
							StockEntrepot newstock = new StockEntrepot();
							newstock.setRef_article(cmd.getArticles().get(i).getRef_article());
							JdbcConnection.getInstance().getConnection();
							JdbcConnection.getInstance().insertStockEntrepot(
									newstock);
							JdbcConnection.getInstance().closeConnection();
						}
						i++;
					}
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