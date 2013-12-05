package fr.epita.sigl.miwa.st.async.file;

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

import fr.epita.sigl.miwa.st.EApplication;

/**
 * L'utilitaire d'envoi de fichiers sur un serveur FTP
 * 
 * @author francois
 * 
 */
public class FTPHelper implements AsyncFileHelper {

	private static final Logger log = Logger.getLogger(FTPHelper.class
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
			log.info("Trying to connect to FTP server " + address + ":"
					+ String.valueOf(port) + "...");
			ftpClient.connect(address, port);

			log.info("Login to FTP server " + address + ":"
					+ String.valueOf(port) + "...");
			ftpClient.login(username, password);
			log.info("Login successful to FTP server " + address + ":"
					+ String.valueOf(port));
			int reply = ftpClient.getReplyCode();
			if (FTPReply.isPositiveCompletion(reply)) {
				log.info("Connection established with FTP server " + address
						+ ":" + String.valueOf(port));
				log.info("FTP Welcome message : " + ftpClient.getReplyString());
			} else {
				disconnect();
				log.severe("Failed to connect to FTP server " + address + ":"
						+ String.valueOf(port) + " !");
				log.severe("FTP error : " + ftpClient.getReplyString());
				throw new AsyncFileException(
						"Failed to connect to FTP server : "
								+ ftpClient.getReplyString());
			}
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			disconnect();
			log.severe("Failed to connect to FTP server" + e);
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
				log.severe("Failed to disconnect to FTP Server");
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
			log.info("Trying to send file " + localFilepath + " to "
					+ remoteFilepath + "...");
			File localFile = new File(localFilepath);
			if (!localFile.exists()) {
				log.severe("The file " + localFilepath + " does not exists.");
				throw new AsyncFileException("The file " + localFilepath
						+ " does not exists.");
			}
			inputStream = new FileInputStream(localFile);
			if (ftpClient != null && ftpClient.isConnected()) {
				log.info("Sending file " + localFilepath + " to "
						+ remoteFilepath + "...");
				ftpClient.storeFile(remoteFilepath, inputStream);
				if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
					disconnect();
					log.severe("Failed to send file " + localFilepath
							+ "  to FTP server !");
					log.severe("FTP error : " + ftpClient.getReplyString());
					throw new AsyncFileException("Failed to send file "
							+ localFilepath + " to FTP server : "
							+ ftpClient.getReplyString());
				}
				log.info("File successfully sent to " + remoteFilepath + " !");
			}
		} catch (FileNotFoundException e) {
			log.severe("File not found : " + e.getMessage());
			throw new AsyncFileException("File not found", e);
		} catch (IOException e) {
			log.severe("A problem occured when trying to send file "
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
					log.severe("Failed to close InputStream");
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
	private void retrieve(String remoteFilepath, String localFilepath)
			throws AsyncFileException {
		OutputStream outputStream = null;
		try {
			log.info("Trying to retrieve file " + remoteFilepath + " to "
					+ localFilepath + "...");
			File localFile = new File(localFilepath);
			outputStream = new FileOutputStream(localFile);
			if (ftpClient != null && ftpClient.isConnected()) {
				log.info("Retrieving file " + remoteFilepath + " to "
						+ localFilepath + "...");
				ftpClient.retrieveFile(remoteFilepath, outputStream);
				if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
					disconnect();
					log.severe("Failed to retrieve file " + remoteFilepath
							+ "  to FTP server !");
					log.severe("FTP error : " + ftpClient.getReplyString());
					throw new AsyncFileException("Failed to retrieve file "
							+ remoteFilepath + " to FTP server : "
							+ ftpClient.getReplyString());
				}
				log.info("File successfully retrieved to " + localFilepath
						+ " !");
			}
		} catch (FileNotFoundException e) {
			log.severe("File not found : " + e.getMessage());
			throw new AsyncFileException("File not found", e);
		} catch (IOException e) {
			log.severe("A problem occured when trying to retrieve file "
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
					log.severe("Failed to close OutputStream");
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.epita.sigl.miwa.st.async.file.AsyncFileHelper#retrieve(java.lang.String
	 * )
	 */
	@Override
	public void retrieve(String filename) throws AsyncFileException {
		// TODO Récupérer les properties qui vont bien
		String localFolder = "/Users/francois/Downloads/";
		String remoteFolder = "TOTO";
		String host = "bnf.sigl.epita.fr";
		int port = 2266;
		String username = "anonymous";
		String password = "";

		connect(host, port, username, password);
		retrieve(remoteFolder + filename, localFolder + filename);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.epita.sigl.miwa.st.async.file.AsyncFileHelper#send(java.lang.String,
	 * fr.epita.sigl.miwa.st.EApplication)
	 */
	@Override
	public void send(String filename, EApplication destination)
			throws AsyncFileException {
		// TODO Récupérer les properties qui vont bien
		String host = "bnf.sigl.epita.fr";
		int port = 2266;
		String username = "anonymous";
		String password = "";
		String remoteFolder = "TOTO";
		String localFolder = "/Users/francois/Downloads/";

		connect(host, port, username, password);
		send(localFolder + filename, remoteFolder + filename);
	}
}
