package fr.epita.sigl.miwa.application.clock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.clock.ClockFactory;
import fr.epita.sigl.miwa.st.clock.IExposedClock;

public class ClockClient {

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
				System.out.println(date.toString() + " : C'est l'heure d'envoyer la base client!");
				
				try {
					//Date date = new Date();
					
					Writer writer = null;

					try {
					    writer = new BufferedWriter(new OutputStreamWriter(
					          new FileOutputStream(new Date().toGMTString() + "filename.txt"), "utf-8"));
					    writer.write("Something");
					} catch (IOException ex) {
					  // report
					} finally {
					   try {writer.close();} catch (Exception ex) {}
					}
					
					
					AsyncFileFactory.getInstance().getFileManager().send("/test.png", EApplication.BI);
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
