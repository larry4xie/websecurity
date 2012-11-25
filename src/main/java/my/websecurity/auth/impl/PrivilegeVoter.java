package my.websecurity.auth.impl;

import java.util.Collection;

import my.websecurity.auth.AccessDecisionVoter;
import my.websecurity.auth.PrivilegesRelation;
import my.websecurity.support.metadata.GrantedPrivilege;
import my.websecurity.support.metadata.UserDetails;


/**
 * 权限投票人
 * 
 * @author xiegang
 * @since 2011-12-09
 * 
 */
public class PrivilegeVoter implements AccessDecisionVoter {

	/**
	 * 支持所有类型
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public int vote(UserDetails userDetails, Object object, Collection<GrantedPrivilege> grantedPrivileges, PrivilegesRelation relation) {
		if(null == grantedPrivileges) {
			return AccessDecisionVoter.ACCESS_GRANTED; // 通过
		}
		Collection<GrantedPrivilege> privileges = userDetails.getAllPrivileges();
		switch (relation) {
			case ALL:
				if(privileges.containsAll(grantedPrivileges)) {
					return AccessDecisionVoter.ACCESS_GRANTED; // 通过
				} else {
					return AccessDecisionVoter.ACCESS_DENIED; // 不通过
				}
			case ANY:
				for(GrantedPrivilege p : grantedPrivileges) {
					if(privileges.contains(p)) {
						return AccessDecisionVoter.ACCESS_GRANTED; // 通过
					} 
				}
				return AccessDecisionVoter.ACCESS_DENIED; // 不通过
			case NOT:
				for(GrantedPrivilege p : grantedPrivileges) {
					if(privileges.contains(p)) {
						return AccessDecisionVoter.ACCESS_DENIED; // 不通过
					} 
				}
				return AccessDecisionVoter.ACCESS_GRANTED; // 通过
			default:
				return AccessDecisionVoter.ACCESS_DENIED; // 不通过
		}
	}

}
