package fr.epita.sigl.miwa.application.clock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.JDOM;
import fr.epita.sigl.miwa.application.XMLManager;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.clock.ClockFactory;
import fr.epita.sigl.miwa.st.clock.IExposedClock;

public class ClockClient {
	private XMLManager manager = new XMLManager();
	private static final Logger LOGGER = Logger.getLogger(XMLManager.class.getName());
	
	/*
	 * R�cup�re l'horloge serveur pour faire des requ�tes dessus (getHour, wakeMeUp, ...)
	 */
	static public IExposedClock getClock() {
		return ClockFactory.getServerClock();
	}
	
	/*
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsque l'horloge vous contacte
	 */
	@Deprecated
	static public void wakeUp(Date date, Object message) {
		if (message instanceof String) {
			if (message.equals("baseclient")) {
				LOGGER.info("***** TODO: envoi de la segmentation client.");
				try {
					String res;
					res = XMLManager.getInstance().getSendClientBI();
					
					AsyncFileFactory.getInstance().getFileManager().send("clients.xml", EApplication.BI);
					LOGGER.info("***** Envoi de la segmentation client.");
				} catch (AsyncFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				System.out.println(date.toString() + " : " + message);
			}
		}
	}	
}
