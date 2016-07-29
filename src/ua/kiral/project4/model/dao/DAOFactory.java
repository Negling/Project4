package ua.kiral.project4.model.dao;

/**
 * Provides unified access interface to different variables of DAO classes.
 *
 */
public interface DAOFactory {

	/**
	 * Returns implementation of UserDAO interface relies to factory.
	 * 
	 * @return
	 */
	public UserDAO getUserDAO();

	/**
	 * Returns implementation of ProductDAO interface relies to factory.
	 * 
	 * @return
	 */
	public ProductDAO getProductDAO();

	/**
	 * Returns implementation of OrderDAO interface relies to factory.
	 * 
	 * @return
	 */
	public OrderDAO getOrderDAO();

	/**
	 * Returns implementation of OrderDetailsDAO interface relies to factory.
	 * 
	 * @return
	 */
	public OrderDetailsDAO getOrderDetailsDAO();
}