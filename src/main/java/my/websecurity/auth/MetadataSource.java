package my.websecurity.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import my.websecurity.exception.SecurityRuntimeException;
import my.websecurity.support.metadata.GrantedPrivilege;


/**
 * 安全元数据源<br/>
 * 比如：访问一个url需要什么权限
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public interface MetadataSource {
	/**
	 * 元数据消息集合
	 * @author xiegang
	 * @since 2012-7-3
	 *
	 */
	public static class Metadata{
		/**
		 * 元数据
		 */
		private Object object;
		
		/**
		 * 获取访问元数据所需要的权限
		 */
		private Collection<GrantedPrivilege> privileges;
		
		/**
		 * 权限间的逻辑关系
		 */
		private PrivilegesRelation relation;

		public Metadata(Object object, Collection<GrantedPrivilege> privileges, PrivilegesRelation relation) {
			super();
			this.object = object;
			this.privileges = privileges;
			this.relation = relation;
		}
		
		public Metadata(Object object, Collection<GrantedPrivilege> privileges, String relation) {
			super();
			this.object = object;
			this.privileges = privileges;
			try {
				this.relation = PrivilegesRelation.valueOf(relation);
			} catch (Exception e) {
				this.relation = PrivilegesRelation.ALL;
			}
		}
		
		/**
		 * PrivilegesRelation.ALL
		 */
		public Metadata(Object object, Collection<GrantedPrivilege> privileges) {
			super();
			this.object = object;
			this.privileges = privileges;
			this.relation = PrivilegesRelation.ALL;
		}
		
		/**
		 * PrivilegesRelation.ALL
		 */
		public Metadata(Object object, GrantedPrivilege privilege) {
			super();
			this.object = object;
			this.privileges = new ArrayList<GrantedPrivilege>(1);
			this.privileges.add(privilege);
			this.relation = PrivilegesRelation.ALL;
		}

		/**
		 * @return the object
		 */
		public Object getObject() {
			return object;
		}

		/**
		 * @param object the object to set
		 */
		public void setObject(Object object) {
			this.object = object;
		}

		/**
		 * @return the privileges
		 */
		public Collection<GrantedPrivilege> getPrivileges() {
			return privileges;
		}

		/**
		 * @param privileges the privileges to set
		 */
		public void setPrivileges(Collection<GrantedPrivilege> privileges) {
			this.privileges = privileges;
		}

		/**
		 * @return the relation
		 */
		public PrivilegesRelation getRelation() {
			return relation;
		}

		/**
		 * @param relation the relation to set
		 */
		public void setRelation(PrivilegesRelation relation) {
			this.relation = relation;
		}
	}
	
	/**
	 * 获取访问元数据的元数据消息集合（权限和关系）
	 * 
	 * @param object
	 * @return 不需要权限返回null
	 * @throws SecurityRuntimeException 获取权限异常
	 */
	public List<Metadata> getMetadatas(Object object) throws SecurityRuntimeException;
	
	/**
	 * 是否支持的元数据类型<br/> {@link #getMetadataPrivileges(Object)}方法的object允许的类型
	 * 
	 * @param 元数据类型的class
	 * 
	 * @return t
	 */
	public boolean supports(Class<?> clazz);
}
