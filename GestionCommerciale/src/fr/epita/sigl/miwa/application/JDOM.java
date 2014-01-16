package fr.epita.sigl.miwa.application;

import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import java.util.List;
import java.util.Iterator;

//////////YOUUUUUUUUUUUU

public class JDOM {
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
		Element clients = racine.getChild("clients");

		List listClient = clients.getChildren("client");
		
	    Iterator i = listClient.iterator();
	   
	    String tmp = racine.getAttributeValue("objet");
	    if (tmp.equals("information-client"))
	    {
		    System.out.println("XML BI!");
	 	    while(i.hasNext())
		    {
		       Element courant = (Element)i.next();
		       System.out.println(courant.getAttributeValue("id"));
		      
		    }
	    }
	}
}
