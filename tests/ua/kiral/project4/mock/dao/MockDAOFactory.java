package ua.kiral.project4.mock.dao;

import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.OrderDAO;
import ua.kiral.project4.model.dao.OrderDetailsDAO;
import ua.kiral.project4.model.dao.ProductDAO;
import ua.kiral.project4.model.dao.UserDAO;

/**
 * Simple mock factory, copies {@link MySQLDAOFactory} realization.
 *
 */
public class MockDAOFactory implements DAOFactory {
	private static MockDAOFactory factoryInstance;
	private MockUserDAO userInstance;
	private MockProductDAO productInstance;
	private MockOrderDAO orderInstance;
	private MockOrderDetailsDAO detailsInstance;

	private MockDAOFactory() {
	}

	/**
	 * Returns singleton instance of factory.
	 * 
	 * @return
	 */
	public static DAOFactory getInstance() {
		if (factoryInstance == null) {
			synchronized (MockDAOFactory.class) {
				if (factoryInstance == null)
					factoryInstance = new MockDAOFactory();
			}
		}
		return factoryInstance;
	}

	@Override
	public UserDAO getUserDAO() {
		if (userInstance == null) {
			synchronized (this) {
				if (userInstance == null)
					userInstance = new MockUserDAO();
			}
		}
		return userInstance;
	}

	@Override
	public ProductDAO getProductDAO() {
		if (productInstance == null) {
			synchronized (this) {
				if (productInstance == null)
					productInstance = new MockProductDAO();
			}
		}
		return productInstance;
	}

	@Override
	public OrderDAO getOrderDAO() {
		if (orderInstance == null) {
			synchronized (this) {
				if (orderInstance == null)
					orderInstance = new MockOrderDAO();
			}
		}
		return orderInstance;
	}

	@Override
	public OrderDetailsDAO getOrderDetailsDAO() {
		if (detailsInstance == null) {
			synchronized (this) {
				if (detailsInstance == null)
					detailsInstance = new MockOrderDetailsDAO();
			}
		}
		return detailsInstance;
	}
}