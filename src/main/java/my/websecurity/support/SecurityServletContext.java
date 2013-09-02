package my.websecurity.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.websecurity.util.UrlUtils;

/**
 * @author xiegang
 * @since 2012-7-3
 *
 */
public class SecurityServletContext {
	/**
	 * http 请求
	 */
	private HttpServletRequest request;
	
	/**
	 * http 响应
	 */
	private HttpServletResponse response;
	
	/**
	 * 请求的路径
	 * 比如：/index.jsp
	 */
	private String requestUrl;
	
	/**
	 * 请求的路径包含queryString
	 * 比如：/index.jsp
	 */
	private String fullRequestUrl;
	
	public SecurityServletContext(HttpServletRequest req) {
		this.request = req;
	}
	
	public SecurityServletContext(HttpServletResponse rep) {
		this.response = rep;
	}
	
	public SecurityServletContext(HttpServletRequest req, HttpServletResponse rep) {
		this.request = req;
		this.response = rep;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * get requestUrl contains queryString
	 * 
	 * @return the requestUrl
	 */
	public String getFullRequestUrl() {
		if(null == fullRequestUrl && null != request) {
			synchronized (this) {
				if(null == fullRequestUrl) {
					this.fullRequestUrl = UrlUtils.buildFullRequestUrl(request, true);
				}
			}
		}
		return fullRequestUrl;
	}
	
	/**
	 * get requestUrl
	 * 
	 * @return the requestUrl
	 */
	public String getRequestUrl() {
		if(null == requestUrl && null != request) {
			synchronized (this) {
				if(null == requestUrl) {
					this.requestUrl = UrlUtils.buildRequestUrl(request, false);
				}
			}
		}
		return requestUrl;
	}
}
