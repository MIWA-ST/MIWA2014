package fr.epita.sigl.miwa.application.messaging;

import java.io.File;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.Main;
import fr.epita.sigl.miwa.bo.object.ArticleList;
import fr.epita.sigl.miwa.bo.parser.DomParserReferential;
import fr.epita.sigl.miwa.bo.util.Convert;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;

public class AsyncMessageListener extends AAsyncMessageListener {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	@Override
	public void onException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String message, EApplication source) {
		LOGGER.severe(message);		
	}

	@Override
	public void onFile(File file, EApplication source) {
		LOGGER.severe(source + " : " + file.getAbsolutePath());	
		switch (source) {
		case MDM:
			System.out.println("***** Le fichier du MDM a été reçu");
			DomParserReferential parserReferential = new DomParserReferential();

			ArticleList articles = parserReferential.articleList(Convert.ReadFile(file));
			
			System.out.println("****** date = " + articles.date);
			System.out.println("****** nombre d'articles = " + articles.articles.size());
			System.out.println("****** Fin du parsing.");
			break;

		default:
			System.out.println("who are you ?");
			break;
		}
	}

}
