package fr.epita.sigl.miwa.application;

import java.util.Date;

public class PromotionForGC {
	private String id;
	private int nbMin;
	private float rebate;
	private Date startDate;
	private Date endDate;
	
	public PromotionForGC(String id, int nbMin, float pourcent, Date startDate, Date endDate) {
		this.id = id;
		this.nbMin = nbMin;
		this.rebate = pourcent;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNbMin() {
		return nbMin;
	}

	public void setNbMin(int nbMin) {
		this.nbMin = nbMin;
	}

	public float getRebate() {
		return rebate;
	}

	public void setRebate(float pourcent) {
		this.rebate = pourcent;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
