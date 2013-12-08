package my.websecurity;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.exception.SecuritySuspendException;
import my.websecurity.support.SecurityApplicaionContext;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.builder.xml.XmlSecurityApplicaionContext;
import my.websecurity.support.metadata.UserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * 安全框架java web 入口Filter
 * </p>
 * 
 * @author xiegang
 * @since 2011-12-06
 *
 */
public class SecurityFilterChain implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(SecurityFilterChain.class);
	
	/**
	 * 配置文件path
	 */
	private String configPath = "/WEB-INF/classes/websecurity.xml";
	
	private ServletContext servletContext;
	
	/**
	 * 安全框架配置上下文
	 */
	private SecurityApplicaionContext securityApplicaionContext;
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		logger.info("websecurity filter 初始化...");
		// init Parameter
		String configLoc = config.getInitParameter("config");
		if(configLoc != null && configLoc.length() > 0) {
			this.configPath = configLoc;
		}
		logger.info("websecurity 使用配置：" + this.configPath);
		// getServletContext
		this.servletContext = config.getServletContext();
		WebSecurity.setServletContext(servletContext);
		// parse deploy to init
		try {
			logger.info("websecurity 初始化配置文件...");
			this.securityApplicaionContext = new XmlSecurityApplicaionContext(servletContext, configPath);
			WebSecurity.setSecurityApplicaionContext(securityApplicaionContext);
			if(logger.isInfoEnabled()) {
				logger.info("websecurity 初始化配置成功：" + this.securityApplicaionContext);
			}
		} catch (SecurityRuntimeException e) {
			logger.error("websecurity 初始化配置文件异常");
			throw new ServletException("SecurityFilterChain initialize Configuration Exception", e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		long _start = System.currentTimeMillis();
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		SecurityServletContext context = new SecurityServletContext(req, rep);
		
		String url = context.getRequestUrl();
		try {
			// 排除全局的不拦截路径
			if(securityApplicaionContext.matchExcludeUrl(url)) {
				if(logger.isDebugEnabled()) {
					logger.debug("{} execute[exclude]: {}ms", url, System.currentTimeMillis() - _start);
				}
				chain.doFilter(request, response);
				return;
			}
			for(SecurityDomainConfiguration config : securityApplicaionContext.getSecurityDomainConfigurations()) {
				if(config.matchUrl(url)) {
					// get UserDetails, May be null
					UserDetails userDetails = config.getUserDetailsHelper().loadUserDetails(context, config);
					// invoke Interceptors
					config.getInterceptorChainInstance().doFilter(context, userDetails, config);
					
					if(config.isShortCircuit()) { // 短路
						break;
					}
				}
			}
			if(logger.isDebugEnabled()) {
				logger.debug("{} execute[pass]: {}ms", url, System.currentTimeMillis() - _start);
			}
			chain.doFilter(request, response);
			return;
		} catch (SecuritySuspendException e) {
			// Suspend chain.doFilter(request, response);
			if(logger.isDebugEnabled()) {
				logger.debug("{} execute[fail]: {}ms", url, System.currentTimeMillis() - _start);
			}
		} catch (SecurityRuntimeException e) {
			logger.error("websecurity 运行时异常");
			throw new ServletException("SecurityFilterChain doFilter SecurityRuntimeException", e);
		}
	}
	
	@Override
	public void destroy() {
		// do nothing
	}
}
