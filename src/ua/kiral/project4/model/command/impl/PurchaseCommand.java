package ua.kiral.project4.model.command.impl;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validation.Validator;
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.OrderDAO;
import ua.kiral.project4.model.dao.OrderDetailsDAO;
import ua.kiral.project4.model.dao.UserDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Bucket;
import ua.kiral.project4.model.entity.Order;
import ua.kiral.project4.model.entity.OrderDetails;

/**
 * This interface provides products purchasing operation. First of all we check
 * session status and user blacklist status, if he pass - we record his bucket
 * as order and its details, than clearing bucket and return back to content
 * page.
 *
 */
public class PurchaseCommand extends Command {
	final static Logger logger = LogManager.getLogger(PurchaseCommand.class);

	public PurchaseCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {// init data
			HttpSession session = container.getSession(false);
			OrderDAO orderDAO = DAOfactory.getOrderDAO();
			OrderDetailsDAO detailsDAO = DAOfactory.getOrderDetailsDAO();
			UserDAO userDAO = DAOfactory.getUserDAO();

			/*
			 * if session was not created before than user logged in, therefore
			 * it's not permited to visit this page
			 */
			if (session == null)
				return getKey("errorPath");

			/*
			 * If user get blocked during session - invalidate
			 */
			if (userDAO.getById((Integer) session.getAttribute(getKey("userId"))).getBlocked()) {
				session.invalidate();

				return getKey("mainPage");
			}

			Bucket bucket = (Bucket) session.getAttribute(getKey("purchaseBucket"));

			// can't continue at this point, no products in bucket
			if (bucket.isEmpty())
				return getKey("contentPath");

			Order newOrder = new Order(null, (Integer) session.getAttribute(getKey("userId")),
					new Date(System.currentTimeMillis()), getKey("newOrderStatus"), bucket.getTotalCost());

			// recording purchase data
			if (orderDAO.create(newOrder)
					&& detailsDAO.create(new OrderDetails(orderDAO.getOrderId(newOrder), bucket.getBucket()))) {
				// success, may countinue shopping
				bucket.clear();
				container.setAttribute(getKey("tnxMSG"), true);

				return getKey("contentPath");
			} else {
				
				/*
				 * failed, delete new order record if exist and redirect to
				 * error
				 */
				if (orderDAO.getOrderId(newOrder) != -1)
					orderDAO.delete(newOrder);

				return getKey("errorPath");
			}
		} catch (DAOException ex) {
			logger.error("DAO exception due invoking PurchaseCommand", ex);
			return getKey("errorPath");
		}
	}
}