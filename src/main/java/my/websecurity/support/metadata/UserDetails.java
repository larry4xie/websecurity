package my.websecurity.support.metadata;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;

/**
 * 用户详细信息，主要包括权限信息
 * 
 * @author xiegang
 * @since 2011-12-07
 *
 */
public interface UserDetails extends Serializable, Principal{
	/**
	 * 获取用户的权限集合,没有任何权限是返回空的集合,不能为null
	 * 
	 * @return
	 */
	public Collection<GrantedPrivilege> getAllPrivileges();
	
	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * 获取用户密码
	 * 
	 * @return
	 */
	public String getPassword();
}
