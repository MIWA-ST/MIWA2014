package fr.epita.sigl.miwa.application.enums;

public enum EPaiementType {
	CB ("Carte bancaire"),
	CF ("Carte fidélité"),
	CQ ("Chèque"),
	ES ("Espèces");

	private String name = "";

	EPaiementType(String name){
		this.name = name;
	}

	public String toString(){
		return name;
	}
	
	public static EPaiementType fromString(String name){
		if (name != null){
			for (EPaiementType p : EPaiementType.values()){
				if (name.equalsIgnoreCase(p.name)){
					return p;
				}
			}
		}
		return null;
	}
}
