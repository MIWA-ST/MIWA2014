package fr.epita.sigl.miwa.application.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import fr.epita.sigl.miwa.application.criteres.AgeValue;
import fr.epita.sigl.miwa.application.criteres.Critere;
import fr.epita.sigl.miwa.application.data.Client;
import fr.epita.sigl.miwa.application.data.DetailSale;
import fr.epita.sigl.miwa.application.data.MDMData;
import fr.epita.sigl.miwa.application.data.Product;
import fr.epita.sigl.miwa.application.data.Promotion;
import fr.epita.sigl.miwa.application.data.Sale;
import fr.epita.sigl.miwa.application.data.SoldProduct;
import fr.epita.sigl.miwa.application.data.Stock;
import fr.epita.sigl.miwa.application.enums.EBOMessageType;
import fr.epita.sigl.miwa.application.enums.ECritereType;
import fr.epita.sigl.miwa.application.enums.EPaiementType;

public class BIParser {

	private static final Logger LOGGER = Logger.getLogger(BIParser.class.getName());
	private DocumentBuilderFactory dBFactory;
	private DocumentBuilder dBuilder;

	public BIParser() {
		try {
			dBFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dBFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.severe("***** Erreur : impossible d'initialiser le parser DOM");
			LOGGER.severe("***** L'erreur est : " + e);
		}
	}

	/**
	 * Parse le message envoyé par la GC
	 * @param message
	 * @return La liste des stocks
	 */
	public List<Stock> parseStockMessage(String message){
		List<Stock> stockList = null;

		try {
			// Ecriture du message dans un fichier
			File file = new File ("stock.xml");

			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(message);
			output.close();

			// Parsage du fichier	
			Document stockFile = dBuilder.parse(file);

			// Parsage du fichier : Partie entête
			NodeList headerNodes = stockFile.getElementsByTagName("ENTETE");
			String dateInfo = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();

			// Parsage du fichier : Partie corps
			NodeList stockNodes = stockFile.getElementsByTagName("STOCK");
			stockList = new ArrayList<Stock>();

			// Création des éléments Stock
			for (int i = 0; i < stockNodes.getLength(); i++) {
				Node stockNode = stockNodes.item(i);
				String tmpInfo;
				Stock stock = new Stock();

				stock.setProductRef(stockNode.getAttributes().getNamedItem("ref-article").getNodeValue());
				tmpInfo = stockNode.getAttributes().getNamedItem("commande").getNodeValue();
				stock.setOrdered(Boolean.valueOf(tmpInfo));
				tmpInfo = stockNode.getAttributes().getNamedItem("stock").getNodeValue();
				stock.setStockQty(Integer.valueOf(tmpInfo));
				tmpInfo = stockNode.getAttributes().getNamedItem("max").getNodeValue();
				stock.setMaxQty(Integer.valueOf(tmpInfo));
				stock.setDateTime((new SimpleDateFormat("yyyy-MM-dd")).parse(dateInfo));
				String store = stockNode.getAttributes().getNamedItem("lieu").getNodeValue();
				stock.setStore(store);

				stockList.add(stock);
			}

			// Suppression du fichier
			file.delete();
		} catch (IOException e1) {
			LOGGER.severe("***** Erreur : impossible de transformer le message en fichier");
			LOGGER.severe("***** L'erreur est : " + e1);
		} catch (SAXException e2) {
			LOGGER.severe("***** Erreur : impossible de parser le fichier");
			LOGGER.severe("***** L'erreur est : " + e2);
		} catch (ParseException e) {
			LOGGER.severe("***** Erreur : date au mauvais format dans le XML");
			LOGGER.severe("***** L'erreur est : " + e);
		}

		return stockList;
	}

