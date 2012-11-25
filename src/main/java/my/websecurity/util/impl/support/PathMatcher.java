package my.websecurity.util.impl.support;

/**
 * 路径匹配器<br/>
 * 参考spring
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public interface PathMatcher {
	/**
	 * path是否是一个Pattern
	 * 
	 * @param path
	 * @return
	 */
	public boolean isPattern(String path);
 
	/**
	 * 使用Pattern匹配path
	 * 
	 * @param pattern
	 * @param path
	 * @return
	 */
    public boolean match(String pattern, String path);
 
    /**
     * 使用Pattern匹配path,只要第一个通配符匹配就可以
     * 
     * @param pattern
     * @param path
     * @return
     */
    public boolean matchStart(String pattern, String path);
}
