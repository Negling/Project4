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
public class DeleteProductCommand extends Command {
	final static Logger logger = LogManager.getLogger(DeleteProductCommand.class);

	public DeleteProductCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {
			ProductDAO productDAO = DAOfactory.getProductDAO();
			HttpSession session = container.getSession(false);
			List<Product> products;

			/*
			 * if session was not created before than user logged in, therefore
			 * it's not permited to visit this page
			 */
			if (session == null)
				return getKey("errorPath");

			int idToDelete = Integer.parseInt(container.getParameter(getKey("productCoreUpdateId")));
			products = (List<Product>) session.getAttribute(getKey("productsList"));

			// removing product from DB
			if (!productDAO.deleteById(idToDelete)) {
				return getKey("errorPath");
			}

			// updating session repo
			products.removeIf(p -> p.getId().equals(idToDelete));

			return getKey("adminPath");
		} catch (DAOException ex) {
			logger.error("DAO exception due invoking DeleteProductCommand", ex);
			return getKey("errorPath");
		}
	}
}