package fr.epita.sigl.miwa.application.statistics;

import java.util.Date;

public class CategorieStatistic {

	private Date dateTime;
	
	private String ref;
	
	private Integer achat;

	public CategorieStatistic(Date dateTime, String ref, Integer achat) {
		this.dateTime = dateTime;
		this.ref = ref;
		this.achat = achat;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Integer getAchat() {
		return achat;
	}

	public void setAchat(Integer achat) {
		this.achat = achat;
	}

}
