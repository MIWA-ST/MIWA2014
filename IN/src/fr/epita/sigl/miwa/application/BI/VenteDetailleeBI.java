package fr.epita.sigl.miwa.application.BI;

import java.util.Date;

public class VenteDetailleeBI {
	private  String numero_client;
	private Integer montant_total;
	private String moyen_paiement;
	private Date dateHeure;
	// Reference de l'article
	private String ref;
	// qtite de l'article
	private Integer qt;
	
	
	public String getNumero_client() {
		return numero_client;
	}
	public void setNumero_client(String numero_client) {
		this.numero_client = numero_client;
	}
	public Integer getMontant_total() {
		return montant_total;
	}
	public void setMontant_total(Integer montant_total) {
		this.montant_total = montant_total;
	}
	public String getMoyen_paiement() {
		return moyen_paiement;
	}
	public void setMoyen_paiement(String moyen_paiement) {
		this.moyen_paiement = moyen_paiement;
	}
	public Date getDateHeure() {
		return dateHeure;
	}
	public void setDateHeure(Date dateHeure) {
		this.dateHeure = dateHeure;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public Integer getQt() {
		return qt;
	}
	public void setQt(Integer qt) {
		this.qt = qt;
	}
}
