package my.websecurity.util.impl;

import my.websecurity.util.UrlMatcher;
import my.websecurity.util.impl.support.AntPathMatcher;
import my.websecurity.util.impl.support.PathMatcher;

/**
 * 通配符 url匹配器<br/>
 * 默认不区分大小写
 * 
 * @author xiegang
 * @since 2011-12-08
 * 
 */
public class AntUrlPathMatcher implements UrlMatcher {
	/** 区分大小写 */
	private boolean requiresLowerCaseUrl;
	
	/** If enabled a method mapped to "/users" also matches to "/users/" */
	private boolean useTrailingSlashMatch;
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	public AntUrlPathMatcher() {
		this(false, true);
	}

	public AntUrlPathMatcher(boolean requiresLowerCaseUrl, boolean useTrailingSlashMatch) {
		this.requiresLowerCaseUrl = requiresLowerCaseUrl;
		this.useTrailingSlashMatch = useTrailingSlashMatch;
	}
	
	@Override
	public Object compile(String path) {
		path = requiresLowerCaseUrl ? path.toLowerCase() : path;

		// useTrailingSlashMatch
		path = (useTrailingSlashMatch && path.length() >= 1 && path.charAt(path.length() - 1) =='/')
				? path.substring(0, path.length() - 1): path;
		return path;
	}

	@Override
	public boolean pathMatchesUrl(Object path, String url) {
		if ("/**".equals(path) || "**".equals(path)) {
			return true;
		}
		
		// useTrailingSlashMatch
		url = (useTrailingSlashMatch && url.length() >= 1 && url.charAt(url.length() - 1) =='/')
				? url.substring(0, url.length() - 1): url;
		return pathMatcher.match((String)path, url);
	}

	@Override
	public String getUniversalMatchPattern() {
		return "/**";
	}

	@Override
	public boolean requiresLowerCaseUrl() {
		return requiresLowerCaseUrl;
	}
	
	public void setRequiresLowerCaseUrl(boolean requiresLowerCaseUrl) {
		this.requiresLowerCaseUrl = requiresLowerCaseUrl;
	}
}
