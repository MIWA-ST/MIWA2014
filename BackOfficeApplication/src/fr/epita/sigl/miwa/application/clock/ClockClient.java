package fr.epita.sigl.miwa.application.clock;

import java.util.Date;

import fr.epita.sigl.miwa.bo.util.MiscFunction;
import fr.epita.sigl.miwa.bo.xmlconstructor.StoreManagementXMLConstructor;
import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.AsyncMessageFactory;
import fr.epita.sigl.miwa.st.async.message.exception.AsyncMessageException;
import fr.epita.sigl.miwa.st.clock.ClockFactory;
import fr.epita.sigl.miwa.st.clock.IExposedClock;

public class ClockClient {

	/*
	 * R�cup�re l'horloge serveur pour faire des requ�tes dessus (getHour, wakeMeUp, ...)
	 */
	static public IExposedClock getClock() {
		return ClockFactory.getServerClock();
	}
	
	/*
	 * Vous ne devez faire aucun appel � cette fonction, seulement remplir le code
	 * Elle est automatiquement appel�e lorsque l'horloge vous contacte
	 */
	@Deprecated
	static public void wakeUp(Date date, Object message) {

		if (message instanceof String) {

			if (message.equals("heure actuelle")) {
				System.out.println("***** Date est heure actuelle : " + date.toString());
			}
			else if (message.equals("fermeture")) {
				
				// BO => GC demande de réassort
				StoreManagementXMLConstructor storeManagementXMLConstructor = new StoreManagementXMLConstructor();
				try {
					AsyncMessageFactory.getInstance().getAsyncMessageManager().
					send(storeManagementXMLConstructor.restockRequest(MiscFunction.getArticlesForRestockRequest()), EApplication.GESTION_COMMERCIALE);
				} catch (AsyncMessageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("***** Demande de réassort envoyée à la GC");
				
			} else {
				System.out.println(date.toString() + " : " + message);
			}
		}
	}	
}
