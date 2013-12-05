package fr.epita.sigl.miwa.st.async.file;

/**
 * Exception du gestionnaire de fichiers asynchrones
 * 
 * @author francois
 *
 */
public class AsyncFileException extends Exception {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8213806152123060594L;

	public AsyncFileException() {
		super();
	}

	public AsyncFileException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AsyncFileException(String arg0) {
		super(arg0);
	}

	public AsyncFileException(Throwable arg0) {
		super(arg0);
	}
}
