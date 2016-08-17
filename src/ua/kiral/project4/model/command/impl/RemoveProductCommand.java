package ua.kiral.project4.model.command.impl;

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
 * This command removes selected product from database, and refresh session
 * products storage.
 *
 */
public class RemoveProductCommand extends Command {
	final static Logger logger = LogManager.getLogger(RemoveProductCommand.class);

	public RemoveProductCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {
			ProductDAO productDAO = DAOfactory.getProductDAO();
			HttpSession session = container.getSession(false);
			List<Product> products;
			Product productToRemove;

			/*
			 * if session was not created before than user logged in, therefore
			 * it's not permited to visit this page
			 */
			if (session == null)
				return getKey("errorPath");

			int idToRemove = Integer.parseInt(container.getParameter(getKey("productCoreUpdateId")));
			products = (List<Product>) session.getAttribute(getKey("productsList"));
			productToRemove = productDAO.getProductByID(idToRemove);

			// product to remove can't be null!
			if (productToRemove == null) {
				return getKey("errorPath");
			}
			
			// updating product status
			productToRemove.setRemoved(true);

			// updating product status in DB
			if (!productDAO.update(productToRemove, productToRemove)) {
				return getKey("errorPath");
			}

			// updating session repo
			products.removeIf(p -> p.getId().equals(idToRemove));

			return getKey("adminPath");
		} catch (DAOException ex) {
			logger.error("DAO exception due invoking RemoveProductCommand", ex);
			return getKey("errorPath");
		}
	}
}