package ua.kiral.project4.model.command.impl;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validator.Validator;
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.ProductDAO;
import ua.kiral.project4.model.dao.UserDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Bucket;
import ua.kiral.project4.model.entity.Product;

/**
 * This command provides interface to remove product instance from user bucket.
 * Bucket stored in session container. When invoked - simply decrements product
 * amount in bucket, or removes if product amount equals to 1.
 *
 */
public class RemoveFromBucketCommand extends Command {
	final static Logger logger = LogManager.getLogger(RemoveFromBucketCommand.class);

	public RemoveFromBucketCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {
			HttpSession session = container.getSession(false);
			UserDAO userDAO = DAOfactory.getUserDAO();
			ProductDAO productDAO = DAOfactory.getProductDAO();

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

			Product productToRemove = productDAO
					.getProductByID(Integer.parseInt(container.getParameter(getKey("productToPurchaseId"))));
			Bucket bucket = (Bucket) session.getAttribute(getKey("purchaseBucket"));

			/*
			 * Product to remove or bucket can't be null
			 */
			if (productToRemove == null || bucket == null)
				return getKey("errorPath");

			/*
			 * Remove product and back to content
			 */
			bucket.decrement(productToRemove);
			return getKey("contentPath");
		} catch (DAOException ex) {
			logger.error("DAO exception due invoking RemoveFromBucketCommand", ex);
			return getKey("errorPath");
		}
	}
}