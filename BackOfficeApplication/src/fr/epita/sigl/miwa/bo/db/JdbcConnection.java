package fr.epita.sigl.miwa.bo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection
{
	private static JdbcConnection instance = null;
	private Connection connection = null;
	
	public static JdbcConnection getInstance()
	{
		if (instance == null)
			instance = new JdbcConnection();
		
		return instance;
	}
	
	public Connection getConnection()
	{
		try {
			if (connection.isClosed())
				OpenConnection();
		} catch (SQLException e) {
			return null;
		}
		
		return connection;
	}
	
	public boolean OpenConnection()
	{
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			return false;
		}
 
		try
		{
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/miwabo", "postgres", "root");
		}
		catch (SQLException e)
		{
 
			System.out.println("Connection Failed! Check output console");
			return false;
		}
 
		if (connection != null)
		{
			return true;
		}
		else
		{
			System.out.println("Failed to make connection!");
			return false;
		}
	}
	
	public boolean closeConnection()
	{
		try
		{
			if (connection != null)
				connection.close();
			
			return true;
		}
		catch (SQLException e)
		{
			System.out.println("Failed to close connection!");
			return false;
		}
	}
}