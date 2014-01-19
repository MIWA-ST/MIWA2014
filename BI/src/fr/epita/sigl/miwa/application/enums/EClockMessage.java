package fr.epita.sigl.miwa.application.enums;

public enum EClockMessage {
	STOCK ("Stock"),
	VENTE("Ventes"),
	REP_PAYMENT("Répartition des moyens de paiement");

	private String name = "";

	private EClockMessage(String name) {
		this.name = name;
	}
	public String toString(){
		return name;
	}
}
