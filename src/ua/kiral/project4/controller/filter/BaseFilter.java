package ua.kiral.project4.controller.filter;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstratct implementation of Filter interface. Motivation - override unused
 * methods with emty body and cast doFilter params to Http-values, cuz we use
 * Http realization only.
 *
 */
public abstract class BaseFilter implements Filter {
	private ResourceBundle filterProperties;
	private FilterConfig filterConfig;

	@Override
	public final void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.filterProperties = ResourceBundle.getBundle("resources/filter");
		init();
	}

	/**
	 * If you need to initialize some filter params before it starts, use this
	 * method instead of overriding "init(FilterConfig)" from Filter interface,
	 * and it will be called in init(FilterConfig) method. To get init params
	 * use getFilterConfig() method.
	 */
	public void init() {
		// Does nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException;

	@Override
	public void destroy() {
		// Does nothing
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	protected String getKey(String value) {
		return filterProperties.getString(value);
	}
}