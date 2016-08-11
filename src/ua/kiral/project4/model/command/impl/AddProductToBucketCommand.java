package ua.kiral.project4.model.command.impl;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validation.Validator;
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.ProductDAO;
import ua.kiral.project4.model.dao.UserDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Bucket;
import ua.kiral.project4.model.entity.Product;

/**
 * This command provides interface to add product to user bucket. Bucket stored
 * in session container. When invoked - increments product amount by one, or
 * simply puts product to bucket, if there was no such one.
 *
 */
public class AddProductToBucketCommand extends Command {
	final static Logger logger = LogManager.getLogger(AddProductToBucketCommand.class);

	public AddProductToBucketCommand(DAOFactory factory, Validator validator) {
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

			Product productToAdd = productDAO
					.getProductByID(Integer.parseInt(container.getParameter(getKey("productToPurchaseId"))));
			Bucket bucket = (Bucket) session.getAttribute(getKey("purchaseBucket"));

			/*
			 * Product to add or bucket can't be null
			 */
			if (productToAdd == null || bucket == null)
				return getKey("errorPath");

			/*
			 * add product and back to content
			 */
			if (!bucket.increment(productToAdd)) {
				bucket.putProduct(productToAdd, 1);
				return getKey("contentPath");
			} else
				return getKey("contentPath");
		} catch (DAOException ex) {
			logger.error("DAO exception due invoking AddProductToBucketCommand", ex);
			return getKey("errorPath");
		}
	}
}