package ua.kiral.project4.model.command.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

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
 * Provides interface to update specified user state.
 *
 */
public class UpdateUserCommand extends Command {
	final static Logger logger = LogManager.getLogger(UpdateUserCommand.class);

	public UpdateUserCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {
			HttpSession session = container.getSession(false);
			UserDAO userDAO = DAOfactory.getUserDAO();
			List<User> users;
			User oldValue;
			User updated;

			/*
			 * if session was not created before than user logged in, therefore
			 * it's not permited to visit this page
			 */
			if (session == null)
				return getKey("errorPath");

			oldValue = userDAO.getByLogin(container.getParameter(getKey("userCoreUpdateLogin")));
			users = (List<User>) session.getAttribute(getKey("usersList"));

			// users can't be empty or null
			if (users == null || users.isEmpty())
				return getKey("errorPath");

			// if there no such user - return error
			if (oldValue == null)
				return getKey("errorPath");
			else {
				updated = fillFieldsToUpdate(container, oldValue);
				
				// updating user
				if (userDAO.update(oldValue, updated)) {

					// updating users repo
					users.removeIf(p -> p.getLogin().equals(oldValue.getLogin()));
					users.add(updated);

					return getKey("adminPath");
				} else
					return getKey("errorPath");

			}

		} catch (DAOException ex) {
			logger.error("DAO exception due invoking UpdateUserCommand", ex);
			return getKey("errorPath");
		}
	}
	
	/**
	 * Simply fills fileds and returns User instance.
	 */
	private User fillFieldsToUpdate(RequestContainer container, User oldValue) {
		User updated = new User();
		updated.setName(container.getParameter(getKey("userToUpdateName")));
		updated.setSurname(container.getParameter(getKey("userToUpdateSurname")));
		updated.setLogin(oldValue.getLogin());
		updated.setPassword(oldValue.getPassword());
		updated.setEmail(container.getParameter(getKey("userToUpdateEmail")));
		updated.setMale(Boolean.parseBoolean(container.getParameter(getKey("userToUpdateGender"))));
		updated.setBlocked(Boolean.parseBoolean(container.getParameter(getKey("userToUpdateStatus"))));

		return updated;
	}
}