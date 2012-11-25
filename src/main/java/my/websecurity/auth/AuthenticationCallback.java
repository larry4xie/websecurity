package my.websecurity.auth;

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
public interface AuthenticationCallback {
	/**
	 * 没有获取到UserDetails
	 * @param context
	 * @param config
	 */
	public void onNullUserDetails(SecurityServletContext context, SecurityDomainConfiguration config);
	
	/**
	 * 通过验证
	 * @param context
	 * @param config
	 * @param userDetails
	 */
	public void onPassAuthentication(SecurityServletContext context, SecurityDomainConfiguration config, UserDetails userDetails);
	
	/**
	 * 没有通过验证
	 * @param context
	 * @param config
	 * @param userDetails
	 */
	public void onNotPassAuthentication(SecurityServletContext context, SecurityDomainConfiguration config, UserDetails userDetails);
}
