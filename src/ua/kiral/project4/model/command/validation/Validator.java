package ua.kiral.project4.model.command.validation;

import java.util.ResourceBundle;

import ua.kiral.project4.controller.request.RequestContainer;

/**
 * This class is responsible for verification of user data passed from request
 * fields like passwords, emails, etc.
 *
 */
public abstract class Validator {
	private ResourceBundle properties;

	public Validator(ResourceBundle properties) {
		this.properties = properties;
	}

	public ResourceBundle getProperties() {
		return properties;
	}

	public void setProperties(ResourceBundle properties) {
		this.properties = properties;
	}

	/**
	 * Method for validating login form.
	 * 
	 * @param container
	 * @return
	 */
	public boolean validateLoginForm(RequestContainer container) {
		if (!validateLoginType(container.getParameter(getKey("userLogin"))))
			return false;
		if (!validatePasswordType(container.getParameter(getKey("userPass"))))
			return false;

		return true;
	}

	/**
	 * Mathod for validating registration form.
	 * 
	 * @param container
	 * @return
	 */
	public boolean validateRegistrationForm(RequestContainer container) {
		String password = container.getParameter(getKey("userPass"));
		String rePassword = container.getParameter(getKey("rePass"));
		String email = container.getParameter(getKey("userEmail"));
		String reEmail = container.getParameter(getKey("reEmail"));

		if (!validateNameType(container.getParameter(getKey("userName"))))
			return false;
		if (!validateNameType(container.getParameter(getKey("userSurname"))))
			return false;
		if (!validateLoginType(container.getParameter(getKey("userLogin"))))
			return false;
		if (!validatePasswordType(container.getParameter(getKey("userPass"))))
			return false;
		if (!validatePasswordType(rePassword))
			return false;
		if (!password.equals(rePassword))
			return false;
		if (!validateEmailType(email))
			return false;
		if (!validateEmailType(reEmail))
			return false;
		if (!email.equals(reEmail))
			return false;

		return true;
	}

	/**
	 * Method for validating password-like fields.
	 * 
	 * @param value
	 * @return
	 */
	public abstract boolean validatePasswordType(String value);

	/**
	 * Method for validating email-like fields.
	 * 
	 * @param value
	 * @return
	 */
	public abstract boolean validateEmailType(String value);

	/**
	 * Method for validating name-like fields.
	 * 
	 * @param value
	 * @return
	 */
	public abstract boolean validateNameType(String value);

	/**
	 * Method for validating login-like fields.
	 * 
	 * @param value
	 * @return
	 */
	public abstract boolean validateLoginType(String value);

	/**
	 * Method for validating currency-like fields.
	 * 
	 * @param value
	 * @return
	 */
	public abstract boolean validateCurrencyType(String value);

	/**
	 * Returns value which mapped to specific commands key properties.
	 * 
	 * @param value
	 * @return
	 */
	protected String getKey(String value) {
		return properties.getString(value);
	}
}