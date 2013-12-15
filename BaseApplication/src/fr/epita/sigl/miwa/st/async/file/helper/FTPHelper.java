package fr.epita.sigl.miwa.st.async.file.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import fr.epita.sigl.miwa.st.ConfigurationContainer;
import fr.epita.sigl.miwa.st.ConfigurationException;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;

/**
 * L'utilitaire d'envoi de fichiers sur un serveur FTP
 * 
 * @author francois
 * 
 */
public class FTPHelper implements IAsyncFileHelper {

	private static final Logger LOGGER = Logger.getLogger(FTPHelper.class
			.getName());

	private FTPClient ftpClient = null;

	/**
	 * Connexion au serveur FTP et initialisation du FTPClient
	 * 
	 * @param address
	 * @param port
	 * @param username
	 * @param password
	 * @throws AsyncFileException
	 */
	private void connect(String address, int port, String username,
			String password) throws AsyncFileException {
		ftpClient = new FTPClient();
		try {
			LOGGER.info("Trying to connect to FTP server " + address + ":"
					+ String.valueOf(port) + "...");
			ftpClient.connect(address, port);

			LOGGER.info("Login to FTP server " + address + ":"
					+ String.valueOf(port) + "...");
			ftpClient.login(username, password);
			LOGGER.info("Login successful to FTP server " + address + ":"
					+ String.valueOf(port));
			int reply = ftpClient.getReplyCode();
			if (FTPReply.isPositiveCompletion(reply)) {
				LOGGER.info("Connection established with FTP server " + address
						+ ":" + String.valueOf(port));
				LOGGER.info("FTP Welcome message : "
						+ ftpClient.getReplyString());
			} else {
				disconnect();
				LOGGER.severe("Failed to connect to FTP server " + address
						+ ":" + String.valueOf(port) + " !");
				LOGGER.severe("FTP error : " + ftpClient.getReplyString());
				throw new AsyncFileException(
						"Failed to connect to FTP server : "
								+ ftpClient.getReplyString());
			}
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			disconnect();
			LOGGER.severe("Failed to connect to FTP server" + e);
			throw new AsyncFileException("Failed to connect to FTP server", e);
		}
	}

