package fr.epita.sigl.miwa.application;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;

import fr.epita.sigl.miwa.application.GC.DemandeNiveauStock;

// Parser des fichiers XML
public class ParseXML {
	private static Document document;
	private static Element root;
	private String filename;
	
	public ParseXML(String filename)
	{
		this.filename = filename;
	}
	
	@SuppressWarnings("deprecation")
	public void readXML()
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		File file = new File(filename);
		
		try {
			document = saxBuilder.build(file);
		} catch (Exception e) {
			System.out.println("Fichier introuvable : " + filename);
		}
		
		// Parse de la GC
		root = document.getRootElement();
		if (root.getName().equals("DEMANDENIVEAUDESTOCKINTERNET"))
		{
			DemandeNiveauStock demandeNiveauStock = new DemandeNiveauStock();
			
			demandeNiveauStock.setNumero(root.getChildText("NUMERO"));
			demandeNiveauStock.setDate(root.getChildText("DATE"));
			
			demandeNiveauStock.print();
		}
	}
}
