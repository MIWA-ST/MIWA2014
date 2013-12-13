package fr.epita.sigl.miwa.st.async.file;

import java.io.Serializable;

import fr.epita.sigl.miwa.st.EApplication;

public class AsyncFileMessage implements Serializable {
	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -4831135977878049700L;
	private EApplication destination = null;
	private EApplication source = null;
	private String filename = null;

	public AsyncFileMessage(EApplication source, EApplication destination,
			String filename) {
		super();
		this.source = source;
		this.destination = destination;
		this.filename = filename;
	}

	public EApplication getDestination() {
		return destination;
	}

	public void setDestination(EApplication destination) {
		this.destination = destination;
	}

	public EApplication getSource() {
		return source;
	}

	public void setSource(EApplication source) {
		this.source = source;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
