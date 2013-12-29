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
	   //On crée une instance de SAXBuilder
	   SAXBuilder sxb = new SAXBuilder();
	   try
	   {
	      //On crée un nouveau document JDOM avec en argument le fichier XML
	      document = sxb.build(new File(filename));
	   }
	   catch(Exception e){
		   System.out.println("Impossible de trouver : " + filename);
	   }
 
	   //On initialise un nouvel élément racine avec l'élément racine du document.
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
	    	Element clients = racine.getChild("ticketvente");

			List listClient = clients.getChildren("article");
			
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
			Element entete = new Element("entete");
			String date = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE).format(new Date());
			
			Attribute objet = new Attribute("objet","information-client");
			entete.setAttribute(objet);
			Attribute source = new Attribute("source","crm");
			entete.setAttribute(source);
			Attribute dateAttribute = new Attribute("date", date);
			entete.setAttribute(dateAttribute);
			org.jdom2.Document doc = new Document(entete);

			Element info = new Element("informations");
			entete.addContent(info);
			
			
			Element client = new Element("client");
			info.addContent(client);
			
			Attribute id = new Attribute("id","01");
			client.setAttribute(id);
			Attribute typecarte = new Attribute("typecarte","01");
			client.setAttribute(typecarte);
			Attribute coord = new Attribute("coord","0130506070");
			client.setAttribute(coord);

			//Les deux méthodes qui suivent seront définies plus loin dans l'article
			affiche(doc);
			enregistre("Exercice1.xml", doc);
		}
	}
	
	static void affiche(org.jdom2.Document doc)
	{
	   try
	   {
	      //On utilise ici un affichage classique avec getPrettyFormat()
	      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	      sortie.output(doc, System.out);
	   }
	   catch (java.io.IOException e){}
	}

	static void enregistre(String fichier, org.jdom2.Document doc)
	{
	   try
	   {
	      //On utilise ici un affichage classique avec getPrettyFormat()
	      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	      //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
	      //avec en argument le nom du fichier pour effectuer la sérialisation.
	      sortie.output(doc, new FileOutputStream(fichier));
	   }
	   catch (java.io.IOException e){}
	}
}
