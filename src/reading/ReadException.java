package reading;

public class ReadException extends Exception {

	private static final long serialVersionUID = -1705839570627952821L;

	public ReadException() {
		super();
	}

	public ReadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReadException(String message) {
		super(message);
	}

	public ReadException(Throwable cause) {
		super(cause);
	}

}
