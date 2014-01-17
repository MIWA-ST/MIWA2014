package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.XmlReader;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	@Override
	public void onException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String message, EApplication source) {
		LOGGER.severe(message);		
	}

	@Override
	public void onFile(File file, EApplication source) {
		if (source.equals(EApplication.GESTION_COMMERCIALE)) {
			LOGGER.severe("***** Réception d'un fichier de la GC");		
			XmlReader xmlReader = new XmlReader("testFileGC.xml");
			xmlReader.parseProducts();
		}
			
		LOGGER.severe(source + " : " + file.getAbsolutePath());		
	}

}
