package my.websecurity.auth;

import java.util.Collection;

import my.websecurity.support.metadata.GrantedPrivilege;
import my.websecurity.support.metadata.UserDetails;


/**
 * 决策器具体投票人<br/>
 * 决策器包含一到多个投票人
 * 
 * @author xiegang
 * @since 2011-12-09
 *
 */
public interface AccessDecisionVoter {
	/**
	 * 通过
	 */
	int ACCESS_GRANTED = 1; 
	/**
	 * 弃权
	 */
    int ACCESS_ABSTAIN = 0;
    /**
	 * 不通过
	 */
    int ACCESS_DENIED = -1;
    
    /**
     * 支持的 {@link #vote(UserDetails, Object, Collection)}中object的类型
     * @param clazz
     * @return
     */
    boolean supports(Class<?> clazz);
    
    /**
     * 投票
     * 
     * @param userDetails
     * @param object
     * @param grantedPrivileges
     * @param relation 权限间的逻辑关系
     * @return
     */
    int vote(UserDetails userDetails, Object object, Collection<GrantedPrivilege> grantedPrivileges, PrivilegesRelation relation);
}
