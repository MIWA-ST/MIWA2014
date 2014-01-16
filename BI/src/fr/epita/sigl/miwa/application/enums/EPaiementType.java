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
}
