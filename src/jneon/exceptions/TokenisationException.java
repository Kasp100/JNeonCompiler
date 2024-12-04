package jneon.exceptions;

public class TokenisationException extends CompileTimeException {

	private static final long serialVersionUID = 6293640068011068801L;

	public TokenisationException() {
		super();
	}

	public TokenisationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenisationException(String message) {
		super(message);
	}

	public TokenisationException(Throwable cause) {
		super(cause);
	}

}