	/**
	 * Déconnexion au serveur FTP
	 */
	private void disconnect() {
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException f) {
				LOGGER.severe("Failed to disconnect to FTP Server");
			}
		}
	}

	/**
	 * Envoi le fichier sur le serveur FTP
	 * 
	 * @param localFilepath
	 *            Le chemin du fichier à envoyer
	 * @param remoteFilepath
	 *            Le chemin de l'endroit où le fichier doit être placé sur le
	 *            serveur FTP
	 * @throws AsyncFileException
	 */
	private void send(String localFilepath, String remoteFilepath)
			throws AsyncFileException {
		InputStream inputStream = null;
		try {
			LOGGER.info("Trying to send file " + localFilepath + " to "
					+ remoteFilepath + "...");
			File localFile = new File(localFilepath);
			if (!localFile.exists()) {
				LOGGER.severe("The file " + localFilepath + " does not exists.");
				throw new AsyncFileException("The file " + localFilepath
						+ " does not exists.");
			}
			inputStream = new FileInputStream(localFile);
			if (ftpClient != null && ftpClient.isConnected()) {
				LOGGER.info("Sending file " + localFilepath + " to "
						+ remoteFilepath + "...");
				ftpClient.storeFile(remoteFilepath, inputStream);
				if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
					disconnect();
					LOGGER.severe("Failed to send file " + localFilepath
							+ "  to FTP server !");
					LOGGER.severe("FTP error : " + ftpClient.getReplyString());
					throw new AsyncFileException("Failed to send file "
							+ localFilepath + " to FTP server : "
							+ ftpClient.getReplyString());
				}
				LOGGER.info("File successfully sent to " + remoteFilepath
						+ " !");
			}
		} catch (FileNotFoundException e) {
			LOGGER.severe("File not found : " + e.getMessage());
			throw new AsyncFileException("File not found", e);
		} catch (IOException e) {
			LOGGER.severe("A problem occured when trying to send file "
					+ localFilepath);
			throw new AsyncFileException(
					"A problem occured when trying to send file "
							+ localFilepath, e);
		} finally {
			disconnect();
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOGGER.severe("Failed to close InputStream");
				}
			}
		}
	}

	/**
	 * Récupère le fichier depuis le serveur FTP
	 * 
	 * @param remoteFilepath
	 *            Le chemin du fichier à récupérer sur le serveur FTP
	 * @param localFilepath
	 *            Le chemin où sera stocké le fichier en local
	 * @throws AsyncFileException
	 */
	private File retrieve(String remoteFilepath, String localFilepath)
			throws AsyncFileException {
		OutputStream outputStream = null;
		File localFile = null;
		try {
			LOGGER.info("Trying to retrieve file " + remoteFilepath + " to "
					+ localFilepath + "...");
			localFile = new File(localFilepath);
			outputStream = new FileOutputStream(localFile);
			if (ftpClient != null && ftpClient.isConnected()) {
				LOGGER.info("Retrieving file " + remoteFilepath + " to "
						+ localFilepath + "...");
				ftpClient.retrieveFile(remoteFilepath, outputStream);
				if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
					disconnect();
					LOGGER.severe("Failed to retrieve file " + remoteFilepath
							+ "  to FTP server !");
					LOGGER.severe("FTP error : " + ftpClient.getReplyString());
					throw new AsyncFileException("Failed to retrieve file "
							+ remoteFilepath + " to FTP server : "
							+ ftpClient.getReplyString());
				}
				LOGGER.info("File successfully retrieved to " + localFilepath
						+ " !");
			}
		} catch (FileNotFoundException e) {
			LOGGER.severe("File not found : " + e.getMessage());
			throw new AsyncFileException("File not found", e);
		} catch (IOException e) {
			LOGGER.severe("A problem occured when trying to retrieve file "
					+ remoteFilepath);
			throw new AsyncFileException(
					"A problem occured when trying to retrieve file "
							+ remoteFilepath, e);
		} finally {
			disconnect();
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					LOGGER.severe("Failed to close OutputStream");
				}
			}
		}
		return localFile;
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
		String localRepository = null;
		String host = null;
		int port = 0;
		String username = null;
		String password = null;
		try {
			localRepository = ConfigurationContainer.getInstance()
					.getLocalRepository();
			host = ConfigurationContainer.getInstance().getFTPHost();
			port = ConfigurationContainer.getInstance().getFTPPort();
			username = ConfigurationContainer.getInstance().getFTPUsername();
			password = ConfigurationContainer.getInstance().getFTPPassword();
		} catch (ConfigurationException e) {
			LOGGER.severe("Failed to load configuration");
			throw new AsyncFileException("Failed to load configuration", e);
		}
		String remoteFolder = ConfigurationContainer.getInstance()
				.getCurrentApplication().getShortName();

		connect(host, port, username, password);
		return retrieve(remoteFolder + File.separatorChar + filename, localRepository
				+ File.separatorChar + filename);
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
		String remoteFolder = destination.getShortName();
		String localRepository = null;
		String host = null;
		int port = 0;
		String username = null;
		String password = null;
		try {
			localRepository = ConfigurationContainer.getInstance()
					.getLocalRepository();
			host = ConfigurationContainer.getInstance().getFTPHost();
			port = ConfigurationContainer.getInstance().getFTPPort();
			username = ConfigurationContainer.getInstance().getFTPUsername();
			password = ConfigurationContainer.getInstance().getFTPPassword();
		} catch (ConfigurationException e) {
			LOGGER.severe("Failed to load configuration");
			throw new AsyncFileException("Failed to load configuration", e);
		}

		connect(host, port, username, password);
		send(localRepository + File.separatorChar + filename, remoteFolder
				+ File.separatorChar + filename);
	}
}
