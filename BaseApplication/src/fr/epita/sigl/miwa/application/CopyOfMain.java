package fr.epita.sigl.miwa.application;

import java.util.logging.Logger;

import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.AsyncFileFactory;
import fr.epita.sigl.miwa.st.async.file.IAsyncFileManager;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class CopyOfMain {
	private static final Logger LOGGER = Logger.getLogger(CopyOfMain.class.getName());
	
	public static void main(String[] args) throws AsyncFileException, AsyncMessageException {
		IAsyncFileManager fileManager = AsyncFileFactory.getInstance().getFileManager();
		fileManager.send("toto.txt", EApplication.BACK_OFFICE);
		
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send("Coucou !", EApplication.BACK_OFFICE);
	}

}
