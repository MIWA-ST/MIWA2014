package fr.epita.sigl.miwa.st.async.file;

import fr.epita.sigl.miwa.st.EApplication;

public interface AsyncFileManager {

	public void retrieve(String filename, EApplication current) throws AsyncFileException;
	public void send(String filename, EApplication destination) throws AsyncFileException;
}
