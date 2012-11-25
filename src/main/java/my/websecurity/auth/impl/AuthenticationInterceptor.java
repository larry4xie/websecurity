package my.websecurity.auth.impl;

import java.io.IOException;
import java.util.List;

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
				UrlUtils.redirect2Page(req, rep, configuration.getSpecialPages().get(INVALID_SESSION_URL) + "?toUrl=" + req.getRequestURL(), INVALID_SESSION_SC);
				if(callback != null) { // callback
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
					if(callback != null) { // callback
						callback.onPassAuthentication(context, configuration, userDetails);
					}
				} else {
					// 权限验证不通过
					UrlUtils.redirect2Page(req, rep, configuration.getSpecialPages().get(ACCESS_DENIED_PAGE) + "?toUrl=" + req.getRequestURL(), ACCESS_DENIED_SC);
					if(callback != null) { // callback
						callback.onNotPassAuthentication(context, configuration, userDetails);
					}
					throw new SecuritySuspendException("权限验证不通过");
				}
			} else {
				logger.error("不支持的权限资源类型,请查看配置" + url.getClass());
				throw new SecurityRuntimeException("不支持的权限资源类型,请查看配置" + url.getClass());
			} 
		} catch(IOException e) {
			logger.error("redirect2Page IO exception", e);
			throw new SecurityRuntimeException("redirect2Page IO exception", e);
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
