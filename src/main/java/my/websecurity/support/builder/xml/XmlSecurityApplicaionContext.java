package my.websecurity.support.builder.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import my.websecurity.beanfactory.BeanFactory;
import my.websecurity.exception.SecurityConfigurationException;
import my.websecurity.interceptor.Interceptor;
import my.websecurity.support.SecurityApplicaionContext;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.metadata.UserDetailsHelper;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 配置
 * 
 * @author xiegang
 * @since 2011-12-09
 * 
 */
public class XmlSecurityApplicaionContext extends SecurityApplicaionContext {
	private static final Logger logger = LoggerFactory.getLogger(XmlSecurityApplicaionContext.class);
	
	/**
	 * 配置文件的root名称
	 */
	public static final String ROOT = "websecurity";
	
	private static final String WEBSECURITY_XSD = "resources/websecurity-1.0.xsd";
	
	/**
	 * default max order
	 */
	public static final int ORDER = 10000;
	
	/**
	 * servlet上下文
	 */
	public ServletContext servletContext;
	
	/**
	 * 从配置文件构建上下文
	 * @param servletContext
	 * @param configPath
	 * @throws SecurityConfigurationException 构建失败
	 */
	public XmlSecurityApplicaionContext(final ServletContext servletContext, String configPath) throws SecurityConfigurationException {
		try {
			this.servletContext = servletContext;
			InputStream in = servletContext.getResourceAsStream(configPath);
			SAXReader reader = new SAXReader(true);
			logger.debug("验证 websecurity Schema ...");
			EntityResolver resolver = new EntityResolver() {
				public InputSource resolveEntity(String publicId, String systemId) {
					InputStream in = getClass().getClassLoader().getResourceAsStream(WEBSECURITY_XSD);
					return new InputSource(in);
				}
			};
			reader.setEntityResolver(resolver);
			reader.setFeature("http://apache.org/xml/features/validation/schema", true);
			Document doc = reader.read(in);
			logger.debug("验证 websecurity Schema 成功");
			Element root = doc.getRootElement();
			
			// 生成 BeanFactory
			this.buildBeanFactory(servletContext, root);
			// 生成 globalUserDetailsHelper
			this.buildGlobalUserDetailsHelper(root);
			// 生成 global-exclude-url-patterns
			this.buildGlobalExcludeUrlPatterns(root);
			// 生成global-special-pages
			this.buildGlobalSpecialPages(root);
			
			// 生成domain(SecurityDomainConfiguration)
			this.buildSecurityDomainConfigurations(root);
		} catch (SAXException e) {
			throw new SecurityConfigurationException("init config SAXException", e);
		} catch (DocumentException e) {
			throw new SecurityConfigurationException("init config DocumentException", e);
		}
	}
	
	/**
	 * 生成 BeanFactory
	 * @param servletConfig
	 * @param root
	 * @throws SecurityConfigurationException
	 */
	private void buildBeanFactory(final ServletContext servletConfig, Element root) throws SecurityConfigurationException {
		try {
			// 生成 BeanFactory
			String beanFactoryClass = root.valueOf("./beanfactory");
			BeanFactory beanFactory; // Bean获取接口
			if(null != beanFactoryClass && beanFactoryClass.trim().length() > 0) {
				Class<?> clazz = Class.forName(beanFactoryClass);
				beanFactory = (BeanFactory)clazz.newInstance();
				this.setBeanFactory(beanFactory);
			} else {
				beanFactory = this.buildDefaultBeanFactory();
			}
			beanFactory.init(servletConfig); // init BeanFactory
		} catch (Exception e) {
			throw new SecurityConfigurationException("init config beanfactory fail...", e);
		}
	}
	
	/**
	 * 生成 globalUserDetailsHelper
	 * @param root
	 * @throws SecurityConfigurationException
	 */
	private void buildGlobalUserDetailsHelper(Element root) throws SecurityConfigurationException {
		try {
			// 生成 globalUserDetailsHelper
			String userdetailsHelperClass = root.valueOf("./global-userdetails-helper");
			UserDetailsHelper userDetailsHelper = _getUserDetailsHelper(userdetailsHelperClass);
			if(null != userDetailsHelper) {
				this.setGlobalUserDetailsHelper(userDetailsHelper);
			} else {
				userDetailsHelper = this.buildDefaultGlobalUserDetailsHelper();
			}
		} catch (Exception e) {
			throw new SecurityConfigurationException("init config global-userdetails-helper fail...", e);
		}
	}
	
	/**
	 * 通过class获取UserDetailsHelper实例
	 * @param userdetailsHelperClass
	 * @return
	 * @throws Exception
	 */
	private UserDetailsHelper _getUserDetailsHelper(String userdetailsHelperClass) throws Exception {
		if(null != userdetailsHelperClass && userdetailsHelperClass.trim().length() > 0) {
			Class<?> clazz = Class.forName(userdetailsHelperClass);
			UserDetailsHelper userDetailsHelper = (UserDetailsHelper) clazz.newInstance();
			return userDetailsHelper;
		} else {
			return null;
		}
	}
	
	/**
	 * 生成 global-exclude-url-patterns
	 * @param root
	 * @throws SecurityConfigurationException
	 */
	private void buildGlobalExcludeUrlPatterns(Element root) throws SecurityConfigurationException {
		// 生成 global-exclude-url-patterns
		List<String> globalExcludeUrlPatterns = _getUrlPatterns(root.selectSingleNode("./global-exclude-url-patterns"));
		if(globalExcludeUrlPatterns.size() > 0) {
			this.setGlobalExcludeUrlPatterns(globalExcludeUrlPatterns);
		}
	}
	
