package ua.kiral.project4.model.command.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validation.Validator;
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.UserDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.User;

/**
 * This command provides interface to change users legality status from admin
 * managment page.
 *
 */
public class BlackListManagmentCommand extends Command {
	final static Logger logger = LogManager.getLogger(BlackListManagmentCommand.class);

	public BlackListManagmentCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {// initializing data
			HttpSession session = container.getSession(false);
			UserDAO userDao = DAOfactory.getUserDAO();
			String banLogin = container.getParameter(getKey("banLogin"));
			String unbanLogin = container.getParameter(getKey("unbanLogin"));
			User currentUser = userDao.getByLogin(banLogin == null ? unbanLogin : banLogin);
			boolean status = banLogin == null ? false : true;
			List<User> users;

			/*
			 * if session was not created before than user logged in, therefore
			 * it's not permited to visit this page
			 */
			if (session == null)
				return getKey("errorPath");

			users = (List<User>) session.getAttribute(getKey("usersList"));

			/*
			 * if operation is success - return to managmatn page
			 */
			if (updateStatus(userDao, currentUser, users, status))
				return getKey("adminPath");
			else
				return getKey("errorPath");
			
		} catch (DAOException ex) {
			logger.error("DAO exception due invoking BlackListManagmentCommand", ex);
			return getKey("errorPath");
		}
	}

	/**
	 * This method performs status update in database and session storage.
	 * 
	 * @param dao
	 *            - dao interface to perform update operation
	 * @param instance
	 *            - user instance
	 * @param users
	 *            - session users storage, by this storage displays users info
	 *            at managment page
	 * @param statusValue
	 *            - new user status, true - banned, false - unbanned
	 * @return true in case of success, false othrwise
	 * @throws DAOException
	 */
	private boolean updateStatus(UserDAO dao, User instance, List<User> users, boolean statusValue)
			throws DAOException {
		// prepearing user instance to DB update method
		instance.setBlocked(statusValue);

		// updating user status in database
		if (dao.update(instance, instance)) {

			// updating user status in session storage
			users.forEach(u -> {
				if (u.getLogin().equals(instance.getLogin())) {
					u.setBlocked(statusValue);
				}
			});

			return true;
		} else
			return false;
	}
}