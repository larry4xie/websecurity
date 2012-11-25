package my.websecurity.auth;

import java.util.List;

/**
 * 抽象 AccessDecisionManager
 * 
 * @author xiegang
 * @since 2011-12-09
 *
 */
public abstract class AbstractAccessDecisionManager implements AccessDecisionManager{
	/**
	 * 投票人
	 */
	protected List<AccessDecisionVoter> decisionVoters;
	
	/**
	 * 所有投票人必须都支持
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		for(int i = decisionVoters.size() - 1; i >= 0; i--) {
			if(!decisionVoters.get(i).supports(clazz)) {
				return false;
			}
		}
		return true;
	}

	public List<AccessDecisionVoter> getDecisionVoters() {
		return decisionVoters;
	}

	public void setDecisionVoters(List<AccessDecisionVoter> decisionVoters) {
		this.decisionVoters = decisionVoters;
	}
}
