package fr.epita.sigl.miwa.st.async.file;

import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;

public interface IAsyncFileManager {

	/**
	 * Envoi le fichier à l'application et notifie l'application de cet envoi
	 * 
	 * @param filename
	 *            le nom du fichier à envoyer
	 * @param destination
	 *            l'application à qui envoyer le fichier
	 * @throws AsyncFileException
	 */
	public void send(String filename, EApplication destination)
			throws AsyncFileException;
}
