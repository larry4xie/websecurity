package my.websecurity;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.websecurity.support.SecurityApplicaionContext;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.GrantedPrivilege;
import my.websecurity.support.metadata.UserDetails;

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
	 * 获取指定domain的UserDetails<br/>
	 * domain为空时,获取默认domain
	 * 
	 * @param request
	 * @param response
	 * @param domain
	 * @return 可能为null
	 */
	public UserDetails getUserDetails(HttpServletRequest request, HttpServletResponse response, String domain) {
		SecurityApplicaionContext context = WebSecurity.getSecurityApplicaionContext();
		SecurityDomainConfiguration configuration = 
			isEmpty(domain) ? context.getDefaultSecurityDomainConfiguration() : 
				context.getSecurityDomainConfiguration(domain);
		return configuration != null ? context.getGlobalUserDetailsHelper().loadUserDetails(new SecurityServletContext((HttpServletRequest)request, (HttpServletResponse)response), configuration) : null;
	}
	
	/**
	 * 获取默认domain的UserDetails
	 * @param request
	 * @param response
	 * @return 可能为null
	 */
	public UserDetails getUserDetails(HttpServletRequest request, HttpServletResponse response) {
		return this.getUserDetails(request, response, null);
	}
	
	/**
	 * 判断是否包含某个权限
	 * 
	 * @param userDetails
	 * @param permission
	 * @return
	 */
	public boolean isGranted(UserDetails userDetails, String permission) {
		Collection<GrantedPrivilege> privileges = userDetails.getAllPrivileges();
		if(privileges == null || privileges.size() < 1) {
			return false;
		}
		
		for(GrantedPrivilege privilege : privileges) {
			if(comparePrivilege(privilege, permission)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否不包含某个权限
	 * @param userDetails
	 * @param permission
	 * @return
	 */
	public boolean notGranted(UserDetails userDetails, String permission) {
		Collection<GrantedPrivilege> privileges = userDetails.getAllPrivileges();
		if(privileges == null || privileges.size() < 1) {
			return true;
		}
		
		for(GrantedPrivilege privilege : privileges) {
			if(comparePrivilege(privilege, permission)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断是否包含权限集合中的至少一个
	 * @param userDetails
	 * @param permissions 逗号分隔
	 * @return
	 */
	public boolean isAnyGranted(UserDetails userDetails, String permissions) {
		Collection<GrantedPrivilege> privileges = userDetails.getAllPrivileges();
		if(privileges == null || privileges.size() < 1) {
			return false;
		}
		
		// anyGranted
		String[] granteds = getGranteds(permissions);
		for(int i = 0; i < granteds.length; i++) {
			for(GrantedPrivilege privilege : privileges) {
				if(comparePrivilege(privilege, granteds[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断是否包含权限集合中的所有权限
	 * @param userDetails
	 * @param permissions 逗号分隔
	 * @return
	 */
	public boolean isAllGranted(UserDetails userDetails, String permissions) {
		Collection<GrantedPrivilege> privileges = userDetails.getAllPrivileges();
		if(privileges == null || privileges.size() < 1) {
			return false;
		}
		
		String[] granteds = getGranteds(permissions);
		for(int i = 0; i < granteds.length; i++) {
			for(Iterator<GrantedPrivilege> it = privileges.iterator(); it.hasNext();) {
				GrantedPrivilege privilege = it.next();
				if(comparePrivilege(privilege, granteds[i])) {
					break;
				}
				if(!it.hasNext()) {
					return false;
				}
			}
		}
		return true;
	}

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
	
	
	/**
	 * 比较granted是否包含了privilege权限
	 * 
	 * @param privilege
	 * @param granted
	 * @return
	 */
	private boolean comparePrivilege(GrantedPrivilege privilege, String granted) {
		return privilege != null ? privilege.getPrivilege().equals(granted) : false;
	}
	
	/**
	 * "123, 456" ==> 123 456
	 * 
	 * @param granted
	 * @return
	 */
	private String[] getGranteds(String granted) {
		String[] granteds = granted != null ? granted.split(",") : new String[]{};
		for(int i = granteds.length - 1; i >= 0; i--) {
			granteds[i] = granteds[i].trim();
		}
		return granteds;
	}
	
	private boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}
}
