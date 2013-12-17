package fr.epita.sigl.miwa.st.async.message.exception;

public class AsyncMessageException extends Exception {

	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = -7455545428902387344L;

	public AsyncMessageException() {
	}

	public AsyncMessageException(String message) {
		super(message);
	}

	public AsyncMessageException(Throwable cause) {
		super(cause);
	}

	public AsyncMessageException(String message, Throwable cause) {
		super(message, cause);
	}

}
