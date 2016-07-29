package ua.kiral.project4.model.command.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validator.Validator;
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.UserDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.User;

/**
 * Provides authorization interface. At first we validate user data, if
 * incorrect - throw back to registration page and display massage about failed
 * validation. Alias check, if user witch suck login have registered before, if
 * true - return and display msg. By passing this brakepoints - record user data
 * to storage and throw login page.
 *
 */
public class RegistrationCommand extends Command {
	final static Logger logger = LogManager.getLogger(RegistrationCommand.class);

	public RegistrationCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {
			UserDAO userDAO = DAOfactory.getUserDAO();
			String login = container.getParameter(getKey("userLogin"));

			// validating data
			if (!validator.validateRegistrationForm(container)) {
				container.setAttribute(getKey("incorrectData"), true);
				return getKey("registrationPath");
			}

			// user with this login is already registered
			if (userDAO.getByLogin(login) != null) {
				container.setAttribute(getKey("registrationFailedMSG"), true);
				return getKey("registrationPath");
			}

			String genderValue = container.getParameter(getKey("userGender"));
			Boolean male;

			// gender value can be null or empty, its equivalent to "other"
			// gender
			if (genderValue == null || genderValue.isEmpty())
				male = null;
			else
				male = Boolean.parseBoolean(genderValue);

			User newUser = fillFieldsToRecord(container, login, male);

			// if registration success - login
			if (userDAO.create(newUser)) {
				return getKey("loginPath");
			} else {
				return getKey("errorPath");
			}
		} catch (DAOException ex) {
			logger.error("DAO exception due invoking RegistrationCommand", ex);
			return getKey("errorPath");
		}
	}
	
	/**
	 * Simply fills fileds and returns User instance.
	 */
	private User fillFieldsToRecord(RequestContainer container, String login, Boolean gender) {
		User newUser = new User();
		newUser.setName(container.getParameter(getKey("userName")));
		newUser.setSurname(container.getParameter(getKey("userSurname")));
		newUser.setLogin(login);
		newUser.setPassword(container.getParameter(getKey("userPass")));
		newUser.setEmail(container.getParameter(getKey("userEmail")));
		newUser.setMale(gender);
		newUser.setBlocked(false);

		return newUser;
	}
}