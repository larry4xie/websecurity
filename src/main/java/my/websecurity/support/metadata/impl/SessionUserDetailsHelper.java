package my.websecurity.support.metadata.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.support.SecurityDomainConfiguration;
import my.websecurity.support.SecurityServletContext;
import my.websecurity.support.metadata.UserDetails;
import my.websecurity.support.metadata.UserDetailsHelper;


/**
 * 用户详细信息服务HttpSession的实现<br/>
 * session name支持逗号割开,返回第一个获取到的<http name="gen" session="admin, user">
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public class SessionUserDetailsHelper implements UserDetailsHelper {
	/**
	 * 创建一个新的session, 把原来session中所有属性复制到新的session中<br/>
	 * 这是默认
	 */
	public static final String MIGRATESESSION = "MIGRATESESSION";
	
	/**
	 * 创建一个新的干净的session，不会复制session中的数据
	 */
	public static final String NEWSESSION = "NEWSESSION";
	
	/**
	 * 什么也不干，继续使用原来的session
	 */
	public static final String NONE = "NONE";
	
	/**
	 * session固定估计
	 * 默认使用新建session并并复制原有属性
	 */
	private String sessionFixationProtection = MIGRATESESSION;
	
	/**
	 * @param sessionFixationProtection the sessionFixationProtection to set
	 */
	public void setSessionFixationProtection(String sessionFixationProtection) {
		this.sessionFixationProtection = sessionFixationProtection;
	}

	@Override
	public UserDetails loadUserDetails(SecurityServletContext context, SecurityDomainConfiguration configuration) throws SecurityRuntimeException {
		try {
			HttpSession session = context.getRequest().getSession(false);
			if(session != null) {
				String[] nameArr = configuration.getSession();
				Object obj = null;
				for(int i = 0; i < nameArr.length; i++) {
					obj = session.getAttribute(nameArr[i]);
					if(obj != null) {
						return (UserDetails) obj;
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw new SecurityRuntimeException("获取UserDetails异常", e);
		}
	}
	
	public void saveUserDetails(HttpServletRequest request, UserDetails userDetails, String sessionName) throws SecurityRuntimeException {
		try {
			if(null == userDetails) {
				return;
			}
			HttpSession session = request.getSession(false);
			if(session != null) {
				if(sessionFixationProtection.equalsIgnoreCase(MIGRATESESSION)) {
					// new session and copy old attrs
					Map<String, Object> attrs = createMigratedAttributeMap(session);
					
					session.invalidate();
					session = request.getSession(true);
					
					for (Map.Entry<String, Object> entry : attrs.entrySet()) {
						session.setAttribute(entry.getKey(), entry.getValue());
					}
				}
				if(sessionFixationProtection.equalsIgnoreCase(NEWSESSION)) {
					// new session
					session.invalidate();
					session = request.getSession(true);
				}
			} else {
				session = request.getSession(true);
			}
			// add userdetail
			session.setAttribute(sessionName, userDetails);
		} catch (Exception e) {
			throw new SecurityRuntimeException("设置UserDetails异常", e);
		}
	}

	@Override
	public void saveUserDetails(SecurityServletContext context, UserDetails userDetails, SecurityDomainConfiguration configuration) throws SecurityRuntimeException {
		this.saveUserDetails(context.getRequest(), userDetails, configuration.getSession()[0]);
	}
	
	/**
	 * 移除session的用户消息
	 * 
	 * @param request
	 * @param sessionName
	 * @param onlythis 是否仅仅删除session中的userDetails
	 * @throws SecurityRuntimeException
	 */
	public void removeUserDetails(HttpServletRequest request, String sessionName, boolean onlythis) throws SecurityRuntimeException {
		try {
			HttpSession session = request.getSession();
			if(onlythis) {
				session.removeAttribute(sessionName);
			} else {
				session.invalidate();
			}
		} catch (Exception e) {
			throw new SecurityRuntimeException("移除UserDetails异常", e);
		}
	}
	
	@Override
	public void removeUserDetails(SecurityServletContext context, SecurityDomainConfiguration configuration) throws SecurityRuntimeException {
		this.removeUserDetails(context.getRequest(), configuration.getSession()[0], false);
	}
	
	/**
	 * 迁移到session中的所有属性
	 * 
	 * @param session
	 * @return
	 */
	private Map<String, Object> createMigratedAttributeMap(HttpSession session) {
		Map<String, Object> attributesToMigrate = new HashMap<String, Object>();
		Enumeration<?> enumer = session.getAttributeNames();

		while (enumer.hasMoreElements()) {
			String key = (String) enumer.nextElement();
			attributesToMigrate.put(key, session.getAttribute(key));
		}
		return attributesToMigrate;
	}
}
