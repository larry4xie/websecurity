package my.websecurity.auth;

import java.util.Collection;
import java.util.List;

import my.websecurity.auth.MetadataSource.Metadata;
import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.support.metadata.UserDetails;


/**
 * 决策管理器<br/>
 * 给定请求访问的主题和想要访问的资源元数据以及需要的权限，决策是否可以访问
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public interface AccessDecisionManager {
	/**
	 * 决策
	 * 
	 * @param userDetails 申请资源的主体
	 * @param object 被访问的资源
	 * @param metadatas 权限间的逻辑关系
	 * @return 是否通过
	 * @throws SecurityRuntimeException 执行发生异常
	 */
	public boolean decide(UserDetails userDetails, Object object, List<Metadata> metadatas) throws SecurityRuntimeException;
	
	/**
     * 是否支持的元数据类型<br/>
     * {@link #decide(Authentication, Object, Collection)}方法的object允许的类型
     *
     * @param 元数据类型的class
     *
     * @return t
     */
    public boolean supports(Class<?> clazz);
}
