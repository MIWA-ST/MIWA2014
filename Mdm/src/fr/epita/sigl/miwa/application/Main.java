package fr.epita.sigl.miwa.application;

import java.io.File;
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
		
		CsvParser parser = new CsvParser("testFile1.csv");
		parser.parse();
	
		XmlWriter xmlWriter;

		try {
			String fileGC = Conf.getInstance().getLocalRepository() + File.separator + EApplication.MDM.getShortName() + File.separator + "outputFileGC.xml";
			xmlWriter = new XmlWriter(fileGC);
			xmlWriter.generateFileForGC();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AsyncFileFactory.getInstance().getFileHelper().send("outputFileGC.xml", EApplication.GESTION_COMMERCIALE);
		
		XmlReader xmlReader = new XmlReader("testFileGC.xml");
		xmlReader.parseProducts();
		
		XmlWriter xmlWriter2 = new XmlWriter("outputFileBI.xml");
		xmlWriter2.generateFileForBI();
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
