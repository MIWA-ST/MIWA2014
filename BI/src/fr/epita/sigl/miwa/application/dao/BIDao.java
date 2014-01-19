package fr.epita.sigl.miwa.application.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.criteres.Critere;
import fr.epita.sigl.miwa.application.data.Client;
import fr.epita.sigl.miwa.application.data.DetailSale;
import fr.epita.sigl.miwa.application.data.Product;
import fr.epita.sigl.miwa.application.data.Promotion;
import fr.epita.sigl.miwa.application.data.Sale;
import fr.epita.sigl.miwa.application.data.SoldProduct;
import fr.epita.sigl.miwa.application.data.Stock;
import fr.epita.sigl.miwa.application.enums.EPaiementType;
import fr.epita.sigl.miwa.application.statistics.CategorieStatistic;
import fr.epita.sigl.miwa.application.statistics.PaymentStatistic;
import fr.epita.sigl.miwa.application.statistics.SaleStatistic;
import fr.epita.sigl.miwa.application.statistics.Segmentation;
import fr.epita.sigl.miwa.application.statistics.StockStatistic;

public class BIDao {
	private static final Logger LOGGER = Logger.getLogger(BIDao.class.getName());

	private String dbUrl;

	private String user;

	private String password;

	private Connection conn;

	public BIDao(){
		dbUrl = "jdbc:postgresql://localhost:5432/miwa";
		user = "postgres";
		password = "admin";
	}

