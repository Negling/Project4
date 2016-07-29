package ua.kiral.project4.model.command.validator;

import java.util.ResourceBundle;

/**
 * 
 * Simple realization of validator interface, all data is checking by regExp.
 */
public class DataValidator extends Validator {

	public DataValidator() {
		this(null);
	}

	public DataValidator(ResourceBundle properties) {
		super(properties);
	}

	@Override
	public boolean validatePasswordType(String value) {
		if (value == null || value.isEmpty())
			return false;
		if (value.length() > Integer.parseInt(getKey("maxPasswordChars")))
			return false;
		if (!value.matches("[A-Za-z0-9_]*"))
			return false;

		return true;
	}

	@Override
	public boolean validateEmailType(String value) {
		if (value == null || value.isEmpty())
			return false;
		if (value.length() > Integer.parseInt(getKey("maxEmailChars")))
			return false;
		if (!value.matches("[A-Za-z0-9_]*@[A-Za-z0-9_]*.[A-Za-z0-9_]{2,10}"))
			return false;

		return true;
	}

	@Override
	public boolean validateLoginType(String value) {
		if (value == null || value.isEmpty())
			return false;
		if (value.length() > Integer.parseInt(getKey("maxLoginChars")))
			return false;
		if (!value.matches("[A-Za-z0-9_]*"))
			return false;

		return true;
	}

	@Override
	public boolean validateNameType(String value) {
		if (value == null || value.isEmpty())
			return false;
		if (value.length() > Integer.parseInt(getKey("maxLoginChars")))
			return false;
		if (!value.matches("[A-Za-z0-9_А-Яа-я]*"))
			return false;

		return true;
	}

	@Override
	public boolean validateCurrencyType(String value) {
		if (value == null || value.isEmpty())
			return false;
		if (value.length() > Integer.parseInt(getKey("maxLoginChars")))
			return false;
		if (!value.matches("([0-9]{1}\\.[0-9]{2})|([1-9]+\\.[0-9]{2})"))
			return false;

		return true;
	}
}