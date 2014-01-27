package fr.epita.sigl.miwa.application.BI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.w3c.dom.Document;

import fr.epita.sigl.miwa.application.MiwaBDDIn;
import fr.epita.sigl.miwa.application.ParseXML;
import fr.epita.sigl.miwa.application.GC.EnvoiCommandeGC;
import fr.epita.sigl.miwa.application.MO.PaiementCbMO;
import fr.epita.sigl.miwa.application.MO.PaiementCfMO;
import fr.epita.sigl.miwa.application.clock.ClockClient;
import fr.epita.sigl.miwa.application.messaging.SyncMessHandler;
import fr.epita.sigl.miwa.st.EApplication;

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
	
	public void generateVentes()
	{
		MiwaBDDIn bdd = MiwaBDDIn.getInstance();
		
		ResultSet rs = bdd.executeStatement_result("Select * from client;");
		try {
			while (rs != null && rs.next())
			{
				// numero_client, montant, moyen_paiement, dateHeure, articles
				VenteBI v = new VenteBI();
				
				v.setNumero_client(rs.getString("matricule"));
				v.setNom(rs.getString("nom"));
				v.setPrenom(rs.getString("prenom"));
				v.setAdresse(rs.getString("adresse"));
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				v.setDateHeure(df.format(ClockClient.getClock().getHour()));
				
				ResultSet rs2 = bdd.executeStatement_result("SELECT * FROM article WHERE stock > 0;");
				if (!rs2.next())
					break;
				
				Float montant = 0.f;
				
				while (rs2 != null && rs2.next())
				{
					String ref = rs2.getString("reference");
					String cat = rs2.getString("categorie");
					
					v.getArticles().add(new ArticleVenteBI(ref, 1, cat));
					
					ResultSet rs3 = bdd.executeStatement_result("SELECT * FROM promotion WHERE reference_article='"+ref+"';");
					if (rs3 != null && rs3.next())
						montant = montant + (rs2.getFloat("prix_vente") * (rs3.getInt("percent") / 100));
					else					
						montant = montant + rs2.getFloat("prix_vente");
				}
				
				v.setMontant(montant);
				Random r = new Random();
				Integer rand = r.nextInt(2);
				if (rand == 0)
					v.setMoyen_paiement("CB");
				else
					v.setMoyen_paiement("CF");
				
				if (!v.getArticles().isEmpty())
				{
					if (v.getMoyen_paiement().equals("CB"))
					{
						PaiementCbMO paiementCB = new PaiementCbMO(v.getMontant(), "1234569856985214", "1116", "586");
						Boolean retr = SyncMessHandler.getSyncMessSender().sendXML(EApplication.MONETIQUE, paiementCB.sendXMLDocument());
						if (retr)
						{
							v.updatesArticles();
							v.addBDD();
							v.envoiCommandeGC();
						}
					}
					else if (v.getMoyen_paiement().equals("CF"))
					{
						PaiementCfMO paiementCF = new PaiementCfMO(v.getMontant(), v.getNumero_client());
						Boolean result = SyncMessHandler.getSyncMessSender().sendXML(EApplication.MONETIQUE, paiementCF.sendXMLDocument());
						if (result)
						{
							v.updatesArticles();
							v.addBDD();
							v.envoiCommandeGC();
						}
					}					
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String createFileXML()
	{
		String path = "C:\\Users\\Yacine\\Desktop\\miwa_env\\local_repo\\IN\\Envoi_ventesdétaillées_IN_to_BI.xml";
		File file = new File(path);
		
		try {
			file.createNewFile();
			
			FileWriter fw = new FileWriter(file);
			BufferedWriter buf = new BufferedWriter(fw);
			buf.write(sendXML());
			buf.flush();
			buf.close();
			return "Envoi_ventesdétaillées_IN_to_BI.xml";
		} catch (IOException e) {
			LOGGER.info("***** Erreur à la création du fichier : " + e.getMessage());
		}
		
		return null;
	}
	
	public Boolean getBDD()
	{
		MiwaBDDIn bdd = MiwaBDDIn.getInstance();
		ResultSet rs = bdd.executeStatement_result("SELECT * FROM vente;");
		
		try {
			while (rs.next())
			{
				VenteBI v = new VenteBI();
				
				v.setDateHeure(rs.getString("dateHeure"));
				v.setMontant(rs.getFloat("montant"));
				v.setMoyen_paiement(rs.getString("moyen_paiement"));
				v.setNumero_client(rs.getString("numero_client"));
				v.getArticlesBDD(rs.getString("articles"));
				
				ventes.add(v);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
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
	
	public String getLieu() {
		return lieu;
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

	public List<VenteBI> getVentes() {
		return ventes;
	}

	public void setVentes(List<VenteBI> ventes) {
		this.ventes = ventes;
	}

	public EnteteBI getEntete() {
		return entete;
	}
	public void setEntete(EnteteBI entete) {
		this.entete = entete;
	}
}
