package fr.epita.sigl.miwa.application.statistics;

public class SaleStatistic {

	private String categorie;
	
	private float evolution;
	
	private float ca;
	
	private float caPourcent;
	
	private int nbSoldProducts;

	public SaleStatistic(String categorie, float evolution, float ca,
			float caPourcent, int nbSoldProducts) {
		this.categorie = categorie;
		this.evolution = evolution;
		this.ca = ca;
		this.caPourcent = caPourcent;
		this.nbSoldProducts = nbSoldProducts;
	}
	
	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public float getEvolution() {
		return evolution;
	}

	public void setEvolution(float evolution) {
		this.evolution = evolution;
	}

	public float getCa() {
		return ca;
	}

	public void setCa(float ca) {
		this.ca = ca;
	}

	public float getCaPourcent() {
		return caPourcent;
	}

	public void setCaPourcent(float caPourcent) {
		this.caPourcent = caPourcent;
	}

	public int getNbSoldProducts() {
		return nbSoldProducts;
	}

	public void setNbSoldProducts(int nbSoldProducts) {
		this.nbSoldProducts = nbSoldProducts;
	}
	
}
