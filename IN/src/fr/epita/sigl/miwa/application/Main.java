package fr.epita.sigl.miwa.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.MDM.ProductsClientMDM;
import fr.epita.sigl.miwa.application.MO.PaiementCbMO;
import fr.epita.sigl.miwa.application.MO.PaiementCfMO;
import fr.epita.sigl.miwa.application.ParseXML.TYPE_LANGUAGE;
import fr.epita.sigl.miwa.application.BI.ArticleVenteBI;
import fr.epita.sigl.miwa.application.BI.CategorieVenteBI;
import fr.epita.sigl.miwa.application.BI.EnteteBI;
import fr.epita.sigl.miwa.application.BI.EnvoiInformationVentesBI;
import fr.epita.sigl.miwa.application.BI.EnvoiVenteDetailleeBI;
import fr.epita.sigl.miwa.application.BI.VenteBI;
import fr.epita.sigl.miwa.application.CR.CreationClientCR;
import fr.epita.sigl.miwa.application.CR.EnvoiEnteteCR;
import fr.epita.sigl.miwa.application.CR.EnvoiMatriculeCR;
import fr.epita.sigl.miwa.application.CR.ModifierUtilisateurCR;
import fr.epita.sigl.miwa.application.CR.SupprimerUtilisateurCR;
import fr.epita.sigl.miwa.application.GC.ArticleCommandeParClientGC;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockArticlesGC;
import fr.epita.sigl.miwa.application.GC.DemandeNiveauStockGC;
import fr.epita.sigl.miwa.application.GC.EnvoiCommandeGC;
import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws AsyncFileException,
			AsyncMessageException {
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		Conf.getInstance();
		SyncMessFactory.initSyncMessReceiver();	
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.initListener(new AsyncMessageListener());
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		/* CODE HERE */
		
		try {
			MiwaBDDIn bdd = MiwaBDDIn.getInstance();			
			bdd.connection();
			
			new BufferedReader(new InputStreamReader(System.in)).readLine();
			
			// ENvoi des messages (wakeMeUp...)
			
			bdd.close();
			Thread.sleep(40000);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	SyncMessHandler.getSyncMessSender().sendMessage(
			//	EApplication.INTERNET, "Coucou IN");
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
