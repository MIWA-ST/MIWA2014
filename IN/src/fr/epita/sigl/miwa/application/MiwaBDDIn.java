package fr.epita.sigl.miwa.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class MiwaBDDIn {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	private String url;
	private String user;
	private String password;
	private Connection conn;
	private static MiwaBDDIn inst;
	
	private MiwaBDDIn()
	{
		
	}
	
	private MiwaBDDIn(String url, String user, String pw)
	{
		this.url = url;
		this.user = user;
		this.password = pw;
	}
	
	public static MiwaBDDIn getInstance()
	{
		String url = "jdbc:postgresql://localhost:5432/MIWA2014_IN";
		String user = "postgres";
		String password = "root";
		
		if (inst == null)
			inst = new MiwaBDDIn(url, user, password);
		return inst;
	}
	
	public Boolean connection()
	{
		try {
			setConn(DriverManager.getConnection(url, user, password));

            return true;
		} catch (SQLException e) {
			LOGGER.info("Erreur SQL : " + e.getMessage());
			return false;
		}
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
	
	public ResultSet executeStatement_result(String query)
	{
		Statement st;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Boolean executeStatement(String query)
	{
		Statement st;
		try {
			st = conn.createStatement();
			st.execute(query);
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public void close()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			LOGGER.info("***** Erreur SQL : " + e.getMessage());
		}
	}
}
