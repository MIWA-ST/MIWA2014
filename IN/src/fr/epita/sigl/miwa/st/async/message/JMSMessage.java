package fr.epita.sigl.miwa.st.async.message;

import java.io.Serializable;

import fr.epita.sigl.miwa.st.EApplication;

public class JMSMessage implements Serializable {
	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -7138653656733783535L;
	protected EApplication source = null;
	protected EApplication destination = null;
	protected String text = null;

	public JMSMessage(EApplication source) {
		this.source = source;
	}

	public EApplication getSource() {
		return source;
	}

	public void setSource(EApplication source) {
		this.source = source;
	}

	public EApplication getDestination() {
		return destination;
	}

	public void setDestination(EApplication destination) {
		this.destination = destination;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
