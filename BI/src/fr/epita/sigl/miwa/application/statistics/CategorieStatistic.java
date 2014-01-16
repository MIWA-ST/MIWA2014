package fr.epita.sigl.miwa.application.statistics;

public class CategorieStatistic {

	private Integer ref;
	
	private Integer achat;

	public CategorieStatistic(Integer ref, Integer achat) {
		this.ref = ref;
		this.achat = achat;
	}

	public Integer getRef() {
		return ref;
	}

	public void setRef(Integer ref) {
		this.ref = ref;
	}

	public Integer getAchat() {
		return achat;
	}

	public void setAchat(Integer achat) {
		this.achat = achat;
	}
}
