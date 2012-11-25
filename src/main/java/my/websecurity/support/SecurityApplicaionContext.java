package my.websecurity.support;

import java.util.List;
import java.util.Map;

import my.websecurity.beanfactory.BeanFactory;
import my.websecurity.beanfactory.SpringBeanFactory;
import my.websecurity.support.metadata.UserDetailsHelper;
import my.websecurity.support.metadata.impl.SessionUserDetailsHelper;
import my.websecurity.util.UrlMatcher;
import my.websecurity.util.impl.AntUrlPathMatcher;


/**
 * 安全框架配置上下文
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public abstract class SecurityApplicaionContext {
	/**
	 * Bean获取接口
	 */
	private BeanFactory beanFactory;
	
	/**
	 * 用户详细信息服务加载器
	 */
	private UserDetailsHelper globalUserDetailsHelper;
	
	/**
	 * 全局的排除不需要拦截的路径
	 */
	private List<String> globalExcludeUrlPatterns;
	
	/**
	 * 全局的特别的页面
	 */
	private Map<String, String> globalSpecialPages;
	
	/**
	 * 安全框架domain配置
	 */
	private List<SecurityDomainConfiguration> securityDomainConfigurations;
	
	/**
	 * url匹配器<br/>
	 * 使用通配符url匹配器AntUrlPathMatcher('*', '?', '**' )
	 */
	private UrlMatcher urlMatcher = new AntUrlPathMatcher();
	
	/**
	 * 构建默认的BeanFactory<br/>
	 * 采用spring
	 */
	public BeanFactory buildDefaultBeanFactory() {
		this.beanFactory = new SpringBeanFactory();
		return this.beanFactory;
	}
	
	/**
	 * 构建默认的UserDetailsHelper<br/>
	 * 采用HttpSession的实现
	 */
	public UserDetailsHelper buildDefaultGlobalUserDetailsHelper() {
		this.globalUserDetailsHelper = new SessionUserDetailsHelper();
		return this.globalUserDetailsHelper;
	}
	
	/**
	 * 判断是否排除指定url<br/>
	 * 
	 * @param url
	 * @return
	 */
	public boolean matchExcludeUrl(String url) {
		if(null != globalExcludeUrlPatterns) {
			for(String urlPattern : globalExcludeUrlPatterns) {
				if(urlMatcher.pathMatchesUrl(urlMatcher.compile(urlPattern), url)) {
					return true; // 排除
				}
			}
		}
		return false;
	}

	public BeanFactory getSecurityBeanHolder() {
		return beanFactory;
	}

	public void setSecurityBeanHolder(BeanFactory securityBeanHolder) {
		this.beanFactory = securityBeanHolder;
	}

	public List<SecurityDomainConfiguration> getSecurityDomainConfigurations() {
		return securityDomainConfigurations;
	}
	
	/**
	 * 通过名称获取SecurityDomainConfiguration
	 * 
	 * @param name
	 * @return
	 */
	public SecurityDomainConfiguration getSecurityDomainConfiguration(String name) {
		if(null != securityDomainConfigurations) {
			for(SecurityDomainConfiguration config : securityDomainConfigurations) {
				if(config.getName().equals(name)) {
					return config;
				}
			}
		}
		return null;
	}

	public void setSecurityDomainConfigurations(List<SecurityDomainConfiguration> securityDomainConfigurations) {
		this.securityDomainConfigurations = securityDomainConfigurations;
	}

	public UserDetailsHelper getGlobalUserDetailsHelper() {
		return globalUserDetailsHelper;
	}

	public void setGlobalUserDetailsHelper(UserDetailsHelper userDetailsHelper) {
		this.globalUserDetailsHelper = userDetailsHelper;
	}

	public List<String> getGlobalExcludeUrlPatterns() {
		return globalExcludeUrlPatterns;
	}

	public void setGlobalExcludeUrlPatterns(List<String> globalExcludeUrlPatterns) {
		this.globalExcludeUrlPatterns = globalExcludeUrlPatterns;
	}
	
	/**
	 * @return the beanFactory
	 */
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * @param beanFactory the beanFactory to set
	 */
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/**
	 * @return the globalSpecialPages
	 */
	public Map<String, String> getGlobalSpecialPages() {
		return globalSpecialPages;
	}

	/**
	 * @param globalSpecialPages the globalSpecialPages to set
	 */
	public void setGlobalSpecialPages(Map<String, String> globalSpecialPages) {
		this.globalSpecialPages = globalSpecialPages;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append("BeanFactory = ").append(beanFactory.getClass().getName()).append(", ");
		sb.append("globalUserDetailsHelper = ").append(globalUserDetailsHelper.getClass().getName()).append(", ");
		sb.append("globalExcludeUrlPatterns = ").append(globalExcludeUrlPatterns).append(", ");
		sb.append("globalSpecialPages = ").append(globalSpecialPages).append(", ");
		sb.append("securityDomainConfigurations = ").append(securityDomainConfigurations);
		return sb.append("]").toString();
	}
}
