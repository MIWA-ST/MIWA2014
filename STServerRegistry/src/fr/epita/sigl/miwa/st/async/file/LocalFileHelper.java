package fr.epita.sigl.miwa.st.async.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.EApplication;

/**
 * L'utilitaire de copie de fichier en local
 * 
 * @author francois
 * 
 */
public class LocalFileHelper implements IAsyncFileHelper {

	private static final Logger log = Logger.getLogger(LocalFileHelper.class
			.getName());

	private File destFolder = null;
	private File localFolder = null;

	/**
	 * Initialisation du dépot local, création des dossiers...
	 * 
	 * @param application
	 *            L'application actuelle
	 * @throws AsyncFileException
	 */
	private void init(EApplication application) throws AsyncFileException {
		// TODO Aller chercher les properties qui vont bien
		String localRepository = "/Users/francois/Downloads/";
		destFolder = new File(localRepository + "TOTO");
		localFolder = new File(localRepository + "local");

		File repository = new File(localRepository);
		if (!repository.exists()) {
			throw new AsyncFileException("The repository "
					+ repository.getPath() + " does not exist !");
		}

		if (!destFolder.exists()) {
			log.info("Destination folder does not exists, trying to create it...");
			destFolder.mkdir();
			log.info("Destination folder created");
		}
		if (!localFolder.exists()) {
			log.info("Local folder does not exists, trying to create it...");
			localFolder.mkdir();
			log.info("Local folder created");
		}
	}

	/**
	 * Copie un fichier de from à to
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	private void copy(Path from, Path to) throws IOException {
		CopyOption[] options = { StandardCopyOption.REPLACE_EXISTING };
		Files.copy(from, to, options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.epita.sigl.miwa.st.async.file.IAsyncFileHelper#retrieve(java.lang.String
	 * )
	 */
	@Override
	public void retrieve(String filename) throws AsyncFileException {
		// TODO recuperer current application
		EApplication current = EApplication.BACK_OFFICE;
		init(current);

		File from = new File(destFolder.getAbsolutePath() + "/" + filename);
		File to = new File(localFolder.getAbsolutePath() + "/" + filename);
		if (!from.exists()) {
			log.severe("Failed to find " + from.getPath());
			throw new AsyncFileException("Failed to find " + from.getPath());
		}
		try {
			log.info("Retrieving " + from + " to " + to + "...");
			copy(from.toPath(), to.toPath());
			log.info("File " + filename + " retrieved " + to + " !");
		} catch (IOException e) {
			log.severe("Failed to retrieve " + from + " to " + to);
			throw new AsyncFileException("Failed to retrieve " + from + " to "
					+ to, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.epita.sigl.miwa.st.async.file.IAsyncFileHelper#send(java.lang.String,
	 * fr.epita.sigl.miwa.st.EApplication)
	 */
	@Override
	public void send(String filename, EApplication destination)
			throws AsyncFileException {
		init(destination);

		File from = new File(localFolder.getAbsolutePath() + "/" + filename);
		File to = new File(destFolder.getAbsolutePath() + "/" + filename);
		if (!from.exists()) {
			log.severe("Failed to find " + from.getPath());
			throw new AsyncFileException("Failed to find " + from.getPath());
		}
		try {
			log.info("Sending " + from + " to " + to + "...");
			copy(from.toPath(), to.toPath());
			log.info("File " + filename + " sent " + to + " !");
		} catch (IOException e) {
			log.severe("Failed to send " + from + " to " + to);
			throw new AsyncFileException(
					"Failed to send " + from + " to " + to, e);
		}
	}

}
