package fr.epita.sigl.miwa.application.CR;

import java.util.Date;

public class PromotionArticle {
	// reference article
	private String article;
	private Date fin;
	private Integer reduc;
	
	
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public Integer getReduc() {
		return reduc;
	}
	public void setReduc(Integer reduc) {
		this.reduc = reduc;
	}
}
