package ua.kiral.project4.model.dao;

import java.util.Date;
import java.util.List;

import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Order;

/**
 * Specifies operations wich relate to Order entity.
 *
 */
public interface OrderDAO extends GenericDAO<Order> {

	/**
	 * Returns Order wich mathes to specified order ID;
	 * 
	 * @param Id
	 * @return null if there was no such order.
	 * @throws DAOException
	 */
	public Order getOrderByID(Integer orderId) throws DAOException;
	
	/**
	 * Returns order id wich mathes to specified order;
	 * 
	 * @param Id
	 * @return -1 if there was no such order.
	 * @throws DAOException
	 */
	public int getOrderId(Order order) throws DAOException;

	/**
	 * Returns list of Orders wich mathes to specified order status;
	 * 
	 * @param status
	 * @return null if no orders match to argument.
	 * @throws DAOException
	 */
	public List<Order> getAllByStatus(String status) throws DAOException;

	/**
	 * Returns list of Orders wich mathes to specified order owner;
	 * 
	 * @param owner
	 * @return null if no orders match to argument.
	 * @throws DAOException
	 */
	public List<Order> getAllByOwner(Integer ownerId) throws DAOException;

	/**
	 * Returns list of Orders wich mathes to specified order date;
	 * 
	 * @param date
	 * @return null if no orders match to argument.
	 * @throws DAOException
	 */
	public List<Order> getAllByDate(Date date) throws DAOException;

	/**
	 * Deletes all Orders wich mathes to specified order status;
	 * 
	 * @param status
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteAllByStatus(String status) throws DAOException;

	/**
	 * Deletes all Orders wich mathes to specified order owner;
	 * 
	 * @param owner
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteAllByOwner(Integer owner) throws DAOException;

	/**
	 * Deletes all Orders wich mathes to specified order date;
	 * 
	 * @param date
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteAllByDate(Date date) throws DAOException;
}