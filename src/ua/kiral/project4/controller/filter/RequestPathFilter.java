package ua.kiral.project4.controller.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Filter defines request path, and if it match to allowed path - passes
 * next, otherwise redirects to error page.
 */
public class RequestPathFilter extends BaseFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String requestedPath = request.getRequestURI();

		// if request is bounded to specified resource-file pattern, pass next
		if (requestedPath.matches(filterProperties.getString("css")) 
				|| requestedPath.matches(filterProperties.getString("image"))
				|| requestedPath.matches(filterProperties.getString("tagDescriptor"))) {
			
			chain.doFilter(request, response);
		} else {

			// if request aint resource, but match to allowed path - pass next
			String[] correctPath = filterProperties.getString("corectPath").split(",");

			for (String pathValue : correctPath) {
				if (requestedPath.equals(pathValue)) {
					chain.doFilter(request, response);
					return;
				}
			}
			// otherwise - throw error page
			response.sendRedirect(filterProperties.getString("errorURI"));
		}
	}
}