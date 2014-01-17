package fr.epita.sigl.miwa.application.enums;

public enum EClockMessage {
	STOCK ("Stock"),
	EVOL_VENTE("Evolution des ventes"),
	REP_VENTE("Répartition des ventes"),
	REP_PAYMENT("Répartition des moyens de paiement");

	private String name = "";

	private EClockMessage(String name) {
		this.name = name;
	}
	public String toString(){
		return name;
	}
}
