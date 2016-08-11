package ua.kiral.project4.mock.validator;

import java.util.ResourceBundle;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.validation.Validator;

/**
 * Simple giglet. Returns true in all cases.
 *
 */
public class MockValidator extends Validator {

	public MockValidator() {
		this(null);
	}

	public MockValidator(ResourceBundle properties) {
		super(null);
	}

	@Override
	public boolean validateRegistrationForm(RequestContainer container) {
		return true;
	}

	@Override
	public boolean validateLoginForm(RequestContainer container) {
		return true;
	}

	@Override
	public boolean validatePasswordType(String value) {
		return true;
	}

	@Override
	public boolean validateEmailType(String value) {
		return true;
	}

	@Override
	public boolean validateNameType(String value) {
		return true;
	}

	@Override
	public boolean validateLoginType(String value) {
		return true;
	}

	@Override
	public boolean validateCurrencyType(String value) {
		return true;
	}
}