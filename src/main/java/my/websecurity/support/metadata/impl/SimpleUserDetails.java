package my.websecurity.support.metadata.impl;

import java.util.Collection;

import my.websecurity.support.metadata.GrantedPrivilege;
import my.websecurity.support.metadata.UserDetails;


/**
 * 用于存放用户名密码
 * @author xiegang
 * @since 2011-12-19
 *
 */
public class SimpleUserDetails implements UserDetails {
	private static final long serialVersionUID = 6924548914698840052L;
	
	private String username;
	private String password;
	
	public SimpleUserDetails(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public Collection<GrantedPrivilege> getAllPrivileges() {
		return null;
	}

	@Override
	public String getName() {
		return this.username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleUserDetails other = (SimpleUserDetails) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmptyUserDetails [username=" + username + ", password=" + password + "]";
	}
}
