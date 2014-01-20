package fr.epita.sigl.miwa.application;

import fr.epita.sigl.miwa.application.BDD.JdbcConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fr.epita.sigl.miwa.application.messaging.AsyncMessageListener;
import fr.epita.sigl.miwa.st.Conf;
import fr.epita.sigl.miwa.st.async.file.exception.AsyncFileException;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.sync.SyncMessFactory;

public class Main {

	public static void main(String[] args) throws AsyncFileException,
			AsyncMessageException {
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		Conf.getInstance();
		SyncMessFactory.initSyncMessReceiver();	
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.initListener(new AsyncMessageListener());
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING ABOVE */
		/* CODE HERE */
		//try {
		//	new BufferedReader(new InputStreamReader(System.in)).readLine();
		//} catch (IOException e1) {
		//	e1.printStackTrace();
		//}
		JdbcConnection.getInstance().getConnection();
		
		try
		{
			Thread.sleep(86400000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		JdbcConnection.getInstance().closeConnection();
		/* !CODE HERE */
		/* ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
		AsyncMessageFactory.getInstance().getAsyncMessageManager()
				.stopListener();
		/* !ST DO NOT REMOVE/MODIFY OR PUT ANYTHING BELOW */
	}

}
