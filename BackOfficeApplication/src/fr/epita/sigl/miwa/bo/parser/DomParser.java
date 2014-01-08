package fr.epita.sigl.miwa.bo.parser;

import org.w3c.dom.Document;

public class DomParser
{
	protected Document doc = null;
	protected String xml = null;
	

	DomParser()
	{
	}
	
	protected void updateDoc()
	{
		this.doc = DomParserHelper.getDocumentFromXMLString(this.xml);
		this.doc.getDocumentElement().normalize();
	}
	
	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
	public void setDoc(String xml) {
		this.doc = DomParserHelper.getDocumentFromXMLString(xml);
		this.doc.getDocumentElement().normalize();
	}
	
	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
}
