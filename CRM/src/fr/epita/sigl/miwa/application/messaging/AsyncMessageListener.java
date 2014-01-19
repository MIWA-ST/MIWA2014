package fr.epita.sigl.miwa.application.messaging;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.XMLManager;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	private XMLManager manager = new XMLManager();
	private Charset encoding = StandardCharsets.UTF_8;

	@Override
	public void onException(Exception e) {
		LOGGER.severe("Erreur : " + e);		
	}

	@Override
	public void onMessage(String message, EApplication source) {	
		//Message du Back-office
		if (source == EApplication.BACK_OFFICE){
			LOGGER.info("*****Message reçu du Back-office");
			LOGGER.info("*****Le message est : " + message);
			
			PrintWriter out = null;
			try {
				out = new PrintWriter("backoffice.xml");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.print(message);
			out.close();
			
			try {
				manager.getInstance().dispatchXML("", "backoffice.xml");
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (source == EApplication.INTERNET) {
			LOGGER.info("*****Message reçu d'Internet");
			LOGGER.info("*****Le message est : " + message);
			
			PrintWriter out = null;
			try {
				out = new PrintWriter("internet.xml");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.print(message);
			out.close();
			
			try {
				manager.getInstance().dispatchXML("", "internet.xml");
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Message pas attendu
		else {
			LOGGER.info("*****Message qui ne nous intéresse pas de " + source);
			LOGGER.info("*****Le message est : " + message);
		}
	}

	@Override
	public void onFile(File file, EApplication source) {
		
		if (source == EApplication.BI){
			// Segmentation client
			LOGGER.info("*****Fichier reçu du BI");
			
			//TODO parer la segmentation reçue
			LOGGER.info("*****Le path du fichier est : " + file.getAbsolutePath());
		/*	
			 byte[] encoded = null;
			try {
				encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String xml = encoding.decode(ByteBuffer.wrap(encoded)).toString();
	*/		
			/*File res;
			try {
				res = AsyncFileFactory.getInstance().getFileHelper().retrieve(file.getAbsolutePath());
			} catch (AsyncFileException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			*/
			try {
				XMLManager.getInstance().dispatchXML("segmentation-client", file.getAbsolutePath());
			} catch (SAXException | IOException | AsyncMessageException
					| ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String result = null;
			try {
				result = manager.getSegmentationClient("Segmentation BI", file.getAbsolutePath());
			} catch (AsyncMessageException | IOException | SAXException
					| ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			LOGGER.info("*****Résultat : " + result);
			
		}	
		// Fichier non attendu
		else {
			LOGGER.severe("*****Fichier non attendu de " + source);
			LOGGER.severe("*****Le path du fichier est : " + file.getAbsolutePath());
		}
	}

}
