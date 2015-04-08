package my.websecurity.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.websecurity.interceptor.Interceptor;
import my.websecurity.interceptor.InterceptorChain;
import my.websecurity.interceptor.InterceptorChainFactory;
import my.websecurity.support.metadata.UserDetailsHelper;
import my.websecurity.util.UrlMatcher;
import my.websecurity.util.impl.AntUrlPathMatcher;


/**
 * 安全框架http配置
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public class SecurityDomainConfiguration {
	/**
	 * 唯一的名称
	 */
	private String name;
	
	/**
	 * session中userDetails中名称
	 */
	private String[] session;
	
	/**
	 * 是否短路
	 */
	private boolean shortCircuit = true;
	
	/**
	 * 用户详细信息服务加载器
	 */
	private UserDetailsHelper userDetailsHelper;
	
	/**
	 * 需要拦截的路径
	 */
	private List<String> urlPatterns;
	
	/**
	 * 排除不需要拦截的路径
	 */
	private List<String> excludeUrlPatterns;
	
	/**
	 * 特别的页面
	 */
	private Map<String, String> specialPages;
	
	/**
	 * 拦截器
	 */
	private List<Interceptor> interceptors;
	
	/**
	 * 所属的安全上下文
	 */
	private SecurityApplicaionContext securityApplicaionContext;
	
	/**
	 * url匹配器<br/>
	 * 使用通配符url匹配器AntUrlPathMatcher('*', '?', '**' )
	 */
	private UrlMatcher urlMatcher = new AntUrlPathMatcher();
	
	public SecurityDomainConfiguration(SecurityApplicaionContext securityApplicaionContext) {
		super();
		// extends SecurityApplicaionContext
		this.userDetailsHelper = securityApplicaionContext.getGlobalUserDetailsHelper();
		// deep copy
		this.specialPages = new HashMap<String, String>(securityApplicaionContext.getGlobalSpecialPages());
		this.securityApplicaionContext = securityApplicaionContext;
	}
	
	/**
	 * 判断是否过滤指定url<br/>
	 * 在过滤列表中且不在排除列表中
	 * 
	 * @param url
	 * @return
	 */
	public boolean matchUrl(String url) {
		// 排除列表
		if(null != excludeUrlPatterns) {
			for(String urlPattern : excludeUrlPatterns) {
				if(urlMatcher.pathMatchesUrl(urlMatcher.compile(urlPattern), url)) {
					return false;
				}
			}
		}
		
		// 过滤列表
		if(null != urlPatterns) {
			for(String urlPattern : urlPatterns) {
				if(urlMatcher.pathMatchesUrl(urlMatcher.compile(urlPattern), url)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取InterceptorChain实例，InterceptorChain是线程不安全的
	 * 
	 * @return
	 */
	public InterceptorChain getInterceptorChainInstance() {
		return interceptors != null ? InterceptorChainFactory.getInterceptorChain(interceptors) : InterceptorChainFactory.getInterceptorChain();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getSession() {
		return session;
	}

	/**
	 * @return the shortCircuit
	 */
	public boolean isShortCircuit() {
		return shortCircuit;
	}

	/**
	 * @param shortCircuit the shortCircuit to set
	 */
	public void setShortCircuit(boolean shortCircuit) {
		this.shortCircuit = shortCircuit;
	}

	/**
	 * @return the userDetailsLoader
	 */
	public UserDetailsHelper getUserDetailsHelper() {
		return userDetailsHelper;
	}

	/**
	 * @param userDetailsLoader the userDetailsLoader to set
	 */
	public void setUserDetailsHelper(UserDetailsHelper userDetailsLoader) {
		this.userDetailsHelper = userDetailsLoader;
	}

	/**
	 * 设置属性session中的名称，多个用逗号割开
	 * @param session
	 */
	public void setSession(String session) {
		this.session = session != null ? session.split(",") : new String[]{};
		for(int i = this.session.length - 1; i >= 0; i--) {
			this.session[i] = this.session[i].trim();
		}
	}

	public List<String> getUrlPatterns() {
		return urlPatterns;
	}

	public void setUrlPatterns(List<String> urlPatterns) {
		this.urlPatterns = urlPatterns;
	}

	public List<Interceptor> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}

	public List<String> getExcludeUrlPatterns() {
		return excludeUrlPatterns;
	}

	public void setExcludeUrlPatterns(List<String> excludeUrlPatterns) {
		this.excludeUrlPatterns = excludeUrlPatterns;
	}

	public Map<String, String> getSpecialPages() {
		return specialPages != null ? specialPages : new HashMap<String, String>();
	}
	
	void setSpecialPages(Map<String, String> specialPages) {
		this.specialPages = specialPages;
	}

	/**
	 * 增加
	 * @param specialPages
	 */
	public void addSpecialPages(Map<String, String> specialPages) {
		if(null == this.specialPages) {
			this.specialPages = specialPages;
		} else {
			this.specialPages.putAll(specialPages);
		}
	}

	public SecurityApplicaionContext getSecurityApplicaionContext() {
		return securityApplicaionContext;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append("name = ").append(name).append(", ");
		sb.append("session = ").append(session != null ? Arrays.toString(session) : "").append(", ");
		sb.append("shortCircuit = ").append(shortCircuit).append(", ");
		sb.append("urlPatterns = ").append(urlPatterns).append(", ");
		sb.append("excludeUrlPatterns = ").append(excludeUrlPatterns).append(", ");
		sb.append("specialPages = ").append(specialPages).append(", ");
		sb.append("userDetailsHelper = ").append(userDetailsHelper.getClass().getName()).append(", ");
		sb.append("interceptors = ").append(interceptors);
		return sb.append("]").toString();
	}
}
