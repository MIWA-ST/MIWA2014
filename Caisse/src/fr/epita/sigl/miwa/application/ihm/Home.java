package fr.epita.sigl.miwa.application.ihm;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Home {
	private Text nbtext;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private int prixtotal = 0;

	/**
	 * Launch the application.
	 * @param args
	/**
	 * Open the window.
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
		lblProduit.setBounds(10, 48, 55, 15);
		lblProduit.setText("Produit");
		
		final Combo produitcombo = new Combo(grpTransaction, SWT.READ_ONLY);
		produitcombo.setBounds(83, 45, 156, 23);
		
		final List listproduct = new List(grpTransaction, SWT.BORDER);
		listproduct.setBounds(10, 160, 284, 169);
		
		Button btnNewButton = new Button(grpTransaction, SWT.NONE);
		btnNewButton.setBounds(110, 413, 184, 25);
		btnNewButton.setText("Nouveau client");
		
		Label lblQuantit = new Label(grpTransaction, SWT.NONE);
		lblQuantit.setBounds(10, 92, 55, 15);
		lblQuantit.setText("Quantité");
		
		nbtext = new Text(grpTransaction, SWT.BORDER);
		nbtext.setBounds(84, 89, 76, 21);
		
		Button btnScanner = new Button(grpTransaction, SWT.NONE);
		
		btnScanner.setBounds(84, 129, 75, 25);
		btnScanner.setText("Scanner");
		
		Button btnNewButton_1 = new Button(grpTransaction, SWT.NONE);
		btnNewButton_1.setBounds(351, 280, 100, 25);
		btnNewButton_1.setText("Payer");
		
		text_1 = new Text(grpTransaction, SWT.BORDER);
		text_1.setBounds(374, 233, 76, 21);
		
		text_2 = new Text(grpTransaction, SWT.BORDER);
		text_2.setBounds(374, 185, 76, 21);
		
		text_3 = new Text(grpTransaction, SWT.BORDER);
		text_3.setBounds(374, 137, 76, 21);
		
		Label lblEspece = new Label(grpTransaction, SWT.NONE);
		lblEspece.setBounds(310, 140, 55, 15);
		lblEspece.setText("Espece");
		
		Label lblCb = new Label(grpTransaction, SWT.NONE);
		lblCb.setBounds(313, 188, 55, 15);
		lblCb.setText("CB");
		
		Label lblFid = new Label(grpTransaction, SWT.NONE);
		lblFid.setBounds(310, 236, 55, 15);
		lblFid.setText("Fid");
		
		Label lblPrixTotal = new Label(grpTransaction, SWT.NONE);
		lblPrixTotal.setBounds(239, 351, 55, 15);
		lblPrixTotal.setText("Prix total");

		//remplissage de la combobox
		Set<Produit> tabproduct = new HashSet<Produit>();
		try {
			
			ResultSet rs = Main.bdd.select("select (produit_id,produit_prix, produit_ref, produit_nom,produit_pourcentagepromo) from produit");
			while (rs.next())
			{
				String[] resu = Main.bdd.parseresult(rs.getString(1));
				Produit prod = new Produit(resu[0],resu[1],resu[2],resu[3], resu[4]);
				tabproduct.add(prod);
				produitcombo.add(resu[2] + "-" + resu[3] + "-" + resu[1] + "€");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//bouton scanner produit, envoi du produit dans la liste et maj du prix
		btnScanner.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				int index = produitcombo.getSelectionIndex();
				String select = produitcombo.getItem(index);
				//String[] tabselect = select.split("-");
				listproduct.add(select + "-*" + nbtext.getText() );
				
				//lblPrixTotal.setText(prixtotal);
				
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
