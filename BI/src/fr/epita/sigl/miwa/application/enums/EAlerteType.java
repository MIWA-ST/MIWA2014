package fr.epita.sigl.miwa.application.enums;

public enum EAlerteType {
	AP ("ALERTE - Plein avec réassort"),
	AV ("ALERTE - Vide avec réassort"),
	AC ("ALERTE CRITIQUE - Vide sans réassort");

	private String name = "";
	private EAlerteType(String name) {
		this.name = name;
	}

	public String toString(){
		return name;
	}
}
