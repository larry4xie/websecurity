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
	/**
	 * 默认区分大小写
	 */
	private boolean requiresLowerCaseUrl = false;
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	public AntUrlPathMatcher() {
        this(false);
    }

    public AntUrlPathMatcher(boolean requiresLowerCaseUrl) {
        this.requiresLowerCaseUrl = requiresLowerCaseUrl;
    }
	
	@Override
	public Object compile(String path) {
		return requiresLowerCaseUrl ? path.toLowerCase() : path;
	}

	@Override
	public boolean pathMatchesUrl(Object path, String url) {
		if ("/**".equals(path) || "**".equals(path)) {
			return true;
		}
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
