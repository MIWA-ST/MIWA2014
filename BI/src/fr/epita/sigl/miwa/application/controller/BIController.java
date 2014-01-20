/***********************************************************************
 * Module:  BIController.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class BIController
 ***********************************************************************/
package fr.epita.sigl.miwa.application.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.computer.BIComputer;
import fr.epita.sigl.miwa.application.criteres.Critere;
import fr.epita.sigl.miwa.application.dao.BIDao;
import fr.epita.sigl.miwa.application.data.Client;
import fr.epita.sigl.miwa.application.data.DetailSale;
import fr.epita.sigl.miwa.application.data.MDMData;
import fr.epita.sigl.miwa.application.data.Product;
import fr.epita.sigl.miwa.application.data.Promotion;
import fr.epita.sigl.miwa.application.data.Sale;
import fr.epita.sigl.miwa.application.data.Stock;
import fr.epita.sigl.miwa.application.enums.EBOMessageType;
import fr.epita.sigl.miwa.application.parser.BIParser;
import fr.epita.sigl.miwa.application.printer.BIPrinter;
import fr.epita.sigl.miwa.application.statistics.PaymentStatistic;
import fr.epita.sigl.miwa.application.statistics.SaleStatistic;
import fr.epita.sigl.miwa.application.statistics.Segmentation;
import fr.epita.sigl.miwa.application.statistics.StockStatistic;

public class BIController {
	private static final Logger LOGGER = Logger.getLogger(BIController.class.getName());

	private BIParser parser = new BIParser();

	private BIComputer computer = new BIComputer();

	private BIPrinter printer = new BIPrinter();

	private BIDao biDao = new BIDao();

	private boolean hasClient = false;

	private boolean hasDetailSaleBOForPayment = false;

	private boolean hasDetailSaleInternetForPayment = false;

	private boolean hasStockGC = false;

	private boolean hasMDMData = false;

	private boolean hasSaleInternet = false;

	private boolean hasSaleBO = false;

	private boolean hasPromoBO = false;

	private boolean hasDetailSaleBOForSegmentation = false;

	private boolean hasDetailSaleInternetSegmentation = false;

	private BIController(){
	}

	private static class BIControllerHolder{
		private final static BIController instance = new BIController();
	}

	public static BIController getInstance(){
		return BIControllerHolder.instance;
	}
	public void generateStockStatistic() {
		if (!hasMDMData || !hasStockGC){
			LOGGER.severe("***** Génération des statistiques des stocks interrompue : manque d'informations");
			LOGGER.severe("***** MDM: " + hasMDMData + ", GC: " + hasStockGC);
			return;
		}
		List<Stock> stocks = biDao.getStockOfToday();
		List<StockStatistic> stockStatistics = computer.computeStockStatistics(stocks);
		hasStockGC = false;
		biDao.insertStockStatistics(stockStatistics);
		printer.publishStockStatistics(stockStatistics);
	}

	public void generateSaleStatistic() {
		if (!hasSaleBO){
			LOGGER.severe("***** Génération des statistiques des ventes interrompue : manque d'informations");
			LOGGER.severe("***** BO: " + hasSaleBO);
			return;
		}
		List<Sale> sales = biDao.getSalesOfToday();
		List<SaleStatistic> lastSaleStatistics = biDao.getSaleStatisticsOfYesterday();
		List<SaleStatistic> saleStatistics = computer.computeSaleStatistics(sales, lastSaleStatistics);
		hasSaleBO = false;
		hasSaleInternet = false;
		biDao.insertSaleStatistics(saleStatistics);
		printer.publishSaleStatistics(saleStatistics);
	}

	public String generateSegmentation(String message) {
		List<Critere> criteres = parser.parseCRMMessage(message);
		if (!hasClient || !hasDetailSaleBOForSegmentation){
			LOGGER.severe("***** Génération de la segmentation interrompue : manque d'informations");
			LOGGER.severe("***** CRM: " + hasClient + ", BO: " + hasDetailSaleBOForSegmentation);
			return printer.createSegmentationFile(criteres, new ArrayList<Segmentation>());
		}
		List<Client> clients = biDao.getClientByCriteria(criteres);
		List<DetailSale> detailSales = biDao.getDetailSalesForClients(clients);
		List<Product> products = biDao.getAllProducts();
		List<Segmentation> segmentations = computer.computeSegmentation(detailSales, products);
		biDao.insertSegmentation(segmentations);
		return printer.createSegmentationFile(criteres, segmentations);
	}

