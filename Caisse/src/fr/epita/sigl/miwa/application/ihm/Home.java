package fr.epita.sigl.miwa.application.ihm;

import java.sql.ResultSet;
import java.sql.SQLException;

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

import fr.epita.sigl.miwa.application.Main;

public class Home {
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;

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
		shlApplicationCaisse.setSize(473, 412);
		shlApplicationCaisse.setText("Application Caisse");
		shlApplicationCaisse.setLayout(null);
		
		Group grpTransaction = new Group(shlApplicationCaisse, SWT.NONE);
		grpTransaction.setText("Transaction");
		grpTransaction.setBounds(0, 0, 463, 373);
		
		Label lblProduit = new Label(grpTransaction, SWT.NONE);
		lblProduit.setBounds(10, 23, 55, 15);
		lblProduit.setText("Produit");
		
		Combo produitcombo = new Combo(grpTransaction, SWT.READ_ONLY);
		produitcombo.setBounds(71, 23, 156, 23);
		
		List listproduct = new List(grpTransaction, SWT.BORDER);
		listproduct.setBounds(71, 114, 184, 169);
		
		Button btnNewButton = new Button(grpTransaction, SWT.NONE);
		btnNewButton.setBounds(71, 338, 184, 25);
		btnNewButton.setText("Nouveau client");
		
		Label lblQuantit = new Label(grpTransaction, SWT.NONE);
		lblQuantit.setBounds(10, 59, 55, 15);
		lblQuantit.setText("Quantité");
		
		text = new Text(grpTransaction, SWT.BORDER);
		text.setBounds(71, 53, 76, 21);
		
		Button btnScanner = new Button(grpTransaction, SWT.NONE);
		btnScanner.setBounds(71, 83, 75, 25);
		btnScanner.setText("Scanner");
		
		Button btnNewButton_1 = new Button(grpTransaction, SWT.NONE);
		btnNewButton_1.setBounds(350, 258, 100, 25);
		btnNewButton_1.setText("Payer");
		
		text_1 = new Text(grpTransaction, SWT.BORDER);
		text_1.setBounds(374, 233, 76, 21);
		
		text_2 = new Text(grpTransaction, SWT.BORDER);
		text_2.setBounds(374, 206, 76, 21);
		
		text_3 = new Text(grpTransaction, SWT.BORDER);
		text_3.setBounds(374, 179, 76, 21);
		
		Label lblEspece = new Label(grpTransaction, SWT.NONE);
		lblEspece.setBounds(300, 185, 55, 15);
		lblEspece.setText("Espece");
		
		Label lblCb = new Label(grpTransaction, SWT.NONE);
		lblCb.setBounds(300, 212, 55, 15);
		lblCb.setText("CB");
		
		Label lblFid = new Label(grpTransaction, SWT.NONE);
		lblFid.setBounds(300, 237, 55, 15);
		lblFid.setText("Fid");
		
		Label lblPrixTotal = new Label(grpTransaction, SWT.NONE);
		lblPrixTotal.setBounds(200, 289, 55, 15);
		lblPrixTotal.setText("Prix total");

		//remplissage de la combobox
		try {
			ResultSet rs = Main.bdd.select("select (produit_nom) from produit");
			while (rs.next())
			{
				produitcombo.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		
		
		
		
		shlApplicationCaisse.open();
		shlApplicationCaisse.layout();
		while (!shlApplicationCaisse.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
