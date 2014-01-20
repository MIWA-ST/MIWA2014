package fr.epita.sigl.miwa.application.CR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.w3c.dom.Document;

import fr.epita.sigl.miwa.application.ParseXML;
import fr.epita.sigl.miwa.application.clock.ClockClient;

public class EnvoiMatriculeCR {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	private EnvoiEnteteCR entete = new EnvoiEnteteCR("connection_client", "Internet", ClockClient.getClock().getHour());
	private String matricule;
	
	public EnvoiMatriculeCR(String matricule)
	{
		this.matricule = matricule;
	}
	
	public EnvoiEnteteCR getEntete() {
		return entete;
	}

	public void setEntete(EnvoiEnteteCR entete) {
		this.entete = entete;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		LOGGER.info("***** Envoi d'un message à CRM : envoi du matricule : " + matricule);
		
		result.append("<XML>");
		result.append(entete.sendXML());
		result.append("<COMPTE matricule=\"" + matricule + "\"></COMPTE></XML>");
		
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
}
