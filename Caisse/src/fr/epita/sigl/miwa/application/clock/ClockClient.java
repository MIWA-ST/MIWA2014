package fr.epita.sigl.miwa.application.clock;

import java.util.Date;

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
			if (message.equals("Ouverture")) {
				System.out.println(date.toString() + " : Hello dear client!");
			} else if (message.equals("Ouverture")){
				System.out.println(date.toString() + " : Bye dear client!");
			}
			else
			{
				System.out.println(date.toString() + " : " + message);
			}
		}
	}	
}
