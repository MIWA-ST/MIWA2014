package fr.epita.sigl.miwa.st.async.file;

public class AsyncFileFactory {

	public IAsyncFileManager getManager() {
		return new FTPManager();
	}
}
