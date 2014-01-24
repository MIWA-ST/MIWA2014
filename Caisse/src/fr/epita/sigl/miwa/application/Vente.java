package fr.epita.sigl.miwa.application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
				Set<Produit> selectedproducts = new HashSet<Produit>();
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
					int minclient = 0;
					int maxclient = 5;
					int fid = (int) (Math.random() * (maxclient - minclient))
							+ minclient;
					String idClient = "";
					System.out.println("SI FID VAUT 1 ALORS CLIENT FIDELISE : " + fid);
					// je suis fidèle
					//if (fid == 1) {
					if (true){	
					// je cherche un client fidèlisé
						ArrayList<String> listclient = new ArrayList<String>();
						int iteratclient = 0;
						try {
							ResultSet rs = Main.bdd
									.select("select client_mat from clientfid");

							while (rs.next()) {
								idClient = rs.getString(1);
								listclient.add(rs.getString(1));
								iteratclient++;
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						int min = 0;
						int max = iteratclient;
						int rclient = (int) (Math.random() * (max - min)) + min;
						int iterator = 0;
						for (String el : listclient) {
							if (iterator == rclient)
								idClient = el;
						}
					}
					
					System.out.println("id du client : " + idClient);
					// random sur le paiment
					int lower = 0;
					int higher = 3;
					int paiement = (int) (Math.random() * (higher - lower))
							+ lower;
					String type = "";
					// si je paye par espece
					if (paiement == 0) {
						type = "esp";
						if (fid == 1) {
							String updatedTicket = XmlGenerator.AskReducToBO(
									selectedproducts, idClient, type);
							{
							String updatedPrice = XmlParser
									.getUpdatedPrice(updatedTicket);
							selectedproducts = XmlParser
									.getUpdatedProducts(updatedTicket);
							prixtotal = Float.parseFloat(updatedPrice);
							}
						}
						System.out.println(prixtotal);
						System.out.println("paiement espece ok");
					}
					// si je paye par CB
					else if (paiement == 1) {
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
						if (fid == 1) {
							String updatedTicket = XmlGenerator.AskReducToBO(
									selectedproducts, idClient, type);
							if (updatedTicket == null)
							{
							String updatedPrice = XmlParser
									.getUpdatedPrice(updatedTicket);
							selectedproducts = XmlParser
									.getUpdatedProducts(updatedTicket);
							prixtotal = Float.parseFloat(updatedPrice);
							}
						}
						Boolean result = XmlGenerator.CheckCbPaymentWithMo(""
								+ prixtotal,
								Integer.toString(cbchiffre + cbchiffre2),
								Integer.toString(cbdate2 + cbdate1),
								Integer.toString(cbpicto));

						if (result)
							System.out.println("CB OK");
						else {
							System.out.println("CB NOK");
							System.out.println("paiement par espece finalement : ok");
							type = "esp";
						}
					}
					// si je paye par carte de fidelité
					else {
						type = "cf";
						System.out.println("paiement par carte de fidelité");
						System.out.println("si fidèle doit être égale à 1 : " + fid);

						//TODO SI JE RENTRE ICI MAIS QUE JE NE SUIS PAS FIDELISE (fid != 1), ERREUR ? PAIEMENT PAR ESPECE
						// je veux mettre à jour le ticket (client fid)
						if (fid == 1) {
							String updatedTicket = XmlGenerator.AskReducToBO(
									selectedproducts, idClient, type);
							if (updatedTicket == null)
							{
							String updatedPrice = XmlParser
									.getUpdatedPrice(updatedTicket);
							selectedproducts = XmlParser
									.getUpdatedProducts(updatedTicket);
							prixtotal = Float.parseFloat(updatedPrice);
							}
						}

						XmlGenerator.CheckFidPaymentWithMo("" + prixtotal,
								idClient);
					}
					System.out.println("-------------------------------------");
					XmlGenerator.SendTicketToBO(selectedproducts, idClient,
							type);
				}
				System.out.println(ClockClient.getClock().getHour());
				i++;

				try {
					Thread.sleep(300);
				} catch (InterruptedException ie) {
				}

			} else {
				// System.out.println("je dors");
				Thread.sleep(300);

			}

		}
	}
}
