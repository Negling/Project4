package ua.kiral.project4.model.command.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validation.Validator;
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.OrderDAO;
import ua.kiral.project4.model.dao.OrderDetailsDAO;
import ua.kiral.project4.model.dao.ProductDAO;
import ua.kiral.project4.model.dao.UserDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Order;
import ua.kiral.project4.model.entity.OrderDetails;
import ua.kiral.project4.model.entity.Product;

/**
 * This command reads user purchases history, ands passes this information to
 * session storage, then passes user to orders page.
 *
 */
public class ShowOrdersCommand extends Command {
	final static Logger logger = LogManager.getLogger(RegistrationCommand.class);

	public ShowOrdersCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try { // init params
			OrderDAO orderDAO = DAOfactory.getOrderDAO();
			OrderDetailsDAO detailsDAO = DAOfactory.getOrderDetailsDAO();
			UserDAO userDAO = DAOfactory.getUserDAO();
			ProductDAO productDAO = DAOfactory.getProductDAO();
			HttpSession session = container.getSession(false);

			/*
			 * if session was not created before than user logged in, therefore
			 * it's not permited to visit this page
			 */
			if (session == null)
				return getKey("errorPath");

			int userId = (Integer) session.getAttribute(getKey("userId"));

			/*
			 * If user get blocked during session - invalidate
			 */
			if (userDAO.getById(userId).getBlocked()) {
				session.invalidate();
				return getKey("mainPage");
			}

			List<Order> orders = orderDAO.getAllByOwner(userId);

			/*
			 * User has no orders in history
			 */
			if (orders == null || orders.isEmpty()) {
				container.setAttribute(getKey("emptyOrderList"), true);
				return getKey("ordersPath");
			}

			Map<Order, OrderDetails> details = new HashMap<>();
			Map<Order, List<Product>> productDetails = new HashMap<>();

			// Every order has its details
			for (Order order : orders) {
				details.put(order, detailsDAO.getByOrderId(order.getOrderId()));
				productDetails.put(order, productDAO.getProductsFromOrder(order));
			}

			session.setAttribute(getKey("ordersList"), orders);
			session.setAttribute(getKey("orderDetailsList"), details);
			session.setAttribute(getKey("productDetailsList"), productDetails);
			return getKey("ordersPath");

		} catch (DAOException ex) {
			logger.error("DAO exception due invoking ShowOrdersCommand", ex);
			return getKey("errorPath");
		}
	}
}