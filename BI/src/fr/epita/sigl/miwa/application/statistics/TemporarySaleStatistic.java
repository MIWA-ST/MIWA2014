package fr.epita.sigl.miwa.application.statistics;

public class TemporarySaleStatistic {

	private int ca;
	
	private int nbSoldProducts;

	public TemporarySaleStatistic(int ca, int nbSoldProducts) {
		super();
		this.ca = ca;
		this.nbSoldProducts = nbSoldProducts;
	}

	public int getCa() {
		return ca;
	}

	public void setCa(int ca) {
		this.ca = ca;
	}

	public int getNbSoldProducts() {
		return nbSoldProducts;
	}

	public void setNbSoldProducts(int nbSoldProducts) {
		this.nbSoldProducts = nbSoldProducts;
	}
	
	public void addCA(int ca){
		this.ca += ca;
	}
	
	public void addNbSoldProducts(int nbSoldProducts){
		this.nbSoldProducts += nbSoldProducts;
	}
}
