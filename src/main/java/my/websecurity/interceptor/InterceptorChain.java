package my.websecurity.interceptor;

import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.exception.SecuritySuspendException;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;

/**
 * 拦截器链，线程不安全
 * 
 * @author xiegang
 * @since 2011-12-07
 */
public interface InterceptorChain {
	/**
	 * 增加一个拦截器
	 * 
	 * @param interceptor
	 * @return
	 */
	public boolean addInterceptor(Interceptor interceptor);
	
	/**
	 * 删除一个拦截器
	 * 
	 * @param interceptor
	 * @return
	 */
	public boolean removeInterceptor(Interceptor interceptor);
	
	/**
	 * 清空所有拦截器
	 */
	public void clear();
	
	/**
	 * 执行拦截器链
	 * 
	 * @param context servlet上下文
	 * @param userDetails 用户详细信息
	 * @param configuration 
	 * @throws SecurityRuntimeException
	 */
	public void doFilter(SecurityServletContext context, UserDetails userDetails, SecurityDomainConfiguration configuration) throws SecuritySuspendException, SecurityRuntimeException;
}