	/** @param path */
	public void generatePaymentStatistics() {
		if (!hasDetailSaleBOForPayment){
			LOGGER.severe("***** Génération des statistiques de paiement interrompue : manque d'informations");
			LOGGER.severe("***** BO: " + hasDetailSaleBOForPayment);
			return;
		}
		List<DetailSale> detailSales = biDao.getAllBODetailSales();
		List<PaymentStatistic> paiementStatistics = computer.computePaymentStatistics(detailSales);
		hasDetailSaleBOForPayment = false;
		hasDetailSaleInternetForPayment = false;
		biDao.insertPaiementStatistics(paiementStatistics);
		printer.publishPaiementStatistics(paiementStatistics);
	}

	public void parseGCMessage(String message){
		if (!hasMDMData){
			LOGGER.severe("***** Insertion des stocks en BDD interrompue : manque d'informations");
			LOGGER.severe("***** MDM: " + hasMDMData);
			return;
		}
		List<Stock> stocks = parser.parseStockMessage(message);
		biDao.insertStocks(stocks);
		hasStockGC = true;
	}

	public void parseBOMessage(String message) {
		if (!hasMDMData){
			LOGGER.severe("***** Insertion des données du BO en BDD interrompue : manque d'informations");
			LOGGER.severe("***** MDM: " + hasMDMData);
			return;
		}
		Map<EBOMessageType, List<Object>> boData = parser.parseBOMessage(message);
		EBOMessageType boMessageType = boData.keySet().iterator().next();
		Collection<List<Object>> values = boData.values();
		switch (boMessageType) {
		case PROMO:
			List<Promotion> promotions = new ArrayList<Promotion>();
			for (Object o : values){
				Promotion promotion = (Promotion) o;
				promotions.add(promotion);
			}
			biDao.insertPromotions(promotions);
			hasPromoBO = true;
			break;
		case VENTE:
			List<Sale> sales = new ArrayList<Sale>();
			for (Object o : values){
				Sale sale = (Sale) o;
				sales.add(sale);
			}
			biDao.insertSales(sales);
			hasSaleBO = true;
		default:
			break;
		}

	}
	public void parseInternetMessage(String message) {
		if (!hasMDMData){
			LOGGER.severe("***** Insertion des ventes de l'Internet en BDD interrompue : manque d'informations");
			LOGGER.severe("***** MDM: " + hasMDMData);
			return;
		}
		List<Sale> sales = parser.parseSaleMessage(message);
		biDao.insertSales(sales);
		hasSaleInternet  = true;
	}

	public void parseCRMFile(File file) {
		List<Client> clients = parser.parseClientFile(file);
		biDao.insertClients(clients);
		hasClient = true;
	}

	public void parseMDMFile(File file) {
		MDMData mdmData = parser.parseMDMFile(file);
		biDao.insertProducts(mdmData.getProducts());
		biDao.insertPromotions(mdmData.getPromotions());
		hasMDMData = true;
	}

	public void parseBOFile(File file) {
		if (!hasClient || !hasMDMData){
			LOGGER.severe("***** Insertion des ventes détaillées du BO en BDD interrompue : manque d'informations");
			LOGGER.severe("***** MDM: " + hasMDMData + ", CRM: " + hasClient);
			return;
		}
		List<DetailSale> detailSales = parser.parseDetailSale(file);
		biDao.insertDetailSales(detailSales);
		hasDetailSaleBOForPayment = true;
		hasDetailSaleBOForSegmentation = true;
	}

	public void parseInternetFile(File file) {
		if (!hasClient || !hasMDMData){
			LOGGER.severe("***** Insertion des ventes détaillées de l'Internet en BDD interrompue : manque d'informations");
			LOGGER.severe("***** MDM: " + hasMDMData + ", CRM: " + hasClient);
			return;
		}
		List<DetailSale> detailSales = parser.parseDetailSale(file);
		biDao.insertDetailSales(detailSales);
		hasDetailSaleInternetForPayment = true;
		hasDetailSaleInternetSegmentation = true;
	}
}
