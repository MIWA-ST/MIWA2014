package fr.epita.sigl.miwa.application.MO;

public class PaiementFideliteMO {
	private Integer montant;
	private String numero_carte;
	
	public Integer getMontant() {
		return montant;
	}
	public void setMontant(Integer montant) {
		this.montant = montant;
	}
	public String getNumero_carte() {
		return numero_carte;
	}
	public void setNumero_carte(String numero_carte) {
		this.numero_carte = numero_carte;
	}
}
