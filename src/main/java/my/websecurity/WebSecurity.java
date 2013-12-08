package my.websecurity;

import javax.servlet.ServletContext;

import my.websecurity.support.SecurityApplicaionContext;

/**
 * WebSecurity
 * 
 * @author xiegang
 * @since 2011-12-19
 *
 */
public class WebSecurity {
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
		WebSecurity.servletContext = servletContext;
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
		WebSecurity.securityApplicaionContext = securityApplicaionContext;
	}
	
	
}
