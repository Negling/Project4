package ua.kiral.project4.model.command;

import java.util.ResourceBundle;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.validator.Validator;
import ua.kiral.project4.model.dao.DAOFactory;

/**
 * Defines overall command interface. For each implementation gratned acces to
 * commands properties file by invoking "getKey" method. Each realization must
 * be specified by DAO layer realization and Validator.
 *
 */
public abstract class Command {
	/**
	 * Resource bundle stores all commands-string shortcuts.
	 */
	private ResourceBundle properties = ResourceBundle.getBundle("resources/command/commands");
	protected DAOFactory DAOfactory;
	protected Validator validator;

	protected Command(DAOFactory factory, Validator validator) {
		this.validator = validator;
		this.validator.setProperties(properties);
		this.DAOfactory = factory;
	}

	public DAOFactory getDAOfactory() {
		return DAOfactory;
	}

	public void setDAOfactory(DAOFactory dAOfactory) {
		DAOfactory = dAOfactory;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	/**
	 * Main method to process request.
	 * 
	 * @param container
	 * @return
	 */
	public abstract String execute(RequestContainer container);

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