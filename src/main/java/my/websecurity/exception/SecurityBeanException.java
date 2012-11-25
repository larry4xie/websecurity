package my.websecurity.exception;

/**
 * 安全框架bean操作异常
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public class SecurityBeanException extends SecurityRuntimeException {
	private static final long serialVersionUID = -1736875389798893438L;

	public SecurityBeanException() {
		super();
	}

	public SecurityBeanException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityBeanException(String message) {
		super(message);
	}

	public SecurityBeanException(Throwable cause) {
		super(cause);
	}
}
