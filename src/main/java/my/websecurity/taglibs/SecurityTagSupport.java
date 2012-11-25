package my.websecurity.taglibs;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.TagSupport;

import my.websecurity.support.SecurityApplicaionContext;
import my.websecurity.support.SecurityApplicaionContextHolder;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;


/**
 * webSecurity系统标签的支持类
 * 
 * @author xiegang
 * @since 2011-12-28
 */
public class SecurityTagSupport extends TagSupport {
	private static final long serialVersionUID = -4063472879571413842L;
	
	/**
	 * 域
	 */
	protected String domain;
	
	public SecurityTagSupport() {
		init();
	}
	
	private void init() {
		this.domain = "";
	}

	/**
	 * 获取domain域中的userdetails<br/>
	 * pageContext环境中的request必须是http请求否者返回null
	 * 
	 * @return 不存在时返回null
	 */
	protected UserDetails getUserDetails() {
		ServletRequest request = pageContext.getRequest();
		ServletResponse response = pageContext.getResponse();
		if(request instanceof HttpServletRequest) {
			SecurityApplicaionContext context = SecurityApplicaionContextHolder.getSecurityApplicaionContext();
			SecurityDomainConfiguration configuration = context.getSecurityDomainConfiguration(domain);
			return configuration != null ? context.getGlobalUserDetailsHelper().loadUserDetails(new SecurityServletContext((HttpServletRequest)request, (HttpServletResponse)response), configuration) : null;
		} else {
			return null;
		}
	}
	
	/**
	 * 获取domain域中的SecurityDomainConfiguration<br/>
	 * pageContext环境中的request必须是Domain请求否者返回null
	 * 
	 * @return 不存在时返回null
	 */
	protected SecurityDomainConfiguration getSecurityDomainConfiguration() {
		ServletRequest request = pageContext.getRequest();
		if(request instanceof HttpServletRequest) {
			SecurityApplicaionContext context = SecurityApplicaionContextHolder.getSecurityApplicaionContext();
			return context.getSecurityDomainConfiguration(domain);
		} else {
			return null;
		}
	}

	public boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = isEmpty(domain) ? "" : domain;
	}
}
