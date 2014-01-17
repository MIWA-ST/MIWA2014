package fr.epita.sigl.miwa.application.MDM;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductsClientEnteteMDM {
	private String objet;
	private String source;
	private Date date;
	
	public ProductsClientEnteteMDM()
	{
	}
	
	public ProductsClientEnteteMDM(String objet, String source, String date)
	{
		this.objet = objet;
		this.source = source;
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
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
