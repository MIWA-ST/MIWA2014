package fr.epita.sigl.miwa.application.MDM;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PromotionArticleMDM {
	private Date debut;
	private Date fin;
	private Integer percent;
	
	public PromotionArticleMDM(String debut, String fin, Integer percent)
	{
		this.percent = percent;
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.debut = df1.parse(debut);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.fin = df2.parse(fin);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("***** 		debut : ");
		if (this.debut != null)
			result.append(this.debut + "\n");
		else
			result.append("NULL\n");
		
		result.append("***** 		fin : ");
		if (this.fin != null)
			result.append(this.fin + "\n");
		else
			result.append("NULL\n");
		
		result.append("***** 		percent : ");
		if (this.percent != null)
			result.append(this.percent + "\n");
		else
			result.append("NULL\n");
		
		return result.toString();
	}
	
	public void print()
	{
		if (this.debut != null)
			System.out.println("		debut : " + this.debut);
		else
			System.out.println("		debut : NULL");
		
		if (this.fin != null)
			System.out.println("		fin : " + this.fin);
		else
			System.out.println("		fin : NULL");
		
		if (this.percent != null)
			System.out.println("		percent : " + this.percent);
		else
			System.out.println("		percent : NULL");
	}
}
