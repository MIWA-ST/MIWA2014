package fr.epita.sigl.miwa.application.statistics;

public class TemporarySaleStatistic {

	private float ca;

	private int nbSoldProducts;

	private String source;
	
	public TemporarySaleStatistic() {
	}
	public TemporarySaleStatistic(float ca, int nbSoldProducts, String source) {
		this.ca = ca;
		this.nbSoldProducts = nbSoldProducts;
		this.source = source;
	}


	public float getCa() {
		return ca;
	}


	public void setCa(float ca) {
		this.ca = ca;
	}


	public int getNbSoldProducts() {
		return nbSoldProducts;
	}


	public void setNbSoldProducts(int nbSoldProducts) {
		this.nbSoldProducts = nbSoldProducts;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}

	public void addCA(float ca){
		this.ca += ca;
	}

	public void addNbSoldProducts(int nbSoldProducts){
		this.nbSoldProducts += nbSoldProducts;
	}
}
