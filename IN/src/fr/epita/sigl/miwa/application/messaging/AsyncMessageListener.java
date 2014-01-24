package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.ParseXML;
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
		if (source == EApplication.CRM)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(message, ParseXML.TYPE_LANGUAGE.STRING))
				parser.parseCRM();
		}
		else if (source == EApplication.GESTION_COMMERCIALE)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(message, ParseXML.TYPE_LANGUAGE.STRING))
				parser.parseGC();
		}
		else if (source == EApplication.MDM)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(message, ParseXML.TYPE_LANGUAGE.STRING))
				parser.parseMDM();
		}
	}

	@Override
	public void onFile(File file, EApplication source) {
		LOGGER.severe(source + " : " + file.getAbsolutePath());
		if (source == EApplication.CRM)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(file, ParseXML.TYPE_LANGUAGE.FICHIER))
				parser.parseCRM();
			else
				LOGGER.info("***** Problème à la lecture du fichier de CRM.");
		}
		else if (source == EApplication.GESTION_COMMERCIALE)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(file, ParseXML.TYPE_LANGUAGE.FICHIER))
				parser.parseGC();
			else
				LOGGER.info("***** Problème à la lecture du fichier de GC.");
		}
		else if (source == EApplication.MDM)
		{
			ParseXML parser = new ParseXML();
			
			if (parser.readXML(file, ParseXML.TYPE_LANGUAGE.FICHIER))
				parser.parseMDM();
			else
				LOGGER.info("***** Problème à la lecture du fichier de MDM.");
		}
	}
}
