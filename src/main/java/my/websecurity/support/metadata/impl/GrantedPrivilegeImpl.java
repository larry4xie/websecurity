package my.websecurity.support.metadata.impl;

import my.websecurity.support.metadata.GrantedPrivilege;

/**
 * 用户受过的权限条目默认实现
 * 
 * @author xiegang
 * @since 2011-12-07
 *
 */
public class GrantedPrivilegeImpl implements GrantedPrivilege {
	private static final long serialVersionUID = -6690953079893998406L;
	
	/**
	 * 权限字符串
	 */
	private String privilege;

	public GrantedPrivilegeImpl(String privilege) {
		super();
		this.privilege = privilege;
	}
	
	@Override
	public String getPrivilege() {
		return this.privilege;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((privilege == null) ? 0 : privilege.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrantedPrivilegeImpl other = (GrantedPrivilegeImpl) obj;
		if (privilege == null) {
			if (other.privilege != null)
				return false;
		} else if (!privilege.equals(other.privilege))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return privilege;
	}

}
