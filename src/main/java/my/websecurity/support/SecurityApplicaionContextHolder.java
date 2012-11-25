package my.websecurity.support;

import javax.servlet.ServletContext;

/**
 * SecurityApplicaionContext 持有器
 * 
 * @author xiegang
 * @since 2011-12-19
 *
 */
public class SecurityApplicaionContextHolder {
	private static ServletContext servletContext;
	private static SecurityApplicaionContext securityApplicaionContext;

	/**
	 * @return the servletContext
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext the servletContext to set
	 */
	public static void setServletContext(ServletContext servletContext) {
		SecurityApplicaionContextHolder.servletContext = servletContext;
	}

	/**
	 * @return the securityApplicaionContext
	 */
	public static SecurityApplicaionContext getSecurityApplicaionContext() {
		return securityApplicaionContext;
	}

	/**
	 * @param securityApplicaionContext the securityApplicaionContext to set
	 */
	public static void setSecurityApplicaionContext(SecurityApplicaionContext securityApplicaionContext) {
		SecurityApplicaionContextHolder.securityApplicaionContext = securityApplicaionContext;
	}
	
	
}
