package fr.epita.sigl.miwa.bo.xmlconstructor;

import java.util.List;

import fr.epita.sigl.miwa.bo.object.NodeAttribute;

public class XMLConstructor
{
	protected String xml = "";
	private int indentSize = 3;
	
	protected void clear()
	{
		this.xml = "";
	}
	
	private void indent(int indentLevel)
	{
		for (int i = 0; i < indentLevel; i++)
		{
			for (int y = 0; y < indentSize; y++)
			{
				this.xml += " ";
			}
		}
	};
	
	protected void openClosedNode(String nodeName, List<NodeAttribute> attributes, int indentLevel)
	{
		this.indent(indentLevel);
		this.xml += "<" + nodeName;
		
		if (attributes == null || attributes.size() == 0)
		{
			this.xml += " ";
		}
		else
		{
			for (NodeAttribute attribute : attributes)
			{
				this.xml += " " + attribute.key + "=\"" + attribute.value + "\""; 
			}
		}
		this.xml += "/>" + "\n";
	}
	
	protected void openNode(String nodeName, List<NodeAttribute> attributes, int indentLevel)
	{
		this.indent(indentLevel);
		this.xml += "<" + nodeName;
		
		if (attributes != null)
		{
			for (NodeAttribute attribute : attributes)
			{
				this.xml += " " + attribute.key + "=\"" + attribute.value + "\""; 
			}
		}
		
		this.xml += ">" + "\n";
	}
	
	protected void closeNode(String nodeName, int indentLevel)
	{
		this.indent(indentLevel);
		this.xml += "</" + nodeName + ">" + "\n";
	}
	
	protected void printText(String text)
	{
		this.xml += text;
	}
}
