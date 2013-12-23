package fr.epita.sigl.miwa.application.MDM;

import java.util.Date;

public class PromotionArticle {
	private Date debut;
	private Date fin;
	private Integer percent;
	
	
	public Date getDebut() {
		return debut;
	}
	public void setDebut(Date debut) {
		this.debut = debut;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public Integer getPercent() {
		return percent;
	}
	public void setPercent(Integer percent) {
		this.percent = percent;
	}
}
