package my.websecurity.interceptor;

import java.util.List;

import my.websecurity.interceptor.impl.InterceptorChainImpl;


/**
 * 拦截器链工厂
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public class InterceptorChainFactory {
	/**
	 * 获得一个空的InterceptorChain
	 * 
	 * @return
	 */
	public static InterceptorChain getInterceptorChain() {
		return new InterceptorChainImpl();
	}
	
	/**
	 * 获取一个指定了拦截器列表的InterceptorChain
	 * 
	 * @param interceptors
	 * @return
	 */
	public static InterceptorChain getInterceptorChain(List<Interceptor> interceptors) {
		return new InterceptorChainImpl(interceptors);
	}
}
