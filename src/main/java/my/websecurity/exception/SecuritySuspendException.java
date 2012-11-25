package my.websecurity.exception;

/**
 * 安全框架终止异常<br/>
 * SecurityFilterChain 在捕获到这个异常时不会执行后续的domain和Filter
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public class SecuritySuspendException extends SecurityRuntimeException {
	private static final long serialVersionUID = -1736875389798893438L;

	public SecuritySuspendException() {
		super();
	}

	public SecuritySuspendException(String message) {
		super(message);
	}
}
