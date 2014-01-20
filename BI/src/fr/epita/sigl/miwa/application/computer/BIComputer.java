/***********************************************************************
 * Module:  BIComputer.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class BIComputer
 ***********************************************************************/
package fr.epita.sigl.miwa.application.computer;

import java.util.*;
import java.util.Map.Entry;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.data.*;
import fr.epita.sigl.miwa.application.enums.EPaiementType;
import fr.epita.sigl.miwa.application.statistics.*;

public class BIComputer {
	/** @param stocks */
	public List<StockStatistic> computeStockStatistics(List<Stock> stocks) {
		Date date = ClockClient.getClock().getHour();
		List<StockStatistic> stockStatistics = new ArrayList<StockStatistic>();
		for (Stock stock : stocks){
			float stockPourcent = (stock.getStockQty() / stock.getMaxQty()) * 100;
			if (stockPourcent <= 15.0 || stockPourcent >= 85.0){
				StockStatistic stockStatistic = new StockStatistic(date, stock.getStore(), stock.getProductRef(), stockPourcent >= 85.0, stockPourcent <= 15.0, stock.getOrdered());
				stockStatistics.add(stockStatistic);
			}
		}
		return stockStatistics;
	}

	/** @param stocks */
	public List<SaleStatistic> computeSaleStatistics(List<Sale> sales, List<SaleStatistic> lastSaleStatistics) {
		Date date = ClockClient.getClock().getHour();
		List<SaleStatistic> saleStatistics = new ArrayList<SaleStatistic>();
		Map<String, TemporarySaleStatistic> categorieStatistics = new HashMap<String, TemporarySaleStatistic>();
		int caTotal = 0;
		for (Sale sale : sales){
			caTotal += sale.getSalesTotal();
			if (categorieStatistics.containsKey(sale.getProductCategory())){
				TemporarySaleStatistic categoryStatistic = categorieStatistics.get(sale.getProductCategory());
				categoryStatistic.addCA(sale.getSalesTotal());
				categoryStatistic.addNbSoldProducts(sale.getSoldQty());
			} else {
				TemporarySaleStatistic categoryStatistic = new TemporarySaleStatistic(sale.getSalesTotal(), sale.getSoldQty(), sale.getSource());
				categorieStatistics.put(sale.getProductCategory(), categoryStatistic);
			}
		}
		for (SaleStatistic lastSaleStatistic : lastSaleStatistics){
			SaleStatistic saleStatistic;
			String category = lastSaleStatistic.getCategorie();
			if (categorieStatistics.containsKey(category)){
				TemporarySaleStatistic temp = categorieStatistics.get(category);
				int diff = temp.getNbSoldProducts() - lastSaleStatistic.getNbSoldProducts();
				saleStatistic = new SaleStatistic(date, category, (float) diff / (float) lastSaleStatistic.getNbSoldProducts() * 100, temp.getCa() , (float) temp.getCa() / (float) caTotal * 100, temp.getNbSoldProducts(), lastSaleStatistic.getSource());
				categorieStatistics.remove(category);
			} else {
				int diff = 0 - lastSaleStatistic.getNbSoldProducts();
				saleStatistic = new SaleStatistic(date, category, (float)diff / (float)lastSaleStatistic.getNbSoldProducts() * 100, 0, 0, 0, lastSaleStatistic.getSource());
			}
			saleStatistics.add(saleStatistic);
		}
		for (Entry<String, TemporarySaleStatistic> entry : categorieStatistics.entrySet()){
			TemporarySaleStatistic value = entry.getValue();
			SaleStatistic saleStatistic = new SaleStatistic(date, entry.getKey(), value.getNbSoldProducts() * 100, value.getCa(), (float)value.getCa() / (float)caTotal * 100, value.getNbSoldProducts(), value.getSource());
			saleStatistics.add(saleStatistic);
		}
		return saleStatistics;
	}

	public List<Segmentation> computeSegmentation(List<DetailSale> detailSales, List<Product> products) {
		Date date = ClockClient.getClock().getHour();
		List<Segmentation> segmentations = new ArrayList<Segmentation>();
		Map<Integer, Map<String, Integer>> categoriesByClient = new HashMap<Integer, Map<String, Integer>>();
		for (DetailSale detailSale : detailSales){
			Map<String, Integer> quantityByCategory = categoriesByClient.get(detailSale.getClientNb());
			if (quantityByCategory == null){
				quantityByCategory = new HashMap<String, Integer>();
			}
			for (SoldProduct soldProduct : detailSale.getProductList()){
				String categoryRef = "";
				for (Product product : products){
					if (product.getReference().equals(soldProduct.getProductRef())){
						categoryRef = product.getCategoryName();
						break;
					}
				}					
				Integer quantity = quantityByCategory.get(categoryRef);
				if (quantity != null){
					quantity += soldProduct.getQuantity();
				} else {
					quantity = new Integer(soldProduct.getQuantity());
				}
				quantityByCategory.put(categoryRef, quantity);
			}
			categoriesByClient.put(detailSale.getClientNb(), quantityByCategory);
		}
		for (Entry<Integer, Map<String, Integer>> clients : categoriesByClient.entrySet()){
			List<CategorieStatistic> categoryStatistics = new ArrayList<CategorieStatistic>();
			for (Entry<String, Integer> quantities : clients.getValue().entrySet()){
				categoryStatistics.add(new CategorieStatistic(quantities.getKey(), quantities.getValue()));
			}
			segmentations.add(new Segmentation(date, clients.getKey(), categoryStatistics));
		}
		return segmentations;
	}

	/** @param detailSales */
	public List<PaymentStatistic> computePaymentStatistics(List<DetailSale> detailSales) {
		Date date = ClockClient.getClock().getHour();
		List<PaymentStatistic> paymentStatistics = new ArrayList<PaymentStatistic>();
		int caCB = 0;
		int caCQ = 0;
		int caCF = 0;
		int caES = 0;
		float caTotal = 0;
		for (DetailSale detailSale : detailSales){
			caTotal += detailSale.getTotal();
			switch (detailSale.getPaymentMean()) {
			case CB:
				caCB += detailSale.getTotal();
				break;
			case CQ:
				caCQ += detailSale.getTotal();
				break;
			case CF:
				caCF += detailSale.getTotal();
				break;
			case ES:
				caES += detailSale.getTotal();
			default:
				break;
			}
		}
		PaymentStatistic cbStatistics = new PaymentStatistic(date, EPaiementType.CB, caCB, (float)caCB / caTotal * 100);
		paymentStatistics.add(cbStatistics);
		PaymentStatistic cqStatistics = new PaymentStatistic(date, EPaiementType.CQ, caCQ, (float)caCQ / caTotal * 100);
		paymentStatistics.add(cqStatistics);
		PaymentStatistic cfStatistics = new PaymentStatistic(date, EPaiementType.CF, caCF, (float)caCF / caTotal * 100);
		paymentStatistics.add(cfStatistics);
		PaymentStatistic esStatistics = new PaymentStatistic(date, EPaiementType.ES, caES, (float)caES / caTotal * 100);
		paymentStatistics.add(esStatistics);
		return paymentStatistics;
	}
}