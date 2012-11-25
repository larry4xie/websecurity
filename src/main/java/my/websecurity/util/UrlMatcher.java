package my.websecurity.util;

/**
 * url匹配器<br/>
 * 流程：{@link #compile(String)} > {@link #pathMatchesUrl(Object, String)}
 * 
 * @author xiegang
 * @since 2011-12-08
 * 
 */
public interface UrlMatcher {
	/**
	 * 编译路径
	 * 
	 * @param urlPattern
	 * @return {@link #pathMatchesUrl(Object, String)}的输入值Object
	 */
	Object compile(String urlPattern);

	/**
	 * 匹配
	 * 
	 * @param compiledUrlPattern
	 * @param url
	 * @return
	 */
	boolean pathMatchesUrl(Object compiledUrlPattern, String url);

	/**
	 * 获取适合所有情况的的url串
	 * 
	 * @return
	 */
	String getUniversalMatchPattern();

	/**
	 * 是否忽略大小写
	 */
	boolean requiresLowerCaseUrl();
}
