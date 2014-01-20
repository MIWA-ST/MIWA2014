package fr.epita.sigl.miwa.application.MO;

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

public class PaiementCfMO {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	private Float montant;
	private String matricule;
	
	public PaiementCfMO(Float montant, String matricule)
	{
		this.matricule = matricule;
		this.montant = montant;
	}
	
	public PaiementCfMO(String montant, String matricule)
	{
		this.matricule = matricule;
		if (montant != null && !montant.equals(""))
			this.montant = Float.parseFloat(montant);
		else
			this.montant = 0.0f;
	}

	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		LOGGER.info("***** Envoi d'un message à MO : demande de paiement par CF - " + this.montant + " € pour le client " + this.matricule + ".");
		
		result.append("<monetique service=\"paiement_cf\">");
		result.append("<montant>" + this.montant + "</montant>");
		result.append("<matricule_client>" + this.matricule + "</matricule_client></monetique>");
		
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
	
	public Float getMontant() {
		return montant;
	}

	public void setMontant(Float montant) {
		this.montant = montant;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
}
