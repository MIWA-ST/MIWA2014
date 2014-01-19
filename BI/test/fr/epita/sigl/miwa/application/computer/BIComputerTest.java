package fr.epita.sigl.miwa.application.computer;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.epita.sigl.miwa.application.criteres.AgeValue;
import fr.epita.sigl.miwa.application.criteres.Critere;
import fr.epita.sigl.miwa.application.data.DetailSale;
import fr.epita.sigl.miwa.application.data.Product;
import fr.epita.sigl.miwa.application.data.Sale;
import fr.epita.sigl.miwa.application.data.SoldProduct;
import fr.epita.sigl.miwa.application.data.Stock;
import fr.epita.sigl.miwa.application.enums.ECritereType;
import fr.epita.sigl.miwa.application.enums.EPaiementType;
import fr.epita.sigl.miwa.application.printer.BIPrinter;
import fr.epita.sigl.miwa.application.statistics.CategorieStatistic;
import fr.epita.sigl.miwa.application.statistics.PaymentStatistic;
import fr.epita.sigl.miwa.application.statistics.SaleStatistic;
import fr.epita.sigl.miwa.application.statistics.Segmentation;
import fr.epita.sigl.miwa.application.statistics.StockStatistic;

public class BIComputerTest {

	private BIComputer computer = new BIComputer();

	@Test
	public void computeStockStatisticsTest() {
		List<Stock> stocks = new ArrayList<Stock>();
		Stock stock1 = new Stock(1, "1", true, 100, 100, "Mag 1");
		Stock stock2 = new Stock(2, "2", false, 5, 100, "Mag 1");
		Stock stock3 = new Stock(3, "3", true, 5, 100, "Mag 1");
		stocks.add(stock1);
		stocks.add(stock2);
		stocks.add(stock3);
		List<StockStatistic> statistics = computer.computeStockStatistics(stocks);
		assertTrue(statistics.get(0).isPlein() && statistics.get(0).isCommande());
		assertTrue(statistics.get(1).isVide() && !statistics.get(1).isCommande());
		assertTrue(statistics.get(2).isVide() && statistics.get(2).isCommande());
	}

	@Test
	public void computeSaleStatisticsTest(){
		List<Sale> sales = new ArrayList<Sale>();
		Sale sale1 = new Sale(1, new Date(), "Mag 1", 50, "1", 100, 100000);
		sales.add(sale1);
		Sale sale2 = new Sale(2, new Date(), "Mag 1", 100, "2", 100, 100000);
		sales.add(sale2);
		Sale sale3 = new Sale(3, new Date(), "Mag 1", 3, "3", 100, 100000);
		sales.add(sale3);
		Sale sale4 = new Sale(4, new Date(), "Mag 1", 50, "1", 100, 100000);
		sales.add(sale4);
		Sale sale5 = new Sale(5, new Date(), "Mag 1", 50, "5", 100, 100000);
		sales.add(sale5);
		List<SaleStatistic> lastSaleStatistics = new ArrayList<SaleStatistic>();
		SaleStatistic stat1 = new SaleStatistic(null, "1", 1, 100000, 10, 50);
		lastSaleStatistics.add(stat1);
		SaleStatistic stat2 = new SaleStatistic(null, "2", 1, 100000, 10, 150);
		lastSaleStatistics.add(stat2);
		SaleStatistic stat3 = new SaleStatistic(null, "4", 1, 100000, 10, 150);
		lastSaleStatistics.add(stat3);
		SaleStatistic stat4 = new SaleStatistic(null, "5", 1, 100000, 10, 50);
		lastSaleStatistics.add(stat4);
		List<SaleStatistic> statistics = computer.computeSaleStatistics(sales, lastSaleStatistics);
		Assert.assertEquals(50f / 50f * 100, statistics.get(0).getEvolution(), 0);
		Assert.assertEquals(-50f / 150f * 100, statistics.get(1).getEvolution(), 0);
		Assert.assertEquals(-150f / 150f * 100, statistics.get(2).getEvolution(), 0);
		Assert.assertEquals(0f / 50f * 100, statistics.get(3).getEvolution(), 0);
		Assert.assertEquals(3f * 100, statistics.get(4).getEvolution(), 0);
		Assert.assertEquals(200000f / 500000f * 100, statistics.get(0).getCaPourcent(), 0);
		Assert.assertEquals(100000f / 500000f * 100, statistics.get(1).getCaPourcent(), 0);
		Assert.assertEquals(0f / 500000f * 100, statistics.get(2).getCaPourcent(), 0);
		Assert.assertEquals(100000f / 500000f * 100, statistics.get(3).getCaPourcent(), 0);
		Assert.assertEquals(100000f / 500000f * 100, statistics.get(4).getCaPourcent(), 0);
	}

