package my.websecurity.util.impl;

import java.util.regex.Pattern;

import my.websecurity.util.UrlMatcher;


/**
 * 正则表达式url匹配器<br/>
 * 默认区分大小写
 * 
 * @author xiegang
 * @since 2011-12-08
 * 
 */
public class RegexUrlPathMatcher implements UrlMatcher {
	/**
	 * 默认区分大小写
	 */
	private boolean requiresLowerCaseUrl = false;

	public Object compile(String path) {
		return requiresLowerCaseUrl ? Pattern.compile(path, Pattern.CASE_INSENSITIVE) : Pattern.compile(path);
	}

	public void setRequiresLowerCaseUrl(boolean requiresLowerCaseUrl) {
		this.requiresLowerCaseUrl = requiresLowerCaseUrl;
	}

	public boolean pathMatchesUrl(Object compiledPath, String url) {
		Pattern pattern = (Pattern) compiledPath;

		return pattern.matcher(url).matches();
	}

	public String getUniversalMatchPattern() {
		return "/.*";
	}

	public boolean requiresLowerCaseUrl() {
		return requiresLowerCaseUrl;
	}
}