package fr.epita.sigl.miwa.application.clock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.db.DbHandler;
import fr.epita.sigl.miwa.st.clock.ClockFactory;
import fr.epita.sigl.miwa.st.clock.IExposedClock;

public class ClockClient {

	private static final Logger log = Logger.getLogger(ClockClient.class.getName());

	private static Integer counter = 0;

	/*
	 * R�cup�re l'horloge serveur pour faire des requ�tes dessus (getHour, wakeMeUp, ...)
	 */
	static public IExposedClock getClock() {
		return ClockFactory.getServerClock();
	}

	/*
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsque l'horloge vous contacte
	 */
	@Deprecated
	static public void wakeUp(Date date, Object message) {
		if (message instanceof String) {
			if (((String) message).equalsIgnoreCase("newWeek")) {
				// Paiement fidélité en fin de mois
				if (counter == 0)
				{		
					System.out.println("***** BEGIN SERVICE PAYOFF CREDITS : " + date.toString() + " *****");
					log.info("***** MONETIQUE SERVICE CALL: REQUEST -> Payoff fidelity credits the: " + date + ".");

					Map<Integer, Float> comptes = new HashMap<>();
					Map<Integer, String> idToMatricule = new HashMap<>();

					DbHandler dbHandler = new DbHandler();
					try 
					{
						// Connexion à la BDD
						Connection connection = dbHandler.open();

						log.info("***** SERVICE PAYOFF CREDITS: Fetching in-debts accounts.");	

						// Sélection des comptes qui ont des crédits à rembourser
						PreparedStatement ps = connection.prepareStatement("SELECT ID_FIDELITY_CREDIT_ACCOUNT, CUSTOMER_CODE FROM fidelity_credit_account WHERE IS_DELETED = FALSE AND (TOTAL_CREDIT_AMOUNT - TOTAL_REPAID_CREDIT__AMOUNT) > 0;");
						ResultSet res = ps.executeQuery();

						while (res.next())
						{
							comptes.put(res.getInt("ID_FIDELITY_CREDIT_ACCOUNT"), 0f);
							idToMatricule.put(res.getInt("ID_FIDELITY_CREDIT_ACCOUNT"), res.getString("CUSTOMER_CODE"));
						}

						// Pour chaque compte sélectionné
						for (Integer c : comptes.keySet()) 
						{
							// Sélection des crédits en cours du compte
							ps = connection.prepareStatement("SELECT SUM(AMOUNT / ECHELON_NB) FROM fidelity_credit WHERE IS_REPAID = false AND ID_FIDELITY_CREDIT_ACCOUNT = ?;");
							ps.setInt(1, c);
							res = ps.executeQuery();
							if (res.next())
							{
								comptes.put(c, res.getFloat(1));

								log.info("***** SERVICE PAYOFF CREDITS: Fidelity account with customer matricule: '" + idToMatricule.get(c) + "' has " + res.getFloat(1)+ "€ to pay.");
							}
						}

						for (Integer c : comptes.keySet()) 
						{
							if (!getBankPaiement())
							{
								ps = connection.prepareStatement("UPDATE fidelity_credit_account SET IS_BLACKLISTED = true, BLAKLISTED_DATE = NOW() WHERE ID_FIDELITY_CREDIT_ACCOUNT = ?;");
								ps.setInt(1, c);
								ps.executeUpdate();

								log.info("***** SERVICE PAYOFF CREDITS : Fidelity account with customer matricule: '" + idToMatricule.get(c) + "' is now blacklisted because bank refuses paiement.");
							}
							else
							{
								ps = connection.prepareStatement("UPDATE fidelity_credit_account SET IS_BLACKLISTED = false, BLAKLISTED_DATE = NOW(), TOTAL_REPAID_CREDIT__AMOUNT = TOTAL_REPAID_CREDIT__AMOUNT + ? WHERE ID_FIDELITY_CREDIT_ACCOUNT = ?;");
								ps.setFloat(1, comptes.get(c));
								ps.setInt(2, c);
								ps.executeUpdate();

								ps = connection.prepareStatement("UPDATE fidelity_credit SET REPAID_AMOUNT = REPAID_AMOUNT + (AMOUNT / ECHELON_NB) WHERE ID_FIDELITY_CREDIT_ACCOUNT = ? AND IS_REPAID = false;");
								ps.setInt(1, c);
								ps.executeUpdate();

								ps = connection.prepareStatement("UPDATE fidelity_credit SET IS_REPAID = true WHERE ID_FIDELITY_CREDIT_ACCOUNT = ? AND IS_REPAID = false AND REPAID_AMOUNT >= AMOUNT;");
								ps.setInt(1, c);
								ps.executeUpdate();

								log.info("***** SERVICE PAYOFF CREDITS : Fidelity account with customer matricule: '" + idToMatricule.get(c) + "' repaid his depts for this month.");
							}
						}	
					} 
					catch (SQLException e) 
					{
						System.err.println("ERROR : " + e.getMessage());
						log.info("***** SERVICE PAYOFF CREDITS: REQUEST -> ERROR.");
						System.out.println("***** END SERVICE *****");
					}
					finally
					{
						counter++;
						dbHandler.close();
					}

					log.info("***** SERVICE PAYOFF CREDITS: REQUEST -> DONE.");
					System.out.println("***** END SERVICE *****");
				}
				else
				{
					counter = (counter + 1) % 4;
				}

			} 
			else if (((String) message).equalsIgnoreCase("newDay")) {
				Date clockDate = ClockClient.getClock().getHour();
				System.out.println("NEW DAY: " + clockDate);
			}
			else {
				System.out.println(date.toString() + " : " + message);
			}
		}
	}

	private static boolean getBankPaiement() 
	{
		log.info("***** BANK SERVICE : Bank Paiement service call started.");		
		Random rnd = new Random();
		Integer myRnd = rnd.nextInt(100);

		log.info("***** BANK SERVICE : Bank Paiement service call stopped with : " + (myRnd < 70) + ".");
		return myRnd < 70;
	}
}
