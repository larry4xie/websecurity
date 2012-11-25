package my.websecurity.auth;

/**
 * 权限间的逻辑关系
 * 
 * @author xiegang
 * @since 2012-7-3
 *
 */
public enum PrivilegesRelation {
	/** 必须包含所有权限 */
	ALL,
	/** 必须包含其中至少一个 */
	ANY,
	/** 必须全部不包含 */
	NOT
}
