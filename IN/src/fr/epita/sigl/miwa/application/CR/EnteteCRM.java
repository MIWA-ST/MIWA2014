package fr.epita.sigl.miwa.application.CR;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnteteCRM {
	private String objet;
	private String source;
	private Date date;
	
	public EnteteCRM()
	{
	}
	
	public EnteteCRM(String objet, String source, String date)
	{
		this.objet = objet;
		this.source = source;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.date = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getObjet() {
		return objet;
	}
	
	public void setObjet(String objet) {
		this.objet = objet;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String print_logger()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("***** 	Entete : [\n");
		result.append("***** 		objet : ");
		if (this.objet != null && !this.objet.equals(""))
			result.append(this.objet + "\n");
		else
			result.append("***** NULL\n");
		
		result.append("***** 		source : ");
		if (this.source != null && !this.source.equals(""))
			result.append(this.source + "\n");
		else
			result.append("***** NULL\n");
		
		result.append("***** 		date : ");
		if (this.date != null)
			result.append(this.date + "\n");
		else
			result.append("***** NULL\n");
		result.append("***** ]\n");
		
		return result.toString();
	}
	
	public void print()
	{
		System.out.println("	Entete : [");
		if (this.objet != null)
			System.out.println("	objet : " + this.objet);
		else
			System.out.println("	objet : NULL");
		
		if (this.source != null)
			System.out.println("	source : " + this.source);
		else
			System.out.println("	source : NULL");
		
		if (this.date != null)
			System.out.println("	date : " + this.date);
		else
			System.out.println("	date : NULL");
		System.out.println("		]");
	}
}
