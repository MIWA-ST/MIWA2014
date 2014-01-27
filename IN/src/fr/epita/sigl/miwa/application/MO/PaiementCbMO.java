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

public class PaiementCbMO {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	private Float montant;
	private String numero;
	private String date_validite;
	private String pictogramme;
	
	public PaiementCbMO(Float montant, String numero, String date_validite, String pictogramme)
	{
		this.numero = numero;
		this.date_validite = date_validite;
		this.montant = montant;
		this.pictogramme = pictogramme;
	}
	
	public PaiementCbMO(String montant, String numero, String date_validite, String pictogramme)
	{
		this.numero = numero;
		this.date_validite = date_validite;
		this.pictogramme = pictogramme;
		if (montant != null && !montant.equals(""))
			this.montant = Float.parseFloat(montant);
		else
			this.montant = 0.0f;
	}

	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		LOGGER.info("***** Envoi d'un message à MO : demande de paiement par CB : montant : " + this.montant + "€ CB numero : " + this.numero);
		
		result.append("<monetique service=\"paiement_cb\">");
		result.append("<montant>" + this.montant + "</montant><cb>");
		result.append("<numero>" + this.numero + "</numero>");
		result.append("<date_validite>" + this.date_validite + "</date_validite>");
		result.append("<pictogramme>" + this.pictogramme + "</pictogramme>");
		result.append("</cb></monetique>");
		
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDate_validite() {
		return date_validite;
	}

	public void setDate_validite(String date_validite) {
		this.date_validite = date_validite;
	}

	public String getPictogramme() {
		return pictogramme;
	}

	public void setPictogramme(String pictogramme) {
		this.pictogramme = pictogramme;
	}
}
