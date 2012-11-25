package my.websecurity.rememberme;

import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;

/**
 * 自动登录信息转换接口
 * 
 * @author xiegang
 * @since 2011-12-19
 *
 */
public interface RememberMeInfo {
	/**
	 * 从request中获取RememberMe的信息，封装到UserDetails，主要有用户名密码不包括权限，是不完整的UserDetails
	 * 
	 * @param context
	 * @param rememberMeKey 自动登录信息key
	 * @param maxAge 信息保存时间(单位：秒)
	 * @return
	 * @throws SecurityRuntimeException
	 */
	public UserDetails loadRememberMeInfo(SecurityServletContext context, String rememberMeKey, int maxAge) throws SecurityRuntimeException;
	
	/**
	 * 将UserDetails中的信息RememberMe
	 * 
	 * @param context
	 * @param userDetails
	 * @param rememberMeKey 自动登录信息key
	 * @param maxAge 信息保存时间(单位：秒)
	 * @throws SecurityRuntimeException
	 */
	public void saveRememberMeInfo(SecurityServletContext context, UserDetails userDetails, String rememberMeKey, int maxAge) throws SecurityRuntimeException;
	
	/**
	 * 移除RememberMe消息
	 * @param context
	 * @param rememberMeKey
	 * @throws SecurityRuntimeException
	 */
	public void removeRememberMeInfo(SecurityServletContext context, String rememberMeKey) throws SecurityRuntimeException;
}
