package my.websecurity.auth.impl;


import java.util.List;

import my.websecurity.auth.AbstractAccessDecisionManager;
import my.websecurity.auth.AccessDecisionVoter;
import my.websecurity.auth.MetadataSource.Metadata;
import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.support.metadata.UserDetails;


/**
 * 只有所有投票人都同意才通过的表决器
 * 
 * @author xiegang
 * @since 2011-12-09
 * 
 */
public class UnanimousAccessDecisionManager extends AbstractAccessDecisionManager {

	/**
	 * 只有所有投票人都同意才通过
	 */
	@Override
	public boolean decide(UserDetails userDetails, Object object, List<Metadata> metadatas) throws SecurityRuntimeException {
		for(AccessDecisionVoter voter : decisionVoters) {
			boolean pass = true;
			for(Metadata m : metadatas) {
				if(voter.vote(userDetails, object, m.getPrivileges(), m.getRelation()) != AccessDecisionVoter.ACCESS_GRANTED) {
					pass =  false;
					break;
				}
			}
			if(!pass) {
				return false;
			}
		}
		return true;
	}

}
