package fr.epita.sigl.miwa.application.enums;

public enum EClockMessage {
	STOCK ("Stock"),
	VENTE("Ventes"),
	REP_PAYMENT("Répartition_des_moyens_de_paiement");

	private String name = "";

	private EClockMessage(String name) {
		this.name = name;
	}
	public String toString(){
		return name;
	}
	
	public static EClockMessage fromString(String name){
		if (name != null){
			for (EClockMessage c : EClockMessage.values()){
				if (name.equalsIgnoreCase(c.name)){
					return c;
				}
			}
		}
		return null;
	}
}
