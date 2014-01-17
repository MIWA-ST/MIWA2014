package fr.epita.sigl.miwa.application.crm;

import java.util.List;

public class ReassortBO
{
	private String civilite;
	private String naissance;
	private String codepostal;
	private String situationfam;
	private int nbenfant;
	private String typecarte;
	
	public String getCivilite() {
		return civilite;
	}
	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}
	public String getNaissance() {
		return naissance;
	}
	public void setCodepostal(String codepostal) {
		this.codepostal = codepostal;
	}
	public String getCodepostal() {
		return codepostal;
	}
	public void setSituationfam(String situationfam) {
		this.situationfam = situationfam;
	}
	public String getSituationfam() {
		return situationfam;
	}
	public void setNbenfant(int nbenfant) {
		this.nbenfant = nbenfant;
	}
	public int getNbenfant() {
		return nbenfant;
	}
	public String getTypecarte() {
		return typecarte;
	}
	public void setBackOfficeAddress(String typecarte) {
		this.typecarte = typecarte;
	}
}
