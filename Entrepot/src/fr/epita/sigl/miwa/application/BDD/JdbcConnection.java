package fr.epita.sigl.miwa.application.BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.epita.sigl.miwa.application.bo.Article;
import fr.epita.sigl.miwa.application.bo.CommandeInternet;
import fr.epita.sigl.miwa.application.bo.LivraisonFournisseur;
import fr.epita.sigl.miwa.application.bo.ReassortBO;

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
	
	public void getConnection()
	{
		try
		{
			Class.forName("org.postgresql.Driver");
 
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("jar de postgeSQL pas trouvé, à ajouter dans le path");
			e.printStackTrace();
			return;
		}
 
		try
		{
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/miwa", "postgres", "root");
		}
		catch (SQLException e)
		{
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
 
		if (connection != null)
			System.out.println("Connexion PostgreSQL ouverte");
		else
			System.out.println("Failed to make connection!");
	}
	
	public void closeConnection()
	{
		try
		{
			if (connection != null)
				connection.close();
			
			System.out.println("Connection closed !");
		}
		catch (SQLException e)
		{
			System.out.println("Failed to close connection!");
			e.printStackTrace();
		}
	}
	
	public void insertCommandeInternet(CommandeInternet command)
	{
		try
		{
			System.out.println("insertion commande internet");
			if (connection != null)
			{
				String request = "INSERT INTO commandeinternet (commandnumber, datebc, datebl, customerref, customerlastname, customerfirstname, customeraddress) VALUES (?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, command.getCommandNumber());
				statement.setString(2, command.getDateBC());
				statement.setString(3, command.getDateBL());
				statement.setString(4, command.getCustomerRef());
				statement.setString(5, command.getCustomerLastname());
				statement.setString(6, command.getCustomerFirstname());
				statement.setString(7, command.getCustomerAddress());

				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("Nouvelle commande ajoutée en base !");
					for (Article a : command.getArticles())
					{
						String request2 = "INSERT INTO article (articleref, category) VALUES (?, ?)";
						
						statement = connection.prepareStatement(request2);
						statement.setString(1, a.getReference());
						statement.setString(2, a.getCategory());
						statement.executeUpdate();
						
						String request3 = "INSERT INTO commandeinternet_article (articleref, commandref, quantity) VALUES (?, ?, ?)";
						statement = connection.prepareStatement(request3);
						statement.setString(1, a.getReference());
						statement.setString(2, command.getCommandNumber());
						statement.setString(3, a.getQuantity());
						statement.executeUpdate();
					}
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	public void insertLivraisonFournisseur(LivraisonFournisseur command)
	{
		try
		{
			System.out.println("insertion livraison fournisseur");
			if (connection != null)
			{
				String request = "INSERT INTO livraisonfournisseur (commandnumber, datebc, datebl) VALUES (?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, command.getCommandNumber());
				statement.setString(2, command.getDateBC());
				statement.setString(3, command.getDateBL());

				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("Nouvelle commande ajoutée en base !");
					for (Article a : command.getArticles())
					{
						String request2 = "INSERT INTO article (articleref, category) VALUES (?, ?)";
						
						statement = connection.prepareStatement(request2);
						statement.setString(1, a.getReference());
						statement.setString(2, a.getCategory());
						statement.executeUpdate();
						
						String request3 = "INSERT INTO livraisonfournisseur_article (articleref, commandref, quantity) VALUES (?, ?, ?)";
						statement = connection.prepareStatement(request3);
						statement.setString(1, a.getReference());
						statement.setString(2, command.getCommandNumber());
						statement.setString(3,  a.getQuantity());
						statement.executeUpdate();
					}
				}
				else
					System.out.println("commande fournisseur pas ajoutée en base");
			}
			else
				System.out.println("connexion nulle");
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	public void insertReassortBO(ReassortBO command)
	{
		try
		{
			System.out.println("insertion reassort bo");
			if (connection != null)
			{
				String request = "INSERT INTO reassortbo (commandnumber, datebc, datebl, backofficeref, backofficephone, backofficeaddress) VALUES (?, ?, ?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, command.getCommandNumber());
				statement.setString(2, command.getDateBC());
				statement.setString(3, command.getDateBL());
				statement.setString(4, command.getBackOfficeRef());
				statement.setString(5, command.getBackOfficePhone());
				statement.setString(6, command.getBackOfficeAddress());
				
				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("Nouvelle commande ajoutée en base !");
					for (Article a : command.getArticles())
					{
						String request2 = "INSERT INTO article (articleref, category) VALUES (?, ?)";
						
						statement = connection.prepareStatement(request2);
						statement.setString(1, a.getReference());
						statement.setString(2, a.getCategory());
						statement.executeUpdate();
						
						String request3 = "INSERT INTO reassortbo_article (articleref, commandref, quantity) VALUES (?, ?, ?)";
						statement = connection.prepareStatement(request3);
						statement.setString(1, a.getReference());
						statement.setString(2, command.getCommandNumber());
						statement.setString(3,  a.getQuantity());
						statement.executeUpdate();
					}
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
}
