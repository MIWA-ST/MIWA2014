package fr.epita.sigl.miwa.application;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.ConfigurationException;
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
		//SyncMessHandler.getSyncMessSender().sendMessage(EApplication.GESTION_COMMERCIALE, "Coucou GC");
		
		LOGGER.severe("***** " + "Lancement de l'application MDM");
		LOGGER.severe("***** " + "Réception du fichier fournisseur");

		CsvParser parser = new CsvParser("testFile1.csv");
		parser.parse();
		
		sendGC();
		
		sendBIBOIN();
	
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

	private static void sendBIBOIN() throws AsyncFileException {
		try {
			String file = Conf.getInstance().getLocalRepository() + File.separator + EApplication.MDM.getShortName() + File.separator;
			
			XmlWriter xmlWriter = new XmlWriter(file + "outputFileBI.xml");
			xmlWriter.generateFileForBI();
			XmlWriter xmlWriter2 = new XmlWriter(file + "outputFileBO.xml");
			xmlWriter2.generateFileForBI();
			XmlWriter xmlWriter3 = new XmlWriter(file + "outputFileIN.xml");
			xmlWriter3.generateFileForBI();
			
			LOGGER.severe("***** " + "Envoi du fichier référentiel au BI");
			AsyncFileFactory.getInstance().getFileManager().send("outputFileBI.xml", EApplication.BI);
			LOGGER.severe("***** " + "Envoi du fichier référentiel au BO");
			AsyncFileFactory.getInstance().getFileManager().send("outputFileBO.xml", EApplication.BACK_OFFICE);
			LOGGER.severe("***** " + "Envoi du fichier référentiel au module INTERNET");
			AsyncFileFactory.getInstance().getFileManager().send("outputFileIN.xml", EApplication.INTERNET);
		
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void sendGC() throws AsyncMessageException {
		try {
			String fileGC = Conf.getInstance().getLocalRepository() + File.separator + EApplication.MDM.getShortName() + File.separator + "outputFileGC.xml";
			XmlWriter xmlWriter = new XmlWriter(fileGC);
			xmlWriter.generateFileForGC();
			
			LOGGER.severe("*****" + "Envoi des prix d'achats à la GC");
			AsyncMessageFactory.getInstance().getAsyncMessageManager().send(xmlWriter.readFile(fileGC, StandardCharsets.UTF_8), EApplication.GESTION_COMMERCIALE);
			
		} catch (ConfigurationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