	/**
	 * Parse les messages envoyés par le BO
	 * @param message
	 * @return Une enum pour savoir ce que contient la liste des résultats
	 */
	public Map<EBOMessageType, List<Object>> parseBOMessage(String message){
		Map<EBOMessageType, List<Object>> result = null;

		try {
			// Ecriture du message dans un fichier
			File file = new File ("bo-object.xml");

			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(message);
			output.close();

			// Parsage du fichier	
			Document boFile = dBuilder.parse(file);

			// Parsage du fichier : Partie entête
			NodeList headerNodes = boFile.getElementsByTagName("ENTETE");
			String object = headerNodes.item(0).getAttributes().getNamedItem("objet").getNodeValue();

			// Parsage du fichier : Partie corps
			if (object.equalsIgnoreCase("ventes 15min")) {
				result = new HashMap<EBOMessageType, List<Object>>();
				result.put(EBOMessageType.VENTE, parseBOVentesMessage(boFile));
			}
			if (object.equalsIgnoreCase("promotions")) {
				result = new HashMap<EBOMessageType, List<Object>>();
				result.put(EBOMessageType.PROMO, parseBOPromotionMessage(boFile));
			}

			// Suppression du fichier
			file.delete();
		} catch (IOException e1) {
			LOGGER.severe("***** Erreur : impossible de transformer le message en fichier");
			LOGGER.severe("***** L'erreur est : " + e1);
		} catch (SAXException e2) {
			LOGGER.severe("***** Erreur : impossible de parser le fichier");
			LOGGER.severe("***** L'erreur est : " + e2);
		}

		return result;
	}

