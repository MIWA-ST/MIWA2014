package fr.epita.sigl.miwa.st.sync;

import org.w3c.dom.Document;

import fr.epita.sigl.miwa.st.EApplication;

public interface ISyncMessSender {
	public boolean sendMessage(EApplication to, String message);
	public String requestMessage(EApplication to, String request);
	public boolean sendXML (EApplication to, Document xml);
	public Document requestXML (EApplication to, String request);
}
