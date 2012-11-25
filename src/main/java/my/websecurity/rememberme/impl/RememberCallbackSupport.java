package my.websecurity.rememberme.impl;

import my.websecurity.rememberme.RememberCallback;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;

/**
 * 自动登录拦截器的回调函数接口
 * @author xiegang
 * @since 2012-7-3
 *
 */
public class RememberCallbackSupport implements RememberCallback{
	public void onAutoLoginSuccess(SecurityServletContext context, SecurityDomainConfiguration config, UserDetails userDetails) {
		// do nothing
	}
}