	private boolean connect(){
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(dbUrl, user, password);
		} catch (SQLException e) {
			LOGGER.severe("Cannot connect to database");
			LOGGER.severe("Error is : " + e);
		} catch (ClassNotFoundException e) {
			LOGGER.severe("Class not found");
			LOGGER.severe("Error is : " + e);
		}
		return conn != null;
	}

	public void insertStocks(List<Stock> stocks) {
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				for (Stock stock : stocks){
					String SQL = "INSERT INTO stock(productreference, ordered, stockqty, maxqty, store) "
							+ "VALUES ('" + stock.getProductRef() + "', '" + stock.getOrdered() + "', '" + stock.getStockQty()
							+ "', '" + stock.getMaxQty() + "', '" + stock.getStore() +"');";
					stmt.execute(SQL);				
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout des stocks");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
	}

	public void insertPromotions(List<Promotion> promotions) {
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				for (Promotion promotion : promotions){
					String SQL = "INSERT INTO promotion(productreference, begindate, enddate, percentage, store) "
							+ "VALUES ('" + promotion.getProductReference() + "', '" + promotion.getBeginDate() 
							+ "', '" + promotion.getEndDate() + "', '" + promotion.getPercentage() + "', '" + promotion.getStore()
							+ "')";
					stmt.execute(SQL);				
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout des promotions");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
	}

	public void insertSales(List<Sale> sales) {
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				for (Sale sale : sales){
					String SQL = "INSERT INTO sale(datetime, store, soldqty, categoryname, suppliertotal, salestotal) "
							+ "VALUES ('" + sale.getDateTime() + "', '" + sale.getStore() + "', '" + sale.getSoldQty()
							+ "', '" + sale.getProductCategory() + "', '" + sale.getSupplierTotal() + "', '" + sale.getSalesTotal()
							+ "')";
					stmt.execute(SQL);				
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout des promotions");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
	}

	public void insertClients(List<Client> clients) {
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				for (Client client : clients){
					// check if client already exists
					String SQL = "SELECT numero FROM client WHERE client.numero = '" + client.getNumero() + "';";
					ResultSet results = stmt.executeQuery(SQL);
					if (results.next()){
						SQL = "UPDATE client SET title = '" + client.getTitle() + "', birthdate = '" + client.getBirthDate() + "', zipcode = '"
								+ client.getZipcode() + "', maritalStatus = '" + client.getMaritalStatus() + "', childrennb = '" + client.getChildrenNb()
								+ "', loyaltytype = '" + client.getLoyaltyType() + "' WHERE numero = '" + client.getNumero() + "';";
						stmt.execute(SQL);
					} else {
						SQL = "INSERT INTO client(numero, title, birthdate, zipcode, maritalstatus, childrennb, loyaltytype) "
								+ "VALUES (' " + client.getNumero() + "', '" + client.getTitle() + "', '" + client.getBirthDate()
								+ "', '" + client.getZipcode() + "', '" + client.getMaritalStatus() + "', '" + client.getChildrenNb()
								+ "', '" + client.getLoyaltyType() + "')";
						stmt.execute(SQL);	
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout des clients");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
	}

	public void insertProducts(List<Product> products) {
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				for (Product product : products){
					// check if product already exists
					String SQL = "SELECT reference FROM product WHERE product.reference = '" + product.getReference() + "';";
					ResultSet results = stmt.executeQuery(SQL);
					if (results.next()){
						SQL = "UPDATE product SET buyingprice = '" + product.getBuyingPrice() + "', sellingprice = '" + product.getSellingPrice()
								+ "', categoryname = '" + product.getCategoryName() + "', margin = '" + product.getMargin() + "' WHERE reference = '" + product.getReference() + "';";
						stmt.execute(SQL);
					}
					SQL = "INSERT INTO product(reference, buyingprice, sellingprice, categoryname, margin) "
							+ "VALUES ('" + product.getReference() + "', '" + product.getBuyingPrice() + "', '" + product.getSellingPrice() + "', '" + product.getCategoryName() + "', '" + product.getMargin() + "');";
					stmt.execute(SQL);				
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout des produits");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
	}

	public void insertDetailSales(List<DetailSale> detailSales) {
		PreparedStatement pStmt = null;
		Statement stmt = null;
		try {
			if (connect()){
				for (DetailSale detailSale : detailSales){
					String SQL = "INSERT INTO detailsale(paymentmean, datetime, total, store, clientnumero) "
							+ "VALUES ('" + detailSale.getPaymentMean() + "', '" + detailSale.getDate() + "', '"
							+ detailSale.getTotal() + "', '" + detailSale.getStore() + "', '" + detailSale.getClientNb() + "');";
					pStmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
					pStmt.executeUpdate();
					ResultSet result = pStmt.getGeneratedKeys();
					Integer id = null;
					if (result.next()){
						id = result.getInt(1);
					}
					if (id != null){
						stmt = conn.createStatement();
						for (SoldProduct soldProduct : detailSale.getProductList()){
							SQL = "INSERT INTO product_has_detailsale(productreference, detailsaleid, quantity) "
									+ "VALUES ('" + soldProduct.getProductRef() + "', '" + id + "', '" + soldProduct.getQuantity() + "');";
							stmt.execute(SQL);
						}
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout des ventes détaillées");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (pStmt != null){
					pStmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
	}

	public void insertSaleStatistics(List<SaleStatistic> saleStatistics) {
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				for (SaleStatistic saleStatistic : saleStatistics){
					String SQL = "INSERT INTO statisticsale(datetime, percentageca, evolution, ca, soldqty, categoryname) "
							+ "VALUES ('"+ saleStatistic.getDateTime() + "', '" + saleStatistic.getCaPourcent() + "', '"
							+ saleStatistic.getEvolution() + "', '" + saleStatistic.getCa() + "', '" + saleStatistic.getNbSoldProducts()
							+"', '" + saleStatistic.getCategorie() + "')";
					stmt.execute(SQL);				
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout des statistiques de ventes");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}

	}

	public void insertStockStatistics(List<StockStatistic> stockStatistics) {
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				for (StockStatistic stockStatistic : stockStatistics){
					String SQL = "INSERT INTO statisticstock(datetime, iswide, isempty, ordered, store, productreference) "
							+ "VALUES ('" + stockStatistic.getDateTime() + "', '" + stockStatistic.isPlein() + "', '"
							+ stockStatistic.isVide() + "', '" + stockStatistic.isCommande() + "', '" + stockStatistic.getStore()
							+ "', '" + stockStatistic.getArticle() + "')";
					stmt.execute(SQL);				
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout des statistiques de stocks");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}

	}

	public void insertSegmentation(List<Segmentation> segmentations) {
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				for (Segmentation segmentation : segmentations){
					for (CategorieStatistic categorieStatistic : segmentation.getCategorieStatistics()){
						String SQL = "INSERT INTO client_has_segmentation(datetime, clientnumero, categoryname, quantity) "
								+ "VALUES ('" + segmentation.getDateTime() + "', '" + segmentation.getClientNumero() + "', '"
								+ categorieStatistic.getRef() + "', '" + categorieStatistic.getAchat() + "')";
						stmt.execute(SQL);				
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout de la segmentation");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
	}

	public void insertPaiementStatistics(List<PaymentStatistic> paiementStatistics) {
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				for (PaymentStatistic paymentStatistic : paiementStatistics){
					String SQL = "INSERT INTO statisticpayment(datetime, paymentmean, percentageca, ca) "
							+ "VALUES ('" + paymentStatistic.getDateTime() + "', '" + paymentStatistic.getType() + "', '"
							+ paymentStatistic.getCaPourcent() + "', '" + paymentStatistic.getCa() + "')";
					stmt.execute(SQL);				
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant l'ajout des statistiques de paiement");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
	}

	public List<DetailSale> getAllDetailSales() {
		List<DetailSale> detailSales = new ArrayList<DetailSale>();
		Statement stmtDetailSale = null;
		Statement stmtProduct = null;
		try {
			if (connect()){
				stmtDetailSale = conn.createStatement();
				String SQL = "SELECT id, paymentmean, datetime, total, store, clientnumero FROM detailsale;";
				ResultSet result = stmtDetailSale.executeQuery(SQL);
				while (result.next()){
					DetailSale detailSale = new DetailSale();
					Integer id = result.getInt("id");
					detailSale.setId(id);
					String paymentType = result.getString("paymentmean");
					detailSale.setPaymentMean(EPaiementType.fromString(paymentType));
					Integer total = result.getInt("total");
					detailSale.setTotal(total);
					String store = result.getString("store");
					detailSale.setStore(store);
					Integer clientNb = result.getInt("clientnumero");
					detailSale.setClientNb(clientNb);


					SQL = "SELECT productreference, detailsaleid, quantity FROM product_has_detailsale WHERE detailsaleid = '" + id + "';";
					stmtProduct = conn.createStatement();
					ResultSet result1 = stmtProduct.executeQuery(SQL);
					List<SoldProduct> soldProducts = new ArrayList<SoldProduct>();
					while (result1.next()){
						SoldProduct soldProduct = new SoldProduct();
						String productRef = result1.getString("productreference");
						soldProduct.setProductRef(productRef);
						Integer quantity = result1.getInt("quantity");
						soldProduct.setQuantity(quantity);
						soldProducts.add(soldProduct);
					}
					detailSale.setProductList(soldProducts);
					detailSales.add(detailSale);
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant la sélection des ventes détaillées");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmtDetailSale != null){
					stmtDetailSale.close();
				}
				if (stmtProduct != null){
					stmtProduct.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
		return detailSales;
	}

	public List<Stock> getStockOfToday() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SaleStatistic> getSaleStatisticsOfYesterday() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Sale> getSalesOfToday() {
		List<Sale> sales = new ArrayList<Sale>();
		Date today = ClockClient.getClock().getHour();
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Calendar dateBegin = Calendar.getInstance();
		dateBegin.setTime(today);
		dateBegin.set(Calendar.SECOND, 0);
		dateBegin.set(Calendar.MINUTE, 0);
		dateBegin.set(Calendar.HOUR, 0);
		dateBegin.set(Calendar.HOUR_OF_DAY, 0);
		String beginDate = format.format(dateBegin.getTime());
		Calendar dateEnd = Calendar.getInstance();
		dateEnd.setTime(today);
		dateEnd.set(Calendar.SECOND, 59);
		dateEnd.set(Calendar.MINUTE, 59);
		dateEnd.set(Calendar.HOUR, 23);
		dateEnd.set(Calendar.HOUR_OF_DAY, 23);
		String endDate = format.format(dateEnd.getTime());
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				String SQL = "SELECT id, datetime, store, soldqty, categoryname, suppliertotal, salestotal FROM sale WHERE datetime >= '" + beginDate + "' AND datetime <= '" + endDate + "';";
				ResultSet result = stmt.executeQuery(SQL);
				while (result.next()){
					Sale sale = new Sale();
					Integer id = result.getInt("id");
					sale.setId(id);
					Date dateTime = result.getDate("datetime");
					sale.setDateTime(dateTime);
					String store = result.getString("store");
					sale.setStore(store);
					Integer soldQty = result.getInt("soldQty");
					sale.setSoldQty(soldQty);
					String categoryName = result.getString("categoryname");
					sale.setProductCategory(categoryName);
					Integer supplierTotal = result.getInt("supplierTotal");
					sale.setSupplierTotal(supplierTotal);
					Integer salesTotal = result.getInt("salestotal");
					sale.setSalesTotal(salesTotal);

					sales.add(sale);
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant la sélection ventes du jour");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
		return sales;
	}

	public List<Client> getClientByCriteria(List<Critere> criteres) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DetailSale> getDetailSalesForClients(List<Client> clients) {
		List<DetailSale> detailSales = new ArrayList<DetailSale>();
		Statement stmtDetailSale = null;
		Statement stmtProduct = null;
		try {
			if (connect()){
				for (Client client : clients){

					stmtDetailSale = conn.createStatement();
					String SQL = "SELECT id, paymentmean, datetime, total, store, clientnumero FROM detailsale WHERE clientnumero = '" + client.getNumero() + "';";
					ResultSet result = stmtDetailSale.executeQuery(SQL);
					while (result.next()){
						DetailSale detailSale = new DetailSale();
						Integer id = result.getInt("id");
						detailSale.setId(id);
						String paymentType = result.getString("paymentmean");
						detailSale.setPaymentMean(EPaiementType.fromString(paymentType));
						Integer total = result.getInt("total");
						detailSale.setTotal(total);
						String store = result.getString("store");
						detailSale.setStore(store);
						Integer clientNb = result.getInt("clientnumero");
						detailSale.setClientNb(clientNb);


						SQL = "SELECT productreference, detailsaleid, quantity FROM product_has_detailsale WHERE detailsaleid = '" + id + "';";
						stmtProduct = conn.createStatement();
						ResultSet result1 = stmtProduct.executeQuery(SQL);
						List<SoldProduct> soldProducts = new ArrayList<SoldProduct>();
						while (result1.next()){
							SoldProduct soldProduct = new SoldProduct();
							String productRef = result1.getString("productreference");
							soldProduct.setProductRef(productRef);
							Integer quantity = result1.getInt("quantity");
							soldProduct.setQuantity(quantity);
							soldProducts.add(soldProduct);
						}
						detailSale.setProductList(soldProducts);
						detailSales.add(detailSale);
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant la sélection des ventes détaillées par client");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmtDetailSale != null){
					stmtDetailSale.close();
				}
				if (stmtProduct != null){
					stmtProduct.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
		return detailSales;
	}

	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<Product>();
		Statement stmt = null;
		try {
			if (connect()){
				stmt = conn.createStatement();
				String SQL = "SELECT reference, buyingprice, sellingprice, categoryname, margin FROM product;";
				ResultSet result = stmt.executeQuery(SQL);
				while (result.next()){
					Product product = new Product();
					String reference = result.getString("reference");
					product.setReference(reference);
					float buyingPrice = result.getFloat("buyingprice");
					product.setBuyingPrice(buyingPrice);
					float sellingPrice = result.getFloat("sellingprice");
					product.setSellingPrice(sellingPrice);
					String categoryName = result.getString("categoryName");
					product.setCategoryName(categoryName);
					float margin = result.getFloat("margin");
					product.setMargin(margin);

					products.add(product);
				}
			}
		} catch (SQLException e) {
			LOGGER.severe("Erreur pendant la sélection des produits");
			LOGGER.severe("Error is : " + e);
		} finally {
			try {
				if (stmt != null){
					stmt.close();
				}
				conn.close();
			} catch (SQLException e) {
				LOGGER.severe("Erreur pendant la fermeture de la connexion");
				LOGGER.severe("Error is : " + e);
			}
		}
		return products;
	}
}
