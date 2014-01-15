package fr.epita.sigl.miwa.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHandler {

	private String dbUrl;
	private String user;
	private String password;
	
	public DbHandler(String dbUrl, String user, String password) {
		this.dbUrl = dbUrl;
		this.user = user;
		this.password = password;
	}

	public void addNewProduct(String EAN, String description, String buyPrice)
	{
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(dbUrl, user, password);

			stmt = conn.createStatement();
			//TODO: Ajouter les produits dans la base de données
			String sql = "INSERT INTO PRODUCT (reference, description, buyPrice) VALUES ('" + EAN + "', '" + description + "', " + buyPrice +");";
			stmt.executeUpdate(sql);
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
