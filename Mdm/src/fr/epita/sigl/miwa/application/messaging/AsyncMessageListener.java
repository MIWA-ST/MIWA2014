package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
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
		if (source.equals(EApplication.GESTION_COMMERCIALE)) {
			LOGGER.severe("***** Réception d'un message de la GC");	
			UUID id = UUID.randomUUID();
			Path path = FileSystems.getDefault().getPath(".", "FileGC" + id.toString());
			try {
				Files.write(path, message.getBytes(), StandardOpenOption.CREATE);
				XmlReader xmlReader = new XmlReader("FileGC" + id.toString());
				if (message.startsWith("<PROMOTIONS>")) {
					xmlReader.parsePromotions();
				if (message.startsWith("<PRIXVENTE>"))
					xmlReader.parseProducts();
				}
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
