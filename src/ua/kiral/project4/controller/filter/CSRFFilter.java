package ua.kiral.project4.controller.filter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.model.util.HashGenerator;

/**
 * This filter Provides CSRF protection. When any request adress to controller,
 * filter checks CSRF tokkens from request and session, if they match - request
 * forwards to next filter. If request tokken is empty, and tokken keyWorld is
 * absent, it generates one and puts to session container.
 *
 */
public class CSRFFilter extends BaseFilter {
	final static Logger logger = LogManager.getLogger(CSRFFilter.class);

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		String requestToken = request.getParameter(filterProperties.getString("token"));
		String sessionToken = (String) session.getAttribute(filterProperties.getString("token"));
		String tokenKey = (String) session.getAttribute(filterProperties.getString("key"));

		/*
		 * if this conditions is true - it means that this is first request, we
		 * need to create tokken, and secret keyWord, wich is actually source of
		 * generated token
		 */
		if (tokenKey == null && (requestToken == null || requestToken.isEmpty())) {
			try {
				tokenKey = HashGenerator.generateKey(10);
				sessionToken = HashGenerator.generateHash(tokenKey, "MD5");

				session.setAttribute(filterProperties.getString("key"), tokenKey);
				session.setAttribute(filterProperties.getString("token"), sessionToken);

				chain.doFilter(request, response);
			} catch (NoSuchAlgorithmException ex) {
				logger.error("Exception in CSRF filter, failed to encode hash", ex);
				response.sendRedirect(filterProperties.getString("errorURI"));
			}
		} else if (requestToken != null && requestToken.equals(sessionToken)) {
			// tokens match, simply continue
			chain.doFilter(request, response);
		} else {
			// In this case, session may be stolen, redirect to error page
			response.sendRedirect(filterProperties.getString("errorURI"));
		}
	}
}