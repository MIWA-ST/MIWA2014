package fr.epita.sigl.miwa.application.BDD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import fr.epita.sigl.miwa.application.crm.TicketReduc;
import fr.epita.sigl.miwa.application.crm.LivraisonFournisseur;
import fr.epita.sigl.miwa.application.crm.ReassortBO;
import fr.epita.sigl.miwa.application.object.Article;
import fr.epita.sigl.miwa.application.object.CarteFidelite;
import fr.epita.sigl.miwa.application.object.Client;
import fr.epita.sigl.miwa.application.object.Segmentation;


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
		System.out.println("-------- PostgreSQL " + "JDBC Connection Testing ------------");
 
		try
		{
			Class.forName("org.postgresql.Driver");
 
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();
			return;
		}
 
		System.out.println("PostgreSQL JDBC Driver Registered!");
 
		try
		{
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/MIWA", "postgres", "plop");
		}
		catch (SQLException e)
		{
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
 
		if (connection != null)
			System.out.println("You made it, take control your database now!");
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
	
	public void insertCommandeInternet(Segmentation command)
	{
		try
		{
			System.out.println("insertion commande internet");
			if (connection != null)
			{
				String request = "INSERT INTO segmentation (commandnumber, datebc, datebl, customerref, customerlastname, customerfirstname, customeraddress) VALUES (?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				/*statement.setString(1, command.getCommandNumber());
				statement.setString(2, command.getDateBC());
				statement.setString(3, command.getDateBL());
				statement.setString(4, command.getCustomerRef());
				statement.setString(5, command.getCustomerLastname());
				statement.setString(6, command.getCustomerFirstname());
				statement.setString(7, command.getCustomerAddress());
*/
				/*int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("Nouvelle commande ajoutée en base !");
					for (TicketReduc a : command.getArticles())
					{
						String request2 = "INSERT INTO article (articleref, category) VALUES (?, ?)";
						
						statement = connection.prepareStatement(request2);
						statement.setString(1, a.getReference());
						statement.setString(2, a.getCategory());
						statement.executeUpdate();
						
						String request3 = "INSERT INTO commandeinternet_article (articleref, commandref) VALUES (?, ?)";
						statement = connection.prepareStatement(request3);
						statement.setString(1, a.getReference());
						statement.setString(2, command.getCommandNumber());
						statement.executeUpdate();
					}
				}*/
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	
	public void insertCarteFed(CarteFidelite carte)
	{
		try
		{
			System.out.println("insertion carte de fidelite");
			if (connection != null)
			{
				String request = "INSERT INTO cartefed (type, limite_m, limite_tot, echellon) VALUES (?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, carte.getType());
				statement.setString(2, Integer.toString(carte.getLimite_m()));
				statement.setString(3, Integer.toString(carte.getLimite_tot()));
				statement.setString(4, Integer.toString(carte.getEchellon()));

				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("Nouvelle carte ajoutée en base !");
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	public void updateCarteFed(CarteFidelite carte)
	{
		try
		{
			System.out.println("update carte de fidelite");
			if (connection != null)
			{
				String request = "UPDATE Client SET limite_m=?, limite_tot=?, echellon=? WHERE type = ?";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, Integer.toString(carte.getLimite_m()));
				statement.setString(2, Integer.toString(carte.getLimite_tot()));
				statement.setString(3, Integer.toString(carte.getEchellon()));
				statement.setString(4, carte.getType());

				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("Carte modifiée en base !");
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur update en base");
			e.printStackTrace();
		}
	}
	
	
	public void deleteCarteFed(String type)
	{
		try
		{
			System.out.println("suppr carte fidelite");
			if (connection != null)
			{
				String request = "DELETE FROM cartefed WHERE type = ?";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, type);

				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("carte suppr en base !");
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur suppr en base");
			e.printStackTrace();
		}
	}
	
	
	public CarteFidelite GetCarteFed(String type)
	{
		CarteFidelite carte = new CarteFidelite(type);
		try
		{
			if (connection != null)
			{
				String request = "SELECT * FROM cartefed WHERE type = '" + type + "'";
				
				PreparedStatement statement = connection.prepareStatement(request);

				ResultSet result = statement.executeQuery();
				while(result.next())
				{
					carte.setLimite_m(Integer.parseInt(result.getString(2)));
					carte.setLimite_tot(Integer.parseInt(result.getString(3)));
					carte.setEchellon(Integer.parseInt(result.getString(4)));
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur recup en base");
			e.printStackTrace();
		}
		return carte;
	}
	
	
	public void insertClientInternet(Client client)
	{
		try
		{
			System.out.println("insertion client internet");
			if (connection != null)
			{
				String request = "INSERT INTO Client (nom, prenom, cp, adresse, mail, tel, matricule, iban, bic, fedelite, civilite, naissance, nbenfant, situation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, client.getNom());
				statement.setString(2, client.getPrenom());
				statement.setString(3, client.getCodePostal());
				statement.setString(4, client.getAdresse());
				statement.setString(5, client.getMail());
				statement.setString(6, client.getTelephone());
				statement.setString(7, Integer.toString(client.getMatricule()));
				statement.setString(8, client.getIBAN());
				statement.setString(9, client.getBIC());
				if (client.getCarteFed() == null)
				{
					CarteFidelite fed = new CarteFidelite("Silver");
					client.setCarteFed(fed);
				}
				statement.setString(10, client.getCarteFed().getType());
				statement.setString(11, client.getCivilite());
				statement.setString(12, (client.getDate()).toString());
				statement.setString(13, Integer.toString(client.getNbenfant()));
				statement.setString(14, client.getSituation());
				
				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("Nouveau client internet ajouté en base !");
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	public void updateClientInternet(Client client)
	{
		try
		{
			System.out.println("update client internet");
			if (connection != null)
			{
				String request = "UPDATE Client SET nom=?, prenom=?, cp=?, adresse=?, mail=?, tel=?, iban=?, bic=?, fidelite=?, civilite=? naissance=? nbenfant=? situation=? WHERE matricule = ?";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, client.getNom());
				statement.setString(2, client.getPrenom());
				statement.setString(3, client.getCodePostal());
				statement.setString(4, client.getAdresse());
				statement.setString(5, client.getMail());
				statement.setString(6, client.getTelephone());
				statement.setString(7, Integer.toString(client.getMatricule()));
				statement.setString(8, client.getIBAN());
				statement.setString(9, client.getBIC());
				statement.setString(10, client.getCarteFed().getType());
				statement.setString(11, client.getCivilite());
				statement.setString(12, client.getDate().toString());
				statement.setString(13, Integer.toString(client.getNbenfant()));
				statement.setString(14, client.getSituation());

				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("Client internet modifié en base !");
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur update en base");
			e.printStackTrace();
		}
	}
	
	
	public void deleteClientInternet(int matricule)
	{
		try
		{
			System.out.println("suppr client internet");
			if (connection != null)
			{
				String request = "DELETE FROM Client WHERE matricule = ?";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, Integer.toString(matricule));

				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("Client internet suppr en base !");
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur suppr en base");
			e.printStackTrace();
		}
	}
	

	public void GetClients()
	{
		Client client;
		try
		{
			if (connection != null)
			{
				String request = "SELECT * FROM Client";
				
				PreparedStatement statement = connection.prepareStatement(request);

				ResultSet result = statement.executeQuery();
				while(result.next())
				{
					client = new Client();
					
					client.setNom(result.getString(2));
					client.setPrenom(result.getString(3));
					client.setCodePostal(result.getString(4));
					client.setAdresse(result.getString(5));
					client.setMail(result.getString(6));
					client.setTelephone(result.getString(7));
					client.setMatricule(Integer.parseInt(result.getString(8)));
					client.setIBAN(result.getString(9));
					client.setBIC(result.getString(10));
					CarteFidelite c = new CarteFidelite(result.getString(11));
					client.setCivilite(result.getString(12));					
					client.setNbenfant(Integer.parseInt(result.getString(13)));
					client.setSituation(result.getString(14));
				/*	
					System.out.println("**************");
					System.out.println(result.getString(15));
					System.out.println("**************");
					System.out.println((new SimpleDateFormat("YYYY-MM-dd")).parse(result.getString(15)));
					System.out.println("**************");
				*/
					client.setDate((new SimpleDateFormat("YYYY-MM-dd")).parse(result.getString(15)));
					client.setCarteFed(c);
				
					//System.out.println((new SimpleDateFormat("YYYY-MM-dd")).format(client.getDate()));
					
					Client.clientsList.add(client);
					
					
					
				}
			}
		}
		catch (SQLException | ParseException e)
		{
			System.out.println("Erreur récupération des clients en base");
			e.printStackTrace();
		}
		
	}
	
	
	public Client GetClientInternet(String id)
	{
		Client client = new Client();
		try
		{
			if (connection != null)
			{
				String request = "SELECT * FROM Client WHERE matricule = '" + id + "'";
				
				PreparedStatement statement = connection.prepareStatement(request);

				ResultSet result = statement.executeQuery();
				while(result.next())
				{
					client.setNom(result.getString(2));
					client.setPrenom(result.getString(3));
					client.setCodePostal(result.getString(4));
					client.setAdresse(result.getString(5));
					client.setMail(result.getString(6));
					client.setTelephone(result.getString(7));
					client.setMatricule(Integer.parseInt(result.getString(8)));
					client.setIBAN(result.getString(9));
					client.setBIC(result.getString(10));
					CarteFidelite c = new CarteFidelite(result.getString(11));
					client.setCivilite(result.getString(12));
					client.setDate((new SimpleDateFormat("YYYY-MM-dd")).parse(result.getString(13)));
					client.setNbenfant(Integer.parseInt(result.getString(14)));
					client.setSituation(result.getString(15));
					
					client.setCarteFed(c);
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;
	}
	
	public void insertArticle(Article article)
	{
		try
		{
			System.out.println("insertion article");
			if (connection != null)
			{
				String request = "INSERT INTO Article (reference, prix) VALUES (?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, article.getRef());
				statement.setString(2, Integer.toString(article.getPrix()));
				
				int rowsInserted = statement.executeUpdate();
				if (rowsInserted > 0)
				{
					System.out.println("Nouveau article ajouté en base !");
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
