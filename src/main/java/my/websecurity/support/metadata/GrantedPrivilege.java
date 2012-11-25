package my.websecurity.support.metadata;

import java.io.Serializable;

/**
 * 用户授权过的权限条目
 * 
 * @author xiegang
 * @since 2011-12-07
 *
 */
public interface GrantedPrivilege extends Serializable {
	/**
	 * 获得权限条目的字符串表示形式
	 * 
	 * @return
	 */
	public String getPrivilege();
}
