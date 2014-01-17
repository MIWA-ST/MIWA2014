package fr.epita.sigl.miwa.st.clock;

public class ClockFactory {

	static public IExposedClock getServerClock() {
		return Clock.getInstance();
	}
}
