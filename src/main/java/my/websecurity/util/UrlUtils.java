package my.websecurity.util;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Url工具类<br/>
 * 参考Spring Security 源码
 * 
 * @author xiegang
 * @since 2011-12-08
 *
 */
public final class UrlUtils {
	/**
	 * 获取请求的完整请求路径
	 * 
	 * @param req
	 * @return
	 */
	public static String buildFullRequestUrl(HttpServletRequest req, boolean hasQuery) {
		return buildFullRequestUrl(req.getScheme(), req.getServerName(), req.getServerPort(), req.getRequestURI(), req.getContextPath(), req.getServletPath(), req.getPathInfo(), hasQuery? req.getQueryString() : null);
	}

	private static String buildFullRequestUrl(String scheme, String serverName, int serverPort, String requestURI, String contextPath, String servletPath, String pathInfo, String queryString) {
		scheme = scheme.toLowerCase();
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);
		// Only add port if not default
		if ("http".equals(scheme)) {
			if (serverPort != 80) {
				url.append(":").append(serverPort);
			}
		} else if ("https".equals(scheme)) {
			if (serverPort != 443) {
				url.append(":").append(serverPort);
			}
		}
		if(servletPath != null) {
			url.append(contextPath).append(servletPath);
			if (pathInfo != null) {
				url.append(pathInfo);
			}
		} else {
			url.append(requestURI);
		}

		if (queryString != null) {
			url.append("?").append(queryString);
		}
		return url.toString();
	}

	/**
	 * 获取请求的路径，应用名称之后的部分<br/>
	 * 比如：/index.jsp
	 * 
	 * @param req
	 * @return
	 */
	public static String buildRequestUrl(HttpServletRequest req, boolean hasQuery) {
		return buildRequestUrl(req.getServletPath(), req.getPathInfo(), req.getRequestURI(), req.getContextPath(), hasQuery ? req.getQueryString() : null);
	}

	private static String buildRequestUrl(String servletPath, String pathInfo, String requestURI, String contextPath, String queryString) {
		StringBuilder url = new StringBuilder();
		if (servletPath != null) {
			url.append(servletPath);
			if (pathInfo != null) {
				url.append(pathInfo);
			}
		} else {
			url.append(requestURI.substring(contextPath.length()));
		}
		if (queryString != null) {
			url.append("?").append(queryString);
		}
		return url.toString();
	}

	/**
	 * 是否是有效的Redirect地址
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isValidRedirectUrl(String url) {
		return url != null && url.startsWith("/") || isAbsoluteUrl(url);
	}

	/**
	 * 是否是绝对地址
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isAbsoluteUrl(String url) {
		final Pattern ABSOLUTE_URL = Pattern.compile("\\A[a-z.+-]+://.*", Pattern.CASE_INSENSITIVE);
		return ABSOLUTE_URL.matcher(url).matches();
	}

	/**
	 * 转发到指定页面
	 * 
	 * @param request
	 * @param response
	 * @param pageUrl 转发的页面
	 * @param sc response status
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void forward2Page(HttpServletRequest request, HttpServletResponse response, String pageUrl, int sc) throws IOException, ServletException {
		if (!response.isCommitted()) {
			if (pageUrl != null) {
				response.setStatus(sc);
				// forward to page.
				RequestDispatcher dispatcher = request.getRequestDispatcher(pageUrl);
				dispatcher.forward(request, response);
			} else {
				response.sendError(sc, "未找到指定的页面");
			}
		}
	}

	/**
	 * 重定向到指定页面
	 * 
	 * @param request
	 * @param response
	 * @param pageUrl 重定向的页面
	 * @param sc response status
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void redirect2Page(HttpServletRequest request, HttpServletResponse response, String pageUrl, int sc) throws IOException {
		if (!response.isCommitted()) {
			if (pageUrl != null) {
				response.setStatus(sc);
				response.sendRedirect(request.getContextPath() + pageUrl);
			} else {
				response.sendError(sc, "未找到指定的页面");
			}
		}
	}
}
