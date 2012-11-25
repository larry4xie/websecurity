package my.websecurity.beanfactory;

import javax.servlet.ServletContext;

import my.websecurity.exception.SecurityBeanException;
import my.websecurity.exception.SecurityRuntimeException;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * 安全框架Bean获取接口的Spring实现
 * 
 * @author xiegang
 * @since 2011-12-07
 *
 */
public class SpringBeanFactory implements BeanFactory {
	/**
	 * spring 上下文
	 */
	private ApplicationContext applicationContext;
	
	@Override
	public void init(ServletContext sc) throws SecurityRuntimeException {
		applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
		if(null == applicationContext) {
			throw new SecurityRuntimeException(this.getClass().toString() + " init Spring ApplicationContext Fail!!!");
		}
	}
	
	@Override
	public Object getBean(String beanname) throws SecurityBeanException {
		return applicationContext.getBean(beanname);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String beanname, Class<T> clazz) throws SecurityBeanException {
		return (T)this.getBean(beanname);
	}
}
