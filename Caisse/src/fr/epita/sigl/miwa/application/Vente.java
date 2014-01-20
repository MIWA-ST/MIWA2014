package fr.epita.sigl.miwa.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import fr.epita.sigl.miwa.application.XmlGenerator;
import fr.epita.sigl.miwa.application.clock.ClockClient;

public class Vente {
	Vente() {
	}

	public void create() throws InterruptedException {
		int i = 0;
		// 1 vente
		while (true) {
			if (Main.open) {
				System.out.println("------------------------------------");
				System.out.println("Vente : " + i);
				final Set<Produit> tabproduct = new HashSet<Produit>();
				final Set<Produit> selectedproducts = new HashSet<Produit>();
				// Construction de la liste des produits
				try {
					ResultSet rs = Main.bdd
							.select("select (produit_id,produit_prix, produit_ref, produit_nom,produit_pourcentagepromo) from produit");
					while (rs.next()) {
						String[] resu = Main.bdd.parseresult(rs.getString(1));
						Produit prod = new Produit(resu[0], resu[1], resu[2],
								resu[3], resu[4]);
						tabproduct.add(prod);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Construction de la liste des produits sélectionnée
				float prixtotal = 0;
				int quant = 1;
				Iterator<Produit> e = tabproduct.iterator();
				Produit current = new Produit();
				while (e.hasNext()) {
					int lower = 0;
					int higher = 3;
					int add = (int) (Math.random() * (higher - lower)) + lower;
					current = e.next();
					if (add == 1) {
						prixtotal += current.getPrix();
						lower = 1;
						higher = 5;
						quant = (int) (Math.random() * (higher - lower))
								+ lower;
						current.setQuantite(quant);
						System.out.println("Ajout de : " + quant + " "
								+ current.getNom() + " dans le panier");
						selectedproducts.add(current);
					}
				}
				if (selectedproducts.isEmpty())
					System.out.println("Pas de produit, vente annulée");
				else {
					// si client non fidélisé
					// traiter paiement par espèce OU cb
					int lower = 0;
					int higher = 3;
					int paiement = (int) (Math.random() * (higher - lower))
							+ lower;
					String type = "";
					String idClient = "";
					if (paiement == 0) {
						type = "esp";
						// paiement espece
						// selectedproducts = liste des produit
						// prixtotal = prix à payer
						System.out.println(prixtotal);
						System.out.println("paiement espece ok");
					} else if (paiement == 1){
						int cbmin = 0;
						int cbmax = 99999999;
						int cbchiffre = (int) (Math.random() * (cbmax - cbmin))
								+ cbmin;
						int cbchiffre2 = (int) (Math.random() * (cbmax - cbmin))
								+ cbmin;
						int datemin = 1;
						int datemax = 13;
						int datemax2 = 31;
						int cbdate1 = (int) (Math.random() * (datemax - datemin))
								+ datemin;
						int cbdate2 = (int) (Math.random() * (datemax2 - datemin))
								+ datemin;
						int picto1 = 100;
						int picto2 = 999;
						int cbpicto = (int) (Math.random() * (picto2 - picto1))
								+ picto1;
						type = "cb";
						Boolean result = XmlGenerator.CheckCbPaymentWithMo(""
								+ prixtotal,
								Integer.toString(cbchiffre + cbchiffre2),
								Integer.toString(cbdate2 + cbdate1),
								Integer.toString(cbpicto));
						
						if (result)
							System.out.println("CB OK");
						else
						{
							System.out.println("CB NOK");
							type = "esp";
						}
						// paiement CB
						// selectedproducts = liste des produit
						// prixtotal = prix à payer
						// traiter paiement CB avec MONETIQUE
						// si paiement OK avec CB (ou paiement espèce
						// automatqieuemtn validé)
						// appeler fonction generation XML (qui va envoyer le
						// ticket au BO)
						// if NOK paiement espece
					}
					else
					{
						System.out.println("paiement par carte de fidelité");
						try {
							ResultSet rs = Main.bdd
									.select("select client_mat from clientfid");
							while (rs.next()) {
								idClient = rs.getString(1);
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						String updatedTicket = XmlGenerator.AskReducToBO(selectedproducts, idClient, type);
						
						String updatedPrice = XmlParser.getUpdatedPrice(updatedTicket);
						selectedproducts = XmlParser.getUpdatedProducts(updatedTicket);
						//TODO RECUPERER LE NOUVEAU PRIX TOTAL DE UPDATEDTICKET
						// TODO CONSTRUIRE LE NOUVEAU SELECTEDPRODUCTS A PARTIR DE UPDATEDTICKET
						XmlGenerator.CheckFidPaymentWithMo("" + updatedPrice, idClient);
					}
					System.out.println("-------------------------------------");
					XmlGenerator.SendTicketToBO(selectedproducts, idClient, type);
				}
				System.out.println(ClockClient.getClock().getHour());
				i++;

				try {
					Thread.sleep(300);
				} catch (InterruptedException ie) {
				}

			}
			else
			{
				//System.out.println("je dors");
				Thread.sleep(300);
				
			}
			
		}
	}
}
