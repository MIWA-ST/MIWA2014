package fr.epita.sigl.miwa.application.BI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.w3c.dom.Document;

import fr.epita.sigl.miwa.application.ParseXML;

public class EnvoiVenteDetailleeBI {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	private EnteteBI entete;
	private String lieu = "internet";
	private List<VenteBI> ventes = new ArrayList<VenteBI>();
	
	public EnvoiVenteDetailleeBI()
	{
		
	}
	
	public EnvoiVenteDetailleeBI(EnteteBI entete, List<VenteBI> ventes)
	{
		this.entete = entete;
		this.ventes = ventes;
	}
	
	public EnvoiVenteDetailleeBI(EnteteBI entete, List<VenteBI> ventes, String lieu)
	{
		this.entete = entete;
		this.ventes = ventes;
		this.lieu = lieu;
	}
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		LOGGER.info("***** Envoi d'un message à BI : envoi des ventes détaillées Internet");
		
		result.append("<XML>");
		
		result.append(entete.sendXML());
		
		result.append("<VENTES-DETAILLEES lieu=\"" + lieu + "\">");
		
		for (VenteBI v : ventes)
			result.append(v.sendXML());
		
		result.append("</VENTES-DETAILLEES>");
		
		result.append("</XML>");
		
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
			LOGGER.info("***** Erreur lors de la création du flux XML : " + e.getMessage());
		}
		return null;
	}
	
	public EnteteBI getEntete() {
		return entete;
	}
	public void setEntete(EnteteBI entete) {
		this.entete = entete;
	}
}