package ua.kiral.project4.model.command.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validation.Validator;
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.OrderDAO;
import ua.kiral.project4.model.dao.ProductDAO;
import ua.kiral.project4.model.dao.UserDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Bucket;
import ua.kiral.project4.model.entity.Order;
import ua.kiral.project4.model.entity.Product;
import ua.kiral.project4.model.entity.User;

/**
 * This command provides authorization. First of all we check if user is already
 * has been authorized, if true - simply refresh content and pass to content
 * page. Otherwise - validate user data, if ok, check user managment rights, if
 * this is manager - fill manager containers, and pass to managment page,
 * otherwise fill user containers and pass to contant page. At last set user
 * online marker.
 *
 */
public class LoginCommand extends Command {
	final static Logger logger = LogManager.getLogger(LoginCommand.class);

	public LoginCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {// initializing data
			HttpSession session = container.getSession(true);
			Boolean online = (Boolean) session.getAttribute(getKey("userOnline"));
			ProductDAO productDAO = DAOfactory.getProductDAO();
			UserDAO userDAO = DAOfactory.getUserDAO();
			OrderDAO orderDAO = DAOfactory.getOrderDAO();
			List<Product> products = productDAO.getAllByStatus(false);
			String login = container.getParameter(getKey("userLogin"));
			String password = container.getParameter(getKey("userPass"));

			/*
			 * if corrent user is already logged in - return content page
			 */
			if (online != null) {
				if (session.getAttribute(getKey("managerStatus")) != null) {

					prepareManagerPage(container, session, userDAO.getCustomersOnly(), products,
							orderDAO.getAllByStatus(getKey("newOrderStatus")));

					return getKey("adminPath");
				} else {
					prepareUserPage(session, products);
					return getKey("contentPath");
				}
			}

			// if sent data is incorrect - return login page with msg
			if (!validator.validateLoginForm(container)) {
				container.setAttribute(getKey("loginFailedMSD"), true);
				return getKey("loginPath");
			}

			User currentUser = userDAO.getByLogin(login);

			if (currentUser != null) { // current user exist

				/*
				 * if user typed his password correctly, and he ain'n in
				 * blacklist - proseed
				 */
				if (currentUser.getPassword().equals(password) && !currentUser.getBlocked()) {

					// if user is OK - put required data into session
					session.setAttribute(getKey("userLogin"), currentUser.getLogin());
					session.setAttribute(getKey("userId"), currentUser.getUserId());

					/*
					 * here we figuring out, if our user is manager, or regular
					 * guy
					 */
					if (currentUser.getManager()) {

						// if manager, prepear data for admin page
						prepareManagerPage(container, session, userDAO.getCustomersOnly(), products,
								orderDAO.getAllByStatus(getKey("newOrderStatus")));
						session.setAttribute(getKey("managerStatus"), true);

						return getKey("adminPath");
					} else {

						// otherwise prepear data for user
						prepareUserPage(session, products);
						session.setAttribute(getKey("purchaseBucket"), new Bucket());
						return getKey("contentPath");
					}

					// if user is in blacklist - return login page with messedge
				} else if (currentUser.getBlocked()) {
					container.setAttribute(getKey("blackListMSG"), true);
					return getKey("loginPath");
				}
			}
			// data was inputed incorrect, return login page
			container.setAttribute(getKey("loginFailedMSD"), true);
			return getKey("loginPath");
		} catch (DAOException ex) {
			logger.error("DAO exception due invoking LoginCommand", ex);
			return getKey("errorPath");
		}
	}

	/**
	 * Same as regular user, but has additional containers for customers info.
	 * 
	 * @param session
	 * @param users
	 * @param products
	 */
	private void prepareManagerPage(RequestContainer container, HttpSession session, List<User> users,
			List<Product> products, List<Order> orders) {
		prepareUserPage(session, products);
		session.setAttribute(getKey("usersList"), users);

		if (orders == null || orders.isEmpty())
			session.setAttribute(getKey("emptyOrderList"), true);
		else
			session.setAttribute(getKey("ordersList"), orders);
	}

	/**
	 * Puts to session storage products data, required to contetn page, and
	 * marks that user is authorized.
	 * 
	 * @param session
	 * @param products
	 */
	private void prepareUserPage(HttpSession session, List<Product> products) {
		session.setAttribute(getKey("userOnline"), true);
		session.setAttribute(getKey("productsList"), products);
	}
}