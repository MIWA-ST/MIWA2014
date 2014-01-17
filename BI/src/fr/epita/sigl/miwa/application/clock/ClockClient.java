package fr.epita.sigl.miwa.application.clock;

import java.util.Date;

import fr.epita.sigl.miwa.application.controller.BIController;
import fr.epita.sigl.miwa.application.enums.EClockMessage;
import fr.epita.sigl.miwa.st.clock.ClockFactory;
import fr.epita.sigl.miwa.st.clock.IExposedClock;

public class ClockClient {

	private static BIController controller = BIController.getInstance();
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
		if (message instanceof EClockMessage) {
			EClockMessage clockMessage = (EClockMessage) message;
			switch (clockMessage) {
			case STOCK:
				controller.generateStockStatistic();
				break;

			default:
				break;
			}
		}
	}	
}
