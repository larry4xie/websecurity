package my.websecurity.auth;

import java.util.ArrayList;
import java.util.List;

import my.websecurity.auth.impl.PrivilegeVoter;
import my.websecurity.auth.impl.UnanimousAccessDecisionManager;
import my.websecurity.interceptor.Interceptor;


/**
 * 抽象的权限认证拦截器<br/>
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public abstract class AbstractAuthenticationInterceptor implements Interceptor {
	/**
	 * 安全元数据源
	 */
	protected MetadataSource metadataSource;
	
	private int order;
	
	/**
	 * 决策管理器
	 */
	protected AccessDecisionManager accessDecisionManager;
	
	/**
	 * 构建默认的决策管理器<br/>
	 * <pre>
	 * &ltbean name="unanimousAccessDecisionManager" class="UnanimousAccessDecisionManager"&gt
	 * 	&ltproperty name="decisionVoters"&gt
	 * 		&ltlist value-type="AccessDecisionVoter"&gt
	 * 			&ltbean name="privilegeVoter" class="PrivilegeVoter" /&gt
	 * 		&lt/list&gt
	 *	 &lt/property&gt
	 * &lt/bean&gt<br/>
	 * </pre>
	 */
	public void buildGeneralAccessDecisionManager() {
		UnanimousAccessDecisionManager unanimousAccessDecisionManager = new UnanimousAccessDecisionManager();
		List<AccessDecisionVoter> decisionVoters = new ArrayList<AccessDecisionVoter>(1);
		decisionVoters.add(new PrivilegeVoter());
		unanimousAccessDecisionManager.setDecisionVoters(decisionVoters);
		this.accessDecisionManager = unanimousAccessDecisionManager;
	}
	
	/**
	 * 检查是否设置了SecurityMetadataSource(安全元数据源)和accessDecisionManager(决策管理器)
	 * 
	 * @return
	 */
	@Override
	public boolean check() {
		return null != metadataSource && null != accessDecisionManager;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public MetadataSource getMetadataSource() {
		return metadataSource;
	}

	public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
		this.accessDecisionManager = accessDecisionManager;
	}

	public AccessDecisionManager getAccessDecisionManager() {
		return accessDecisionManager;
	}

	public void setMetadataSource(MetadataSource metadataSource) {
		this.metadataSource = metadataSource;
	}
}
