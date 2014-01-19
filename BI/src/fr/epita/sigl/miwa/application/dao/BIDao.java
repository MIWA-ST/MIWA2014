package fr.epita.sigl.miwa.application.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.criteres.Critere;
import fr.epita.sigl.miwa.application.data.Client;
import fr.epita.sigl.miwa.application.data.DetailSale;
import fr.epita.sigl.miwa.application.data.Product;
import fr.epita.sigl.miwa.application.data.ProductCategory;
import fr.epita.sigl.miwa.application.data.Promotion;
import fr.epita.sigl.miwa.application.data.Sale;
import fr.epita.sigl.miwa.application.data.Stock;
import fr.epita.sigl.miwa.application.statistics.PaymentStatistic;
import fr.epita.sigl.miwa.application.statistics.SaleStatistic;
import fr.epita.sigl.miwa.application.statistics.Segmentation;
import fr.epita.sigl.miwa.application.statistics.StockStatistic;

public class BIDao {
	private static final Logger LOGGER = Logger.getLogger(BIDao.class.getName());

	private String dbUrl;

	private String user;

	private String password = "admin";

	private Connection conn;

	public BIDao(){
		dbUrl = "jdbc:postgresql://localhost:5432/miwa";
		user = "admin";
		password = "admin";
	}

	private void connect(){
		try {
			conn = DriverManager.getConnection(dbUrl, user, password);
		} catch (SQLException e) {
			LOGGER.severe("Cannot connect to database");
		}
	}

	public void insertStocks(List<Stock> stocks) {
		Statement stmt = null;
		try {
			connect();
			stmt = conn.createStatement();
			String SQL = "";
			stmt.execute(SQL);
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout des stocks");
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
			}
		}
	}

	public void insertPromotions(List<Promotion> promotions) {
		// TODO Auto-generated method stub

	}

	public void insertSales(List<Sale> sales) {
		// TODO Auto-generated method stub

	}

	public void insertClients(List<Client> clients) {
		// TODO Auto-generated method stub

	}

	public void insertProducts(List<Product> products) {
		// TODO Auto-generated method stub

	}

	public void insertProductsCategories(List<ProductCategory> categories) {
		// TODO Auto-generated method stub

	}

	public void insertDetailSales(List<DetailSale> detailSales) {
		// TODO Auto-generated method stub

	}

	public void insertSaleStatistics(List<SaleStatistic> saleStatistics) {
		// TODO Auto-generated method stub

	}

	public void insertStockStatistics(
			List<StockStatistic> stockStatistics) {
		// TODO Auto-generated method stub

	}

	public void insertSegmentation(List<Segmentation> segmentations) {
		// TODO Auto-generated method stub

	}

	public void insertPaiementStatistics(
			List<PaymentStatistic> paiementStatistics) {
		// TODO Auto-generated method stub

	}

	public List<Client> getAllClients(){
		return null;
	}

	public List<DetailSale> getAllDetailSales() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Stock> getLastStocks() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SaleStatistic> getLastSaleStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Sale> getSalesOfToday() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Client> getClientByCriteria(List<Critere> criteres) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DetailSale> getDetailSalesForClients(List<Client> clients) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return null;
	}
}
