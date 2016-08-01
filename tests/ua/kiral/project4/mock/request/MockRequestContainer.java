package ua.kiral.project4.mock.request;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import ua.kiral.project4.controller.request.RequestContainer;

/**
 * Simple managable request wrapper, required for commands-case tests. We can
 * manualy add and remove options to session attributes, request attributes and
 * request params.
 *
 */
@SuppressWarnings("deprecation")
public class MockRequestContainer implements RequestContainer {
	private MockSession session;
	private boolean nullSession;
	private Map<String, String> requestParams;
	private Map<String, Object> requestAttributes;

	public MockRequestContainer(Map<String, String> requestParams, Map<String, Object> sessionParams,
			Map<String, Object> requestAttributes) {
		this.requestAttributes = requestAttributes;
		this.requestParams = requestParams;
		this.session = new MockSession(sessionParams);
	}

	@Override
	public String getParameter(String key) {
		return requestParams.get(key);
	}

	@Override
	public HttpSession getSession(boolean flag) {
		if (!nullSession)
			return session;
		else
			return null;
	}

	public HttpSession getSession() {
		return getSession(true);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		requestAttributes.put(arg0, arg1);
	}

	public void setParameter(String arg0, String arg1) {
		if (requestParams.containsKey(arg0))
			requestParams.replace(arg0, arg1);
		else
			requestParams.put(arg0, arg1);
	}

	@Override
	public Object getAttribute(String arg1) {
		return requestAttributes.get(arg1);
	}

	/**
	 * If flag set to true, method getSession always will return null.
	 * 
	 * @param flag
	 */
	public void setNullSession(boolean flag) {
		this.nullSession = flag;
	}

	/**
	 * Clears up all session attributes.
	 */
	public void clearSessionAttributes() {
		session.sessionParams.clear();
	}

	/**
	 * Clears up all container params.
	 */
	public void clearContainerParams() {
		requestParams.clear();
	}

	/**
	 * Clears up all container attributes.
	 */
	public void clearContainerAttributes() {
		requestAttributes.clear();
	}

	/**
	 * Session fake implementation, most of methods does nothing, realized just
	 * methods required for tests.
	 *
	 */
	public static class MockSession implements HttpSession {
		private Map<String, Object> sessionParams;

		public MockSession(Map<String, Object> sessionParams) {
			this.sessionParams = sessionParams;
		}

		@Override
		public Object getAttribute(String arg0) {
			return sessionParams.get(arg0);
		}

		@Override
		public Enumeration<String> getAttributeNames() {
			return new Enumeration<String>() {
				Iterator<String> iterator = sessionParams.keySet().iterator();

				@Override
				public boolean hasMoreElements() {
					return iterator.hasNext();
				}

				@Override
				public String nextElement() {
					return iterator.next();
				}
			};
		}

		@Override
		public void removeAttribute(String arg0) {
			sessionParams.remove(arg0);
		}

		@Override
		public void setAttribute(String arg0, Object arg1) {
			if (sessionParams.containsKey(arg0))
				sessionParams.replace(arg0, arg1);
			else
				sessionParams.put(arg0, arg1);
		}

		@Override
		public long getCreationTime() {
			// Does nothing
			return 0;
		}

		@Override
		public String getId() {
			// Does nothing
			return null;
		}

		@Override
		public long getLastAccessedTime() {
			// Does nothing
			return 0;
		}

		@Override
		public int getMaxInactiveInterval() {
			// Does nothing
			return 0;
		}

		@Override
		public ServletContext getServletContext() {
			// Does nothing
			return null;
		}

		@Override
		public HttpSessionContext getSessionContext() {
			// Does nothing
			return null;
		}

		@Override
		public Object getValue(String arg0) {
			// Does nothing
			return null;
		}

		@Override
		public String[] getValueNames() {
			// Does nothing
			return null;
		}

		@Override
		public void invalidate() {
			// Does nothing
		}

		@Override
		public boolean isNew() {
			// Does nothing
			return false;
		}

		@Override
		public void putValue(String arg0, Object arg1) {
			// Does nothing
		}

		@Override
		public void removeValue(String arg0) {
			// Does nothing
		}

		@Override
		public void setMaxInactiveInterval(int arg0) {
			// Does nothing
		}
	}
}