package fr.epita.sigl.miwa.st;

/**
 * Enumerate the different applications of the MIWA's virtual information system
 * 
 * @author francois
 * 
 */
public enum EApplication {
	BI("BI", "BI"), BACK_OFFICE("BO", "Back office"), GESTION_COMMERCIALE("GC",
			"Gestion commerciale"), CAISSE("CA", "Caisse"), ENTREPOT("EN",
			"Entrepot"), CRM("CR", "CRM"), MDM("MD", "MDM"), MONETIQUE("MO",
			"Monétique"), INTERNET("IN", "Internet"), DEBUG("DB", "Debug");
	private String _shortName;
	private String _longName;

	public String getShortName() {
		return _shortName;
	}

	public String getLongName() {
		return _longName;
	}

	public static EApplication getFromShortName(String shortName) {
		for (EApplication app : EApplication.values()) {
			if (app.getShortName().equals(shortName))
				return app;
		}
		return null;
	}

	private EApplication(String shortName, String longName) {
		_shortName = shortName;
		_longName = longName;
	}

	@Override
	public String toString() {
		return _longName;
	}
}
