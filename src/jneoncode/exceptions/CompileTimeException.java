package jneoncode.exceptions;

public class CompileTimeException extends RuntimeException {

	private static final long serialVersionUID = 9208389327810656687L;

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
