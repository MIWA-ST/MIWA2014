package fr.epita.sigl.miwa.application.messaging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import fr.epita.sigl.miwa.db.DbHandler;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.sync.ISyncMessSender;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class SyncMessHandler {

	//Init logger
	private static final Logger LOGGER = Logger.getLogger(SyncMessHandler.class.getName());
	
	/* 
	 * A utiliser pour pouvoir envoyer des messages synchrones ou faire des request
	 */
	static public ISyncMessSender getSyncMessSender() {
		return SyncMessFactory.getSyncMessSender();
	}

	/*
	 * l'application sender vous envoie la string message.
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public boolean receiveMessage(EApplication sender, String message) {
		return false;
	}

	/*
	 * L'application sender vous demande request
	 * Vous devez lui renvoyer une string
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public String answerToRequestMessage(EApplication sender, String request){
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * L'application sender vous envoie le XML xml
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public boolean receiveXML(EApplication sender, Document xml){
		LOGGER.fine("***** MONETIQUE SERVICE CALL : Recepting message.");

		xml.getDocumentElement().normalize();
		String serviceToPerform = xml.getDocumentElement().getAttribute("service");
		String actionToPerform = xml.getDocumentElement().getAttribute("action");

		if (serviceToPerform.equals("paiement_cb"))
		{
			System.out.println("***** BEGIN SERVICE PAYMENT CB *****");
			LOGGER.info("***** SERVICE CB : Paiement by CB service started.");

			String montant = null;
			String[] cb = {"", "", ""};

			NodeList nl = xml.getDocumentElement().getChildNodes();
			NodeList cnl = null;
			for (int i = 0; i < nl.getLength(); ++i)
			{
				if (nl.item(i).getNodeName().equalsIgnoreCase("montant"))
					montant = nl.item(i).getTextContent();
				if (nl.item(i).getNodeName().equalsIgnoreCase("cb"))
					cnl = nl.item(i).getChildNodes();
			}
			if (cnl == null)
			{
				LOGGER.info("***** SERVICE CB : ERROR -> Missing data in request ('cb' balise).");
				LOGGER.info("***** SERVICE CB : Paiement by CB service terminated normally with: " + false + ".");
				System.out.println("***** END SERVICE *****");
				return false;
			}
			for (int i = 0; i < cnl.getLength(); ++i) 
			{
				if (cnl.item(i).getNodeName().equalsIgnoreCase("numero"))
					cb[i] = cnl.item(i).getTextContent();
				if (cnl.item(i).getNodeName().equalsIgnoreCase("date_validite"))
					cb[i] = cnl.item(i).getTextContent();
				if (cnl.item(i).getNodeName().equalsIgnoreCase("pictogramme"))
					cb[i] = cnl.item(i).getTextContent();
			}
			if (montant == null || cb[0].equals("") || cb[1].equals("") || cb[2].equals(""))
			{
				LOGGER.info("***** SERVICE CB : ERROR -> Missing data in request ('montant' or 'numero' or 'date_validite' or 'pictogramme').");
				LOGGER.info("***** SERVICE CB : Paiement by CB service terminated normally with: " + false + ".");
				System.out.println("***** END SERVICE *****");
				return false;
			}
			Float mt;
			try 
			{
				mt = Float.parseFloat(montant);
				if (mt < 0)
				{
					LOGGER.info("***** SERVICE CB : ERROR -> Invalid data ('montant' : " + mt + ") in request.");
					LOGGER.info("***** SERVICE CB : Paiement by CB service terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}
			}
			catch (Exception e)
			{
				LOGGER.info("***** SERVICE CB : ERROR -> Invalid data ('montant' : " + montant + ") in request.");
				LOGGER.info("***** SERVICE CB : Paiement by CB service terminated normally with: " + false + ".");
				System.out.println("**** END SERVICE ****");
				return false;
			}
			LOGGER.info("***** SERVICE CB : REQUEST -> " + montant + "€ for the credit card : " + cb[0]);
			Boolean bankResponse = getBankPaiement();
			LOGGER.info("***** SERVICE CB : REQUEST -> Done.");
			LOGGER.info("***** SERVICE CB : Paiement by CB service terminated normally with : " + bankResponse + ".");
			System.out.println("***** END SERVICE *****");
			return bankResponse;
		}
		else if (serviceToPerform.equals("paiement_cf"))
		{
			System.out.println("***** BEGIN SERVICE PAYMENT CF *****");
			LOGGER.info("***** SERVICE CB : Paiement by fidelity service started.");

			String montantXML = null;
			String matriculeClientXML = null;

			// Récupération des données dans le XML
			NodeList nl = xml.getDocumentElement().getChildNodes();
			for (int i = 0; i < nl.getLength(); ++i)
			{
				if (nl.item(i).getNodeName().equalsIgnoreCase("montant"))
					montantXML = nl.item(i).getTextContent();
				else if (nl.item(i).getNodeName().equalsIgnoreCase("matricule_client"))
					matriculeClientXML = nl.item(i).getTextContent();
			}
			// En cas d'absence de données dans le XML
			if (montantXML == null || matriculeClientXML == null) 
			{
				LOGGER.info("***** SERVICE CF : ERROR -> Missing data in request ('montant' or 'matricule_client').");
				LOGGER.info("***** SERVICE CF : Paiement by fidelity service terminated normally with: " + false + ".");
				System.out.println("***** END SERVICE *****");
				return false;
			}
			// En cas de données incohérentes dans le XML
			Float montantFloat = null; 
			try 
			{
				montantFloat = Float.parseFloat(montantXML);
				if (montantFloat <= 0f)
				{
					LOGGER.info("***** SERVICE CF : ERROR -> Invalid data ('montant' : " + montantFloat + ") in request.");
					LOGGER.info("***** SERVICE CF : Paiement by fidelity service terminated normally with: " + false + ".");
					System.out.println("**** END SERVICE ****");
					return false;
				}
			} 
			catch (Exception e) 
			{
				LOGGER.info("***** SERVICE CF : ERROR -> Invalid data ('montant' : " + montantXML + ") in request.");
				LOGGER.info("***** SERVICE CF : Paiement by fidelity service terminated normally with: " + false + ".");
				System.out.println("***** END SERVICE *****");
				return false;
			}
			
			DbHandler dbHandler = new DbHandler();
			try 
			{
				// Connexion à la BDD
				Connection connection = dbHandler.open();

				// Récupération de l'id du client en vérifiant limite crédit totale et liste noire
				// TODO AND monthCreditSum < montly_credit_limit
				PreparedStatement pS = connection.prepareStatement("SELECT id_fidelity_credit_account as id, customer_code, echelon_nb, total_credit_limit, montly_credit_limit "
						+ "FROM fidelity_credit_account as fca LEFT JOIN loyalty_card_type as lct ON fca.id_loyalty_card_type = lct.id_loyalty_card_type "
						+ "WHERE customer_code = ? AND (total_credit_amount - total_repaid_credit__amount) < total_credit_limit "
						//+ "AND (SELECT SUM(amount / echelon_nb) FROM fidelity_credit WHERE fidelity_credit.id_fidelity_credit_account = fca.id_fidelity_credit_account AND is_repaid = FALSE) < montly_credit_limit "
						+ "AND is_blacklisted = FALSE AND IS_DELETED = FALSE;");
				pS.setString(1, matriculeClientXML);
				ResultSet result = pS.executeQuery();

				if (result.next()) 
				{				
					Integer idClient = result.getInt("id");
					Integer echelonNb = result.getInt("echelon_nb");					
					
					LOGGER.info("***** SERVICE CF : REQUEST -> Credit of " + montantFloat + "€ (" + echelonNb + " month(s)) for the fidelity account: " + matriculeClientXML + ".");			
					
					// Ajout crédit
					pS = connection.prepareStatement("INSERT INTO fidelity_credit (id_fidelity_credit_account, fidelity_credit_date, amount, repaid_amount, is_repaid, echelon_nb) VALUES "
							+ "(?, NOW(), ?, 0, FALSE, ?);");
					pS.setInt(1, idClient);
					pS.setFloat(2, montantFloat);
					pS.setInt(3, echelonNb);
					pS.executeUpdate();
					
					// MAJ compte crédit
					pS = connection.prepareStatement("UPDATE fidelity_credit_account SET total_credit_amount = total_credit_amount + ? WHERE id_fidelity_credit_account = ?");
					pS.setFloat(1, montantFloat);
					pS.setInt(2, idClient);
					pS.executeUpdate();	
					
					LOGGER.info("***** SERVICE CF : REQUEST -> Done.");
				}			
				// En cas de client inexistant ou non éligible pour crédit
				else
				{
					LOGGER.info("***** ERROR -> Unknown or ineligible fidelity credit account with customer matricule : " + matriculeClientXML + ".");
					LOGGER.info("***** Paiement by fidelity service terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}		
			} 
			catch (SQLException e) 
			{
				System.err.println("ERROR : " + e.getMessage());
				LOGGER.info("***** SERVICE CF : Paiement by fidelity service terminated normally with: " + false + ".");
				System.out.println("***** END SERVICE *****");
				return false;
			}
			finally
			{
				dbHandler.close();
			}

			LOGGER.info("***** SERVICE CF : Paiement by fidelity service terminated normally with: " + true + ".");
			System.out.println("***** END SERVICE *****");
			return true;
		}
		else if (serviceToPerform.equals("cms_type_carte"))
		{
			System.out.println("***** BEGIN SERVICE CARD TYPE *****");
			LOGGER.info("***** SERVICE CARD TYPE : Card type service started.");
						
			if (actionToPerform.equals("c"))
			{
				System.out.println("     ***** ACTION CREATE *****");
				LOGGER.info("***** SERVICE CARD TYPE : Create a card.");
				
				String id = "";
				String month_lim = "";
				String tot_lim = "";
				String nb_ech = "";
				
				NodeList nl = xml.getDocumentElement().getChildNodes().item(0).getChildNodes();
				
				for (int i = 0; i < nl.getLength(); ++i)
				{
					if (nl.item(i).getNodeName().equals("id"))
						id = nl.item(i).getTextContent();
					if (nl.item(i).getNodeName().equals("limite_mensuelle"))
						month_lim = nl.item(i).getTextContent();
					if (nl.item(i).getNodeName().equals("limite_totale"))
						tot_lim = nl.item(i).getTextContent();
					if (nl.item(i).getNodeName().equals("nb_echelon"))
						nb_ech = nl.item(i).getTextContent();					
				}
				
				Float month_lim_db = null;
				Float tot_lim_db = null;
				Integer nb_ech_db = null;				
				try
				{
					month_lim_db = Float.parseFloat(month_lim);
					tot_lim_db = Float.parseFloat(tot_lim);
					nb_ech_db = Integer.parseInt(nb_ech);
					if (month_lim_db < 0f || tot_lim_db < 0f || nb_ech_db < 0f)
					{
						LOGGER.info("***** SERVICE CARD TYPE : ERROR -> Invalid data in request.");
						LOGGER.info("***** SERVICE CARD TYPE : Card type service (create) terminated normally with: " + false + ".");
						System.out.println("***** END SERVICE *****");
						return false;
					}
				}
				catch (Exception e)
				{
					LOGGER.info("***** SERVICE CARD TYPE : ERROR -> Invalid data in request.");
					LOGGER.info("***** SERVICE CARD TYPE : Card type service (create) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}

				DbHandler dbHandler = new DbHandler();
				try 
				{
					// Connexion à la BDD
					Connection connection = dbHandler.open();

					LOGGER.info("***** SERVICE CARD TYPE : REQUEST -> Creation of a new card with ID : " + id + ", MONTH LIMIT : " + month_lim + ", TOTAL LIMIT : " + tot_lim + " AND WITH " + nb_ech + " ECHELONS.");			
						
					// Ajout carte
					PreparedStatement pS = connection.prepareStatement("INSERT INTO loyalty_card_type(CARD_TYPE_CODE, MONTLY_CREDIT_LIMIT, TOTAL_CREDIT_LIMIT, ECHELON_NB) VALUES (?, ?, ?, ?);");
					pS.setString(1, id);
					pS.setFloat(2, month_lim_db);
					pS.setFloat(3, tot_lim_db);
					pS.setInt(4, nb_ech_db);
					pS.executeUpdate();
						
					LOGGER.info("***** SERVICE CARD TYPE : REQUEST -> Done");	
				} 
				catch (SQLException e) 
				{
					System.err.println("ERROR : " + e.getMessage());
					LOGGER.info("***** SERVICE CARD TYPE : Card type service (create) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}
				finally
				{
					dbHandler.close();
				}				

				LOGGER.info("***** SERVICE CARD TYPE : Card type service (create) terminated normally with: " + true + ".");
				System.out.println("***** END SERVICE *****");
				return true;
			}
			else if (actionToPerform.equals("m"))
			{
				System.out.println("     ***** ACTION UPDATE *****");
				LOGGER.info("***** SERVICE CARD TYPE : Modify a card.");
			
				NamedNodeMap attrs = xml.getDocumentElement().getChildNodes().item(0).getAttributes();
				String id = attrs.item(0).getTextContent();
				String month_lim = "";
				String tot_lim = "";
				String nb_ech = "";
				
				NodeList nl = xml.getDocumentElement().getChildNodes().item(0).getChildNodes();
				
				for (int i = 0; i < nl.getLength(); ++i)
				{
					if (nl.item(i).getNodeName().equals("limite_mensuelle"))
						month_lim = nl.item(i).getTextContent();
					if (nl.item(i).getNodeName().equals("limite_totale"))
						tot_lim = nl.item(i).getTextContent();
					if (nl.item(i).getNodeName().equals("nb_echelon"))
						nb_ech = nl.item(i).getTextContent();					
				}
				
				Float month_lim_db = null;
				Float tot_lim_db = null;
				Integer nb_ech_db = null;			
				try
				{
					month_lim_db = Float.parseFloat(month_lim);
					tot_lim_db = Float.parseFloat(tot_lim);
					nb_ech_db = Integer.parseInt(nb_ech);
					if (month_lim_db < 0f || tot_lim_db < 0f || nb_ech_db < 0f)
					{
						LOGGER.info("***** SERVICE CARD TYPE : ERROR -> Invalid data in request.");
						LOGGER.info("***** SERVICE CARD TYPE : Card type service (update) terminated normally with: " + false + ".");
						System.out.println("***** END SERVICE *****");
						return false;
					}
				}
				catch (Exception e)
				{
					LOGGER.info("***** SERVICE CARD TYPE : ERROR -> Invalid data in request.");
					LOGGER.info("***** SERVICE CARD TYPE : Card type service (update) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}

				DbHandler dbHandler = new DbHandler();
				try 
				{
					// Connexion à la BDD
					Connection connection = dbHandler.open();

					LOGGER.info("***** SERVICE CARD TYPE : REQUEST -> Modification of the card with ID : " + id + ", with the following values -> MONTH LIMIT : " + month_lim + ", TOTAL LIMIT : " + tot_lim + " AND WITH " + nb_ech + " ECHELONS.");			
						
					// Modifier carte
					PreparedStatement pS = connection.prepareStatement("UPDATE loyalty_card_type SET MONTLY_CREDIT_LIMIT = ?, TOTAL_CREDIT_LIMIT = ?, ECHELON_NB = ? WHERE CARD_TYPE_CODE = ?;");
					pS.setFloat(1, month_lim_db);
					pS.setFloat(2, tot_lim_db);
					pS.setInt(3, nb_ech_db);
					pS.setString(4, id);
					pS.executeUpdate();
						
					LOGGER.info("***** SERVICE CARD TYPE : REQUEST -> Done");	
				} 
				catch (SQLException e) 
				{
					System.err.println("ERROR : " + e.getMessage());	
					LOGGER.info("***** SERVICE CARD TYPE : Card type service (update) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}
				finally
				{
					dbHandler.close();
				}	

				LOGGER.info("***** SERVICE CARD TYPE : Card type service (update) terminated normally with: " + true + ".");
				System.out.println("***** END SERVICE *****");
				return true;
			}
			else if (actionToPerform.equals("s"))
			{
				System.out.println("     ***** ACTION DELETE *****");
				LOGGER.info("***** SERVICE CARD TYPE : Delete a card.");
				
				NamedNodeMap attrs = xml.getDocumentElement().getChildNodes().item(0).getAttributes();
				String id_old = attrs.item(0).getTextContent();
				String id_new = xml.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getTextContent();
				
				if (id_old == null || id_new == null)
				{
					LOGGER.info("***** SERVICE CARD TYPE : ERROR -> Invalid data in request.");
					LOGGER.info("***** SERVICE CARD TYPE : Card type service (delete) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}

				DbHandler dbHandler = new DbHandler();
				try 
				{
					// Connexion à la BDD
					Connection connection = dbHandler.open();

					LOGGER.info("***** SERVICE CARD TYPE : REQUEST -> Delete the card with ID: " + id_old + " and replace it with " + id_new + ".");			
						
					// Récupérer l'ID de la nouvelle carte
					PreparedStatement ps = connection.prepareStatement("SELECT ID_LOYALTY_CARD_TYPE FROM loyalty_card_type WHERE CARD_TYPE_CODE = ?;");
					ps.setString(1, id_old);
					ResultSet res = ps.executeQuery();
					
					Integer id_old_key = null;
					Integer id_new_key = null;
					
					if (res.next())
					{
						id_old_key = res.getInt("ID_LOYALTY_CARD_TYPE");
					}
					else
					{
						LOGGER.info("***** SERVICE CARD TYPE : ERROR -> Unknown fidelity card type for id: " + id_old + ".");
						LOGGER.info("***** SERVICE CARD TYPE : Card type service (delete) terminated normally with: " + false + ".");
						System.out.println("***** END SERVICE *****");
						return false;
					}
					
					// Récupérer l'ID de l'ancienne carte
					ps = connection.prepareStatement("SELECT ID_LOYALTY_CARD_TYPE FROM loyalty_card_type WHERE CARD_TYPE_CODE = ?;");
					ps.setString(1, id_new);
					res = ps.executeQuery();
					
					if (res.next())
					{
						id_new_key = res.getInt("ID_LOYALTY_CARD_TYPE");
					}
					else
					{
						LOGGER.info("***** SERVICE CARD TYPE : ERROR -> Unknown fidelity card type for id: " + id_new + ".");
						LOGGER.info("***** SERVICE CARD TYPE : Card type service (delete) terminated normally with: " + false + ".");
						System.out.println("***** END SERVICE *****");
						return false;
					}
					
					// Modifier la carte des clients de l'ancienne carte
					ps = connection.prepareStatement("UPDATE fidelity_credit_account SET ID_LOYALTY_CARD_TYPE = ? WHERE ID_LOYALTY_CARD_TYPE = ?;");
					ps.setInt(1, id_new_key);
					ps.setInt(2, id_old_key);
					ps.executeUpdate();
					
					// Supprimer l'ancienn carte
					ps = connection.prepareStatement("DELETE FROM LOYALTY_CARD_TYPE WHERE CARD_TYPE_CODE = ?");
					ps.setString(1, id_old);
					ps.executeUpdate();
						
					LOGGER.info("***** SERVICE CARD TYPE : REQUEST -> Done");	
				} 
				catch (SQLException e) 
				{
					System.err.println("ERROR : " + e.getMessage());	
					LOGGER.info("***** SERVICE CARD TYPE : Card type service (delete) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}
				finally
				{
					dbHandler.close();
				}	

				LOGGER.info("***** SERVICE CARD TYPE : Card type service (delete) terminated normally with: " + true + ".");
				System.out.println("***** END SERVICE *****");
				return true;
			}
			else
			{
				LOGGER.severe("***** SERVICE CARD TYPE : ERROR: Unknown action (" + actionToPerform + ") for card type service.");
				System.out.println("***** END SERVICE *****");
				return false;
			}
		}
		else if (serviceToPerform.equals("cms_compte_cf"))
		{
			System.out.println("***** BEGIN SERVICE FIDELITY ACCOUNT *****");
			LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service started.");
			if (actionToPerform.equals("c"))
			{
				System.out.println("     ***** ACTION CREATE *****");
				LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Create a new account.");
				
				String matriculeClientXML = null;
				String BICXML = null;
				String IBANXML = null;
				String idTypeCfXML = null;

				// Récupération des données dans le XML
				NodeList nl = xml.getDocumentElement().getChildNodes();
				NodeList accountCfNl = null;
				for (int i = 0; i < nl.getLength(); ++i)
				{
					if (nl.item(i).getNodeName().equalsIgnoreCase("compte_cf"))
						accountCfNl = nl.item(i).getChildNodes();
				}
				if (accountCfNl != null)
				{
					for (int i = 0; i < accountCfNl.getLength(); ++i)
					{
						if (accountCfNl.item(i).getNodeName().equalsIgnoreCase("matricule_client"))
							matriculeClientXML = accountCfNl.item(i).getTextContent();
						else if (accountCfNl.item(i).getNodeName().equalsIgnoreCase("BIC"))
							BICXML = accountCfNl.item(i).getTextContent();
						else if (accountCfNl.item(i).getNodeName().equalsIgnoreCase("IBAN"))
							IBANXML = accountCfNl.item(i).getTextContent();
						else if (accountCfNl.item(i).getNodeName().equalsIgnoreCase("id_type_cf"))
							idTypeCfXML = accountCfNl.item(i).getTextContent();
					}
					// En cas d'absence de données dans le XML
					if (matriculeClientXML == null || BICXML == null || IBANXML == null || idTypeCfXML == null) 
					{
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : ERROR -> Missing data in request ('matricule_client' or 'BIC' or 'IBAN' or 'id_type_cf').");
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (create) terminated normally with : " + false + ".");
						System.out.println("***** END SERVICE *****");
						return false;
					}
				}
				else
				{
					LOGGER.info("***** SERVICE FIDELITY ACCOUNT : ERROR -> Missing balise 'compte_cf' in request.");
					LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (create) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}
				
				DbHandler dbHandler = new DbHandler();
				try 
				{
					// Connexion à la BDD
					Connection connection = dbHandler.open();
					
					// Récupération de l'id du type de carte
					PreparedStatement pS = connection.prepareStatement("SELECT id_loyalty_card_type as id FROM loyalty_card_type WHERE card_type_code = ?;");
					pS.setString(1, idTypeCfXML);
					ResultSet result = pS.executeQuery();
					
					if (result.next()) 
					{				
						Integer idCardType = result.getInt("id");
						
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : REQUEST -> Creation of fidelity credit account for customer with matricule: " + matriculeClientXML + ".");		
						
						// Ajout du compte client fidélité
						pS = connection.prepareStatement("INSERT INTO fidelity_credit_account "
								+ "(id_loyalty_card_type, customer_code, is_blacklisted, blaklisted_date, total_credit_amount, total_repaid_credit__amount, is_deleted) "
								+ "VALUES (?, ?, FALSE, NULL, 0, 0, FALSE);");
						pS.setInt(1, idCardType);
						pS.setString(2, matriculeClientXML);
						pS.executeUpdate();
						
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : REQUEST -> Done.");
					}
					// En cas de type de carte inexistant
					else 
					{
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : ERROR -> Unknown fidelity card type for id: " + idTypeCfXML + ".");
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account (create) service terminated normally with: " + false + ".");
						System.out.println("***** END SERVICE *****");
						return false;
					}
				} 
				catch (SQLException e) 
				{
					System.err.println("ERROR : " + e.getMessage());
					LOGGER.info("***** SERVICE FIDELITY ACCOUNT : ERROR -> Duplicate fidelity credit account for customer matricule: " + matriculeClientXML + ".");
					LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (create) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}
				finally
				{
					dbHandler.close();
				}
				
				LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (create) terminated normally with: " + true + ".");
				System.out.println("***** END SERVICE *****");
				return true;
			}
			else if (actionToPerform.equals("m"))
			{
				System.out.println("     ***** ACTION UPDATE *****");
				LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Modify an account.");
				
				String matriculeClientXML = null;
				String BICXML = null;
				String IBANXML = null;
				String idTypeCfXML = null;

				// Récupération des données dans le XML
				NodeList nl = xml.getDocumentElement().getChildNodes();
				NodeList accountCfNl = null;
				for (int i = 0; i < nl.getLength(); ++i)
				{
					if (nl.item(i).getNodeName().equalsIgnoreCase("compte_cf"))
					{
						accountCfNl = nl.item(i).getChildNodes();
						matriculeClientXML = nl.item(i).getAttributes().getNamedItem("matricule_client").getNodeValue();
					}
				}
				if (accountCfNl != null)
				{
					for (int i = 0; i < accountCfNl.getLength(); ++i)
					{
						if (accountCfNl.item(i).getNodeName().equalsIgnoreCase("BIC"))
							BICXML = accountCfNl.item(i).getTextContent();
						else if (accountCfNl.item(i).getNodeName().equalsIgnoreCase("IBAN"))
							IBANXML = accountCfNl.item(i).getTextContent();
						else if (accountCfNl.item(i).getNodeName().equalsIgnoreCase("id_type_cf"))
							idTypeCfXML = accountCfNl.item(i).getTextContent();
					}
					// En cas d'absence de données dans le XML
					if (matriculeClientXML == null || BICXML == null || IBANXML == null || idTypeCfXML == null) 
					{
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : ERROR -> Missing data in request ('matricule_client' or 'BIC' or 'IBAN' or 'id_type_cf').");
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (update) terminated normally with : " + false + ".");
						System.out.println("***** END SERVICE *****");
						return false;
					}
				}
				else
				{
					LOGGER.info("***** SERVICE FIDELITY ACCOUNT : ERROR -> Missing balise 'compte_cf' in request.");
					LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (update) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}
				
				DbHandler dbHandler = new DbHandler();
				try 
				{
					// Connexion à la BDD
					Connection connection = dbHandler.open();
					
					// Récupération de l'id du compte crédit fidélité
					PreparedStatement pS = connection.prepareStatement("SELECT id_fidelity_credit_account as id FROM fidelity_credit_account WHERE customer_code = ?;");
					pS.setString(1, matriculeClientXML);
					ResultSet result = pS.executeQuery();
					
					if (result.next()) 
					{				
						Integer idAccount = result.getInt("id");
						
						// Récupération de l'id du type de carte
						pS = connection.prepareStatement("SELECT id_loyalty_card_type as id FROM loyalty_card_type WHERE card_type_code = ?;");
						pS.setString(1, idTypeCfXML);
						result = pS.executeQuery();
						
						if (result.next()) 
						{				
							Integer idCardType = result.getInt("id");
						
							LOGGER.info("***** SERVICE FIDELITY ACCOUNT : REQUEST -> Modify fidelity credit account for customer with matricule: " + matriculeClientXML + ".");		
							
							// Modification du compte client fidélité
							pS = connection.prepareStatement("UPDATE fidelity_credit_account SET id_loyalty_card_type = ? WHERE id_fidelity_credit_account = ?;");
							pS.setInt(1, idCardType);
							pS.setInt(2, idAccount);
							pS.executeUpdate();
							
							LOGGER.info("***** SERVICE FIDELITY ACCOUNT : REQUEST -> Done.");
						}
						// En cas de type de carte inexistant
						else 
						{
							LOGGER.info("***** SERVICE FIDELITY ACCOUNT : ERROR -> Unknown fidelity card type for id: " + idTypeCfXML + ".");
							LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account (delete) service terminated normally with: " + false + ".");
							System.out.println("***** END SERVICE *****");
							return false;
						}
					}
					// En cas de client inexistant
					else 
					{
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : ERROR -> Unknown customer with matricule: " + matriculeClientXML + ".");
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account (update) service terminated normally with: " + false + ".");
						System.out.println("***** END SERVICE *****");
						return false;
					}
				} 
				catch (SQLException e) 
				{
					System.err.println("ERROR : " + e.getMessage());
					LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (update) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}
				finally
				{
					dbHandler.close();
				}
				
				LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (update) terminated normally with: " + true + ".");
				System.out.println("***** END SERVICE *****");
				return true;
			}
			else if (actionToPerform.equals("s"))
			{
				System.out.println("     ***** ACTION DELETE *****");
				LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Delete an account.");
				
				String matriculeClientXML = null;

				// Récupération des données dans le XML
				NodeList nl = xml.getDocumentElement().getChildNodes();
				for (int i = 0; i < nl.getLength(); ++i)
				{
					if (nl.item(i).getNodeName().equalsIgnoreCase("compte_cf"))
						matriculeClientXML = nl.item(i).getAttributes().getNamedItem("matricule_client").getNodeValue();
				}
				// En cas d'absence de données dans le XML
				if (matriculeClientXML == null) 
				{
					LOGGER.info("***** SERVICE FIDELITY ACCOUNT : ERROR -> Missing data in request ('compte_cf' balise or 'matricule_client').");
					LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (delete) terminated normally with : " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}		
				
				DbHandler dbHandler = new DbHandler();
				try 
				{
					// Connexion à la BDD
					Connection connection = dbHandler.open();
					
					// Récupération de l'id du compte crédit fidélité
					PreparedStatement pS = connection.prepareStatement("SELECT id_fidelity_credit_account as id FROM fidelity_credit_account WHERE customer_code = ?;");
					pS.setString(1, matriculeClientXML);
					ResultSet result = pS.executeQuery();
					
					if (result.next()) 
					{				
						Integer idAccount = result.getInt("id");
						
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : REQUEST -> Suppression of fidelity credit account for customer with matricule: " + matriculeClientXML + ".");		
						
						// Suppression du compte crédit fidélité
						pS = connection.prepareStatement("UPDATE fidelity_credit_account SET is_deleted = TRUE WHERE id_fidelity_credit_account = ?");
						pS.setInt(1, idAccount);
						pS.executeUpdate();
						
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : REQUEST -> Done.");
					}
					// En cas de client inexistant
					else 
					{
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : ERROR -> Unknown customer with matricule: " + matriculeClientXML + ".");
						LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account (delete) service terminated normally with: " + false + ".");
						System.out.println("***** END SERVICE *****");
						return false;
					}
				} 
				catch (SQLException e) 
				{
					System.err.println("ERROR : " + e.getMessage());
					LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (delete) terminated normally with: " + false + ".");
					System.out.println("***** END SERVICE *****");
					return false;
				}
				finally
				{
					dbHandler.close();
				}
				
				LOGGER.info("***** SERVICE FIDELITY ACCOUNT : Fidelity account service (delete) terminated normally with: " + true + ".");
				System.out.println("***** END SERVICE *****");
				return true;
			}
			else
			{
				LOGGER.severe("***** SERVICE FIDELITY ACCOUNT : ERROR: Unknown action (" + actionToPerform + ") for fidelity account service.");
				System.out.println("***** END SERVICE *****");
				return false;
			}			
		}
		else
		{
			LOGGER.severe("***** MONETIQUE ERROR: Unknown service.");
			return false;
		}
	}

	private static boolean getBankPaiement() 
	{
		System.out.println("     ***** BEGIN BANK *****");
		LOGGER.info("***** BANK SERVICE : Bank Paiement service call started.");		
		Random rnd = new Random();
		Integer myRnd = rnd.nextInt(100);
		
		LOGGER.info("***** BANK SERVICE : Bank Paiement service call stopped with : " + (myRnd < 70) + ".");
		System.out.println("     ***** END BANK *****");
		return myRnd < 70;
	}

	/*
	 * L'application sender vous demande un XML avec la requete request
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsqu'une application vous contacte
	 */
	@Deprecated
	static public Document answerToRequestXML(EApplication sender, String request){
		// TODO Auto-generated method stub
		return null;
	}

	private SyncMessHandler() {

	}
}
