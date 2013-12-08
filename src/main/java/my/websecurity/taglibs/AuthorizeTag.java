package my.websecurity.taglibs;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import my.websecurity.support.metadata.GrantedPrivilege;
import my.websecurity.support.metadata.UserDetails;


/**
 * domain 只能获取权限的域,与配置中的webdomain的name属性一致<br/>
 * granted\any-granted\all-granted\not-granted同时只能配置一个,配置多个时只有第一个有效<br/>  
 * <pre>
 * &lt;sec:authorize domain = "user" granted = "" any-granted = "" all-granted = "" not-granted = "">
 *    这段内容只能被授权的用户看到
 * &lt;/sec:authorize>
 * </pre>
 *
 * @author xiegang
 * @since 2011-12-28
 *
 */
public class AuthorizeTag extends SecurityTagSupport {
	private static final long serialVersionUID = 760185884427618522L;
	
	/**
	 * 必须拥有这个权限
	 */
	private String granted;
	/**
	 * 至少包含一个权限
	 */
	private String anyGranted;
	/**
	 * 必须拥有所有权限
	 */
	private String allGranted;
	/**
	 * 必须没有这个权限
	 */
	private String notGranted;
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		UserDetails userDetails = getUserDetails();
		if(userDetails != null) {
			if(authorize(userDetails)) {
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
	}

	/**
	 * 认证
	 * 
	 * @param userDetails
	 * @return 认证通过（执行body）返回true
	 */
	private boolean authorize(UserDetails userDetails) {
		Collection<GrantedPrivilege> privileges = userDetails.getAllPrivileges();
		if(privileges == null || privileges.size() < 1) {
			return isEmpty(notGranted) ? false : true;
		}
		if(!isEmpty(granted)) {
			// granted
			for(GrantedPrivilege privilege : privileges) {
				if(comparePrivilege(privilege, granted)) {
					return true;
				}
			}
		} else if(!isEmpty(anyGranted)) {
			// anyGranted
			String[] granteds = getGranteds(anyGranted);
			for(int i = 0; i < granteds.length; i++) {
				for(GrantedPrivilege privilege : privileges) {
					if(comparePrivilege(privilege, granteds[i])) {
						return true;
					}
				}
			}
		} else if(!isEmpty(allGranted)) {
			// allGranted
			String[] granteds = getGranteds(allGranted);
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
		} else if(!isEmpty(notGranted)) {
			// notGranted
			for(GrantedPrivilege privilege : privileges) {
				if(comparePrivilege(privilege, notGranted)) {
					return false;
				}
			}
			return true;
		}
		return false;
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

	/**
	 * @return the granted
	 */
	public String getGranted() {
		return granted;
	}

	/**
	 * @param granted the granted to set
	 */
	public void setGranted(String granted) {
		this.granted = granted;
	}

	/**
	 * @return the anyGranted
	 */
	public String getAnyGranted() {
		return anyGranted;
	}

	/**
	 * @param anyGranted the anyGranted to set
	 */
	public void setAnyGranted(String anyGranted) {
		this.anyGranted = anyGranted;
	}

	/**
	 * @return the allGranted
	 */
	public String getAllGranted() {
		return allGranted;
	}

	/**
	 * @param allGranted the allGranted to set
	 */
	public void setAllGranted(String allGranted) {
		this.allGranted = allGranted;
	}

	/**
	 * @return the notGranted
	 */
	public String getNotGranted() {
		return notGranted;
	}

	/**
	 * @param notGranted the notGranted to set
	 */
	public void setNotGranted(String notGranted) {
		this.notGranted = notGranted;
	}
}
