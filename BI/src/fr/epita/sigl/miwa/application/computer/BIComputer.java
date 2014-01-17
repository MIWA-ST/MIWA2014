/***********************************************************************
 * Module:  BIComputer.java
 * Author:  Elodie NGUYEN THANH NHAN, Laura OLLIVIER & Chloé VASSEUR
 * Purpose: Defines the Class BIComputer
 ***********************************************************************/
package fr.epita.sigl.miwa.application.computer;

import java.util.*;
import java.util.Map.Entry;

import fr.epita.sigl.miwa.application.criteres.*;
import fr.epita.sigl.miwa.application.data.*;
import fr.epita.sigl.miwa.application.enums.EPaiementType;
import fr.epita.sigl.miwa.application.statistics.*;

public class BIComputer {
	/** @param stocks */
	public List<StockStatistic> computeStockStatistics(List<Stock> stocks) {
		List<StockStatistic> stockStatistics = new ArrayList<StockStatistic>();
		for (Stock stock : stocks){
			float stockPourcent = (stock.getStockQty() / stock.getMaxQty()) * 100;
			if (stockPourcent <= 15.0 || stockPourcent >= 85.0){
				StockStatistic stockStatistic = new StockStatistic(stock.getStore(), stock.getProductRef(), stockPourcent >= 85.0, stockPourcent <= 15.0, stock.getOrdered());
				stockStatistics.add(stockStatistic);
			}
		}
		return stockStatistics;
	}

	/** @param stocks */
	public List<SaleStatistic> computeSaleStatistics(List<Sale> sales, List<SaleStatistic> lastSaleStatistics) {
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
				TemporarySaleStatistic categoryStatistic = new TemporarySaleStatistic(sale.getSalesTotal(), sale.getSoldQty());
				categorieStatistics.put(sale.getProductCategory(), categoryStatistic);
			}
		}
		for (SaleStatistic lastSaleStatistic : lastSaleStatistics){
			SaleStatistic saleStatistic;
			String category = lastSaleStatistic.getCategorie();
			if (categorieStatistics.containsKey(category)){
				TemporarySaleStatistic temp = categorieStatistics.get(category);
				int diff = temp.getNbSoldProducts() - lastSaleStatistic.getNbSoldProducts();
				saleStatistic = new SaleStatistic(category, (float) diff / (float) lastSaleStatistic.getNbSoldProducts() * 100, temp.getCa() , (float) temp.getCa() / (float) caTotal * 100, temp.getNbSoldProducts());
				categorieStatistics.remove(category);
			} else {
				int diff = 0 - lastSaleStatistic.getNbSoldProducts();
				saleStatistic = new SaleStatistic(category, (float)diff / (float)lastSaleStatistic.getNbSoldProducts() * 100, 0, 0, 0);
			}
			saleStatistics.add(saleStatistic);
		}
		for (Entry<String, TemporarySaleStatistic> entry : categorieStatistics.entrySet()){
			TemporarySaleStatistic value = entry.getValue();
			SaleStatistic saleStatistic = new SaleStatistic(entry.getKey(), value.getNbSoldProducts() * 100, value.getCa(), (float)value.getCa() / (float)caTotal * 100, value.getNbSoldProducts());
			saleStatistics.add(saleStatistic);
		}
		return saleStatistics;
	}

	/** @param clients 
	 * @param detailSales 
	 * @param criteria */
	public List<Segmentation> computeSegmentation(List<Client> clients, List<DetailSale> detailSales, List<Critere> critere) {
		// TODO: implement
		return null;
	}

	/** @param detailSales */
	public List<PaymentStatistic> computePaymentStatistics(List<DetailSale> detailSales) {
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
		PaymentStatistic cbStatistics = new PaymentStatistic(EPaiementType.CB, caCB, (float)caCB / caTotal * 100);
		paymentStatistics.add(cbStatistics);
		PaymentStatistic cqStatistics = new PaymentStatistic(EPaiementType.CQ, caCQ, (float)caCQ / caTotal * 100);
		paymentStatistics.add(cqStatistics);
		PaymentStatistic cfStatistics = new PaymentStatistic(EPaiementType.CF, caCF, (float)caCF / caTotal * 100);
		paymentStatistics.add(cfStatistics);
		PaymentStatistic esStatistics = new PaymentStatistic(EPaiementType.ES, caES, (float)caES / caTotal * 100);
		paymentStatistics.add(esStatistics);
		return paymentStatistics;
	}
}