package my.websecurity.rememberme;

import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;

/**
 * 自动登录拦截器的回调函数接口
 * @author xiegang
 * @since 2012-7-3
 *
 */
public interface RememberCallback {
	/**
	 * 自动登录成功
	 * @param context
	 * @param config
	 * @param userDetails
	 */
	public void onAutoLoginSuccess(SecurityServletContext context, SecurityDomainConfiguration config, UserDetails userDetails);
}
