package ua.kiral.project4.controller.request;

import javax.servlet.http.HttpSession;

/**
 * This interface serves as shell for request, to grant access to only methods
 * we need and as gasket between request and logic layers.
 *
 */
public interface RequestContainer {

	/**
	 * Returns String-type parameter from request.
	 * 
	 * @param key
	 * @return
	 */
	public String getParameter(String key);

	/**
	 * Returns session instance.
	 * 
	 * @param flag
	 * @return
	 */
	public HttpSession getSession(boolean flag);

	/**
	 * Sets attribute to request container.
	 * 
	 * @param arg0
	 *            - key
	 * @param arg1
	 *            - value wich bound to specific key
	 */
	public void setAttribute(String arg0, Object arg1);

	/**
	 * Returns attrubute bonded to key, or null if there was no such key-value
	 * pair.
	 * 
	 * @param arg1
	 * @return
	 */
	public Object getAttribute(String arg1);
}