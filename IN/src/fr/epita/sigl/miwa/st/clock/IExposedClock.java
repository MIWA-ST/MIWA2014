package fr.epita.sigl.miwa.st.clock;

import java.util.Date;

public interface IExposedClock {
	public Date getHour();
	public void wakeMeUp(Date date, Object message);
	public void wakeMeUpEveryHours(Date nextOccurence, Object message);
	public void wakeMeUpEveryDays(Date nextOccurence, Object message);
	public void wakeMeUpEveryWeeks(Date nextOccurence, Object message);
}
