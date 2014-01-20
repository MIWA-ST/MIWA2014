package fr.epita.sigl.miwa.application.messaging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Logger;

import org.w3c.dom.Document;
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
		LOGGER.info("***** Recepting message.");

		xml.getDocumentElement().normalize();
		String serviceToPerform = xml.getDocumentElement().getAttribute("service");
		String actionToPerform = xml.getDocumentElement().getAttribute("action");

		if (serviceToPerform.equals("paiement_cb"))
		{
			LOGGER.info("***** Paiement by CB service started.");

			String montant = "";
			String[] cb = {"", "", ""};

			NodeList nl = xml.getDocumentElement().getChildNodes();
			NodeList cnl = null;
			for (int i = 0; i < nl.getLength(); ++i)
			{
				if (nl.item(i).getNodeName().equals("montant"))
					montant = nl.item(i).getTextContent();
				if (nl.item(i).getNodeName().equals("cb"))
					cnl = nl.item(i).getChildNodes();
			}
			for (int i = 0; i < cnl.getLength(); ++i) 
			{
				if (cnl.item(i).getNodeName().equals("numero"))
					cb[i] = cnl.item(i).getTextContent();
				if (cnl.item(i).getNodeName().equals("date_validite"))
					cb[i] = cnl.item(i).getTextContent();
				if (cnl.item(i).getNodeName().equals("pictogramme"))
					cb[i] = cnl.item(i).getTextContent();
			}
			
			if (montant.equals("") || cb[0].equals("") || cb[1].equals("") || cb[2].equals(""))
			{
				LOGGER.info("***** ERROR : Bad XML input.");
				return false;
			}
			Float mt;
			try 
			{
				mt = Float.parseFloat(montant);
				if (mt < 0)
				{
					LOGGER.info("***** ERROR : The amount indicated is not a positive number.");
					return false;
				}
			}
			catch (NumberFormatException e)
			{
				LOGGER.info("***** ERROR : The amount indicated is not a valid number.");
				return false;
			}
			LOGGER.info("***** REQUEST -> " + montant + "€ for the credit card : " + cb[0]);
			Boolean bankResponse = getBankPaiement();
			LOGGER.info("***** Paiement by CB service terminated normally with : " + bankResponse + ".");
			return bankResponse;
		}
		else if (serviceToPerform.equals("paiement_cf"))
		{
			LOGGER.info("***** Paiement by fidelity service started.");

			String montantXML = null;
			String matriculeClientXML = null;

			// Récupération des données dans le XML
			NodeList nl = xml.getDocumentElement().getChildNodes();
			for (int i = 0; i < nl.getLength(); ++i)
			{
				if (nl.item(i).getNodeName().equals("montant"))
					montantXML = nl.item(i).getTextContent();
				if (nl.item(i).getNodeName().equals("matricule_client"))
					matriculeClientXML = nl.item(i).getTextContent();
			}
			// En cas d'absence de données dans le XML
			if (montantXML == null || matriculeClientXML == null) 
			{
				LOGGER.info("***** ERROR -> Missing data in request ('montant' or 'matricule_client'.");
				LOGGER.info("***** Paiement by fidelity service terminated normally with : " + false + ".");
				return false;
			}
			// En cas de données incohérentes dans le XML
			Float montantFloat = null; 
			try 
			{
				montantFloat = Float.parseFloat(montantXML);
			} 
			catch (Exception e) 
			{
				LOGGER.info("***** ERROR -> Invalid data ('montant' : " + montantFloat + ") in request.");
				LOGGER.info("***** Paiement by fidelity service terminated normally with : " + false + ".");
				return false;
			}
			if (montantFloat <= 0f)
			{
				LOGGER.info("***** ERROR -> Invalid data ('montant' : " + montantFloat + ") in request.");
				LOGGER.info("***** Paiement by fidelity service terminated normally with : " + false + ".");
				return false;
			}

			DbHandler dbHandler = new DbHandler();
			try 
			{
				// Connexion à la BDD
				Connection connection = dbHandler.open();

				// Récupération de l'id du client en vérifiant limite crédit totale et liste noire
				// TODO vérifier limite crédit mensuelle
				PreparedStatement pS = connection.prepareStatement("SELECT id_fidelity_credit_account as id, customer_code, echelon_nb, total_credit_limit "
						+ "FROM fidelity_credit_account as fca LEFT JOIN loyalty_card_type as lct ON fca.id_loyalty_card_type = lct.id_loyalty_card_type "
						+ "WHERE customer_code = ? AND is_blacklisted = FALSE AND (total_credit_amount - total_repaid_credit__amount) < total_credit_limit;");
				pS.setString(1, matriculeClientXML);
				ResultSet result = pS.executeQuery();

				if (result.next()) 
				{				
					Integer idClient = result.getInt("id");
					Integer echelonNb = result.getInt("echelon_nb");
					String bla = result.getString("total_credit_limit");
					
					LOGGER.info("***** REQUEST -> Credit of " + montantFloat + "€ (" + echelonNb + " month(s))for the fidelity account : " + matriculeClientXML);			
					
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
					
					LOGGER.info("***** REQUEST -> Done");
				}			
				// En cas de client inexistant ou non éligible pour crédit
				else
				{
					LOGGER.info("***** Paiement by fidelity service terminated normally with : " + false + ".");
					return false;
				}		
			} 
			catch (SQLException e) 
			{
				System.err.println("ERROR : " + e.getMessage());		
				return false;
			}
			finally
			{
				dbHandler.close();
			}

			LOGGER.info("***** Paiement by fidelity service terminated normally with : " + true + ".");
			return true;
		}
		else if (serviceToPerform.equals("cms_type_carte"))
		{
			LOGGER.info("***** Carte type service started.");
			if (actionToPerform.equals("c"))
			{
				LOGGER.info("***** Create a new card.");
				return true;
			}
			else if (actionToPerform.equals("m"))
			{
				LOGGER.info("***** Modify a card.");
				return true;
			}
			else if (actionToPerform.equals("s"))
			{
				LOGGER.info("***** Delete a card.");
				return true;
			}
			else
			{
				LOGGER.severe("***** A fatal error occured when processing Carte type service.");
				return false;
			}
		}
		else if (serviceToPerform.equals("cms_compte_cf"))
		{
			LOGGER.info("***** Fidelity account service started.");
			if (actionToPerform.equals("c"))
			{
				LOGGER.info("***** Create a new account.");
				return true;
			}
			else if (actionToPerform.equals("m"))
			{
				LOGGER.info("***** Modify an account.");
				return true;
			}
			else if (actionToPerform.equals("s"))
			{
				LOGGER.info("***** Delete an account.");
				return true;
			}
			else
			{
				LOGGER.severe("***** A fatal error occured when processing Fidelity Account service.");
				return false;
			}			
		}
		else
		{
			LOGGER.severe("***** A fatal error occured in the Monetique system.");
			return false;
		}
	}

	private static boolean getBankPaiement() 
	{
		LOGGER.info("***** Bank Paiement started.");		
		Random rnd = new Random();
		Integer jaimelesfrites = rnd.nextInt(100);

		LOGGER.info("***** Bank Paiement stopped.");
		return jaimelesfrites < 70;
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
