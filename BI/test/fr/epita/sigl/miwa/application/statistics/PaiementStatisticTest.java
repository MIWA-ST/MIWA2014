package fr.epita.sigl.miwa.application.statistics;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.epita.sigl.miwa.application.enums.EPaiementType;

public class PaiementStatisticTest {

	private PaiementStatistic paiementStatistic;
	
	@Test
	public void cbToStringTest() {
		paiementStatistic = new PaiementStatistic(EPaiementType.CB, 10000, 10);
		String ref = "Carte bancaire : 10000 € (10 %)";		
		assertTrue(ref.equals(paiementStatistic.toString()));
	}
	
	@Test
	public void cfToStringTest() {
		paiementStatistic = new PaiementStatistic(EPaiementType.CF, 10000, 10);
		String ref = "Carte fidélité : 10000 € (10 %)";		
		assertTrue(ref.equals(paiementStatistic.toString()));
	}
	
	@Test
	public void cqToStringTest() {
		paiementStatistic = new PaiementStatistic(EPaiementType.CQ, 10000, 10);
		String ref = "Chèque : 10000 € (10 %)";		
		assertTrue(ref.equals(paiementStatistic.toString()));
	}
	
	@Test
	public void esToStringTest() {
		paiementStatistic = new PaiementStatistic(EPaiementType.ES, 10000, 10);
		String ref = "Espèces : 10000 € (10 %)";		
		assertTrue(ref.equals(paiementStatistic.toString()));
	}
}
