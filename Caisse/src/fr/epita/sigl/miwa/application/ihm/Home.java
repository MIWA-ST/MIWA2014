package fr.epita.sigl.miwa.application.ihm;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

import fr.epita.sigl.miwa.application.BddAccess;
import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.Produit;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.custom.ScrolledComposite;

public class Home {
	private Text nbtext;
	private Text fidtext;
	private Text cbtext;
	private Text esptext;
	private float prixtotal = 0;
	private float aPayer = 0;
	private Text numtext;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            /** Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		Shell shlApplicationCaisse = new Shell();
		shlApplicationCaisse.setSize(542, 487);
		shlApplicationCaisse.setText("Application Caisse");
		shlApplicationCaisse.setLayout(null);

		Group grpTransaction = new Group(shlApplicationCaisse, SWT.NONE);
		grpTransaction.setText("Transaction");
		grpTransaction.setBounds(0, 0, 526, 448);

		Label lblProduit = new Label(grpTransaction, SWT.NONE);
		lblProduit.setBounds(10, 34, 68, 29);
		lblProduit.setText("Produit");

		final Combo produitcombo = new Combo(grpTransaction, SWT.READ_ONLY);
		produitcombo.setBounds(84, 45, 156, 31);

		final List listproduct = new List(grpTransaction, SWT.BORDER
				| SWT.V_SCROLL);
		listproduct.setBounds(10, 160, 284, 169);

		Button btnNewButton = new Button(grpTransaction, SWT.NONE);

		btnNewButton.setBounds(110, 394, 184, 44);
		btnNewButton.setText("Nouveau client");

		Label lblQuantit = new Label(grpTransaction, SWT.NONE);
		lblQuantit.setBounds(10, 69, 55, 38);
		lblQuantit.setText("Quantité");

		nbtext = new Text(grpTransaction, SWT.BORDER);
		nbtext.setBounds(84, 74, 76, 36);

		Button btnScanner = new Button(grpTransaction, SWT.NONE);

		btnScanner.setBounds(84, 116, 100, 38);
		btnScanner.setText("Scanner");

		Button btnNewButton_1 = new Button(grpTransaction, SWT.NONE);

		btnNewButton_1.setBounds(351, 280, 119, 49);
		btnNewButton_1.setText("Payer");

		fidtext = new Text(grpTransaction, SWT.BORDER);
		fidtext.setBounds(374, 225, 76, 29);

		cbtext = new Text(grpTransaction, SWT.BORDER);
		cbtext.setBounds(374, 177, 76, 29);

		esptext = new Text(grpTransaction, SWT.BORDER);
		esptext.setBounds(374, 129, 76, 29);

		Label lblEspece = new Label(grpTransaction, SWT.NONE);
		lblEspece.setBounds(310, 129, 55, 26);
		lblEspece.setText("Espece");

		Label lblCb = new Label(grpTransaction, SWT.NONE);
		lblCb.setBounds(313, 177, 55, 26);
		lblCb.setText("CB");

		Label lblFid = new Label(grpTransaction, SWT.NONE);
		lblFid.setBounds(310, 225, 55, 26);
		lblFid.setText("Fid");

		final Label lblPrixTotal = new Label(grpTransaction, SWT.NONE);
		lblPrixTotal.setBounds(218, 351, 76, 29);
		lblPrixTotal.setText("Prix total");

		numtext = new Text(grpTransaction, SWT.BORDER);
		numtext.setBounds(375, 77, 76, 35);

		Label lblNumClient = new Label(grpTransaction, SWT.NONE);
		lblNumClient.setBounds(297, 81, 76, 26);
		lblNumClient.setText("Num client");

		// remplissage de la combobox
		final Set<Produit> tabproduct = new HashSet<Produit>();
		final Set<Produit> selectedproducts = new HashSet<Produit>();
		try {
			ResultSet rs = Main.bdd
					.select("select (produit_id,produit_prix, produit_ref, produit_nom,produit_pourcentagepromo) from produit");
			while (rs.next()) {
				String[] resu = Main.bdd.parseresult(rs.getString(1));
				Produit prod = new Produit(resu[0], resu[1], resu[2], resu[3],
						resu[4]);
				tabproduct.add(prod);
				produitcombo.add(resu[2] + "-" + resu[3] + "-" + resu[1] + "€");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// bouton scanner produit, envoi du produit dans la liste et maj du prix
		btnScanner.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				// je chope litem de la combox
				int index = produitcombo.getSelectionIndex();
				String select = produitcombo.getItem(index);
				// je l'add dans ma liste d'objet produit
				String prod = select.split("-")[0];
				Iterator<Produit> e = tabproduct.iterator();
				Produit current = new Produit();
				while (e.hasNext()) {
					current = e.next();
					if (current.getRef() == (Long.parseLong(prod))) {
						selectedproducts.add(current);
						System.out.println("Ajout de : " + current.getRef()
								+ " dans le panier");
					}
				}
				// je l'add dans la liste visuel
				listproduct.add(select + "-*" + nbtext.getText());
				// j'update le prix total
				String upprix = select.split("-")[2];
				upprix = upprix.replace("€", "");
				prixtotal += (Float.parseFloat(upprix) * Integer
						.parseInt(nbtext.getText()));
				lblPrixTotal.setText(Float.toString(prixtotal) + "€");

				// update du reste à payer
				aPayer = prixtotal;

			}
		});

		// paiement listner j'envoi tous les élément pour une vente
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				// les infos à envoyer lorsque l'on paye sont les suivantes
				// Appel de la fonction d'envoi de la vente de patou
				// prixtotal = le prix total
				// selectedproducts = le panier (une liste de produits)
				// cbtext.getText() = payé en cb (string)
				// fidtext.getText() = payé en carte de fidelité (string)
				// esptext.getText() = payé en especèce (string)
				// numtest.getText() = numéro du client
				// utiliser la fonction Float.parseFloat(mastring) pour avoir un
				// flat
				JOptionPane.showMessageDialog(null,
						"reste à payer = " + aPayer, null, 1, null);

				// si on essaye de payer mais que le reste à payer est nul
				if (aPayer == 0) {
					JOptionPane.showMessageDialog(null,
							"L'achat a déjà été totalement réglé !", null, 1,
							null);
					return;
				}

				System.out.println("LOOOOOOOOOOOL");

				// paiement par carte de fidélité
				if (fidtext.getText() != "") {
					if (numtext.getText() == "") {
						JOptionPane.showMessageDialog(null,
								"Le client n'a pas de carte fidélité !", null,
								1, null);
					} else {
						if (Float.parseFloat(fidtext.getText()) <= aPayer) {
							String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"paiement_cf\"><montant>"
									+ fidtext.getText()
									+ "</montant><matricule_client>"
									+ numtext.getText()
									+ "</matricule_client></monetique>";
							boolean result = SyncMessFactory
									.getSyncMessSender().sendMessage(
											EApplication.MONETIQUE, message);
							if (result) {
								aPayer -= Float.parseFloat(fidtext.getText());

								if (aPayer == 0)
									JOptionPane
											.showMessageDialog(
													null,
													"Paiement fidélité validé ! Achat totalement réglé.",
													null, 1, null);
								else
									JOptionPane.showMessageDialog(null,
											"Paiement fidélité validé ! Reste à payer = "
													+ Float.toString(aPayer),
											null, 1, null);
							} else
								JOptionPane.showMessageDialog(null,
										"Paiement fidélité refusé ! Reste à payer = "
												+ Float.toString(aPayer), null,
										1, null);
						} else
							JOptionPane
									.showMessageDialog(
											null,
											"Le montant indiqué pour le paiement fidélité est incorrect.",
											null, 1, null);
					}
					return;
				}

				// paiement en espèce
				if (esptext.getText() != "") {
					if (Float.parseFloat(esptext.getText()) >= aPayer) {
						String monnaie = Float.toString(Float
								.parseFloat(esptext.getText()) - aPayer);
						aPayer = 0;
						JOptionPane.showMessageDialog(null,
								"Paiement en espèces validé ! Achat totalement réglé. Monnaie : "
										+ monnaie + "€", null, 1, null);
					} else {
						aPayer -= Float.parseFloat(esptext.getText());
						JOptionPane
								.showMessageDialog(null,
										"Paiement en espèces validé ! Reste à payer = "
												+ Float.toString(aPayer), null,
										1, null);
					}
					return;
				}

				// paiement en CB
				if (cbtext.getText() != "") {
					if (Float.parseFloat(cbtext.getText()) <= aPayer) {
						// TODO AJOUTER DES CHAMPS POUR NUMEROCB
						String numeroCB = "";
						String dateValidite = "";
						String picto = "";
						String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><monetique service=\"paiement_cb\"><montant>"
								+ cbtext.getText()
								+ "</montant><cb><numero>"
								+ numeroCB
								+ "</numero><date_validite>"
								+ dateValidite
								+ "</date_validite><pictogramme>"
								+ picto
								+ "</pictogramme></cb></monetique>";
						boolean result = SyncMessFactory.getSyncMessSender()
								.sendMessage(EApplication.MONETIQUE, message);
						if (result) {
							aPayer -= Float.parseFloat(cbtext.getText());

							if (aPayer == 0)
								JOptionPane
										.showMessageDialog(
												null,
												"Paiement en CB validé ! Achat totalement réglé.",
												null, 1, null);
							else
								JOptionPane.showMessageDialog(null,
										"Paiement en CB validé ! Reste à payer = "
												+ Float.toString(aPayer), null,
										1, null);
						} else
							JOptionPane.showMessageDialog(null,
									"Paiement en CB refusé ! Reste à payer = "
											+ Float.toString(aPayer), null, 1,
									null);
					} else
						JOptionPane
								.showMessageDialog(
										null,
										"Le montant indiqué pour le paiement par CB est incorrect.",
										null, 1, null);
					return;
				}
			}
		});

		// bouton nouveau client
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				selectedproducts.clear();
				cbtext.setText("");
				fidtext.setText("");
				esptext.setText("");
				numtext.setText("");
				nbtext.setText("");
				listproduct.removeAll();
				lblPrixTotal.setText("Prix total");
				aPayer = 0;
				JOptionPane.showMessageDialog(null, "Nouveau client", null, 1,
						null);
			}
		});

		shlApplicationCaisse.open();
		shlApplicationCaisse.layout();
		while (!shlApplicationCaisse.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}