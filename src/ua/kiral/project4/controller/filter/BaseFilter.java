package ua.kiral.project4.controller.filter;

import java.io.IOException;

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

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
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
}