package my.websecurity.auth;

import my.websecurity.util.UrlMatcher;
import my.websecurity.util.impl.AntUrlPathMatcher;


/**
 * 抽象的String类型安全元数据的抽象类<br/>
 * 提供一些公共方法
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public abstract class AbstractStringMetadataSource implements MetadataSource{
	/**
	 * url匹配器<br/>
	 * 使用通配符url匹配器AntUrlPathMatcher('*', '?', '**' )
	 */
	protected UrlMatcher urlMatcher = new AntUrlPathMatcher();
	
	@Override
	public boolean supports(Class<?> clazz) {
		return String.class.equals(clazz);
	}
}
