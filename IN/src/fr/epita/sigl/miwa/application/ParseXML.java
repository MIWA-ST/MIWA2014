package fr.epita.sigl.miwa.application;
import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;

// Parser des fichiers XML
public class ParseXML {
	public void readXML(String filename)
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		File file = new File(filename);
		
		try {
			Document document = saxBuilder.build(file);
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
