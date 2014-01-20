package fr.epita.sigl.miwa.application.CR;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PromotionArticleCR {
	// reference article
	private String article;
	private Date fin;
	private Integer reduc;
	
	public PromotionArticleCR(String article, String fin, String reduc)
	{
		this.article = article;
		if (reduc != null && !reduc.equals(""))
			this.reduc = Integer.parseInt(reduc);
		else
			this.reduc = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.fin = df.parse(fin);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setArticle(String article) {
		this.article = article;
	}
	public String getArticle()
	{
		return this.article;
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
	
	public void stReduc(String reduc) {
		if (reduc != null && !reduc.equals(""))
			this.reduc = Integer.parseInt(reduc);
		else
			this.reduc = 0;
	}
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		if (article != null && !article.equals(""))
			result.append("***** 		Article : " + this.article + "\n");
		else
			result.append("***** 		Article : NULL\n");
		
		if (fin != null)
			result.append("***** 		Fin : " + this.fin + "\n");
		else
			result.append("***** 		Fin : NULL\n");
		
		if (reduc != null)
			result.append("***** 		Reduc : " + this.reduc + "\n");
		else
			result.append("***** 		Reduc : NULL\n");
		
		return result.toString();
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
