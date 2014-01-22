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
	private String date;
	
	public EnteteBI()
	{
	}
	
	public EnteteBI(String objet, String source, String date)
	{
		this.objet = objet;
		this.source = source;
		this.date = date;
	}
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("<ENTETE objet=\"" + objet + "\" source=\"" + source + "\" date=\"" + date + "\" />");
		
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
