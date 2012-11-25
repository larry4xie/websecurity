package my.websecurity.exception;

/**
 * 安全异常
 * 
 * @author xiegang
 * @since 2011-12-07
 *
 */
public class SecurityRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -728545268493086807L;

	public SecurityRuntimeException() {
		super();
	}

	public SecurityRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityRuntimeException(String message) {
		super(message);
	}

	public SecurityRuntimeException(Throwable cause) {
		super(cause);
	}
}
