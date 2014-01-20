package fr.epita.sigl.miwa.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/miwa", "postgres", "root");
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
	
	public void insertArticle(Articles article) {
		try
		{
			System.out.println("Insert Articles");
			if (connection != null)
			{
				String request = "INSERT INTO articles (ref_article, nom, prix_fournisseur, prix_vente, stock_max_entrepo, stock_max_magasin, categorie, quantite_min_commande_fournisse) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, article.getRef_article());
				statement.setString(2, article.getNom());
				statement.setString(3, article.getPrix_fournisseur());
				statement.setString(4, article.getPrix_vente());
				statement.setString(5, article.getStock_max_entre());
				statement.setString(6, article.getStock_max_mag());
				statement.setString(7, article.getCategory());
				statement.setString(8, article.getQuantite_min_fournisseur());
			
				statement.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	public void insertPromoFournisseur(PromoFournisseur article) {
		try
		{
			System.out.println("Insert promo fournisseur");
			if (connection != null)
			{
				String request = "INSERT INTO promo_fournisseur (ref_article, datedebut, datefin, pourcentage, quantite_min_application) VALUES (?, ?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, article.getRef_article());
				statement.setString(2, article.getDateDebut());
				statement.setString(3, article.getDateFin());
				statement.setString(4, article.getPourcentage());
				statement.setString(5, article.getMinquantite());
			
				statement.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	public void insertCommandeFournisseur(CommandeFournisseur cmd) {
		try
		{
			System.out.println("Insert commandes_fournisseur");
			if (connection != null)
			{
				String request = "INSERT INTO commandes_fournisseur (numero_commande, date_bon_de_commande, date_bon_de_livraison, traitee) VALUES (?, ?, ?, ?)";

				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, cmd.getNumero_commande());
				statement.setString(2, cmd.getBon_commande());
				statement.setString(3, cmd.getBon_livraion());
				statement.setString(4, cmd.getTraitee());
				
				statement.executeUpdate();
					
				int indice = 0;
			
				for (Articles a : cmd.getArticles()) {
					String request2 = "INSERT INTO commande_fournisseur_line (numero_commande, ref_article, quantite) VALUES (?, ?, ?)";
							
					PreparedStatement statement2 = connection.prepareStatement(request2);
					statement2.setString(1, cmd.getNumero_commande());
					statement2.setString(2, a.getRef_article());
					statement2.setString(3, cmd.getquantity().get(indice));
				
					statement2.executeUpdate();
					indice++;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	public void insertCommandeInternet(CommandeInternet cmd) {
		try
		{
			System.out.println("Insert commandes internet");
			if (connection != null)
			{
				String request = "INSERT INTO commandes_internet (numero_commande, ref_client, date_bon_commande, date_bon_livraison, nom_client, prenom_client, adresse_client, traitee) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, cmd.getCommandNumber());
				statement.setString(2, cmd.getCustomerRef());
				statement.setString(3, cmd.getDateBC());
				statement.setString(4, cmd.getDateBL());
				statement.setString(5, cmd.getCustomerLastname());
				statement.setString(6, cmd.getCustomerFirstname());
				statement.setString(7, cmd.getCustomerAddress());
				statement.setString(8, cmd.getTraite());
				
				statement.executeUpdate();
				
				/// Si y a un bug, ça vient de là
				ResultSet res = statement.getGeneratedKeys();
				int id = res.getInt(1);
				int indice = 0;
				for (Articles a : cmd.getArticles()) {
				String request2 = "INSERT INTO commande_internet_line (numero_commande, ref_article, quantite) VALUES (?, ?, ?)";
							
				PreparedStatement statement2 = connection.prepareStatement(request2);
				statement.setString(1, Integer.toString(id));
				statement.setString(2, a.getRef_article());
				statement.setString(3, cmd.getquantity().get(indice));
				
				statement2.executeUpdate();
				indice++;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	public void insertDemandeReassort(DemandeReassort dmd) {
		try
		{
			System.out.println("Insert demande reassort");
			if (connection != null)
			{
				String request = "INSERT INTO demandes_reassort (numero_demande, ref_bo, adresse_bo, tel_bo, date_bc, traite) VALUES (?, ?, ?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, dmd.getCommandNumber());
				statement.setString(2, dmd.getBackOfficeRef());
				statement.setString(3, dmd.getBackOfficeAddress());
				statement.setString(4, dmd.getBackOfficePhone());
				statement.setString(5, dmd.getDateBC());
				statement.setString(6, dmd.getTraite());
				
				statement.executeUpdate();
				
				/// Si y a un bug, ça vient de là
				ResultSet res = statement.getGeneratedKeys();
				int id = res.getInt(1);
				int indice = 0;
				for (Articles a : dmd.getArticles()) {
				String request2 = "INSERT INTO commande_internet_line (numero_commande, ref_article, quantite) VALUES (?, ?, ?)";
							
				PreparedStatement statement2 = connection.prepareStatement(request2);
				statement.setString(1, Integer.toString(id));
				statement.setString(2, a.getRef_article());
				statement.setString(3, dmd.getQuantity().get(indice));
				
				statement2.executeUpdate();
				indice++;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}		
	}
	
	public void insertPromotion(Promotions promo) {
		try
		{
			System.out.println("Insert promotions");
			if (connection != null)
			{
				String request = "INSERT INTO promotions (ref_article, date_debut, date_fin, pourcentage) VALUES (?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, promo.getArticle().getRef_article());
				statement.setString(2, promo.getBegin());
				statement.setString(3, promo.getEnd());
				statement.setString(4, promo.getPourcentage());
		
				statement.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base pour promotion");
			e.printStackTrace();
		}
	}
	
	public void insertStockEntrepot(StockEntrepot stck) {
		try
		{
			System.out.println("Insert stock entrepôt");
			if (connection != null)
			{
				String request = "INSERT INTO stock_entrepot (ref_article, quantite) VALUES (?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, stck.getArticle().getRef_article());
				statement.setString(2, stck.getQuantity());
				
				statement.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	public void insertStockMagasin(StockMagasin mgs) {
		try
		{
			System.out.println("Insert stock magasin");
			if (connection != null)
			{
				String request = " INSERT INTO stock_magasin (ref_article, id_magasin, quantite) VALUES (?, ?, ?) MATCHING (ref_article, id_magasin)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, mgs.getArticle().getRef_article());
				statement.setString(2, mgs.getIdmag());
				statement.setString(3, mgs.getQuantity());

				statement.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
	}
	
	public void insertDemandeNiveauStock(DemandeNiveauStock dmd) {
		try
		{
			System.out.println("Insert demande niveau stock");
			if (connection != null)
			{
				String request = "INSERT INTO demande_niveau_stock (numero_demande, date_demande, date_reponse, ref_bo) VALUES (?, ?, ?, ?)";
				
				PreparedStatement statement = connection.prepareStatement(request);
				statement.setString(1, dmd.getCommandNumber());
				statement.setString(2, dmd.getDatedemand());
				statement.setString(3, dmd.getDaterep());
				statement.setString(4, dmd.getRefbo());
				
				statement.executeUpdate();
				
				/// Si y a un bug, ça vient de là
				ResultSet res = statement.getGeneratedKeys();
				int id = res.getInt(1);
				int indice = 0;
				for (Articles a : dmd.getArticles()) {
				String request2 = "INSERT INTO articles_map (ref_article, id_demande, quantite) VALUES (?, ?, ?)";
							
				PreparedStatement statement2 = connection.prepareStatement(request2);
				statement.setString(1, a.getRef_article());
				statement.setString(2, Integer.toString(id));
				statement.setString(3, dmd.getQuantity().get(indice));
				
				statement2.executeUpdate();
				indice++;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}		
	}
	
	public DemandeNiveauStock envoiStock(DemandeNiveauStock dns)
	{
		DemandeNiveauStock res = new DemandeNiveauStock();
		
		res = dns;
		
		try
		{
			System.out.println("Recup niveau stock");
			if (connection != null)
			{
				//int indice = 0;
				for (Articles a : dns.getArticles()) {
				
					String request = "SELECT quantite FROM stock_entrepot WHERE ref_article = ?";
				
					PreparedStatement statement = connection.prepareStatement(request);
					statement.setString(1, a.getRef_article());
				
					statement.executeUpdate();
				
					/// Si y a un bug, ça vient de là
					
					ResultSet ret = statement.executeQuery();
					int qt = ret.getInt(1);
					List<String> nouv = dns.getQuantity();
					nouv.add(Integer.toString(qt));
					res.setQuantity(nouv);
					//indice++;
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}	
		
		return res;
	}
	
	public List<Articles> envoiPrixArticle () {
		List<Articles> res = new ArrayList<>();
		
		try
		{
			System.out.println("Recup prix article");
			if (connection != null)
			{
				String request = "SELECT ref_article, prix_vente FROM articles";
				
				PreparedStatement statement = connection.prepareStatement(request);
													
				ResultSet ret = statement.executeQuery();

				while(ret.next()) {
					String ref_article = ret.getString("ref_article");
					String prix_vente = ret.getString("prix_vente");
					Articles a = new Articles ();
					a.setRef_article(ref_article);
					a.setPrix_vente(prix_vente);
					res.add(a);
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
		return res;
	}

	public List<Promotions> envoiPromotions () {
		List<Promotions> res = new ArrayList<>();
		
		try
		{
			System.out.println("Recup promotions");
			if (connection != null)
			{
				String request = "SELECT ref_article, date_debut, date_fin, pourcentage FROM promotions";
				
				PreparedStatement statement = connection.prepareStatement(request);
													
				ResultSet ret = statement.executeQuery();

				while(ret.next()) {
					String ref_article = ret.getString("ref_article");
					String date_debut = ret.getString("date_debut");
					String date_fin = ret.getString("date_fin");
					String pourcentage = ret.getString("pourcentage");
					Promotions a = new Promotions ();
					a.setRef_article(ref_article);
					a.setBegin(date_debut);
					a.setEnd(date_fin);
					a.setPourcentage(pourcentage);
					res.add(a);
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}
		return res;
	}
	
	public DemandeNiveauStock envoi_all_stock()
	{
		DemandeNiveauStock res = new DemandeNiveauStock();
		List<Articles> art = new ArrayList<>();
		List<String> quantites = new ArrayList<String>();
		try
		{
			System.out.println("Recup niveau stock");
			if (connection != null)
			{
				//int indice = 0;
				String request = "SELECT ref_article, quantite FROM stock_entrepot";
				
				PreparedStatement statement = connection.prepareStatement(request);
				
				statement.executeUpdate();
				
				// Si y a un bug, ça vient de là
				ResultSet ret = statement.executeQuery();

				while (ret.next()) {
					String ref_article = ret.getString("ref_article");
					String quantite = ret.getString("quantite");

					quantites.add(quantite);
					Articles a = new Articles();
					a.setRef_article(ref_article);
					art.add(a);
					
					res.setArticles(art);
					res.setQuantity(quantites);
				}
					
				//indice++;
			}
		}
		catch (SQLException e)
		{
			System.out.println("Erreur insertion en base");
			e.printStackTrace();
		}	
		
		return res;
	}
}