	/**
	 * 通过urlPatterns节点获取url 列表
	 * @param urlPatternsNode
	 * @return
	 */
	private List<String> _getUrlPatterns(Node urlPatternsNode) {
		List<String> globalExcludeUrlPatterns = new ArrayList<String>();
		if(null != urlPatternsNode) {
			List<?> nodes = urlPatternsNode.selectNodes("./url");
			for(Object obj : nodes) {
				globalExcludeUrlPatterns.add(((Node)obj).getText().trim());
			}
		}
		return globalExcludeUrlPatterns;
	}
	
	/**
	 * 生成global-special-pages
	 * 
	 * @param root
	 * @throws SecurityConfigurationException
	 */
	private void buildGlobalSpecialPages(Element root) throws SecurityConfigurationException {
		// 生成global-special-pages
		Map<String, String> globalSpecialPages = _getSpecialPages(root.selectSingleNode("./global-special-pages"));
		this.setGlobalSpecialPages(globalSpecialPages);
	}
	
	/**
	 * 通过specialPages节点获取page 列表
	 * @param specialPagesNode
	 * @return
	 */
	private Map<String, String> _getSpecialPages(Node specialPagesNode) {
		Map<String, String> specialPages = new HashMap<String, String>();
		
		if(null != specialPagesNode) {
			List<?> nodes = specialPagesNode.selectNodes("./page");
			for(Object page : nodes) {
				Element pe = (Element) page;
				String pagename = pe.valueOf("@name").trim();
				String pagevalue = pe.valueOf("@value").trim();
				specialPages.put(pagename, pagevalue);
			}
		}
		return specialPages;
	}
	
	private void buildSecurityDomainConfigurations(Element root) throws SecurityConfigurationException {
		try {
			// 生成domains
			Attribute defaultAttribute = (Attribute) root.selectSingleNode("./domains/@default");
			String defaultDomain = defaultAttribute != null ? defaultAttribute.getValue() : null;
			
			// 生成domain(SecurityDomainConfiguration)
			List<SecurityDomainConfiguration> securityDomainConfigurations = new ArrayList<SecurityDomainConfiguration>(); // 安全框架domain配置
			List<?> nodes = root.selectNodes("./domains/domain");
			for(Object obj : nodes) {
				Element webdomain = (Element) obj;
				SecurityDomainConfiguration domainConfig = new SecurityDomainConfiguration(this);
				String domainName = webdomain.valueOf("@name").trim();
				String domainSession = webdomain.valueOf("@session").trim();
				String shortCircuit = webdomain.valueOf("@short-circuit").trim();
				domainConfig.setName(domainName); // 唯一的名称
				domainConfig.setSession(domainSession); // session中userDetails中名称
				domainConfig.setShortCircuit("true".equalsIgnoreCase(shortCircuit)? true : false);
				
				// filter-url-patterns
				List<String> urlPatterns = _getUrlPatterns(webdomain.selectSingleNode("./filter-url-patterns")); // 需要拦截的路径
				domainConfig.setUrlPatterns(urlPatterns);
				
				// exclude-url-patterns
				List<String> excludeUrlPatterns = _getUrlPatterns(webdomain.selectSingleNode("./exclude-url-patterns")); // 排除不需要拦截的路径
				if(excludeUrlPatterns.size() > 0) {
					domainConfig.setExcludeUrlPatterns(excludeUrlPatterns);
				}
				
				// special-pages
				Map<String, String> specialPages = _getSpecialPages(webdomain.selectSingleNode("./special-pages"));
				domainConfig.addSpecialPages(specialPages);
				
				// 生成 userDetailsHelper
				String userdetailsHelperClass = webdomain.valueOf("./userdetails-helper");
				UserDetailsHelper userDetailsHelper = _getUserDetailsHelper(userdetailsHelperClass);
				if(null != userDetailsHelper) {
					domainConfig.setUserDetailsHelper(userDetailsHelper);
				}
				
				// interceptors
				String beanname, order;
				int _order = ORDER;
				Set<Interceptor> interceptors = new TreeSet<Interceptor>(Interceptor.DEFAULECOMPARATOR);
				List<?> interceptorNodes = webdomain.selectNodes("./interceptors/interceptor");
				if(null != interceptorNodes && interceptorNodes.size() > 0) {
					for(Object obj1 : interceptorNodes) {
						Element interceptorE = (Element) obj1;
						beanname = interceptorE.valueOf("@bean").trim();
						order = interceptorE.valueOf("@order");
						Interceptor interceptor = this.getBeanFactory().getBean(beanname, Interceptor.class);
						if(null != order && order.trim().length() > 0) {
							interceptor.setOrder(Integer.parseInt(order.trim()));
						} else {
							interceptor.setOrder(_order--); // default
						}
						if(!interceptor.check()) {
							// 不可用
							throw new SecurityConfigurationException("Interceptor is not available : " + interceptor);
						}
						interceptors.add(interceptor);
					}
				}
				domainConfig.setInterceptors(new ArrayList<Interceptor>(interceptors));
				securityDomainConfigurations.add(domainConfig);
			}
			// set default domain
			this.setDefaultDomain(defaultDomain != null ? defaultDomain : securityDomainConfigurations.get(0).getName());
			// set securityDomainConfigurations
			this.setSecurityDomainConfigurations(securityDomainConfigurations);
		} catch (Exception e) {
			throw new SecurityConfigurationException("init config domains fail...", e);
		}
	}
}
