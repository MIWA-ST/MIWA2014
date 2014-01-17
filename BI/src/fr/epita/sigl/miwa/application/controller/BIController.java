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
	
	private boolean hasClient = false;
	
	private boolean hasDetailSaleBOForPayment = false;
	
	private boolean hasDetailSaleInternetForPayment = false;
	
	private boolean hasStockGC = false;
	
	private boolean hasStockBO = false;
	
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
		while (!hasStockBO && !hasStockGC){
			try {
				wait(10000);
			} catch (InterruptedException e) {
				LOGGER.severe("Probleme pendant la génération des statistiques de stock");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		List<Stock> stocks = BIDao.getLastStocks();
		List<StockStatistic> stockStatistics = computer.computeStockStatistics(stocks);
		hasStockBO = false;
		hasStockGC = false;
		BIDao.insertStockStatistics(stockStatistics);
		printer.publishStockStatistics(stockStatistics);
	}

	public void generateSaleStatistic() {
		while (!hasSaleBO){
			try {
				wait(10000);
			} catch (InterruptedException e) {
				LOGGER.severe("Probleme pendant la la génération des statistiques des ventes détaillées");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		List<Sale> sales = BIDao.getSalesOfToday();
		List<SaleStatistic> lastSaleStatistics = BIDao.getLastSaleStatistics();
		List<SaleStatistic> saleStatistics = computer.computeSaleStatistics(sales, lastSaleStatistics);
		hasSaleBO = false;
		hasSaleInternet = false;
		BIDao.insertSaleStatistics(saleStatistics);
		printer.publishSaleStatistics(saleStatistics);
	}

	/** @param message */
	public String generateSegmentation(String message) {
		List<Critere> criteres = parser.parseCRMMessage(message);
		while (!hasClient && !hasDetailSaleBOForSegmentation && !hasDetailSaleInternetSegmentation){
			try {
				wait(10000);
			} catch (InterruptedException e) {
				LOGGER.severe("Probleme pendant la segmentation");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		List<Client> clients = BIDao.getAllClients();
		List<DetailSale> detailSales = BIDao.getAllDetailSales();
		List<Segmentation> segmentations = computer.computeSegmentation(clients, detailSales, criteres);
		BIDao.insertSegmentation(segmentations);
		return printer.createSegmentationFile(criteres, segmentations);
	}

	/** @param path */
	public void generatePaymentStatistics(String path) {
		while (!hasDetailSaleBOForPayment && !hasDetailSaleInternetForPayment){
			try {
				wait(10000);
			} catch (InterruptedException e) {
				LOGGER.severe("Probleme pendant la génération des statistiques du paiement");
				LOGGER.severe("L'erreur est : " + e);
			}
		}
		List<DetailSale> detailSales = BIDao.getAllDetailSales();
		List<PaymentStatistic> paiementStatistics = computer.computePaymentStatistics(detailSales);
		hasDetailSaleBOForPayment = false;
		hasDetailSaleInternetForPayment = false;
		BIDao.insertPaiementStatistics(paiementStatistics);
		printer.publishPaiementStatistics(paiementStatistics);
	}

	public void parseGCMessage(String message){
		List<Stock> stocks = parser.parseStockMessage(message);
		BIDao.insertStocks(stocks);
		hasStockGC = true;
	}

	public void parseBOMessage(String message) {
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
			BIDao.insertPromotions(promotions);
			hasPromoBO = true;
			break;
		case STOCK:
			List<Stock> stocks = new ArrayList<Stock>();
			for (Object o : values){
				Stock stock = (Stock) o;
				stocks.add(stock);
			}
			BIDao.insertStocks(stocks);
			hasStockBO = true;
			break;
		case VENTE:
			List<Sale> sales = new ArrayList<Sale>();
			for (Object o : values){
				Sale sale = (Sale) o;
				sales.add(sale);
			}
			BIDao.insertSales(sales);
			hasSaleBO = true;
		default:
			break;
		}

	}
	public void parseInternetMessage(String message) {
		List<Sale> sales = parser.parseSaleMessage(message);
		BIDao.insertSales(sales);
		hasSaleInternet  = true;
	}

	public void parseCRMFile(File file) {
		List<Client> clients = parser.parseClientFile(file);
		BIDao.insertClients(clients);
		hasClient = true;
	}

	public void parseMDMFile(File file) {
		MDMData mdmData = parser.parseMDMFile(file);
		BIDao.insertProducts(mdmData.getProducts());
		BIDao.insertProductsCategories(mdmData.getCategories());
		BIDao.insertPromotions(mdmData.getPromotions());
		hasMDMData = true;
	}

	public void parseBOFile(File file) {
		List<DetailSale> detailSales = parser.parseDetailSale(file);
		BIDao.insertDetailSales(detailSales);
		hasDetailSaleBOForPayment = true;
		hasDetailSaleBOForSegmentation = true;
	}

	public void parseInternetFile(File file) {
		List<DetailSale> detailSales = parser.parseDetailSale(file);
		BIDao.insertDetailSales(detailSales);
		hasDetailSaleInternetForPayment = true;
		hasDetailSaleInternetSegmentation = true;
	}
}
