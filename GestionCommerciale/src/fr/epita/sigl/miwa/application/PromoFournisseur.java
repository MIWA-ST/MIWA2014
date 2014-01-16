package fr.epita.sigl.miwa.application;

public class PromoFournisseur {
	private String ref_article;
	private String DateDebut;
	private String DateFin;
	private String pourcentage;
	private String minquantite;
	
	public String getRef_article() {
		return ref_article;
	}
	public void setRef_article(String ref_article) {
		this.ref_article = ref_article;
	}
	public String getDateDebut() {
		return DateDebut;
	}
	public void setDateDebut(String dateDebut) {
		DateDebut = dateDebut;
	}
	public String getDateFin() {
		return DateFin;
	}
	public void setDateFin(String dateFin) {
		DateFin = dateFin;
	}
	public String getPourcentage() {
		return pourcentage;
	}
	public void setPourcentage(String pourcentage) {
		this.pourcentage = pourcentage;
	}
	public String getMinquantite() {
		return minquantite;
	}
	public void setMinquantite(String minquantite) {
		this.minquantite = minquantite;
	}
	
	
}
