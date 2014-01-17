package fr.epita.sigl.miwa.st.async.file;

import java.io.Serializable;

import fr.epita.sigl.miwa.st.EApplication;
import fr.epita.sigl.miwa.st.async.message.JMSMessage;

public class AsyncFileMessage extends JMSMessage implements Serializable {
	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -4831135977878049700L;

	public AsyncFileMessage(EApplication source, EApplication destination,
			String filename) {
		super(source);
		this.destination = destination;
		this.text = filename;
	}
	
	public String getFilename() {
		return this.text;
	}
	
	public void setFilename(String filename) {
		this.text = filename;
	}
}
