package fr.epita.sigl.miwa.application;

import java.io.File;
import java.util.Date;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.clock.ClockClientToUse;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AAsyncMessageListener;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args) throws AsyncFileException, AsyncMessageException {
		AsyncMessageFactory.getInstance().getAsyncMessageManager().initListener(new AAsyncMessageListener() {
			
			@Override
			public void onException(Exception arg0) {
				
			}
			
			@Override
			public void onMessage(String message, EApplication source) {
				LOGGER.severe(message);
			}
			
			@Override
			public void onFile(File file, EApplication source) {
				LOGGER.severe(source + " : " + file.getAbsolutePath());
			}
		});
		Date hour = ClockClientToUse.getClock().getHour();
		System.out.println(hour.toString());
		hour.setHours(hour.getHours() + 10);
		ClockClientToUse.getClock().wakeMeUpEveryHours(hour, "COUCOU HOUR");
		
		AsyncMessageFactory.getInstance().getAsyncMessageManager().send("Salut BI", EApplication.BI);
		
		AsyncMessageFactory.getInstance().getAsyncMessageManager().stopListener();
	}

}
