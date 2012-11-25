package my.websecurity.rememberme.impl;

import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.exception.SecuritySuspendException;
import my.websecurity.interceptor.Interceptor;
import my.websecurity.interceptor.InterceptorChain;
import my.websecurity.rememberme.RememberCallback;
import my.websecurity.rememberme.RememberMeService;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 自动登录拦截器<br/>
 * 
 * @author xiegang
 * @since 2011-12-19
 */
public class RememberMeInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(RememberMeInterceptor.class);
	
	private int order;
	
	/**
	 * 回调
	 */
	private RememberCallback callback;
	
	private RememberMeService rememberMeService;
	
	@Override
	public void doFilter(SecurityServletContext context, UserDetails userDetails, SecurityDomainConfiguration configuration, InterceptorChain interceptorChain) throws SecuritySuspendException, SecurityRuntimeException {
		if(null == userDetails){
			try {
				userDetails = rememberMeService.rememberMe(context, configuration);
				if(null != userDetails) {
					if(null != callback) { // callback
						callback.onAutoLoginSuccess(context, configuration, userDetails);
					}
				}
			} catch (Exception e) {
				// 不继续上抛
				logger.warn("rememberMe exception", e);
			}
		}
		
		interceptorChain.doFilter(context, userDetails, configuration);
	}

	@Override
	public void setOrder(int order) {
		this.order = order;
	}


	@Override
	public int getOrder() {
		return order;
	}

	/* (non-Javadoc)
	 * @see my.websecurity.interceptor.Interceptor#check()
	 */
	@Override
	public boolean check() {
		return rememberMeService != null && rememberMeService.getRememberMeInfo() != null;
	}

	/**
	 * @return the callback
	 */
	public RememberCallback getCallback() {
		return callback;
	}

	/**
	 * @param callback the callback to set
	 */
	public void setCallback(RememberCallback callback) {
		this.callback = callback;
	}

	/**
	 * @return the rememberMeService
	 */
	public RememberMeService getRememberMeService() {
		return rememberMeService;
	}

	/**
	 * @param rememberMeService the rememberMeService to set
	 */
	public void setRememberMeService(RememberMeService rememberMeService) {
		this.rememberMeService = rememberMeService;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