	/**
	 * Parse le message "Promotions" envoyé par le BO
	 * @param message
	 * @return La liste des promotions
	 */
	private List<Object> parseBOPromotionMessage(Document promotionFile){
		List<Object> promotionList = null;

		try {
			// Parsage du fichier : Partie corps
			NodeList bodyNodes = promotionFile.getElementsByTagName("PROMOTIONS");
			String storeId = bodyNodes.item(0).getAttributes().getNamedItem("lieu").getNodeValue();
			NodeList promotionNodes = bodyNodes.item(0).getChildNodes();
			promotionList = new ArrayList<Object>();

			// Création des éléments Promotion
			for (int i = 0; i < promotionNodes.getLength(); i++) {
				Node promotionNode = promotionNodes.item(i);
				String tmpInfo;
				Promotion promotion = new Promotion();

				tmpInfo = promotionNode.getAttributes().getNamedItem("ref-article").getNodeValue();
				promotion.setProductReference(tmpInfo);
				tmpInfo = promotionNode.getAttributes().getNamedItem("debut").getNodeValue();
				promotion.setBeginDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(tmpInfo));
				tmpInfo = promotionNode.getAttributes().getNamedItem("fin").getNodeValue();
				promotion.setEndDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(tmpInfo));
				tmpInfo = promotionNode.getAttributes().getNamedItem("pourcent").getNodeValue();
				promotion.setPercentage(Integer.valueOf(tmpInfo));
				promotion.setStore(storeId);

				promotionList.add(promotion);
			}
		}  catch (ParseException e) {
			LOGGER.severe("***** Erreur : date au mauvais format dans le XML");
			LOGGER.severe("***** L'erreur est : " + e);
		}

		return promotionList;
	}

	/**
	 * Parse le message "Ventes 15 minutes" envoyé par le BO
	 * @param message
	 * @return La liste des ventes
	 */
	private List<Object> parseBOVentesMessage(Document saleFile){
		List<Object> saleList = null;

		try {
			// Parsage du fichier : Partie entête
			NodeList headerNodes = saleFile.getElementsByTagName("ENTETE");
			String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
			Date saleDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateStr);

			// Parsage du fichier : Partie corps
			NodeList bodyNodes = saleFile.getElementsByTagName("VENTES");
			String storeId = bodyNodes.item(0).getAttributes().getNamedItem("lieu").getNodeValue();
			NodeList saleNodes = bodyNodes.item(0).getChildNodes();
			saleList = new ArrayList<Object>();

			// Création des éléments Sale
			for (int i = 0; i < saleNodes.getLength(); i++) {
				Node saleNode = saleNodes.item(i);
				String tmpInfo;
				Sale sale = new Sale();

				sale.setDateTime(saleDate);
				sale.setStore(storeId);
				tmpInfo = saleNode.getAttributes().getNamedItem("quantite_vendue").getNodeValue();
				sale.setSoldQty(Integer.valueOf(tmpInfo));
				sale.setProductCategory(saleNode.getAttributes().getNamedItem("ref-categorie").getNodeValue());
				tmpInfo = saleNode.getAttributes().getNamedItem("montant_fournisseur").getNodeValue();
				sale.setSupplierTotal(Float.valueOf(tmpInfo));
				tmpInfo = saleNode.getAttributes().getNamedItem("montant_vente").getNodeValue();
				sale.setSalesTotal(Float.valueOf(tmpInfo));
				sale.setSource("bo");

				saleList.add(sale);
			}
		}  catch (ParseException e) {
			LOGGER.severe("***** Erreur : date au mauvais format dans le XML");
			LOGGER.severe("***** L'erreur est : " + e);
		}

		return saleList;
	}

	/**
	 * Parse les critères envoyés par le CRM
	 * @param message
	 * @return La liste des critères
	 */
	public List<Critere> parseCRMMessage(String message) {
		List<Critere> criteriaList = null;

		try {
			// Ecriture du message dans un fichier
			File file = new File ("criteria.xml");
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(message);
			output.close();

			// Parsage du fichier	
			Document criteriaFile = dBuilder.parse(file);

			// Parsage du fichier : Partie corps
			NodeList criteriaNodes = criteriaFile.getElementsByTagName("CRITERE");
			criteriaList = new ArrayList<Critere>();

			// Création des éléments Critere
			for (int i = 0; i < criteriaNodes.getLength(); i++) {
				Node criteriaNode = criteriaNodes.item(i);
				Critere criteria = null;
				String tmpInfo;

				tmpInfo = criteriaNode.getAttributes().getNamedItem("type").getNodeValue();
				if (tmpInfo.equalsIgnoreCase("age")) {
					String min = criteriaNode.getAttributes().getNamedItem("min").getNodeValue();
					String max = criteriaNode.getAttributes().getNamedItem("max").getNodeValue();
					AgeValue ageValue = new AgeValue(Integer.valueOf(min), Integer.valueOf(max));
					criteria = new Critere(ECritereType.AGE, ageValue);
				}
				else if (tmpInfo.equalsIgnoreCase("geographie")) {
					String valeur = criteriaNode.getAttributes().getNamedItem("valeur").getNodeValue();
					criteria = new Critere(ECritereType.GEO, valeur);
				}
				else if (tmpInfo.equalsIgnoreCase("sexe")) {
					String valeur = criteriaNode.getAttributes().getNamedItem("valeur").getNodeValue();
					criteria = new Critere(ECritereType.SEXE, valeur);
				}
				else if (tmpInfo.equalsIgnoreCase("situation-familiale")) {
					String valeur = criteriaNode.getAttributes().getNamedItem("valeur").getNodeValue();
					criteria = new Critere(ECritereType.SF, valeur);
				}
				else if (tmpInfo.equalsIgnoreCase("enfant")) {
					String valeur = criteriaNode.getAttributes().getNamedItem("valeur").getNodeValue();
					criteria = new Critere(ECritereType.ENF, valeur);
				}
				else if (tmpInfo.equalsIgnoreCase("fidelite")) {
					String valeur = criteriaNode.getAttributes().getNamedItem("valeur").getNodeValue();
					criteria = new Critere(ECritereType.FID, valeur);
				}

				criteriaList.add(criteria);
			}

			// Suppression du fichier
			file.delete();
		} catch (IOException e1) {
			LOGGER.severe("***** Erreur : impossible de transformer le message en fichier");
			LOGGER.severe("***** L'erreur est : " + e1);
		} catch (SAXException e2) {
			LOGGER.severe("***** Erreur : impossible de parser le fichier");
			LOGGER.severe("***** L'erreur est : " + e2);
		}

		return criteriaList;
	}

	/**
	 * Parse les ventes envoyées par internet
	 * @param message
	 * @return La liste des ventes
	 */
	public List<Sale> parseSaleMessage(String message) {
		List<Sale> saleList = null;

		try {
			// Ecriture du message dans un fichier
			File file = new File ("sale.xml");

			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(message);
			output.close();

			// Parsage du fichier	
			Document saleFile = dBuilder.parse(file);

			// Parsage du fichier : Partie entête
			NodeList headerNodes = saleFile.getElementsByTagName("ENTETE");
			String dateStr = headerNodes.item(0).getAttributes().getNamedItem("date").getNodeValue();
			Date saleDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateStr);

			// Parsage du fichier : Partie corps
			NodeList saleNodes = saleFile.getElementsByTagName("CATEGORIE");
			saleList = new ArrayList<Sale>();

			// Création des éléments Sale
			for (int i = 0; i < saleNodes.getLength(); i++) {
				Node saleNode = saleNodes.item(i);
				String tmpInfo;
				Sale sale = new Sale();

				sale.setDateTime(saleDate);
				sale.setStore("internet");
				tmpInfo = saleNode.getAttributes().getNamedItem("quantite_vendue").getNodeValue();
				sale.setSoldQty(Integer.valueOf(tmpInfo));
				sale.setProductCategory(saleNode.getAttributes().getNamedItem("ref-categorie").getNodeValue());
				tmpInfo = saleNode.getAttributes().getNamedItem("montant_fournisseur").getNodeValue();
				sale.setSupplierTotal(Float.valueOf(tmpInfo));
				tmpInfo = saleNode.getAttributes().getNamedItem("montant_vente").getNodeValue();
				sale.setSalesTotal(Float.valueOf(tmpInfo));
				sale.setSource("internet");

				saleList.add(sale);
			}

			// Suppression du fichier
			file.delete();
		} catch (IOException e1) {
			LOGGER.severe("***** Erreur : impossible de transformer le message en fichier");
			LOGGER.severe("***** L'erreur est : " + e1);
		} catch (SAXException e2) {
			LOGGER.severe("***** Erreur : impossible de parser le fichier");
			LOGGER.severe("***** L'erreur est : " + e2);
		} catch (ParseException e3) {
			LOGGER.severe("***** Erreur : date au mauvais format dans le XML");
			LOGGER.severe("***** L'erreur est : " + e3);
		}

		return saleList;		
	}

	/**
	 * Parse le fichier des clients envoyé par le CRM
	 * @param file
	 * @return La liste des clients
	 */
	public List<Client> parseClientFile(File file) {
		List<Client> clientList = null;

		try {
			Document clientFile = dBuilder.parse(file);

			// Parsage du fichier : Partie corps
			NodeList clientNodes = clientFile.getElementsByTagName("CLIENT");
			clientList = new ArrayList<Client>();

			// Création des éléments Client
			for (int i = 0; i < clientNodes.getLength(); i++) {
				Node clientNode = clientNodes.item(i);
				String tmpInfo;
				Client client = new Client();

				tmpInfo = clientNode.getAttributes().getNamedItem("id").getNodeValue();
				client.setNumero(Integer.valueOf(tmpInfo));
				client.setTitle(clientNode.getAttributes().getNamedItem("civilite").getNodeValue());
				tmpInfo = clientNode.getAttributes().getNamedItem("naissance").getNodeValue();
				if (tmpInfo.equalsIgnoreCase("null")){
					client.setBirthDate(null);
				} else {
					client.setBirthDate((new SimpleDateFormat("yyyy-MM-dd")).parse(tmpInfo));
				}
				tmpInfo = clientNode.getAttributes().getNamedItem("codepostal").getNodeValue();
				client.setZipcode(Integer.valueOf(tmpInfo));
				client.setMaritalStatus(clientNode.getAttributes().getNamedItem("situationfam").getNodeValue());
				tmpInfo = clientNode.getAttributes().getNamedItem("nbenfant").getNodeValue();
				if (tmpInfo.equalsIgnoreCase("null")){
					client.setChildren(null);
				} else {
					client.setChildren(Boolean.valueOf(tmpInfo));
				}
				client.setLoyaltyType(clientNode.getAttributes().getNamedItem("typecarte").getNodeValue());

				clientList.add(client);
			}
		} catch (IOException | SAXException e1) {
			LOGGER.severe("***** Erreur : impossible de parser le fichier");
			LOGGER.severe("***** L'erreur est : " + e1);
		} catch (ParseException e2) {
			LOGGER.severe("***** Erreur : date au mauvais format dans le XML");
			LOGGER.severe("***** L'erreur est : " + e2);
		}

		return clientList;
	}

	/**
	 * Parse le fichier envoyé par le référentiel
	 * @param file Le fichier envoyé par le référentiel
	 * @return La liste des données envoyées par le référentiel (promos + catégorie + produits)
	 */
	public MDMData parseMDMFile(File file) {
		MDMData mdmData = null;

		try {
			Document refFile = dBuilder.parse(file);

			// Parsage du fichier : Partie corps
			NodeList articleNodes = refFile.getElementsByTagName("ARTICLE");
			ArrayList<Product> productList = new ArrayList<Product>();
			ArrayList<Promotion> promotionList = new ArrayList<Promotion>();
			mdmData = new MDMData();

			// Création des éléments Product
			for (int i = 0; i < articleNodes.getLength(); i++) {
				Node articleNode = articleNodes.item(i);
				String tmpInfo;
				Product product = new Product();

				// Lecture de la partie "Article"
				product.setReference(articleNode.getAttributes().getNamedItem("reference").getNodeValue());
				product.setCategoryName(articleNode.getAttributes().getNamedItem("categorie").getNodeValue());
				tmpInfo = articleNode.getAttributes().getNamedItem("prix_fournisseur").getNodeValue();
				Float buyingPrice = Float.valueOf(tmpInfo);
				product.setBuyingPrice(buyingPrice);
				tmpInfo = articleNode.getAttributes().getNamedItem("prix_vente").getNodeValue();
				Float sellingPrice = Float.valueOf(tmpInfo);
				product.setSellingPrice(sellingPrice);
				product.setMargin(sellingPrice - buyingPrice);
				productList.add(product);

				// Lecture des sous-balises de "Article"
				NodeList childNodes = articleNode.getChildNodes();

				for (int j = 0; j < childNodes.getLength(); j++) {
					Node childNode = childNodes.item(j);

					// Récupération de la liste des promotions d'un article
					if (childNode.getNodeName().equalsIgnoreCase("PROMOTIONS")) {
						NodeList promotionNodes = childNode.getChildNodes();

						// Parsage de l'ensemble des promotions appliquées à l'article
						for (int k = 0; k < promotionNodes.getLength(); k++) {
							Node promotionNode = promotionNodes.item(k);
							Promotion promotion = new Promotion();

							promotion.setProductReference(product.getReference());
							tmpInfo = promotionNode.getAttributes().getNamedItem("debut").getNodeValue();
							promotion.setBeginDate((new SimpleDateFormat("yyyy-MM-dd")).parse(tmpInfo));
							tmpInfo = promotionNode.getAttributes().getNamedItem("fin").getNodeValue();
							promotion.setEndDate((new SimpleDateFormat("yyyy-MM-dd")).parse(tmpInfo));
							tmpInfo = promotionNode.getAttributes().getNamedItem("pourcent").getNodeValue();
							promotion.setPercentage(Integer.valueOf(tmpInfo));
							promotion.setStore("");

							promotionList.add(promotion);
						}
					}
				}
			}

			mdmData.setProducts(productList);
			mdmData.setPromotions(promotionList);

		} catch (IOException | SAXException e1) {
			LOGGER.severe("***** Erreur : impossible de parser le fichier");
			LOGGER.severe("***** L'erreur est : " + e1);
		} catch (ParseException e2) {
			LOGGER.severe("***** Erreur : date au mauvais format dans le XML");
			LOGGER.severe("***** L'erreur est : " + e2);
		}

		return mdmData;		
	}

	/**
	 * Parse le fichier envoyé par le BO et internet
	 * @param file
	 * @return La liste des ventes détaillées
	 */
	public List<DetailSale> parseDetailSale(File file) {
		List<DetailSale> detailSaleList = null;

		try {
			Document detailSaleFile = dBuilder.parse(file);
			NodeList headerNodes = detailSaleFile.getElementsByTagName("ENTETE");
			String source = headerNodes.item(0).getAttributes().getNamedItem("source").getNodeValue();
			// Parsage du fichier : Partie corps
			NodeList detailSaleNodes = detailSaleFile.getElementsByTagName("VENTE");
			detailSaleList = new ArrayList<DetailSale>();

			// Création des éléments DetailSale
			for (int i = 0; i < detailSaleNodes.getLength(); i++) {
				Node detailSaleNode = detailSaleNodes.item(i);
				String tmpInfo;
				DetailSale detailSale = new DetailSale();
				detailSale.setSource(source);
				List<SoldProduct> soldProductList = new ArrayList<SoldProduct>();

				String paymentMean = detailSaleNode.getAttributes().getNamedItem("moyen_paiement").getNodeValue();
				if (paymentMean.equalsIgnoreCase("CB"))
					detailSale.setPaymentMean(EPaiementType.CB);
				else if (paymentMean.equalsIgnoreCase("CF"))
					detailSale.setPaymentMean(EPaiementType.CF);
				else if (paymentMean.equalsIgnoreCase("CQ"))
					detailSale.setPaymentMean(EPaiementType.CQ);
				else if (paymentMean.equalsIgnoreCase("ES"))
					detailSale.setPaymentMean(EPaiementType.ES);

				tmpInfo = detailSaleNode.getAttributes().getNamedItem("dateHeure").getNodeValue();
				detailSale.setDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(tmpInfo));

				tmpInfo = detailSaleNode.getAttributes().getNamedItem("montant").getNodeValue();
				detailSale.setTotal(Integer.valueOf(tmpInfo));

				String storeId = detailSaleNode.getParentNode().getAttributes().getNamedItem("lieu").getNodeValue();
				detailSale.setStore(storeId);

				tmpInfo = detailSaleNode.getAttributes().getNamedItem("numero_client").getNodeValue();
				detailSale.setClientNb(Integer.valueOf(tmpInfo));

				// Construction de la liste des produits vendus pour le ticket
				NodeList soldProductsNode = detailSaleNode.getChildNodes().item(1).getChildNodes();

				for (int j = 0; j < soldProductsNode.getLength(); j++) {
					Node soldProductNode = soldProductsNode.item(j);
					if (soldProductNode instanceof Element){
						SoldProduct soldProduct = new SoldProduct();
						tmpInfo = soldProductNode.getAttributes().getNamedItem("quantite").getNodeValue();
						soldProduct.setQuantity(Integer.valueOf(tmpInfo));
						String test = soldProductNode.getAttributes().getNamedItem("ref-article").getNodeValue();
						soldProduct.setProductRef(test);

						soldProductList.add(soldProduct);
					}
				}
				detailSale.setProductList(soldProductList);

				detailSaleList.add(detailSale);
			}
		} catch (IOException | SAXException e1) {
			LOGGER.severe("***** Erreur : impossible de parser le fichier");
			LOGGER.severe("***** L'erreur est : " + e1);
		} catch (ParseException e2) {
			LOGGER.severe("***** Erreur : date au mauvais format dans le XML");
			LOGGER.severe("***** L'erreur est : " + e2);
		}

		return detailSaleList;		
	}
}
