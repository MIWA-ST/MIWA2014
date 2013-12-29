/**
 * 
 */
package fr.epita.sigl.miwa.application;

import java.io.*;

import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.Locale;

/**
 * @author Romain
 *
 */
public class JDOM 
{
	static org.jdom2.Document document;
	static Element racine;

	public static void browseXML(String filename)
	{
	   SAXBuilder sxb = new SAXBuilder();
	   try
	   {
	      document = sxb.build(new File(filename));
	   }
	   catch(Exception e){
		   System.out.println("Impossible de trouver : " + filename);
	   }
 
	   racine = document.getRootElement();

	   getData();
	}
	
	
	static void getData()
	{
	    String tmp = racine.getAttributeValue("objet");
	    if (tmp.equals("segmentation-client"))
	    {
	    	Element groupes = racine.getChild("GROUPES");

			List listGroupes = groupes.getChildren("GROUPE");
			
		    Iterator i = listGroupes.iterator();
		    System.out.println("XML BI!");
	 	    while(i.hasNext())
		    {
	 	    	Element groupeCourant = (Element)i.next();
	 	    	Element criteres = groupeCourant.getChild("CRITERES");
	 	    	List listCriteres = criteres.getChildren("CRITERE");
	 	    	Iterator j = listCriteres.iterator();
		 	    while(j.hasNext())
			    {
		 	    	Element critereCourant = (Element)j.next();
		 	    	System.out.println(critereCourant.getAttributeValue("type"));
			    }
		 	    Element clients = groupeCourant.getChild("CLIENTS");
		 	    List listClients = clients.getChildren("CLIENT");
	 	    	Iterator k = listClients.iterator();
		 	    while(k.hasNext())
			    {
		 	    	Element clientCourant = (Element)k.next();
		 	    	System.out.println(clientCourant.getAttributeValue("numero"));
		 	    	
		 	    	Element articles = clientCourant.getChild("CATEGORIEARTICLES");
		 	    	List listArticles = articles.getChildren("CATEGORIE");
		 	    	Iterator l = listArticles.iterator();
			 	    while(l.hasNext())
				    {
			 	    	Element articleCourant = (Element)l.next();
			 	    	System.out.println(articleCourant.getAttributeValue("ref"));
				    }
			    }
		      
		    }
	    }
	    if (tmp.equals("ticket-client-fidelise"))
	    {
	    	Element clients = racine.getChild("TICKETVENTE");

			List listClient = clients.getChildren("ARTICLE");
			
		    Iterator i = listClient.iterator();
		    System.out.println("XML BO!");
	 	    while(i.hasNext())
		    {
		       Element courant = (Element)i.next();
		       System.out.println(courant.getAttributeValue("refarticle"));
		      
		    }
	    }
	    //TODO
	}
	

	public static void createXML(String type)
	{
		if (type.equals("monetique"))
		{
			Element entete = new Element("ENTETE");
			String date = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE).format(new Date());
			
			Attribute objet = new Attribute("objet","information-client");
			entete.setAttribute(objet);
			Attribute source = new Attribute("source","crm");
			entete.setAttribute(source);
			Attribute dateAttribute = new Attribute("date", date);
			entete.setAttribute(dateAttribute);
			org.jdom2.Document doc = new Document(entete);

			Element info = new Element("INFORMATIONS");
			entete.addContent(info);
			
			
			Element client = new Element("CLIENT");
			info.addContent(client);
			
			Attribute id = new Attribute("id","01");
			client.setAttribute(id);
			Attribute typecarte = new Attribute("typecarte","01");
			client.setAttribute(typecarte);
			Attribute coord = new Attribute("coord","0130506070");
			client.setAttribute(coord);

			affiche(doc);
			enregistre("monetique.xml", doc);
		}
		else if (type.equals("bi"))
		{
			Element entete = new Element("ENTETE");
			String date = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE).format(new Date());
			
			Attribute objet = new Attribute("objet","information-client");
			entete.setAttribute(objet);
			Attribute source = new Attribute("source","crm");
			entete.setAttribute(source);
			Attribute dateAttribute = new Attribute("date", date);
			entete.setAttribute(dateAttribute);
			org.jdom2.Document doc = new Document(entete);

			Element info = new Element("CLIENTS");
			entete.addContent(info);
			
			
			Element client = new Element("CLIENT");
			info.addContent(client);
			
			Attribute id = new Attribute("id","01");
			client.setAttribute(id);
			Attribute civilite = new Attribute("civilite","M");
			client.setAttribute(civilite);
			Attribute naissance = new Attribute("naissance","AAAAA-MM-JJ");
			client.setAttribute(naissance);
			Attribute codepostal = new Attribute("codepostal","75000");
			client.setAttribute(codepostal);
			Attribute situationfam = new Attribute("situationfam","Marie");
			client.setAttribute(situationfam);
			Attribute nbenfant = new Attribute("nbenfant","3");
			client.setAttribute(nbenfant);
			Attribute typecarte = new Attribute("typecarte","Super+");
			client.setAttribute(typecarte);

			affiche(doc);
			enregistre("bi.xml", doc);
		}
		
	}
	
	static void affiche(org.jdom2.Document doc)
	{
	   try
	   {
	      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	      sortie.output(doc, System.out);
	   }
	   catch (java.io.IOException e){}
	}

	static void enregistre(String fichier, org.jdom2.Document doc)
	{
	   try
	   {
	      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	      sortie.output(doc, new FileOutputStream(fichier));
	   }
	   catch (java.io.IOException e){}
	}
}
