package fr.epita.sigl.miwa.bo.parser;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class DomParserHelper
{
	 
	public static Document getDocumentFromXMLString(String xml)
	{
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
			return doc;
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getHeader(String xml)
	{		
		String[] xmlSplited = xml.split(">");

		return xmlSplited[0] + ">";
	}
	
	public static String getBody(String xml)
	{
		String[] xmlSplited = xml.split(">");
		
		String res = "";
		
		for (int i = 1; i < xmlSplited.length; i++)
		{
			res += xmlSplited[i] + ">";
		}

		return res;
	}
	
	public static String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            NamedNodeMap attributes = node.getAttributes();
	        	Node item = attributes.getNamedItem(attrName);
	        	if (item != null)
	        	{
	        		return item.getNodeValue(); 
	        	}
	        }
	    }
	 
	    return null;
	}
	
	public static String getNodeAttr(String tagName, String attrName, List<Node> nodes ) {
	    for ( int x = 0; x < nodes.size(); x++ ) {
	        Node node = nodes.get(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            NamedNodeMap attributes = node.getAttributes();
	        	Node item = attributes.getNamedItem(attrName);
	        	if (item != null)
	        	{
	        		return item.getNodeValue(); 
	        	}
	        }
	    }
	 
	    return null;
	}
	
	public static String getNodeAttr(String attrName, Node node) {
 
	    NamedNodeMap attributes = node.getAttributes();
		Node item = attributes.getNamedItem(attrName);

		if (item != null)
		{
			return item.getNodeValue(); 
		}
		
	    return null;
	}
	
	public static List<Node> getNodes(String tagName, NodeList nodes) {
		
		List<Node> res = new ArrayList<>();
		
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        for ( int y = 0; y < node.getChildNodes().getLength(); y++ ){
		        if (node.getChildNodes().item(y).getNodeName().equalsIgnoreCase(tagName)) {
		            res.add(node.getChildNodes().item(y));	
		        }
	        }
	    }
	 
	    return res;
	}
	
	public static List<Node> getNodes(String tagName, Node node) {
		
		List<Node> res = new ArrayList<>();
		
		for ( int y = 0; y < node.getChildNodes().getLength(); y++ ){
	        if (node.getChildNodes().item(y).getNodeName().equalsIgnoreCase(tagName)) {
	            res.add(node.getChildNodes().item(y));	
	        }
        }
	    
	    return res;
	}
	
	public static Node getNode(String tagName, Node node) {
		
		for ( int y = 0; y < node.getChildNodes().getLength(); y++ ){
	        if (node.getChildNodes().item(y).getNodeName().equalsIgnoreCase(tagName)) {
	            return node.getChildNodes().item(y);	
	        }
        }
	    
	    return null;
	}
}
