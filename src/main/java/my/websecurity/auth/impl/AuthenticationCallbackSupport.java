package my.websecurity.auth.impl;

import my.websecurity.auth.AuthenticationCallback;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;

/**
 * 认证拦截器的回调函数接口
 * 
 * @author xiegang
 * @since 2012-7-3
 *
 */
public class AuthenticationCallbackSupport implements AuthenticationCallback {
	/* (non-Javadoc)
	 * @see my.websecurity.auth.AuthenticationCallback#onNotPassAuthentication(my.websecurity.support.SecurityServletContext, my.websecurity.support.SecurityDomainConfiguration, my.websecurity.support.metadata.UserDetails)
	 */
	@Override
	public void onNotPassAuthentication(SecurityServletContext context, SecurityDomainConfiguration config, UserDetails userDetails) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see my.websecurity.auth.AuthenticationCallback#onNullUserDetails(my.websecurity.support.SecurityServletContext, my.websecurity.support.SecurityDomainConfiguration)
	 */
	@Override
	public void onNullUserDetails(SecurityServletContext context, SecurityDomainConfiguration config) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see my.websecurity.auth.AuthenticationCallback#onPassAuthentication(my.websecurity.support.SecurityServletContext, my.websecurity.support.SecurityDomainConfiguration, my.websecurity.support.metadata.UserDetails)
	 */
	@Override
	public void onPassAuthentication(SecurityServletContext context, SecurityDomainConfiguration config, UserDetails userDetails) {
		// do nothing
	}
}
