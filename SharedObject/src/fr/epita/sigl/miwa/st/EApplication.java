package fr.epita.sigl.miwa.st;

/**
 * Enumerate the different applications of the MIWA's virtual information system
 * 
 * @author francois
 * 
 */
public enum EApplication {
	BI("BI"), BACK_OFFICE("BO"), GESTION_COMMERCIALE("GC"), CAISSE("CA"), ENTREPOT("EN"), CRM("CR"), MDM("MD"), MONETIQUE("MO"), INTERNET("IN");
	private String _shortName;
	
	public String getShortName() {
		return _shortName;
	}
	
	public static EApplication getFromShortName(String shortName) {
		for (EApplication app : EApplication.values()) {
			if (app.getShortName().equals(shortName))
				return app;
		}
		return null;
	}
	
	private EApplication(String shortName) {
		_shortName = shortName;
	}
}
