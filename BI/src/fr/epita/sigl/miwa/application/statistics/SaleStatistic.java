package fr.epita.sigl.miwa.application.statistics;

import java.util.Date;

public class SaleStatistic {
	
	private Date dateTime;

	private String categorie;
	
	private float evolution;
	
	private float ca;
	
	private float caPourcent;
	
	private int nbSoldProducts;

	public SaleStatistic(Date dateTime, String categorie, float evolution,
			float ca, float caPourcent, int nbSoldProducts) {
		this.dateTime = dateTime;
		this.categorie = categorie;
		this.evolution = evolution;
		this.ca = ca;
		this.caPourcent = caPourcent;
		this.nbSoldProducts = nbSoldProducts;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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
