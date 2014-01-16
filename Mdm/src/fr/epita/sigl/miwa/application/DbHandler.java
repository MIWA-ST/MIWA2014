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
			String sql = "INSERT INTO PRODUCT (reference, description, buyPrice, nbMin) "
					+ "VALUES ('" + ref.substring(0, 32) + "', '" + p.getDescription() + "', " + p.getBuyPrice() + ", " + p.getNbMin() + ");";

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

			String sql = "select EAN, reference, description, buyPrice, nbMin from product;";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()){
				String EAN = rs.getString("EAN");				
				String reference = rs.getString("reference");
				String description = rs.getString("description");
				String buyPrice  = Integer.toString(rs.getInt("buyPrice"));
				String nbMin =  Integer.toString(rs.getInt("nbMin"));
		

				Product p = new Product(EAN, description, buyPrice, nbMin, reference);
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
}
