/**
 * 
 */
package fr.epita.sigl.miwa.application;

import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.filter.*;
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
	      //Le parsing est terminé ;)
	      document = sxb.build(new File(filename));
	   }
	   catch(Exception e){}
 
	   //On initialise un nouvel élément racine avec l'élément racine du document.
	   racine = document.getRootElement();

	   //Méthode définie dans la partie 3.2. de cet article
	   displayALL();
	}
	
	
	static void displayALL()
	{
	   //On crée une List contenant tous les noeuds "etudiant" de l'Element racine
	   List listEtudiants = racine.getChildren("etudiant");

	   //On crée un Iterator sur notre liste
	   Iterator i = listEtudiants.iterator();
	   while(i.hasNext())
	   {
	      //On recrée l'Element courant à chaque tour de boucle afin de
	      //pouvoir utiliser les méthodes propres aux Element comme :
	      //sélectionner un nœud fils, modifier du texte, etc...
	      Element courant = (Element)i.next();
	      //On affiche le nom de l’élément courant
	      System.out.println(courant.getChild("nom").getText());
	   }
	}
}
