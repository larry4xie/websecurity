package my.websecurity.interceptor;

import java.util.Comparator;

import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.exception.SecuritySuspendException;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;


/**
 * 拦截器
 * 
 * @author xiegang
 * @since 2011-12-07
 */
public interface Interceptor {
	/**
	 * 倒序
	 */
	public static final Comparator<Interceptor> DEFAULECOMPARATOR = new Comparator<Interceptor>() {
		@Override
		public int compare(Interceptor one, Interceptor two) {
			return two.getOrder() - one.getOrder();
		}
		
	};
	
	/**
	 * 如果想短路后续的domain设置configuration的短路属性
	 * 
	 * @param context servlet上下文
	 * @param userDetails
	 * @param configuration domain配置
	 * @param interceptorChain 所属的拦截器链
	 * @throws SecuritySuspendException
	 * @throws SecurityRuntimeException
	 */
	public void doFilter(SecurityServletContext context, UserDetails userDetails, SecurityDomainConfiguration configuration, InterceptorChain interceptorChain) throws SecuritySuspendException, SecurityRuntimeException;
	
	public void setOrder(int order);
	
	public int getOrder();
	
	/**
	 * 简单检测可用性<br/>
	 * 比如初始化上下文的时候检测配置的interceptor是否可用
	 * @return
	 */
	public boolean check();
}
