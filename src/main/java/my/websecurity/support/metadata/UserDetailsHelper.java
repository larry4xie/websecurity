package my.websecurity.support.metadata;

import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;

/**
 * 用户详细信息服务
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public interface UserDetailsHelper {
	/**
	 * 获取当前请求下用户信息信息
	 * 
	 * @param context
	 * @param configuration
	 * @return 不存在返回nul
	 * @throws SecurityRuntimeException 发生异常
	 */
	public UserDetails loadUserDetails(SecurityServletContext context, SecurityDomainConfiguration configuration) throws SecurityRuntimeException;
	
	/**
	 * 设置当前请求下用户信息信息
	 * 
	 * @param context
	 * @param userDetails
	 * @param configuration
	 * @throws SecurityRuntimeException 发生异常
	 */
	public void saveUserDetails(SecurityServletContext context, UserDetails userDetails,SecurityDomainConfiguration configuration) throws SecurityRuntimeException;
	
	/**
	 * 删除当前请求下用户信息信息
	 * 
	 * @param context
	 * @param userDetails
	 * @param configuration
	 * @throws SecurityRuntimeException 发生异常
	 */
	public void removeUserDetails(SecurityServletContext context, SecurityDomainConfiguration configuration) throws SecurityRuntimeException;
}
