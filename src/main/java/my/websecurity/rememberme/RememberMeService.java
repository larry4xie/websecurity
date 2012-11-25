package my.websecurity.rememberme;

import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;

/**
 * 自动登录services<br/>
 * 
 * @author xiegang
 * @since 2011-12-19
 *
 */
public abstract class RememberMeService {
	private String rememberMeKey = "WEBSECURITY_REMEMBER_ME";
	
	private int maxAge = 365 * 24 * 60 * 60; // 一年
	
	private RememberMeInfo rememberMeInfo;
	
	/**
	 * 保存RememberMe消息
	 * @param context
	 * @param userDetails
	 * @throws SecurityRuntimeException
	 */
	public void saveRememberMeInfo(SecurityServletContext context, UserDetails userDetails) throws SecurityRuntimeException {
		rememberMeInfo.saveRememberMeInfo(context, userDetails, rememberMeKey, maxAge);
	}
	
	/**
	 * 清楚RememberMe消息
	 * @param context
	 * @param userDetails
	 * @throws SecurityRuntimeException
	 */
	public void removeRememberMeInfo(SecurityServletContext context) throws SecurityRuntimeException {
		rememberMeInfo.removeRememberMeInfo(context, rememberMeKey);
	}
	
	/**
	 * 自动登录
	 * 
	 * @param context
	 * @return 1.成功：返回UserDetails<br/>
	 *         2.失败：返回null,可能是没有自动登录信息,也可能是自动登录失败和异常
	 * @throws SecurityRuntimeException
	 */
	public UserDetails rememberMe(SecurityServletContext context, SecurityDomainConfiguration configuration) throws SecurityRuntimeException {
		// 获取RememberMeInfo
		UserDetails userDetails = rememberMeInfo.loadRememberMeInfo(context, rememberMeKey, maxAge);
		
		// 获取RememberMe UserDetails信息
		if(userDetails != null) {
			userDetails = autoLogin(userDetails);
			// 设置UserDetails到全局访问点
			if(userDetails != null) {
				configuration.getUserDetailsHelper().saveUserDetails(context, userDetails, configuration);
			}
		}
		
		return userDetails;
	}
	
	/**
	 * 根据给定的simple(只有用户名\密码) userDetails进行自动登录，成功返回详细的userDetails，失败返回null
	 * 
	 * @param userDetails
	 * @return
	 */
	protected abstract UserDetails autoLogin(UserDetails userDetails);

	/**
	 * @return the rememberMeKey
	 */
	public String getRememberMeKey() {
		return rememberMeKey;
	}

	/**
	 * @param rememberMeCookieKey the rememberMeCookieKey to set
	 */
	public void setRememberMeKey(String rememberMeKey) {
		this.rememberMeKey = rememberMeKey;
	}

	/**
	 * @return the rememberMeInfo
	 */
	public RememberMeInfo getRememberMeInfo() {
		return rememberMeInfo;
	}

	/**
	 * @param rememberMeInfo the rememberMeInfo to set
	 */
	public void setRememberMeInfo(RememberMeInfo rememberMeInfo) {
		this.rememberMeInfo = rememberMeInfo;
	}

	/**
	 * @return the maxAge
	 */
	public int getMaxAge() {
		return maxAge;
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}
}
