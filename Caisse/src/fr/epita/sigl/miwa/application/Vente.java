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
					e.printStackTrace();
				}
				// Construction de la liste des produits sélectionnée
				float prixtotal = 0;
				int quant = 1;
				Iterator<Produit> e = tabproduct.iterator();
				Produit current = new Produit();
				while (e.hasNext()) {
					int add = random(0,3);
					current = e.next();
					if (add == 1) {
						quant = random(0,5);
						prixtotal += (current.getPrix() - (current.getPrix() * current.getPromo() / 100)) * quant;
						current.setQuantite(quant);
						System.out.println("Ajout de : " + quant + " "
								+ current.getNom() + " dans le panier");
						selectedproducts.add(current);
					}
				}
				if (selectedproducts.isEmpty())
					System.out.println("Pas de produit, vente annulée");
				else {
					int fid = random(0,5);
					String idClient = "";
					
					// je suis fidèle
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
							e1.printStackTrace();
						}
						int rclient = random(0, iteratclient);
						int iterator = 0;
						for (String el : listclient) {
							if (iterator == rclient)
								idClient = el;
						}
					}
					
					System.out.println("id du client : " + idClient);
					// random sur le paiment
					int paiement = random(0,3);
					String type = "";
					// si je paye par espece
					if (paiement == 0) {
						type = "esp";
						if (fid == 1) {
							String updatedTicket = XmlGenerator.AskReducToBO(
									selectedproducts, idClient, type);
							if (updatedTicket == "")
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
						int cbchiffre = random(1111111,999999);
						int cbchiffre2 = random(1111111,999999);
						int cbdate1 = random(1, 13);
						int cbdate2 = random(1, 31);
						int cbpicto = random(100, 999);
						type = "cb";
						if (fid == 1) {
							String updatedTicket = XmlGenerator.AskReducToBO(
									selectedproducts, idClient, type);
							if (updatedTicket == "")
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

						// je veux mettre à jour le ticket (client fid)
						if (fid == 1) {
							String updatedTicket = XmlGenerator.AskReducToBO(
									selectedproducts, idClient, type);
							if (updatedTicket == "")
							{
							String updatedPrice = XmlParser
									.getUpdatedPrice(updatedTicket);
							selectedproducts = XmlParser
									.getUpdatedProducts(updatedTicket);
							prixtotal = Float.parseFloat(updatedPrice);
							}
							XmlGenerator.CheckFidPaymentWithMo("" + prixtotal,
									idClient);
						}
						else
						{
							System.out.println("paiement par espece finalement : ok");
							type = "esp";
						}
						
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
				Thread.sleep(300);
			}

		}
	}
	public int random(int min, int max)
	{
		int rand = 0;
		rand = (int) (Math.random() * (max - min))
				+ min;
		return rand;
	}
}
