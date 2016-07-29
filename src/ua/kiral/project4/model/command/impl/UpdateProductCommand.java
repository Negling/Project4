package ua.kiral.project4.model.command.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validator.Validator;
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.ProductDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Product;
/**
 * Provides interface to update specified product state.
 *
 */
public class UpdateProductCommand extends Command {
	final static Logger logger = LogManager.getLogger(UpdateProductCommand.class);

	public UpdateProductCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		try {
			HttpSession session = container.getSession(false);
			ProductDAO productDAO = DAOfactory.getProductDAO();
			List<Product> products;
			Product oldValue;
			Product updated;

			/*
			 * if session was not created before than user logged in, therefore
			 * it's not permited to visit this page
			 */
			if (session == null)
				return getKey("errorPath");

			products = (List<Product>) session.getAttribute(getKey("productsList"));
			
			// products list cant be null or empty
			if (products == null || products.isEmpty())
				return getKey("errorPath");
			
			oldValue = productDAO
					.getProductByID(Integer.parseInt(container.getParameter(getKey("productCoreUpdateId"))));

			// if there no such product - return error
			if (oldValue == null)
				return getKey("errorPath");
			else {

				BigDecimal newPrice = new BigDecimal(container.getParameter(getKey("productToUpdatePrice")))
						.setScale(2,RoundingMode.HALF_EVEN);
				updated = new Product(oldValue.getId(), container.getParameter(getKey("productToUpdateName")),
						newPrice);
				
				// updating product
				if (productDAO.update(oldValue, updated)) {
					
					// updating products repo
					products.removeIf(p -> p.getId().equals(oldValue.getId()));
					products.add(updated);

					return getKey("adminPath");
				} else
					return getKey("errorPath");
			}
		} catch (DAOException ex) {
			logger.error("DAO exception due invoking UpdateProductCommand", ex);
			return getKey("errorPath");
		}
	}
}