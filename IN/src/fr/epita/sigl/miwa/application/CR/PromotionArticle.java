package fr.epita.sigl.miwa.application.CR;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PromotionArticle {
	// reference article
	private String article;
	private Date fin;
	private Integer reduc;
	
	public PromotionArticle(String article, String fin, Integer reduc)
	{
		this.article = article;
		this.reduc = reduc;
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			this.fin = df.parse(fin);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	public void print()
	{
		if (this.article != null)
			System.out.println("		Article : " + this.article);
		else
			System.out.println("		Article : NULL");
		
		if (this.fin != null)
			System.out.println("		Fin : " + this.fin);
		else
			System.out.println("		Fin : NULL");
		
		if (this.reduc != null)
			System.out.println("		Reduc : " + this.reduc);
		else
			System.out.println("		Reduc : NULL");
	}
}
