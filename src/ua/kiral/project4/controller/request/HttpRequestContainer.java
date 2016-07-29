package ua.kiral.project4.controller.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Simple realization of proxy pattern. All requests are delegated to
 * HttPServletRequest instance.
 *
 */
public class HttpRequestContainer implements RequestContainer {
	private HttpServletRequest request;

	public HttpRequestContainer(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getParameter(String key) {
		return request.getParameter(key);
	}

	@Override
	public HttpSession getSession(boolean flag) {
		return request.getSession(flag);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		request.setAttribute(arg0, arg1);
	}

	@Override
	public Object getAttribute(String arg1) {
		return request.getAttribute(arg1);
	}
}