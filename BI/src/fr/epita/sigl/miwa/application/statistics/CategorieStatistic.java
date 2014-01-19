package fr.epita.sigl.miwa.application.statistics;

import java.util.Date;

public class CategorieStatistic {

	private String ref;
	
	private Integer achat;

	public CategorieStatistic(String ref, Integer achat) {
		this.ref = ref;
		this.achat = achat;
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
