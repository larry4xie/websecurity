package my.websecurity.beanfactory;

import javax.servlet.ServletContext;

import my.websecurity.exception.SecurityBeanException;
import my.websecurity.exception.SecurityRuntimeException;


/**
 * 安全框架Bean获取接口,获取bean之前必须调用init初始化
 * 
 * @author xiegang
 * @since 2011-12-07
 *
 */
public interface BeanFactory {
	/**
	 * 初始化
	 * 
	 * @param sc
	 * @throws SecurityRuntimeException 初始化失败
	 */
	public void init(ServletContext sc) throws SecurityRuntimeException;
	
	/**
	 * 获取bean的Object对象
	 * 
	 * @param beanname bean姓名
	 * @return
	 * @throws SecurityBeanException 获取bean异常
	 */
	public Object getBean(String beanname) throws SecurityBeanException;
	
	/**
	 * 获取bean
	 * 
	 * @param <T>
	 * @param beanname bean姓名
	 * @param clazz 返回bean的类型
	 * @return
	 * throws SecurityBeanException 获取bean异常
	 */
	public <T> T getBean(String beanname, Class<T> clazz) throws SecurityBeanException;
}
