package fr.epita.sigl.miwa.st.async.file.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.ConfigurationContainer;
import fr.epita.sigl.miwa.st.ConfigurationException;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;

/**
 * L'utilitaire de copie de fichier en local
 * 
 * @author francois
 * 
 */
public class LocalFileHelper implements IAsyncFileHelper {

	private static final Logger LOGGER = Logger.getLogger(LocalFileHelper.class
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
		String localRepository = null;
		try {
			localRepository = ConfigurationContainer.getInstance()
					.getLocalRepository();
		} catch (ConfigurationException e) {
			LOGGER.severe("Failed to load configuration");
			throw new AsyncFileException("Failed to load configuration", e);
		}

		destFolder = new File(localRepository + File.separatorChar
				+ application.getShortName());
		localFolder = new File(localRepository
				+ File.separatorChar
				+ ConfigurationContainer.getInstance().getCurrentApplication()
						.getShortName());
		File repository = new File(localRepository);
		if (!repository.exists()) {
			throw new AsyncFileException("The repository "
					+ repository.getPath() + " does not exist !");
		}

		if (!destFolder.exists()) {
			LOGGER.info("Destination folder does not exists, trying to create it...");
			destFolder.mkdir();
			LOGGER.info("Destination folder created");
		}
		if (!localFolder.exists()) {
			LOGGER.info("Local folder does not exists, trying to create it...");
			localFolder.mkdir();
			LOGGER.info("Local folder created");
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
	 * fr.epita.sigl.miwa.st.async.file.IAsyncFileHelper#retrieve(java.lang.
	 * String )
	 */
	@Override
	public File retrieve(String filename) throws AsyncFileException {
		EApplication current = ConfigurationContainer.getInstance()
				.getCurrentApplication();
		init(current);

		File from = new File(destFolder.getAbsolutePath() + File.separatorChar
				+ filename);
		File to = new File(localFolder.getAbsolutePath() + File.separatorChar
				+ filename);
		if (!from.exists()) {
			LOGGER.severe("Failed to find " + from.getPath());
			throw new AsyncFileException("Failed to find " + from.getPath());
		}
		try {
			LOGGER.info("Retrieving " + from + " to " + to + "...");
			copy(from.toPath(), to.toPath());
			LOGGER.info("File " + filename + " retrieved " + to + " !");
		} catch (IOException e) {
			LOGGER.severe("Failed to retrieve " + from + " to " + to);
			throw new AsyncFileException("Failed to retrieve " + from + " to "
					+ to, e);
		}
		return to;
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

		File from = new File(localFolder.getAbsolutePath() + File.separatorChar
				+ filename);
		File to = new File(destFolder.getAbsolutePath() + File.separatorChar
				+ filename);
		if (!from.exists()) {
			LOGGER.severe("Failed to find " + from.getPath());
			throw new AsyncFileException("Failed to find " + from.getPath());
		}
		try {
			LOGGER.info("Sending " + from + " to " + to + "...");
			copy(from.toPath(), to.toPath());
			LOGGER.info("File " + filename + " sent " + to + " !");
		} catch (IOException e) {
			LOGGER.severe("Failed to send " + from + " to " + to);
			throw new AsyncFileException(
					"Failed to send " + from + " to " + to, e);
		}
	}

}
