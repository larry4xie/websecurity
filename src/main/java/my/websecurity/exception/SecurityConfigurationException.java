package my.websecurity.exception;

/**
 * 安全框架读取配置文件异常
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public class SecurityConfigurationException extends SecurityRuntimeException {
	private static final long serialVersionUID = -1736875389798893438L;

	public SecurityConfigurationException() {
		super();
	}

	public SecurityConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityConfigurationException(String message) {
		super(message);
	}

	public SecurityConfigurationException(Throwable cause) {
		super(cause);
	}
}
