package my.websecurity.auth.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.websecurity.auth.AbstractAuthenticationInterceptor;
import my.websecurity.auth.AuthenticationCallback;
import my.websecurity.auth.MetadataSource.Metadata;
import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.exception.SecuritySuspendException;
import my.websecurity.interceptor.InterceptorChain;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;
import my.websecurity.util.UrlUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * java servlet filter的实现<br/>
 * SecurityMetadataSource和accessDecisionManager必须支持string类型的资源(Url)
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public class AuthenticationInterceptor extends AbstractAuthenticationInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);
	
	private static final String TO_URL = "toUrl";
	public static final String INVALID_SESSION_URL = "invalid-session-url"; // 没有登录或者session失效的转向页面
	public static final int INVALID_SESSION_SC = HttpServletResponse.SC_UNAUTHORIZED; // 没有登录或者session失效的response status 编码
	
	public static final String ACCESS_DENIED_PAGE = "access-denied-page"; // 没有权限的转向页面
	public static final int ACCESS_DENIED_SC = HttpServletResponse.SC_FORBIDDEN; // 没有权限的response status 编码
	
	/**
	 * 回调
	 */
	private AuthenticationCallback callback;
	
	@Override
	public void doFilter(SecurityServletContext context, UserDetails userDetails, SecurityDomainConfiguration configuration, InterceptorChain interceptorChain) throws SecuritySuspendException, SecurityRuntimeException {
		HttpServletRequest req = context.getRequest();
		HttpServletResponse rep = context.getResponse();
		try {
			if(null == userDetails){
				// 还没有登录或者session失效
				// 获取没有登录的转发页面
				String invalidSessionUrl = configuration.getSpecialPages().get(INVALID_SESSION_URL);
				if(null != invalidSessionUrl && invalidSessionUrl.length() > 0) {
					StringBuffer toUrl = new StringBuffer(invalidSessionUrl)
							.append('?').append(TO_URL).append('=')
							.append(context.getFullRequestUrl());
					UrlUtils.forward2Page(req, rep, toUrl.toString(), INVALID_SESSION_SC);
				} else {
					rep.sendError(INVALID_SESSION_SC, "request requires HTTP authentication");
				}
				
				// callback
				if(callback != null) {
					callback.onNullUserDetails(context, configuration);
				}
				throw new SecuritySuspendException("还没有登录");
			}
			// get url
			String url = context.getRequestUrl();
			
			if(metadataSource.supports(url.getClass()) && accessDecisionManager.supports(url.getClass())) {
				List<Metadata> metadatas = metadataSource.getMetadatas(url);
				if(null == metadatas || metadatas.size() < 1 || accessDecisionManager.decide(userDetails, url, metadatas)) {
					// 不需要权限或者权限验证通过
					interceptorChain.doFilter(context, userDetails, configuration);
					// callback
					if(callback != null) {
						callback.onPassAuthentication(context, configuration, userDetails);
					}
				} else {
					// 权限验证不通过
					String accessDeniedPage = configuration.getSpecialPages().get(ACCESS_DENIED_PAGE);
					if (null != accessDeniedPage && accessDeniedPage.length() > 0) {
						StringBuffer toUrl = new StringBuffer(accessDeniedPage)
								.append('?').append(TO_URL).append('=')
								.append(context.getFullRequestUrl());
						UrlUtils.forward2Page(req, rep, toUrl.toString(), ACCESS_DENIED_SC);
					} else {
						rep.sendError(ACCESS_DENIED_SC, "server understood the request but refused to fulfill it");
					}
					
					// callback
					if(callback != null) {
						callback.onNotPassAuthentication(context, configuration, userDetails);
					}
					throw new SecuritySuspendException("权限验证不通过");
				}
			} else {
				logger.error("不支持的权限资源类型,请查看配置" + url.getClass());
				throw new SecurityRuntimeException("不支持的权限资源类型,请查看配置" + url.getClass());
			} 
		} catch(IOException e) {
			logger.warn("sendError IO exception", e);
			throw new SecurityRuntimeException("sendError IO exception", e);
		} catch (ServletException e) {
			logger.warn("redirect2Page Servlet exception", e);
			throw new SecurityRuntimeException("redirect2Page Servlet exception", e);
		}
	}

	/**
	 * @return the callback
	 */
	public AuthenticationCallback getCallback() {
		return callback;
	}

	/**
	 * @param callback the callback to set
	 */
	public void setCallback(AuthenticationCallback callback) {
		this.callback = callback;
	}

	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
