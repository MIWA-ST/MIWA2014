package fr.epita.sigl.miwa.application;

import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DbHandler {

	private String dbUrl;
	private String user;
	private String password;

	public DbHandler(String dbUrl, String user, String password) {
		this.dbUrl = dbUrl;
		this.user = user;
		this.password = password;
	}

	public void addNewProduct(Product p)
	{
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(dbUrl, user, password);

			stmt = conn.createStatement();
			UUID id = UUID.randomUUID();
			String ref = id.toString();
			if (ref.length() < 32)
				ref += p.getEAN();
			String sql = "INSERT INTO PRODUCT (EAN, reference, description, buyPrice, nbMin, providerNumber, categorie) "
					+ "VALUES ('" + p.getEAN() + "', '" + ref.substring(0, 32) + "', '" + p.getDescription() + "', " +
					p.getBuyPrice() + ", " + p.getNbMin() + ", " + p.getProviderNumber() + ", '" + p.getCategorie() + "');";

			//System.out.println(sql);
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			try{
				if(stmt!=null)
					stmt.close();
			} catch(SQLException se2){
			}
			try{
				if(conn!=null)
					conn.close();
			} catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
	
	public ArrayList<Product>  getAllProducts() {
		ArrayList<Product> productList = new ArrayList<Product>();
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(dbUrl, user, password);

			stmt = conn.createStatement();

			String sql = "select EAN, reference, description, buyPrice, nbMin, providerNumber from product;";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()){
				String EAN = rs.getString("EAN");				
				String reference = rs.getString("reference");
				String description = rs.getString("description");
				Float buyPrice  = rs.getFloat("buyPrice");
				int nbMin =  rs.getInt("nbMin");
				int providerNumber = rs.getInt("providerNumber");

				Product p = new Product(EAN, description, buyPrice, nbMin, reference, providerNumber);
				productList.add(p);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			try{
				if(stmt!=null)
					stmt.close();
			} catch(SQLException se2){
			}
			try{
				if(conn!=null)
					conn.close();
			} catch(SQLException se){
				se.printStackTrace();
			}
		}

		return productList;
	}
	
	public void createNewPromotion(Promotion promo, ArrayList<Product> productList) {
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(dbUrl, user, password);

			stmt = conn.createStatement();
			String getMaxPromotionId = "select MAX(idPromotion) from Promotion";
			ResultSet rs = stmt.executeQuery(getMaxPromotionId);

			int promoId = -1;
			while(rs.next()){
				promoId = rs.getInt("MAX(idPromotion)");
			}
			stmt.close();
			promoId++;

			stmt = conn.createStatement();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			String createPromo = "INSERT INTO Promotion (idPromotion, startDate, endDate, rebate) VALUES ("
					+ promoId + ", DATE('" + df.format(promo.getDate_debut()) + "'), DATE('" + df.format(promo.getDate_fin()) +"'), " + promo.getPourcentage() + ");";

			stmt.executeUpdate(createPromo);
			stmt.close();
			
			stmt = conn.createStatement();
			for (Product p : productList) {
				String queryProductHasPromo = "INSERT INTO Product_has_Promotion (Product_reference, Promotion_idPromotion) VALUES ('"
						+ p.getReference() + "', " + promoId + ");";
				stmt.addBatch(queryProductHasPromo);
			}
			stmt.executeBatch();
			stmt.close();
			conn.close();

		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			try{
				if(stmt!=null)
					stmt.close();
			} catch(SQLException se2){
			}
			try{
				if(conn!=null)
					conn.close();
			} catch(SQLException se){
				se.printStackTrace();
			}
		}

	}
	
	public ArrayList<Promotion> getPromotionsForProduct(String reference) {
		ArrayList<Promotion> promoList = new ArrayList<Promotion>();
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(dbUrl, user, password);

			stmt = conn.createStatement();

			String sql = "select idPromotion, rebate, startDate, endDate from promotion INNER JOIN Product_has_Promotion ON promotion.idPromotion = Product_has_Promotion.Promotion_idPromotion where Product_has_Promotion.Product_reference ='" + reference + "';";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()){
				int pourcent = rs.getInt("rebate");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = formatter.parse(rs.getString("startDate"));
				Date endDate  = formatter.parse(rs.getString("endDate"));
				
				Promotion p = new Promotion(startDate, endDate, pourcent);
				promoList.add(p);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			try{
				if(stmt!=null)
					stmt.close();
			} catch(SQLException se2){
			}
			try{
				if(conn!=null)
					conn.close();
			} catch(SQLException se){
				se.printStackTrace();
			}
		}

		return promoList;
	}

	
	public ArrayList<PromotionForGC> getPromotionsForGCForProduct(String reference) {
		ArrayList<PromotionForGC> promoList = new ArrayList<PromotionForGC>();
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(dbUrl, user, password);

			stmt = conn.createStatement();

			String sql = "select idPromotionGC, nbMin, rebate, startDate, endDate from promotionGC INNER JOIN Product_has_PromotionGC ON promotionGC.idPromotionGC = Product_has_PromotionGC.PromotionGC_idPromotionGC where Product_has_PromotionGC.Product_reference ='" + reference + "';";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()){
				String idPromotionGC = rs.getString("idPromotionGC");
				int nbMin = rs.getInt("nbMin");				
				float pourcent = rs.getFloat("rebate");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
				Date startDate = formatter.parse(rs.getString("startDate"));
				Date endDate  = formatter.parse(rs.getString("endDate"));
				
				PromotionForGC p = new PromotionForGC(idPromotionGC, nbMin, pourcent, startDate, endDate);
				promoList.add(p);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			try{
				if(stmt!=null)
					stmt.close();
			} catch(SQLException se2){
			}
			try{
				if(conn!=null)
					conn.close();
			} catch(SQLException se){
				se.printStackTrace();
			}
		}

		return promoList;
	}
	
	public void clearProductsForProvider(Integer providerNumber) {
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, user, password);
			stmt = conn.createStatement();

			String sql = "DELETE from Product where providerNumber = " + providerNumber + ";";

			//System.out.println(sql);
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			try{
				if(stmt!=null)
					stmt.close();
			} catch(SQLException se2){
			}
			try{
				if(conn!=null)
					conn.close();
			} catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
	
	
	public void updateProduct(String ref, Float sellPrice)
	{
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, user, password);
			stmt = conn.createStatement();

			String sql = "UPDATE Product SET sellPrice=" + sellPrice + " WHERE reference='" + ref + "';";

			//System.out.println(sql);
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally{
			try{
				if(stmt!=null)
					stmt.close();
			} catch(SQLException se2){
			}
			try{
				if(conn!=null)
					conn.close();
			} catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
}
