package fr.epita.sigl.miwa.application.statistics;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.epita.sigl.miwa.application.enums.EPaiementType;

public class PaiementStatisticTest {

	private PaymentStatistic paiementStatistic;
	
	@Test
	public void cbToStringTest() {
		paiementStatistic = new PaymentStatistic(EPaiementType.CB, 10000, 10);
		String ref = "Carte bancaire : 10000 € (10.0 %)";		
		assertEquals(ref, paiementStatistic.toString());
	}
	
	@Test
	public void cfToStringTest() {
		paiementStatistic = new PaymentStatistic(EPaiementType.CF, 10000, 10);
		String ref = "Carte fidélité : 10000 € (10.0 %)";		
		assertEquals(ref, paiementStatistic.toString());
	}
	
	@Test
	public void cqToStringTest() {
		paiementStatistic = new PaymentStatistic(EPaiementType.CQ, 10000, 10);
		String ref = "Chèque : 10000 € (10.0 %)";		
		assertEquals(ref, paiementStatistic.toString());
	}
	
	@Test
	public void esToStringTest() {
		paiementStatistic = new PaymentStatistic(EPaiementType.ES, 10000, 10);
		String ref = "Espèces : 10000 € (10.0 %)";
		assertEquals(ref, paiementStatistic.toString());
	}
}
