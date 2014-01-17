package fr.epita.sigl.miwa.application;

import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
			String sql = "INSERT INTO PRODUCT (EAN, reference, description, buyPrice, nbMin, providerNumber) "
					+ "VALUES ('" + p.getEAN() + "', '" + ref.substring(0, 32) + "', '" + p.getDescription() + "', " +
					p.getBuyPrice() + ", " + p.getNbMin() + ", " + p.getProviderNumber() + ");";

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
				String buyPrice  = Integer.toString(rs.getInt("buyPrice"));
				String nbMin =  Integer.toString(rs.getInt("nbMin"));
				Integer providerNumber = rs.getInt("providerNumber");

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
	
	
	public void updateProduct(String ref, Integer sellPrice)
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
