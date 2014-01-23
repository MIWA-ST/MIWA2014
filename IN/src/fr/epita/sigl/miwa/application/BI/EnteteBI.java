package fr.epita.sigl.miwa.application.BI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import fr.epita.sigl.miwa.application.ParseXML;

public class EnteteBI {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	private String objet;
	private String source;
	private Date date;
	
	public EnteteBI()
	{
	}
	
	public EnteteBI(String objet, String source, String date)
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
	
	public EnteteBI(String objet, String source, Date date)
	{
		this.objet = objet;
		this.source = source;
		this.date = date;
	}
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		SimpleDateFormat df;
		if (objet != null && objet.equals("ventes 15min"))
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else
			df = new SimpleDateFormat("yyyy-MM-dd");
		
		result.append("<ENTETE objet=\"" + objet + "\" source=\"" + source + "\" date=\"" + df.format(date) + "\" />");
		
		return result.toString();
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
}
