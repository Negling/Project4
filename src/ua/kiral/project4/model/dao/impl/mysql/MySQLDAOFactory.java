package ua.kiral.project4.model.dao.impl.mysql;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;

import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.OrderDAO;
import ua.kiral.project4.model.dao.OrderDetailsDAO;
import ua.kiral.project4.model.dao.ProductDAO;
import ua.kiral.project4.model.dao.UserDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;

/**
 * All instances of dao interfaces in this factory(except the factory) - is
 * non-static singleton ipml, cuz we dont need new instance of DAO object every
 * time when we use MySQL methods, and invoke of this methods doesn't modify
 * state of objects in java heap. Every method invoke - is separate transaction
 * with own connection from connections pool.
 *
 */
public class MySQLDAOFactory implements DAOFactory {
	final static Logger logger = LogManager.getLogger(MySQLDAOFactory.class);
	private static MySQLDAOFactory factoryInstance;
	private MySQLUserDAO userInstance;
	private MySQLProductDAO productInstance;
	private MySQLOrderDAO orderInstance;
	private MySQLOrderDetailsDAO detailsInstance;
	/*
	 * Connection pool
	 */
	private DataSource pool;

	/**
	 * We use Tomcat JDBC Connection Pool.
	 * 
	 * @throws DAOException
	 */
	private MySQLDAOFactory() {
		try {
			Context ctx = new InitialContext();
			this.pool = (DataSource) ctx.lookup("java:comp/env/jdbc/project4DB");
		} catch (NamingException cause) {
			logger.error("Exception due initializing data sorce. ", cause);
		}
	}

	/**
	 * Returns singleton instance of factory.
	 * 
	 * @return
	 */
	public static DAOFactory getInstance() {
		if (factoryInstance == null) {
			synchronized (MySQLDAOFactory.class) {
				if (factoryInstance == null)
					factoryInstance = new MySQLDAOFactory();
			}
		}
		return factoryInstance;
	}

	@Override
	public UserDAO getUserDAO() {
		if (userInstance == null) {
			synchronized (this) {
				if (userInstance == null)
					userInstance = new MySQLUserDAO(pool);
			}
		}
		return userInstance;
	}

	@Override
	public ProductDAO getProductDAO() {
		if (productInstance == null) {
			synchronized (this) {
				if (productInstance == null)
					productInstance = new MySQLProductDAO(pool);
			}
		}
		return productInstance;
	}

	@Override
	public OrderDAO getOrderDAO() {
		if (orderInstance == null) {
			synchronized (this) {
				if (orderInstance == null)
					orderInstance = new MySQLOrderDAO(pool);
			}
		}
		return orderInstance;
	}

	@Override
	public OrderDetailsDAO getOrderDetailsDAO() {
		if (detailsInstance == null) {
			synchronized (this) {
				if (detailsInstance == null)
					detailsInstance = new MySQLOrderDetailsDAO(pool);
			}
		}
		return detailsInstance;
	}
}