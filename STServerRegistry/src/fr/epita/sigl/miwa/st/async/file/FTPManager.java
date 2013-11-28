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

public class FTPManager implements IAsyncFileManager {

	private static final Logger log = Logger.getLogger(FTPManager.class
			.getName());

	private FTPClient ftpClient = null;

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
			throw new AsyncFileException(
					"Failed to connect to FTP server", e);
		}
	}

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

	private void send(String localFilepath, String destApplication,
			String remoteFilename) throws AsyncFileException {
		InputStream inputStream = null;
		try {
			log.info("Trying to send file " + localFilepath + " to "
					+ destApplication + "...");
			inputStream = new FileInputStream(new File(localFilepath));
			if (ftpClient != null && ftpClient.isConnected()) {
				log.info("Sending file " + localFilepath + " to "
						+ destApplication + "...");
				ftpClient.storeFile(destApplication + "/" + remoteFilename,
						inputStream);
				if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
					disconnect();
					log.severe("Failed to send file " + localFilepath
							+ "  to FTP server !");
					log.severe("FTP error : " + ftpClient.getReplyString());
					throw new AsyncFileException("Failed to send file "
							+ localFilepath + " to FTP server : "
							+ ftpClient.getReplyString());
				}
				log.info("File successfully sent to " + destApplication + " !");
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

	private void retrieve(String remoteFilepath, String application,
			String localFilepath) throws AsyncFileException {
		OutputStream outputStream = null;
		try {
			log.info("Trying to retrieve file " + remoteFilepath + " to "
					+ localFilepath + "...");
			File localFile = new File(localFilepath);
			outputStream = new FileOutputStream(localFile);
			if (ftpClient != null && ftpClient.isConnected()) {
				log.info("Retrieving file " + remoteFilepath + " to "
						+ localFilepath + "...");
				ftpClient.retrieveFile(application + "/" + remoteFilepath,
						outputStream);
				if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
					disconnect();
					log.severe("Failed to retrieve file " + remoteFilepath
							+ "  to FTP server !");
					log.severe("FTP error : " + ftpClient.getReplyString());
					throw new AsyncFileException("Failed to retrieve file "
							+ remoteFilepath + " to FTP server : "
							+ ftpClient.getReplyString());
				}
				log.info("File successfully retrieved to " + localFilepath + " !");
			}
		} catch (FileNotFoundException e) {
			log.severe("File not found : " + e.getMessage());
			throw new AsyncFileException("File not found", e);
		} catch (IOException e) {
			log.severe("A problem occured when trying to retrieve file " + remoteFilepath);
			throw new AsyncFileException(
					"A problem occured when trying to retrieve file " + remoteFilepath, e);
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

//	public static void main(String[] args) throws AsyncFileException {
//		FTPManager ftpManager = new FTPManager();
//		ftpManager.send("FTPClientExample.java", EApplication.BACK_OFFICE);
//		ftpManager.retrieve("FTPClientExample.java", EApplication.BACK_OFFICE);
//	}

	@Override
	public void retrieve(String filename, EApplication current)
			throws AsyncFileException {
		// TODO Récupérer les properties qui vont bien
		String localFolder = "/Users/francois/Downloads/";
		String remoteFolder = "TOTO";
		String host = "bnf.sigl.epita.fr";
		int port = 2266;
		String username = "anonymous";
		String password = "";
		
		connect(host, port, username, password);
		retrieve(filename, remoteFolder, localFolder + filename);
	}

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
		send(localFolder + filename, remoteFolder, filename);
	}
}
