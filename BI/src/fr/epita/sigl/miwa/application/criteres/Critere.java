package fr.epita.sigl.miwa.application.criteres;

import fr.epita.sigl.miwa.application.enums.ECritereType;

public class Critere {

	private ECritereType type;

	private Object value;

	public Critere(ECritereType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public String getTypeString() {
		return type.toString();
	}
	
	public ECritereType getType(){
		return type;
	}

	public void setType(ECritereType type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}	
}
