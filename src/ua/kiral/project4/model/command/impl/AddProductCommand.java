package ua.kiral.project4.model.command.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validation.Validator;
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.ProductDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Product;

/**
 * This command provides interface to add new product to database.
 *
 */
public class AddProductCommand extends Command {
	final static Logger logger = LogManager.getLogger(AddProductCommand.class);

	public AddProductCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {
			ProductDAO productDAO = DAOfactory.getProductDAO();
			HttpSession session = container.getSession(false);
			String productName = container.getParameter(getKey("productToUpdateName"));
			String productPrice = container.getParameter(getKey("productToUpdatePrice"));
			List<Product> products;

			/*
			 * if session was not created before than user logged in, therefore
			 * it's not permited to visit this page
			 */
			if (session == null)
				return getKey("errorPath");

			// incorrect product params, pass back, set message
			if (!validator.validateNameType(productName) || !validator.validateCurrencyType(productPrice)) {
				container.setAttribute(getKey("productManagmentFailedMSG"), true);
				return getKey("adminPath");
			}

			products = (List<Product>) session.getAttribute(getKey("productsList"));

			BigDecimal newPrice = new BigDecimal(productPrice).setScale(2, RoundingMode.HALF_EVEN);

			// creating instance of new product
			Product updated = new Product(null, productName, newPrice, false);

			if (productDAO.create(updated)) {

				// updating products repo
				updated.setId(productDAO.getProductId(updated));
				products.add(updated);

				return getKey("adminPath");
			} else
				return getKey("errorPath");
		} catch (DAOException ex) {
			logger.error("DAO exception due invoking AddProductCommand", ex);
			return getKey("errorPath");
		}
	}
}