	@Test
	public void computePaymentStatisticsTest(){
		List<DetailSale> detailSales = new ArrayList<DetailSale>();
		DetailSale sale1 = new DetailSale(1, EPaiementType.CB, null, 100, "Mag 1", null, null);
		detailSales.add(sale1);
		DetailSale sale2 = new DetailSale(2, EPaiementType.CB, null, 50, "Mag 1", null, null);
		detailSales.add(sale2);
		DetailSale sale3 = new DetailSale(3, EPaiementType.CQ, null, 100, "Mag 1", null, null);
		detailSales.add(sale3);
		DetailSale sale4 = new DetailSale(4, EPaiementType.CF, null, 150, "Mag 1", null, null);
		detailSales.add(sale4);
		DetailSale sale5 = new DetailSale(5, EPaiementType.ES, null, 25, "Mag 1", null, null);
		detailSales.add(sale5);
		List<PaymentStatistic> statistics = computer.computePaymentStatistics(detailSales);
		Assert.assertEquals(150f / 425f * 100, statistics.get(0).getCaPourcent(), 0);
		Assert.assertEquals(100f / 425f * 100, statistics.get(1).getCaPourcent(), 0);
		Assert.assertEquals(150f / 425f * 100, statistics.get(2).getCaPourcent(), 0);
		Assert.assertEquals(25f / 425f * 100, statistics.get(3).getCaPourcent(), 0);
	}
	
	@Test
	public void computeSegmentationTest(){
		List<SoldProduct> soldProducts = new ArrayList<SoldProduct>();
		SoldProduct sp1 = new SoldProduct("1", 15);
		soldProducts.add(sp1);
		SoldProduct sp2 = new SoldProduct("3", 10);
		soldProducts.add(sp2);
		SoldProduct sp3 = new SoldProduct("2", 15);
		soldProducts.add(sp3);
		List<DetailSale> detailSales = new ArrayList<DetailSale>();
		DetailSale ds1 = new DetailSale(1, EPaiementType.CB, null, 0, null, 1, soldProducts);
		detailSales.add(ds1);
		DetailSale ds2 = new DetailSale(2, EPaiementType.CB, null, 0, null, 1, soldProducts);
		detailSales.add(ds2);
		DetailSale ds3 = new DetailSale(3, EPaiementType.CB, null, 0, null, 2, soldProducts);
		detailSales.add(ds3);
		List<Product> products = new ArrayList<Product>();
		Product p1 = new Product("1", 0f, 0f, 0f, "1");
		products.add(p1);
		Product p2 = new Product("2", 0f, 0f, 0f, "2");
		products.add(p2);
		Product p3 = new Product("3", 0f, 0f, 0f, "2");
		products.add(p3);
		List<Segmentation> segmentations = computer.computeSegmentation(detailSales, products);
		assertEquals(2, segmentations.size());
		// Client 1
		Segmentation client1 = segmentations.get(0);
		assertEquals(new Integer(1), client1.getClientNumero());
		assertEquals(2, client1.getCategorieStatistics().size());
		// Client 2
		Segmentation client2 = segmentations.get(1);
		assertEquals(new Integer(2), client2.getClientNumero());
		assertEquals(2, client2.getCategorieStatistics().size());
	}

	@Test
	public void createSegmentationTest(){
		BIPrinter printer = new BIPrinter();
		List<Critere> criteres = new ArrayList<Critere>();
		Critere c1 = new Critere(ECritereType.AGE, new AgeValue(30, 50));
		criteres.add(c1);
		Critere c2 = new Critere(ECritereType.GEO, "95");
		criteres.add(c2);
		Critere c3 = new Critere(ECritereType.SEXE, "M");
		criteres.add(c3);
		Critere c4 = new Critere(ECritereType.SF, "marie");
		criteres.add(c4);
		Critere c5 = new Critere(ECritereType.ENF, "true");
		criteres.add(c5);
		Critere c6 = new Critere(ECritereType.FID, "1");
		criteres.add(c6);
		List<Segmentation> segmentations = new ArrayList<Segmentation>();
		List<CategorieStatistic> categorieStatistics = new ArrayList<CategorieStatistic>();
		CategorieStatistic cs1 = new CategorieStatistic("1", 100);
		categorieStatistics.add(cs1);
		CategorieStatistic cs2 = new CategorieStatistic("2", 42);
		categorieStatistics.add(cs2);
		Segmentation s1 = new Segmentation(null, 1, categorieStatistics);
		segmentations.add(s1);
		Segmentation s2 = new Segmentation(null, 2, categorieStatistics);
		segmentations.add(s2);
		printer.createSegmentationFile(criteres, segmentations);
	}
}
