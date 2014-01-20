package fr.epita.sigl.miwa.application.CR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.sound.sampled.spi.FormatConversionProvider;

import org.jdom2.JDOMException;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.w3c.dom.Document;

import fr.epita.sigl.miwa.application.ParseXML;
import fr.epita.sigl.miwa.application.clock.ClockClient;

public class EnvoiEnteteCR {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	private String objet;
	private String source;
	private Date date;
	
	public EnvoiEnteteCR(String objet, String source, Date date)
	{
		this.objet = objet;
		this.source = source;
		this.date = date;
	}
	
	public EnvoiEnteteCR(String objet, String source, String date)
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
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		String datFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
		
		result.append("<ENTETE objet=\"" + objet + "\" source=\"" + source + "\" date=\"" + datFormat + "\" />");
		
		return result.toString();
	}
	
	public Document sendXMLDocument()
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		
		File file = new File("temp.xml");
		
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(file));
			out.write(this.sendXML());
			out.close();
			
			org.jdom2.Document documentJdom = saxBuilder.build(file);
			DOMOutputter outputDocument = new DOMOutputter();
			
			return outputDocument.output(documentJdom);
		} catch (IOException | JDOMException e) {
			LOGGER.info("*****Erreur lors de la création du flux XML : " + e.getMessage());
		}
		return null;
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
