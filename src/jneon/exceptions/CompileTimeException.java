package jneon.exceptions;

public class CompileTimeException extends Exception {

	private static final long serialVersionUID = -2020350219772795066L;

	public CompileTimeException() {
		super();
	}

	public CompileTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompileTimeException(String message) {
		super(message);
	}

	public CompileTimeException(Throwable cause) {
		super(cause);
	}

}
