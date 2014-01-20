package fr.epita.sigl.miwa.application;

import java.util.Date;

public class Promotion {
	private Date date_debut;
	private Date date_fin;
	private int pourcentage;
	private int quantityMin;

	public int getQuantityMin() {
		return quantityMin;
	}
	public void setQuantityMin(int quantityMin) {
		this.quantityMin = quantityMin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	private int id;
	
	public Date getDate_debut() {
		return date_debut;
	}
	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}
	public Date getDate_fin() {
		return date_fin;
	}
	public void setDate_fin(Date date_fin) {
		this.date_fin = date_fin;
	}
	public int getPourcentage() {
		return pourcentage;
	}
	public void setPourcentage(int pourcentage) {
		this.pourcentage = pourcentage;
	}
	
	public Promotion(Date startDate, Date endDate, int rebate) {
		this.date_debut = startDate;
		this.date_fin = endDate;
		this.pourcentage = rebate;
	}
	
	public Promotion(Date startDate, Date endDate, int rebate, int quantityMin, int id) {
		this.date_debut = startDate;
		this.date_fin = endDate;
		this.pourcentage = rebate;
		this.quantityMin = quantityMin;
		this.id = id;
	}

}

