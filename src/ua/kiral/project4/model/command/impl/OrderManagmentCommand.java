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
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Order;

/**
 * This command provides interface to manage new orders, anmin may cinfirm new
 * order, or reject.
 *
 */
public class OrderManagmentCommand extends Command {
	final static Logger logger = LogManager.getLogger(OrderManagmentCommand.class);

	public OrderManagmentCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {
			HttpSession session = container.getSession(false);
			OrderDAO orderDAO = DAOfactory.getOrderDAO();
			String confirm = container.getParameter(getKey("confirm"));
			String reject = container.getParameter(getKey("reject"));
			Integer orderId = confirm == null ? Integer.parseInt(reject) : Integer.parseInt(confirm);
			String newStatus = confirm == null ? getKey("rejectedOrderStatus") : getKey("confirmedOrderStatus");
			Order order = orderDAO.getOrderByID(orderId);
			List<Order> orders;

			/*
			 * if session was not created before than user logged in, therefore
			 * it's not permited to visit this page
			 */
			if (session == null)
				return getKey("errorPath");

			orders = (List<Order>) session.getAttribute(getKey("ordersList"));
			order.setStatus(newStatus);
			
			// updating status of order
			if (orderDAO.update(order, order)) {
				
				// updating session orders repo
				orders.removeIf(o -> o.getOrderId().equals(orderId));

				if (orders.isEmpty())
					container.setAttribute(getKey("emptyOrderList"), true);

				return getKey("adminPath");
			} else
				return getKey("errorPath");

		} catch (DAOException ex) {
			logger.error("DAO exception due invoking OrderManagmentCommand", ex);
			return getKey("errorPath");
		}
	}
}