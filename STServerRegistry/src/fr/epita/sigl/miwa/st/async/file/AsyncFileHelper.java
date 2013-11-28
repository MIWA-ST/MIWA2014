package fr.epita.sigl.miwa.st.async.file;

import fr.epita.sigl.miwa.st.EApplication;

public interface AsyncFileHelper {

	public void retrieve(String filename) throws AsyncFileException;
	public void send(String filename, EApplication destination) throws AsyncFileException;
}
