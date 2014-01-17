package fr.epita.sigl.miwa.application.IN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VenteIN {
	private String numero_client;
	private Integer montant;
	private String moyen_paiement;
	private Date dateHeure;
	private List<ArticleIN> ventes = new ArrayList<ArticleIN>();
	
	public String getNumero_client() {
		return numero_client;
	}
	public void setNumero_client(String numero_client) {
		this.numero_client = numero_client;
	}
	public Integer getMontant() {
		return montant;
	}
	public void setMontant(Integer montant) {
		this.montant = montant;
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
	public List<ArticleIN> getVentes() {
		return ventes;
	}
	public void setVentes(List<ArticleIN> ventes) {
		this.ventes = ventes;
	}
}
