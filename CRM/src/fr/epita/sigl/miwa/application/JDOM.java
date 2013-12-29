/**
 * 
 */
package fr.epita.sigl.miwa.application;

import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import java.util.List;
import java.util.Iterator;

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

	   displayALL();
	}
	
	
	static void displayALL()
	{	   
	    String tmp = racine.getAttributeValue("objet");
	    if (tmp.equals("information-client"))
	    {
	    	Element clients = racine.getChild("clients");

			List listClient = clients.getChildren("client");
			
		    Iterator i = listClient.iterator();
		    System.out.println("XML BI!");
	 	    while(i.hasNext())
		    {
		       Element courant = (Element)i.next();
		       System.out.println(courant.getAttributeValue("id"));
		      
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
	}
}
