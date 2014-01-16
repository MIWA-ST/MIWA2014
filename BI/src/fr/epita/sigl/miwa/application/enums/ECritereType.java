package fr.epita.sigl.miwa.application.enums;

public enum ECritereType {
	AGE ("age"),
	GEO ("geographie"),
	SEXE ("sexe"),
	SF ("situation-familiale"),
	ENF ("enfant"),
	FID ("fidelite");

	private String name = "";

	ECritereType(String name){
		this.name = name;
	}
	public String toString(){
		return name;
	}
}
