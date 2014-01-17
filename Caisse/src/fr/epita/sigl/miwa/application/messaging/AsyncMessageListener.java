package fr.epita.sigl.miwa.application.messaging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.application.ReadXMLFile;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	/*
	 * liste des messages/fichiers : FA - BO vers Caisse (articles, prix et
	 * promotions) MA - BO vers Caisse (mise à jour des articles, prix et
	 * promotions en cours de journée) MS - à confirmer - Caisse vers BO
	 * (demande de mise à jour du prix final si client fidélisé) MA - Caisse
	 * vers BO (ticket de vente au fil de l’eau) FA - Caisse vers BO (tous les
	 * tickets de vente en fin de journée) MS - Caisse vers Monétique pour CB MS
	 * - Caisse vers Monétique pour fidélité
	 */
	/*
	 * try { String content =
	 * "<xml><ENTETE objet=\"segmentation-client\" source=\"bi\" date=\"2013-12-18\"/><GROUPES><GROUPE><CRITERES><CRITERE type=\"age\" min=\"20\" max=\"30\"/><CRITERE type=\"geographie\" departement=\"75\"/><CRITERE type=\"sexe\" sexe=\"F\"/><CRITERE type=\"situation-familiale\" situation=\"marie\"/><CRITERE type=\"enfant\" enfant=\"1\"/><CRITERE type=\"fidelite\" carte=\"1\"/></CRITERES><CLIENTS><CLIENT numero=\"1\"><CATEGORIEARTICLES><CATEGORIE ref=\"0000000000000001\" achat=\"6\"/></CATEGORIEARTICLES></CLIENT></CLIENTS></GROUPE></GROUPES></xml>"
	 * ; String fileName = "/segmentation-client.xml"; String repo = (String)
	 * Conf.getInstance().getProp().get(Conf.LOCAL_REPOSITORY_KEY); File file =
	 * new File(repo + "/" + EApplication.BI.getShortName() + fileName);
	 * BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	 * writer.write(content); writer.close();
	 * AsyncFileFactory.getInstance().getFileManager().send(fileName,
	 * EApplication.CRM); LOGGER.info("Fichier envoyé au CRM");
	 * LOGGER.info("Nom du fichier : " + fileName); } catch (AsyncFileException
	 * e) { LOGGER.severe("Erreur pendant l'envoi du fichier au CRM");
	 * LOGGER.severe("L'erreur est : " + e); } catch (IOException e){
	 * LOGGER.severe("Erreur pendant l'écriture du fichier");
	 * LOGGER.severe("L'erreur est : " + e); }
	 */

	@Override
	public void onException(Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(String message, EApplication source) {
		if (source == EApplication.BACK_OFFICE) {
			LOGGER.info("Message reçu du Back Office");
			LOGGER.info("Le message est : " + message);
			// message de màj des prix
			ReadXMLFile.ParseBOString(message);
		} else {
			LOGGER.severe("From Caisse : La source de ce message ("
					+ source
					+ ") est inconnue et ne devrait pas communiquer avec nous !");
			LOGGER.severe("Le message est : " + message);
		}
	}

	@Override
	public void onFile(File file, EApplication source) {
		if (source == EApplication.BACK_OFFICE) {
			LOGGER.info("Fichier reçu du Back Office");
			LOGGER.info("Le path du fichier est : " + file.getAbsolutePath());
			// fichier de listing des produits
			ReadXMLFile.ParseBOFile(file);
		} else {
			LOGGER.severe("From Caisse : La source de ce fichier ("
					+ source
					+ ") est inconnue et ne devrait pas communiquer avec nous !");
			LOGGER.severe("Le path du fichier est : " + file.getAbsolutePath());
		}
	}

}
