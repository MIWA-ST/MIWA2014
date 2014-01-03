package fr.epita.sigl.miwa.application.MO;

public class PaiementCBMO {
	private Integer montant;
	private String numero_cb;
	private String numero_cb_validite;
	private String cb_pictogramme;
	
	public Integer getMontant() {
		return montant;
	}
	public void setMontant(Integer montant) {
		this.montant = montant;
	}
	public String getNumero_cb() {
		return numero_cb;
	}
	public void setNumero_cb(String numero_cb) {
		this.numero_cb = numero_cb;
	}
	public String getNumero_cb_validite() {
		return numero_cb_validite;
	}
	public void setNumero_cb_validite(String numero_cb_validite) {
		this.numero_cb_validite = numero_cb_validite;
	}
	public String getCb_pictogramme() {
		return cb_pictogramme;
	}
	public void setCb_pictogramme(String cb_pictogramme) {
		this.cb_pictogramme = cb_pictogramme;
	}
}
