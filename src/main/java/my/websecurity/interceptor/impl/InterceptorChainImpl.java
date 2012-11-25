package my.websecurity.interceptor.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.exception.SecuritySuspendException;
import my.websecurity.interceptor.Interceptor;
import my.websecurity.interceptor.InterceptorChain;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;


public class InterceptorChainImpl implements InterceptorChain {
	/**
	 * 拦截器列表
	 */
	private List<Interceptor> interceptors;
	
	/**
	 * 迭代器
	 */
	private Iterator<Interceptor> iterator;
	
	public InterceptorChainImpl() {
		super();
		interceptors = new ArrayList<Interceptor>();
		resetIterator();
	}

	public InterceptorChainImpl(List<Interceptor> interceptors) {
		super();
		this.interceptors = interceptors;
		resetIterator();
	}

	@Override
	public void doFilter(SecurityServletContext context, UserDetails userDetails, SecurityDomainConfiguration configuration) throws SecuritySuspendException, SecurityRuntimeException {
		if(iterator.hasNext()) {
			Interceptor interceptor = iterator.next();
			interceptor.doFilter(context, userDetails, configuration, this);
		}
	}

	@Override
	public boolean addInterceptor(Interceptor interceptor) {
		boolean result = this.interceptors.add(interceptor);
		if(result) {
			resetIterator();
		}
		return result;
	}

	@Override
	public boolean removeInterceptor(Interceptor interceptor) {
		boolean result = this.interceptors.remove(interceptor);
		if(result) {
			resetIterator();
		}
		return result;
	}

	@Override
	public void clear() {
		int size = this.interceptors.size();
		this.interceptors.clear();
		if(size > 0) {
			resetIterator();
		}
	}
	
	/**
	 * 重置迭代器
	 */
	private void resetIterator() {
		this.iterator = this.interceptors.iterator();
	}
}
