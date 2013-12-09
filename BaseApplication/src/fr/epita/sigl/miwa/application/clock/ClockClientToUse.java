package fr.epita.sigl.miwa.application.clock;

import java.util.Date;

import fr.epita.sigl.miwa.st.ClockClient;

public class ClockClientToUse {

	static public Date getHour() {
		return ClockClient.getInstance().getHour();
	}
	
	static public void wakeUp(Date date, Object message) {
		if (message instanceof String) {
			if (message.equals("Hello World!")) {
				System.out.println(date.toString() + " : Hello dear client!");
			} else {
				System.out.println(date.toString() + " : " + message);
			}
		}
	}
	
}
