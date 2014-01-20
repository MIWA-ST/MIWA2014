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

public class CreationClientCR {
	private static final Logger LOGGER = Logger.getLogger(ParseXML.class.getName());
	private EnvoiEnteteCR entete;
	private String nom;
	private String prenom;
	private String adresse;
	private String code_postal;
	private String email;
	private String telephone;
	private String civilite;
	private String situation;
	private String naissance;
	private String iban;
	private String bic;
	private String nbenfant;
	
	public CreationClientCR(EnvoiEnteteCR entete, String nom, String prenom,
			String adresse, String code_postal, String email, String telephone,
			String civilite, String situation, String naissance, String iban,
			String bic, String nbenfant)
	{
		this.entete = entete;
		this.nom = nom;
		this.prenom = prenom;
		this.adresse = adresse;
		this.code_postal = code_postal;
		this.email = email;
		this.telephone = telephone;
		this.civilite = civilite;
		this.situation = situation;
		this.naissance = naissance;
		this.iban = iban;
		this.bic = bic;
		this.nbenfant = nbenfant;
	}
	
	public String sendXML()
	{
		StringBuilder result = new StringBuilder();
		
		LOGGER.info("***** Envoi d'un message à CRM : demande de création de " + nom + " " + prenom + ".");
		
		result.append("<XML>");
		result.append(entete.sendXML());
		result.append("<COMPTE civilite=\"" + civilite + "\" nom=\"" + nom + "\""
				+ " prenom=\"" + prenom + "\""
				+ " adresse=\"" + adresse + "\""
				+ " code_postal=\"" + code_postal + "\""
				+ " email=\"" + email + "\""
				+ " telephone=\"" + telephone + "\""
				+ " situation=\"" + situation + "\""
				+ " naissance=\"" + naissance + "\""
				+ " iban=\"" + iban + "\""
				+ " bic=\"" + bic + "\""
				+ " nbenfant=\"" + nbenfant + "\""
				+ "></COMPTE></XML>");
		
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
	
	public String getNom() {
		return nom;
	}

	public EnvoiEnteteCR getEntete() {
		return entete;
	}

	public void setEntete(EnvoiEnteteCR entete) {
		this.entete = entete;
	}

	public String getCivilite() {
		return civilite;
	}

	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getCode_postal() {
		return code_postal;
	}

	public void setCode_postal(String code_postal) {
		this.code_postal = code_postal;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public String getNaissance() {
		return naissance;
	}

	public void setNaissance(String naissance) {
		this.naissance = naissance;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getNbenfant() {
		return nbenfant;
	}

	public void setNbenfant(String nbenfant) {
		this.nbenfant = nbenfant;
	}
	
}
