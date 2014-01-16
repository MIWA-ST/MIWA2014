package fr.epita.sigl.miwa.application.statistics;

public class SaleStatistic {

	private int categorie;
	
	private float evolution;
	
	private boolean hausse;
	
	private int ca;
	
	private float caPourcent;

	public SaleStatistic(int categorie, float evolution, boolean hausse, int ca,
			float caPourcent) {
		this.categorie = categorie;
		this.evolution = evolution;
		this.hausse = hausse;
		this.ca = ca;
		this.caPourcent = caPourcent;
	}

	public int getCategorie() {
		return categorie;
	}

	public void setCategorie(int categorie) {
		this.categorie = categorie;
	}

	public float getEvolution() {
		return evolution;
	}

	public void setEvolution(float evolution) {
		this.evolution = evolution;
	}

	public boolean isHausse() {
		return hausse;
	}

	public void setHausse(boolean hausse) {
		this.hausse = hausse;
	}

	public int getCa() {
		return ca;
	}

	public void setCa(int ca) {
		this.ca = ca;
	}

	public float getCaPourcent() {
		return caPourcent;
	}

	public void setCaPourcent(float caPourcent) {
		this.caPourcent = caPourcent;
	}
	
}
