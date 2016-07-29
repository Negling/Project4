package ua.kiral.project4.model.dao;

import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.OrderDetails;

/**
 * Specifies operations wich relate to OrderDetails entity.
 *
 */
public interface OrderDetailsDAO extends GenericDAO<OrderDetails> {

	/**
	 * Returns details match to specified order id.
	 * 
	 * @param orderId
	 * @return null if there was no such orderId.
	 * @throws DAOException
	 */
	public OrderDetails getByOrderId(int orderId) throws DAOException;

	/**
	 * Deletes all records wich match to specified orderid.
	 * 
	 * @param orderId
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteAllByOrderId(int orderId) throws DAOException;
}